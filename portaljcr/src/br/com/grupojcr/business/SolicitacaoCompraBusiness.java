package br.com.grupojcr.business;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AprovadorCentroCustoDAO;
import br.com.grupojcr.dao.CotacaoDAO;
import br.com.grupojcr.dao.SolicitacaoCompraDAO;
import br.com.grupojcr.dao.SolicitacaoCompraItemDAO;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.dto.SolicitacaoCompraDTO;
import br.com.grupojcr.entity.AprovadorCentroCusto;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
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
	private AprovadorCentroCustoDAO daoAprovadorCentroCusto;
	
	@EJB
	private FluigBusiness fluigBusiness;
	
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
			
			Integer nivel = 0;
			Boolean mesmoAprovador = Boolean.FALSE;
			AprovadorCentroCusto aprovador = daoAprovadorCentroCusto.obterAprovadorCentroCusto(solicitacao.getCodigoCentroCusto(), nivel);
			if(Util.isNotNull(aprovador)) {
				while(solicitacao.getUsuarioSolicitante().getUsuario().equals(aprovador.getUsuario())) {
					AprovadorCentroCusto novoAprovador = daoAprovadorCentroCusto.obterAprovadorCentroCusto(solicitacao.getCodigoCentroCusto(), ++nivel);
					if(Util.isNull(novoAprovador)) {
//						solicitacao.setSituacao(SituacaoSolicitacaoCompra.APROVADA_COTACAO);
//						solicitacao.setDtAprovacao(Calendar.getInstance().getTime());
//						mesmoAprovador = Boolean.TRUE; //TODO DESCOMENTAR
						break;
					}
				}
				solicitacao.setUsuarioAprovacaoFluig(aprovador.getAprovador());
				
			} else {
				throw new ApplicationException("message.empty", new String[] {"Centro de Custo não possui aprovador, solicite o cadastro dos aprovadores."}, FacesMessage.SEVERITY_ERROR);
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
						"\", \"observacao\": \"" + sci.getObservacao().toUpperCase() +
						"\"}");
			}
			
			itens.append("]}");
			
			if(!mesmoAprovador) {
				
				String [][] parametros = new String[][] { 
					{ "idSolicitacao", solicitacao.getId().toString()}, 
					{ "empresa", solicitacao.getColigada().getRazaoSocial().toUpperCase()},
					{"descricaoPadrao", "SOLICITAÇÃO Nº " + solicitacao.getId().toString()},
					{ "solicitante", solicitacao.getUsuarioSolicitante().getNome().toUpperCase()}, 
					{ "centroCusto", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto().toUpperCase()},
					{ "modalidade", solicitacao.getModalidade().getDescricao().toUpperCase()},
					{ "itens", itens.toString()} 
				};
				
				// Inicia processo do Fluig
				String[][] resultado = fluigBusiness.iniciarProcessoFluig("Solicitacao de Compra", aprovador.getAprovador(), 9, parametros);
	
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
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	public void aprovar(Long idSolicitacao) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = daoSolicitacaoCompra.obter(idSolicitacao);
			if(Util.isNotNull(solicitacao)) {
				solicitacao.setSituacao(SituacaoSolicitacaoCompra.APROVADA_COTACAO);
				solicitacao.setDtAprovacao(Calendar.getInstance().getTime());
				daoSolicitacaoCompra.alterar(solicitacao);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "aprovar" }, e);
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

	public void recusar(Long idSolicitacao, String motivo) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = daoSolicitacaoCompra.obter(idSolicitacao);
			if(Util.isNotNull(solicitacao)) {
				solicitacao.setSituacao(SituacaoSolicitacaoCompra.CANCELADA);
				solicitacao.setMotivoCancelamento(motivo);
				solicitacao.setDtCancelamento(Calendar.getInstance().getTime());
				daoSolicitacaoCompra.alterar(solicitacao);
			}
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
	
	public void liberar(SolicitacaoCompra solicitacao) throws ApplicationException {
		try {
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.LIBERADO_ORDEM_COMPRA);
			
			daoSolicitacaoCompra.alterar(solicitacao);
			
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
	
	public String montarXML(OrdemCompra ordemCompra) throws ApplicationException {
		try {
			StringBuilder xml = new StringBuilder();
			StringWriter sw = new StringWriter();
			JAXBContext context = null;
			Marshaller marshaller = null;
			
			TMOVXML tmov = new TMOVXML();
			
			tmov.setCODCOLIGADA(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString());
			tmov.setCODCFO("00000003");
			
			if(ordemCompra.getSolicitacaoCompra().getModalidade().equals(Modalidade.MATERIAL)) {
				tmov.setCODTMV("1.1.04");
			} else if(ordemCompra.getSolicitacaoCompra().getModalidade().equals(Modalidade.SERVICO)) {
				tmov.setCODTMV("1.1.17");
			} else {
				tmov.setCODTMV("1.1.04");
			}
			tmov.setCODCPG("42");
			tmov.setCODCFOAUX("00000003");
			tmov.setCODCCUSTO(ordemCompra.getSolicitacaoCompra().getCodigoCentroCusto());
			tmov.preencherValores("667,00");
			tmov.preencherUsuario("leonan");
			tmov.setCODCOLIGADA1(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString());
			tmov.setHISTORICOLONGO(ordemCompra.getSolicitacaoCompra().getMotivoCompra());
			
			
			context = JAXBContext.newInstance(TMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmov, sw);
			xml.append(sw.toString());
			
			TNFEXML tnfe = new TNFEXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TNFEXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tnfe, sw);
			xml.append(sw.toString());

			TMOVFISCALXML tmovfiscal = new TMOVFISCALXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVFISCALXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovfiscal, sw);
			xml.append(sw.toString());
			
			TMOVRATCCUXML tmovratccu = new TMOVRATCCUXML(ordemCompra.getSolicitacaoCompra().getColigada().getId().toString(), ordemCompra.getSolicitacaoCompra().getCodigoCentroCusto(), ordemCompra.getSolicitacaoCompra().getCentroCusto(), "667,00");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVRATCCUXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovratccu, sw);
			xml.append(sw.toString());
			
			TITMMOVXML titmmov = new TITMMOVXML("7", "1", "3660", "02.001.0015", "TONERS/CARTUCHOS", "0137",
					"2,00", "113,00", "226,00", "UN", "001.001.013", "2.226", "COMPRA DE TONERS  951XL COR MAGENTA", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmov, sw);
			xml.append(sw.toString());

			TITMMOVXML titmmov2 = new TITMMOVXML("7", "2", "3660", "02.001.0015", "TONERS/CARTUCHOS", "0137",
					"3,00", "147,00", "441,00", "UN", "001.001.013", "2.226", "950 XL COR PRETA", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmov2, sw);
			xml.append(sw.toString());
			
			TITMMOVRATCCUXML titmmovratccu = new TITMMOVRATCCUXML("7", "1", "001.001.013", "Tecnologia da Informação", "226,00");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVRATCCUXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmovratccu, sw);
			xml.append(sw.toString());
			
			TITMMOVRATCCUXML titmmovratccu2 = new TITMMOVRATCCUXML("7", "2", "001.001.013", "Tecnologia da Informação", "441,00");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVRATCCUXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmovratccu2, sw);
			xml.append(sw.toString());
			
			TMOVCOMPLXML tmovcompl = new TMOVCOMPLXML("7", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVCOMPLXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovcompl, sw);
			xml.append(sw.toString());
			
			TITMMOVCOMPLXML titmmovcmpl = new TITMMOVCOMPLXML("7", "1", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVCOMPLXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmovcmpl, sw);
			xml.append(sw.toString());
			
			TITMMOVCOMPLXML titmmovcmpl2 = new TITMMOVCOMPLXML("7", "2", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVCOMPLXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmovcmpl2, sw);
			xml.append(sw.toString());
			
			TMOVTRANSPXML tmovtransp = new TMOVTRANSPXML("7", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TMOVTRANSPXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tmovtransp, sw);
			xml.append(sw.toString());
			
			TCTRCMOVXML tctrcmov = new TCTRCMOVXML("7", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TCTRCMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(tctrcmov, sw);
			xml.append(sw.toString());

			TTRBITMMOVXML ttrbitmmov = new TTRBITMMOVXML("7", "1", "ICMS", "226,00", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TTRBITMMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(ttrbitmmov, sw);
			xml.append(sw.toString());
			
			TTRBITMMOVXML ttrbitmmov2 = new TTRBITMMOVXML("7", "1", "IPI", "226,00", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TTRBITMMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(ttrbitmmov2, sw);
			xml.append(sw.toString());
			
			TTRBITMMOVXML ttrbitmmov3 = new TTRBITMMOVXML("7", "2", "ICMS", "441,00", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TTRBITMMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(ttrbitmmov3, sw);
			xml.append(sw.toString());
			
			TTRBITMMOVXML ttrbitmmov4 = new TTRBITMMOVXML("7", "2", "IPI", "441,00", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TTRBITMMOVXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(ttrbitmmov4, sw);
			xml.append(sw.toString());
			
			TITMMOVFISCALXML titmmovfiscal = new TITMMOVFISCALXML("7", "1", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVFISCALXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmovfiscal, sw);
			xml.append(sw.toString());

			TITMMOVFISCALXML titmmovfiscal2 = new TITMMOVFISCALXML("7", "2", "leonan");
			
			sw = new StringWriter();
			context = JAXBContext.newInstance(TITMMOVFISCALXML.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(titmmovfiscal2, sw);
			xml.append(sw.toString());
			
			
			String xmlCompleto = "<MovMovimento>" + xml.toString() + "</MovMovimento>";
			System.out.println(xmlCompleto);
			return xmlCompleto;
			
//		} catch (ApplicationException e) {
//			LOG.info(e.getMessage(), e);
//			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "montarXML" }, e);
		}
	}
	
}
