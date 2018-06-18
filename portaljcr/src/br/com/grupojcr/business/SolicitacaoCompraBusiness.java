package br.com.grupojcr.business;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.SolicitacaoCompraDAO;
import br.com.grupojcr.dao.SolicitacaoCompraItemDAO;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.dto.SolicitacaoCompraDTO;
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
			solicitacao.setValorAproximado(dto.getValorAproximado());
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
	
}
