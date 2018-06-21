package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.CotacaoDAO;
import br.com.grupojcr.dao.SolicitacaoCompraDAO;
import br.com.grupojcr.dao.SolicitacaoCompraItemDAO;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.dto.SolicitacaoCompraDTO;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
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
	private FluigBusiness fluigBusiness;
	
	public void salvar(SolicitacaoCompraDTO dto, Usuario usuario) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = new SolicitacaoCompra();
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.AGUARDANDO_APRV);
			solicitacao.setCodigoCentroCusto(dto.getCentroCusto().getCodigoCentroCusto());
			solicitacao.setCentroCusto(dto.getCentroCusto().getCentroCusto());
			solicitacao.setCodigoNaturezaOrcamentaria(dto.getNaturezaOrcamentaria().getCodigoNaturezaOrcamentaria());
			solicitacao.setNaturezaOrcamentaria(dto.getNaturezaOrcamentaria().getNaturezaOrcamentaria());
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
			solicitacao.setUsuarioAprovacaoFluig("leonan"); // TODO Buscar aprovadores
			
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
			
			String [][] parametros = new String[][] { 
				{ "idSolicitacao", solicitacao.getId().toString()}, 
				{ "empresa", solicitacao.getColigada().getRazaoSocial().toUpperCase()},
				{"descricaoPadrao", "SOLICITAÇÃO Nº " + solicitacao.getId().toString()},
				{ "solicitante", solicitacao.getUsuarioSolicitante().getNome().toUpperCase()}, 
				{ "centroCusto", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto().toUpperCase()},
				{ "natureza", solicitacao.getCodigoNaturezaOrcamentaria() + " - " + solicitacao.getNaturezaOrcamentaria().toUpperCase()}, 
				{ "modalidade", solicitacao.getModalidade().getDescricao().toUpperCase()},
				{ "itens", itens.toString()} 
			};
			
			// Inicia processo do Fluig
			String[][] resultado = fluigBusiness.iniciarProcessoFluig("Solicitacao de Compra", "leonan", 9, parametros);

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
						if(cotacao.getValorTotal() < cotacaoMenorValor.getValorTotal()) {
							cotacaoMenorValor = cotacao;
						} else if(cotacao.getValorTotal().equals(cotacaoMenorValor.getValorTotal())) {
							if(cotacao.getDtCotacao().before(cotacaoMenorValor.getDtCotacao())) {
								cotacaoMenorValor = cotacao;
							}
						}
					}
				}
				
				cotacaoMenorValor.setMelhorOpcao(Boolean.TRUE);
				daoCotacao.alterar(cotacaoMenorValor);
				
				for(Cotacao cotacao : cotacoes) {
					if(!cotacao.getId().equals(cotacaoMenorValor.getId())) {
						cotacao.setMelhorOpcao(Boolean.FALSE);
						daoCotacao.alterar(cotacao);
					}
				}

			} else {
				Cotacao cotacaoMenorValor = null;
				for(Cotacao cotacao : cotacoes) {
					if(Util.isNull(cotacaoMenorValor)) {
						cotacaoMenorValor = cotacao;
					} else {
						if(cotacao.getValorTotal() < cotacaoMenorValor.getValorTotal()) {
							cotacaoMenorValor = cotacao;
						} else if(cotacao.getValorTotal().equals(cotacaoMenorValor.getValorTotal())) {
							if(cotacao.getDtCotacao().before(cotacaoMenorValor.getDtCotacao())) {
								cotacaoMenorValor = cotacao;
							}
						}
					}
				}
				
				cotacaoMenorValor.setMelhorOpcao(Boolean.TRUE);
				daoCotacao.alterar(cotacaoMenorValor);
				
				for(Cotacao cotacao : cotacoes) {
					if(!cotacao.getId().equals(cotacaoMenorValor.getId())) {
						cotacao.setMelhorOpcao(Boolean.FALSE);
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
	
}
