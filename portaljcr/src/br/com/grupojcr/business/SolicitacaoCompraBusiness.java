package br.com.grupojcr.business;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.CotacaoDAO;
import br.com.grupojcr.dao.GrupoCotacaoDAO;
import br.com.grupojcr.dao.GrupoDAO;
import br.com.grupojcr.dao.OrdemCompraDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dao.SolicitacaoCompraDAO;
import br.com.grupojcr.dao.SolicitacaoCompraItemDAO;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.dto.OrdemCompraDTO;
import br.com.grupojcr.dto.ProdutoDTO;
import br.com.grupojcr.dto.SolicitacaoCompraDTO;
import br.com.grupojcr.email.EmailSolicitacaoCompra;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.Grupo;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.OrdemCompra;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.xml.TCTRCMOVXML;
import br.com.grupojcr.entity.xml.TITMMOVCOMPLXML;
import br.com.grupojcr.entity.xml.TITMMOVFISCALXML;
import br.com.grupojcr.entity.xml.TITMMOVRATCCUXML;
import br.com.grupojcr.entity.xml.TITMMOVXML;
import br.com.grupojcr.entity.xml.TMOVCOMPLXML;
import br.com.grupojcr.entity.xml.TMOVFISCALXML;
import br.com.grupojcr.entity.xml.TMOVRATCCUXML;
import br.com.grupojcr.entity.xml.TMOVTRANSPXML;
import br.com.grupojcr.entity.xml.TMOVXML;
import br.com.grupojcr.entity.xml.TNFEXML;
import br.com.grupojcr.entity.xml.TTRBITMMOVXML;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.enumerator.PrioridadeSolicitacaoCompra;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;
import br.com.grupojcr.rm.AprovadorRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class SolicitacaoCompraBusiness {
	
	private static Logger LOG = Logger.getLogger(ChamadoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private SolicitacaoCompraDAO daoSolicitacaoCompra;
	
	@EJB
	private SolicitacaoCompraItemDAO daoSolicitacaoCompraItem;

	@EJB
	private CotacaoDAO daoCotacao;
	
	@EJB
	private OrdemCompraDAO daoOrdemCompra;
	
	@EJB
	private FluigBusiness fluigBusiness;
	
	@EJB
	private GrupoCotacaoDAO daoGrupoCotacao;
	
	@EJB
	private GrupoDAO daoGrupo;
	
	@EJB
	private RMDAO daoRM;
	
	public void salvar(SolicitacaoCompraDTO dto, Usuario usuario) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = new SolicitacaoCompra();
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.AGUARDANDO_APRV);
			solicitacao.setCodigoCentroCusto(dto.getCentroCusto().getCodigoCentroCusto());
			solicitacao.setCentroCusto(dto.getCentroCusto().getCentroCusto());
			solicitacao.setModalidade(dto.getModalidade());
			solicitacao.setPrioridade(dto.getPrioridade());
			solicitacao.setMotivoCompra(dto.getMotivoCompra());
			solicitacao.setSugestaoFornecedor(dto.getSugestaoFornecedor());
			solicitacao.setLocalEntrega(dto.getLocalEntrega());
			solicitacao.setDtSolicitacao(Calendar.getInstance().getTime());
			solicitacao.setUsuarioSolicitante(usuario);
			solicitacao.setColigada(dto.getColigada());
			if(dto.getPossuiGrupoCotacao()) {
				solicitacao.setGrupoCotacao(dto.getGrupoCotacao());
			} else {
				solicitacao.setUsuarioCotacao(usuario);
			}
			
			Calendar prazo = Calendar.getInstance();
			
			if(dto.getPrioridade().equals(PrioridadeSolicitacaoCompra.ALTA)) {
				prazo.add(Calendar.DAY_OF_MONTH, 10);
			} else if(dto.getPrioridade().equals(PrioridadeSolicitacaoCompra.MEDIA)) {
				prazo.add(Calendar.DAY_OF_MONTH, 7);
			} else {
				prazo.add(Calendar.DAY_OF_MONTH, 3);
			}
			
			while(prazo.get(Calendar.DAY_OF_WEEK) == 1 || prazo.get(Calendar.DAY_OF_WEEK) == 7) {
				prazo.add(Calendar.DAY_OF_MONTH, 1);
			}
			solicitacao.setDtPrazo(prazo.getTime());
			
			BigDecimal valorAproximado = new BigDecimal(0);
			for(SolicitacaoCompraItem sci : dto.getItens()) {
				valorAproximado	= valorAproximado.add(sci.getValorAproximado());
			}
			
			solicitacao.setValorTotalAproximado(valorAproximado);
			
			String lotacao = daoRM.obterLotacaoCentroCusto(solicitacao.getCodigoCentroCusto(), solicitacao.getColigada().getId());
			if(TreatString.isBlank(lotacao)) {
				throw new ApplicationException("message.empty", new String[] {"Centro de Custo não possui lotação cadastrada, solicite o cadastro da Lotação para a equipe de TI."}, FacesMessage.SEVERITY_ERROR);
			}
			AprovadorRM primeiroAprovador = null;
			AprovadorRM segundoAprovador = null;
			
			List<AprovadorRM> listaPrimeiroAprovador = daoRM.listarPrimeiroAprovador(lotacao);
			List<AprovadorRM> listaSegundoAprovador = daoRM.listarSegundoAprovador(lotacao);
			
			for(AprovadorRM aprov : listaPrimeiroAprovador) {
				if(solicitacao.getValorTotalAproximado().compareTo(aprov.getValorMovimentoDe()) == 1 
						&& solicitacao.getValorTotalAproximado().compareTo(aprov.getValorMovimentoAte()) != 1) {
					primeiroAprovador = aprov;
				}
			}
			
			for(AprovadorRM aprov : listaSegundoAprovador) {
				if(solicitacao.getValorTotalAproximado().compareTo(aprov.getValorMovimentoDe()) == 1 
						&& solicitacao.getValorTotalAproximado().compareTo(aprov.getValorMovimentoAte()) != 1) {
					segundoAprovador = aprov;
				}
			}
			
			if(Util.isNull(primeiroAprovador) || Util.isNull(segundoAprovador)) {
				throw new ApplicationException("message.empty", new String[] {"Cadastro de Aprovadores no RM incorreto, solicite a equipe de TI para verificar. "}, FacesMessage.SEVERITY_ERROR);
			}
			
			if(usuario.getUsuario().equals(segundoAprovador)) {
				solicitacao.setUsuarioAprovacaoFluig(primeiroAprovador.getAprovador());
			} else {
				solicitacao.setUsuarioAprovacaoFluig(segundoAprovador.getAprovador());
			}
			
			daoSolicitacaoCompra.incluir(solicitacao);
			
			StringBuilder itens = new StringBuilder("{\"itens\": [");
			int tamanho = itens.length();
			
			for(SolicitacaoCompraItem sci : dto.getItens()) {
				sci.setSolicitacaoCompra(solicitacao);
				
				daoSolicitacaoCompraItem.incluir(sci);
				
				
				if(itens.length() > tamanho) {
					itens.append(",");
				}
				itens.append("{\"produto\": \"" + sci.getDescricaoProduto().toUpperCase() + 
						"\", \"quantidade\": \"" + sci.getQuantidade() + 
						"\", \"unidade\": \"" + sci.getUnidade() +
						"\", \"valorAproximado\": \"" + TreatNumber.formatMoneyCurrency(sci.getValorAproximado()) + 
						"\", \"observacao\": \"" + sci.getObservacao().toUpperCase() +
						"\"}");
			}
			
			itens.append("]}");
			
			String [][] parametros = new String[][] { 
				{ "idSolicitacao", solicitacao.getId().toString()}, 
				{ "empresa", solicitacao.getColigada().getRazaoSocial().toUpperCase()},
				{ "descricaoPadrao", "SOLICITAÇÃO Nº " + solicitacao.getId().toString()},
				{ "solicitante", solicitacao.getUsuarioSolicitante().getNome().toUpperCase()}, 
				{ "centroCusto", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto().toUpperCase()},
				{ "modalidade", solicitacao.getModalidade().getDescricao().toUpperCase()},
				{ "motivoCompra", solicitacao.getMotivoCompra()},
				{ "itens", itens.toString()} 
			};
			
			// Inicia processo do Fluig
			String[][] resultado = fluigBusiness.iniciarProcessoFluig("Solicitacao de Compra", /*solicitacao.getUsuarioAprovacaoFluig()*/ "leonan", 9, parametros);

			for(int i = 0; i < resultado.length; i++) {
				for(int j = 0; j < resultado[i].length; j++) {
					if(resultado[i][j].equals("iProcess")) {
						try {
							Long idFluig = Long.parseLong((resultado[i][j + 1]).toString());
							solicitacao.setIdentificadorFluig(idFluig);
							
							daoSolicitacaoCompra.alterar(solicitacao);
							break;
						} catch(NumberFormatException e) {
							solicitacao.setIdentificadorFluig(null);
						}
					}
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	
	public SolicitacaoCompra aprovar(Long idSolicitacao) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = daoSolicitacaoCompra.obter(idSolicitacao);
			if(Util.isNotNull(solicitacao)) {
				solicitacao.setSituacao(SituacaoSolicitacaoCompra.APROVADA_COTACAO);
				solicitacao.setDtAprovacao(Calendar.getInstance().getTime());
				daoSolicitacaoCompra.alterar(solicitacao);
			}
			
			return solicitacao;
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "aprovar" }, e);
		}
	}
	
	@Asynchronous
	public void enviarEmailAprovacao(SolicitacaoCompra sol) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = daoSolicitacaoCompra.obterSolicitacao(sol.getId());
			List<String> emails = new ArrayList<String>();
			if(Util.isNotNull(solicitacao.getGrupoCotacao())) {
				GrupoCotacao grupoCotacao = daoGrupoCotacao.obterGrupoCotacao(solicitacao.getGrupoCotacao().getId());
				for(Usuario usuario : grupoCotacao.getUsuarios()) {
					emails.add(usuario.getEmail());
				}
			} else {
				emails.add(solicitacao.getUsuarioCotacao().getEmail());
			}
			EmailSolicitacaoCompra emailSolicitacao = new EmailSolicitacaoCompra();
			if(CollectionUtils.isNotEmpty(emails)) {
				emailSolicitacao.novaSolicitacaoCompra("[PORTAL JCR] Nova Solicitação de Compra", emails , solicitacao);
			}
			emailSolicitacao.solicitacaoAprovada("[PORTAL JCR] Solicitação de Compra Aprovada para Cotação", new ArrayList<String>(Arrays.asList(solicitacao.getUsuarioSolicitante().getEmail())) , solicitacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Asynchronous
	public void enviarEmailRecusar(SolicitacaoCompra sol) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = daoSolicitacaoCompra.obterSolicitacao(sol.getId());
			EmailSolicitacaoCompra emailSolicitacao = new EmailSolicitacaoCompra();
			emailSolicitacao.solicitacaoRecusada("[PORTAL JCR] Solicitação de Compra Recusada para Cotação", new ArrayList<String>(Arrays.asList(solicitacao.getUsuarioSolicitante().getEmail())) , solicitacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Asynchronous
	public void enviarEmailConcluir(SolicitacaoCompra solicitacao) throws ApplicationException {
		try {
			EmailSolicitacaoCompra emailSolicitacao = new EmailSolicitacaoCompra();
			emailSolicitacao.cotacaoConcluida("[PORTAL JCR] Cotação Finalizada", new ArrayList<String>(Arrays.asList(solicitacao.getUsuarioSolicitante().getEmail())) , solicitacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Asynchronous
	public void enviarEmailLiberacao(SolicitacaoCompra solicitacao, Cotacao cotacao) throws ApplicationException {
		try {
			List<String> emails = new ArrayList<String>();
			Grupo grupoAdministrativo = daoGrupo.obterGrupo("ADMINISTRATIVO");
			
			if(Util.isNotNull(grupoAdministrativo)) {
				if(CollectionUtils.isNotEmpty(grupoAdministrativo.getUsuarios())) {
					for(Usuario usuario : grupoAdministrativo.getUsuarios()) {
						emails.add(usuario.getEmail());
					}
				}
			}
			if(CollectionUtils.isNotEmpty(emails)) {
				EmailSolicitacaoCompra emailSolicitacao = new EmailSolicitacaoCompra();
				emailSolicitacao.ordemCompraDisponivel("[PORTAL JCR] Nova Solicitação de Ordem de Compra Disponível", emails , solicitacao, cotacao);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Asynchronous
	public void enviarEmailOrdemCompra(OrdemCompra oc) throws ApplicationException {
		try {
			List<String> emails = new ArrayList<String>();
			Grupo grupoAdministrativo = daoGrupo.obterGrupo("ADMINISTRATIVO");
			
			if(Util.isNotNull(grupoAdministrativo)) {
				if(CollectionUtils.isNotEmpty(grupoAdministrativo.getUsuarios())) {
					for(Usuario usuario : grupoAdministrativo.getUsuarios()) {
						emails.add(usuario.getEmail());
					}
				}
			}
			
			if(Util.isNotNull(oc.getSolicitacaoCompra().getUsuarioSolicitante().getEmail())) {
				emails.add(oc.getSolicitacaoCompra().getUsuarioSolicitante().getEmail());
			}
			
			
			if(CollectionUtils.isNotEmpty(emails)) {
				EmailSolicitacaoCompra emailSolicitacao = new EmailSolicitacaoCompra();
				emailSolicitacao.ordemCompraGerada("[PORTAL JCR] Solicitação de Ordem de Compra Gerada", emails , oc);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void solicitarCotacoes(Long idSolicitacao) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = daoSolicitacaoCompra.obter(idSolicitacao);
			if(Util.isNotNull(solicitacao)) {
				solicitacao.setSituacao(SituacaoSolicitacaoCompra.EM_COTACAO);
				solicitacao.setDtCotacao(null);
				solicitacao.setJustificativa(null);
				
				daoSolicitacaoCompra.alterar(solicitacao);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "solicitarCotacoes" }, e);
		}
	}

	public SolicitacaoCompra recusar(Long idSolicitacao, String motivo) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = daoSolicitacaoCompra.obter(idSolicitacao);
			if(Util.isNotNull(solicitacao)) {
				solicitacao.setSituacao(SituacaoSolicitacaoCompra.CANCELADA);
				solicitacao.setMotivoCancelamento(motivo);
				solicitacao.setDtCancelamento(Calendar.getInstance().getTime());
				daoSolicitacaoCompra.alterar(solicitacao);
			}
			
			return solicitacao;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "recusar" }, e);
		}
	}
	
	public Integer obterQtdSolicitacaoCompra(FiltroSolicitacaoCompra filtro) throws ApplicationException {
		try {
			return daoSolicitacaoCompra.obterQtdSolicitacaoCompra(filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdSolicitacaoCompra" }, e);
		}
	}
	
	public List<SolicitacaoCompra> listarSolicitacaoCompraPaginado(int first, int pageSize, FiltroSolicitacaoCompra filtro) throws ApplicationException {
		try {
			return daoSolicitacaoCompra.listarSolicitacaoCompraPaginado(first, pageSize, filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarSolicitacaoCompraPaginado" }, e);
		}
	}
	
	public List<Cotacao> listarCotacoesPorSolicitacao(Long idSolicitacao) throws ApplicationException {
		try {
			return daoCotacao.listarCotacoesPorSolicitacao(idSolicitacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarItensPorSolicitacao" }, e);
		}
	}
	
	public List<SolicitacaoCompraItem> listarItensPorSolicitacao(Long idSolicitacao) throws ApplicationException {
		try {
			return daoSolicitacaoCompraItem.listarItemPorSolicitacao(idSolicitacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarItensPorSolicitacao" }, e);
		}
	}
	
	public List<OrdemCompra> listarOrdemCompraPorSolicitacao(Long idSolicitacao) throws ApplicationException {
		try {
			return daoOrdemCompra.listarOrdemCompraPorSolicitacao(idSolicitacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarOrdemCompraPorSolicitacao" }, e);
		}
	}
	
	public void cancelar(SolicitacaoCompra solicitacao, Usuario usuario) throws ApplicationException {
		try {
			solicitacao.setUsuarioCancelamento(usuario);
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.CANCELADA);
			
			daoSolicitacaoCompra.alterar(solicitacao);
			
			if(Util.isNotNull(solicitacao.getIdentificadorFluig())) {
				fluigBusiness.cancelarProcessoFluig(solicitacao.getIdentificadorFluig(), solicitacao.getId(), solicitacao.getMotivoCancelamento(), usuario.getNome().toUpperCase());
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "cancelar" }, e);
		}
	}
	
	public List<SolicitacaoCompra> listarSolicitacaoCompraPendente(FiltroSolicitacaoCompra filtro) throws ApplicationException {
		try {
			return daoSolicitacaoCompra.listarSolicitacaoCompraPendente(filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarSolicitacaoCompraPendente" }, e);
		}
	}
	
	public void iniciarCotacao(SolicitacaoCompra solicitacao, Usuario usuario) throws ApplicationException {
		try {
			solicitacao.setDtInicioCotacao(Calendar.getInstance().getTime());
			solicitacao.setUsuarioCotacao(usuario);
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.EM_COTACAO);
			
			daoSolicitacaoCompra.alterar(solicitacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarCotacao" }, e);
		}
	}
	
	public void concluir(SolicitacaoCompra solicitacao) throws ApplicationException {
		try {
			solicitacao.setDtCotacao(Calendar.getInstance().getTime());
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.AGUARDANDO_APRV_COTACAO);
			
			daoSolicitacaoCompra.alterar(solicitacao);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "concluir" }, e);
		}
	}

	public OrdemCompra encerrar(OrdemCompraDTO dto, String identificadorRM, Usuario usuario) throws ApplicationException {
		try {
			SolicitacaoCompra sc = daoSolicitacaoCompra.obterSolicitacao(dto.getSolicitacaoCompra().getId());
			sc.setSituacao(SituacaoSolicitacaoCompra.FINALIZADA);
			sc.setDtFechamento(Calendar.getInstance().getTime());
			
			daoSolicitacaoCompra.alterar(sc);
			
			Cotacao cotacaoPrincipal = daoCotacao.obterCotacao(dto.getCotacao().getId());
			cotacaoPrincipal.setCodigoFornecedor(dto.getFornecedor().getCodigoFornecedor());
			cotacaoPrincipal.setFornecedor(dto.getFornecedor().getFornecedor());
			
			daoCotacao.alterar(cotacaoPrincipal);
			
			List<SolicitacaoCompraItem> listaItemSolicitacao = daoSolicitacaoCompraItem.listarItemPorSolicitacao(sc.getId());
			for(SolicitacaoCompraItem sci : listaItemSolicitacao) {
				for(ProdutoDTO produtoDTO : dto.getListaProduto()) {
					if(produtoDTO.getCotacaoItem().getSolicitacaoCompraItem().getId().equals(sci.getId())) {
						if(TreatString.isNotBlank(produtoDTO.getCotacaoItem().getSolicitacaoCompraItem().getCodigoProduto())) {
							sci.setCodigoProduto(produtoDTO.getCotacaoItem().getSolicitacaoCompraItem().getCodigoProduto());
							sci.setDescricaoProduto(produtoDTO.getCotacaoItem().getSolicitacaoCompraItem().getDescricaoProduto());
						}
						daoSolicitacaoCompraItem.alterar(sci);
					}
				}
			}
			
			OrdemCompra ordemCompra = new OrdemCompra();
			ordemCompra.setSolicitacaoCompra(sc);
			ordemCompra.setCotacao(cotacaoPrincipal);
			ordemCompra.setUsuario(usuario);
			ordemCompra.setIdentificadorRM(identificadorRM);
			ordemCompra.setDtOrdemCompra(Calendar.getInstance().getTime());
			ordemCompra.setCodigoCondicaoPagamento(dto.getCondicaoPagamento().getCodigoCondicaoPagamento());
			ordemCompra.setCondicaoPagamento(dto.getCondicaoPagamento().getCondicaoPagamento());
			
			daoOrdemCompra.incluir(ordemCompra);
			
			return ordemCompra;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "encerrar" }, e);
		}
	}
	
	public void liberar(SolicitacaoCompra solicitacao, Cotacao cotacao) throws ApplicationException {
		try {
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.LIBERADO_ORDEM_COMPRA);
			daoSolicitacaoCompra.alterar(solicitacao);
			
			List<Cotacao> cotacoes = daoCotacao.listarCotacoesPorSolicitacao(solicitacao.getId());
			for(Cotacao cot : cotacoes) {
				if(cot.getId().equals(cotacao.getId())) {
					cot.setCotacaoPrincipal(Boolean.TRUE);
				} else {
					cot.setCotacaoPrincipal(Boolean.FALSE);
				}
				daoCotacao.alterar(cotacao);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "liberar" }, e);
		}
	}
	
	public void calcularMelhorOpcao(SolicitacaoCompra solicitacao) throws ApplicationException {
		try {
			List<Cotacao> cotacoes = daoCotacao.listarCotacoesPorSolicitacao(solicitacao.getId());
			
			List<Cotacao> cotacoesCompletas = new ArrayList<Cotacao>();
			
			for(Cotacao cotacao : cotacoes) {
				Boolean cotacaoCompleta = Boolean.TRUE;
				for(CotacaoItem item : cotacao.getItens()) {
					if(item.getNaoPossui()) {
						cotacaoCompleta = Boolean.FALSE;
					}
				}
				
				if(cotacaoCompleta) {
					cotacoesCompletas.add(cotacao);
				}
			}
			
			if(CollectionUtils.isNotEmpty(cotacoesCompletas)) {
				
				Cotacao cotacaoMenorValor = null;
				for(Cotacao cotacao : cotacoesCompletas) {
					if(Util.isNull(cotacaoMenorValor)) {
						cotacaoMenorValor = cotacao;
					} else {
						if(cotacao.getValorTotal().compareTo(cotacaoMenorValor.getValorTotal()) == -1) {
							cotacaoMenorValor = cotacao;
						} else if(cotacao.getValorTotal().equals(cotacaoMenorValor.getValorTotal())) {
							if(cotacao.getDtCotacao().before(cotacaoMenorValor.getDtCotacao())) {
								cotacaoMenorValor = cotacao;
							}
						}
					}
				}
				
				cotacaoMenorValor.setMelhorOpcao(Boolean.TRUE);
				cotacaoMenorValor.setCotacaoPrincipal(Boolean.TRUE);
				daoCotacao.alterar(cotacaoMenorValor);
				
				for(Cotacao cotacao : cotacoes) {
					if(!cotacao.getId().equals(cotacaoMenorValor.getId())) {
						cotacao.setMelhorOpcao(Boolean.FALSE);
						cotacao.setCotacaoPrincipal(Boolean.FALSE);
						daoCotacao.alterar(cotacao);
					}
				}

			} else {
				Cotacao cotacaoMenorValor = null;
				for(Cotacao cotacao : cotacoes) {
					if(Util.isNull(cotacaoMenorValor)) {
						cotacaoMenorValor = cotacao;
					} else {
						if(cotacao.getValorTotal().compareTo(cotacaoMenorValor.getValorTotal()) == -1) {
							cotacaoMenorValor = cotacao;
						} else if(cotacao.getValorTotal().equals(cotacaoMenorValor.getValorTotal())) {
							if(cotacao.getDtCotacao().before(cotacaoMenorValor.getDtCotacao())) {
								cotacaoMenorValor = cotacao;
							}
						}
					}
				}
				
				cotacaoMenorValor.setMelhorOpcao(Boolean.TRUE);
				cotacaoMenorValor.setCotacaoPrincipal(Boolean.TRUE);
				daoCotacao.alterar(cotacaoMenorValor);
				
				for(Cotacao cotacao : cotacoes) {
					if(!cotacao.getId().equals(cotacaoMenorValor.getId())) {
						cotacao.setMelhorOpcao(Boolean.FALSE);
						cotacao.setCotacaoPrincipal(Boolean.FALSE);
						daoCotacao.alterar(cotacao);
					}
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcularMelhorOpcao" }, e);
		}
	}
	
	public String montarXML(OrdemCompraDTO ordemCompra) throws ApplicationException {
		try {
			StringBuilder xml = new StringBuilder();
			StringWriter sw = new StringWriter();
			JAXBContext context = null;
			Marshaller marshaller = null;
			String usuario = "portaljcr";
			
			TMOVXML tmov = new TMOVXML();
			
			tmov.setCODCOLIGADA(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString());
			tmov.setCODCFO(ordemCompra.getFornecedor().getCodigoFornecedor());
			
			if(ordemCompra.getSolicitacaoCompra().getModalidade().equals(Modalidade.MATERIAL)) {
				tmov.setCODTMV("1.1.04");
			} else if(ordemCompra.getSolicitacaoCompra().getModalidade().equals(Modalidade.SERVICO)) {
				tmov.setCODTMV("1.1.17");
			}
			
			tmov.setCODCPG(ordemCompra.getCondicaoPagamento().getCodigoCondicaoPagamento());
			tmov.setCODCFOAUX(ordemCompra.getFornecedor().getCodigoFornecedor());
			tmov.setCODCCUSTO(ordemCompra.getSolicitacaoCompra().getCodigoCentroCusto());
			tmov.preencherValores(TreatNumber.formatMoney(ordemCompra.getCotacao().getValorTotal()));
			tmov.preencherUsuario(usuario);
			tmov.setCODCOLIGADA1(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString());
			tmov.setHISTORICOLONGO(ordemCompra.getSolicitacaoCompra().getMotivoCompra());
			
			
			context = JAXBContext.newInstance(TMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmov, sw);
			xml.append(sw.toString());
			
			TNFEXML tnfe = new TNFEXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), usuario);
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TNFEXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tnfe, sw);
			xml.append(sw.toString());

			TMOVFISCALXML tmovfiscal = new TMOVFISCALXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), usuario);
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVFISCALXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovfiscal, sw);
			xml.append(sw.toString());
			
			TMOVRATCCUXML tmovratccu = new TMOVRATCCUXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), ordemCompra.getSolicitacaoCompra().getCodigoCentroCusto(), ordemCompra.getSolicitacaoCompra().getCentroCusto(), TreatNumber.formatMoney(ordemCompra.getCotacao().getValorTotal()));
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVRATCCUXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovratccu, sw);
			xml.append(sw.toString());
			
			Integer idx = 1;
			for(CotacaoItem item : ordemCompra.getCotacao().getItens()) {
				ProdutoRM produtoRM = daoRM.obterProduto(item.getSolicitacaoCompraItem().getIdProduto());
				
				String idProduto = produtoRM.getIdProduto().toString();
				String codigoProduto = produtoRM.getCodigoProduto();
				String descricaoProduto = produtoRM.getProduto();
				String codigoReduzido = produtoRM.getCodigoReduzido();
				String quantidade = TreatNumber.formatMoney(item.getQuantidade());
				String valorUnitario = TreatNumber.formatMoney(item.getValor());
				String valorTotal = TreatNumber.formatMoney(item.getValorTotal());
				String codigoUnidade = item.getCodigoUnidade();
				String codigoCentroCusto = ordemCompra.getSolicitacaoCompra().getCodigoCentroCusto();
				String centroCusto = ordemCompra.getSolicitacaoCompra().getCentroCusto();
				String codigoNatureza = produtoRM.getCodigoNatureza();
				String observacaoItem = item.getObservacao();
				
				if(TreatString.isBlank(codigoNatureza)) {
					throw new ApplicationException("message.empty", new String[] {"Produto sem Natureza Orçamentária, cadastre uma natureza para o produto: " + descricaoProduto}, FacesMessage.SEVERITY_WARN);
				}
				
				TITMMOVXML titmmov = new TITMMOVXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), idx.toString(), 
						idProduto, codigoProduto, descricaoProduto, codigoReduzido, quantidade, valorUnitario, valorTotal, codigoUnidade, codigoCentroCusto, codigoNatureza, observacaoItem, usuario);
				
				sw = new StringWriter();
				context = JAXBContext.newInstance(TITMMOVXML.class);
				marshaller = context.createMarshaller();
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
				marshaller.marshal(titmmov, sw);
				xml.append(sw.toString());

				TITMMOVRATCCUXML titmmovratccu = new TITMMOVRATCCUXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), idx.toString(),
						codigoCentroCusto, centroCusto , valorTotal);
				
				sw = new StringWriter();
				context = JAXBContext.newInstance(TITMMOVRATCCUXML.class);
				marshaller = context.createMarshaller();
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
				marshaller.marshal(titmmovratccu, sw);
				xml.append(sw.toString());
				
				TITMMOVCOMPLXML titmmovcmpl = new TITMMOVCOMPLXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), idx.toString(), usuario);
				
				sw = new StringWriter();
				context = JAXBContext.newInstance(TITMMOVCOMPLXML.class);
				marshaller = context.createMarshaller();
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
				marshaller.marshal(titmmovcmpl, sw);
				xml.append(sw.toString());
				
				TTRBITMMOVXML ttrbitmmovICMS = new TTRBITMMOVXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), idx.toString(), "ICMS", valorTotal, usuario);
				
				sw = new StringWriter();
				context = JAXBContext.newInstance(TTRBITMMOVXML.class);
				marshaller = context.createMarshaller();
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
				marshaller.marshal(ttrbitmmovICMS, sw);
				xml.append(sw.toString());
				
				TTRBITMMOVXML ttrbitmmovIPI = new TTRBITMMOVXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), idx.toString(), "IPI", valorTotal, usuario);
				
				sw = new StringWriter();
				context = JAXBContext.newInstance(TTRBITMMOVXML.class);
				marshaller = context.createMarshaller();
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
				marshaller.marshal(ttrbitmmovIPI, sw);
				xml.append(sw.toString());
				
				TITMMOVFISCALXML titmmovfiscal = new TITMMOVFISCALXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), idx.toString(), usuario);
				
				sw = new StringWriter();
				context = JAXBContext.newInstance(TITMMOVFISCALXML.class);
				marshaller = context.createMarshaller();
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
				marshaller.marshal(titmmovfiscal, sw);
				xml.append(sw.toString());
				
				idx++;

			}
			
			
			TMOVCOMPLXML tmovcompl = new TMOVCOMPLXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), usuario);
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVCOMPLXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovcompl, sw);
			xml.append(sw.toString());
			
			
			TMOVTRANSPXML tmovtransp = new TMOVTRANSPXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), usuario);
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVTRANSPXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovtransp, sw);
			xml.append(sw.toString());
			
			TCTRCMOVXML tctrcmov = new TCTRCMOVXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), usuario);
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TCTRCMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tctrcmov, sw);
			xml.append(sw.toString());
			
			String xmlCompleto = "<MovMovimento>" + xml.toString() + "</MovMovimento>";
			System.out.println(xmlCompleto);
			return xmlCompleto;
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "montarXML" }, e);
		}
	}
	
}
