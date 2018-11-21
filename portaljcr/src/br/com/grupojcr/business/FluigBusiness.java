package br.com.grupojcr.business;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.totvs.technology.ecm.dm.ws.CardFieldDto;
import com.totvs.technology.ecm.dm.ws.ECMCardServiceServiceLocator;
import com.totvs.technology.ecm.dm.ws.ECMCardServiceServiceSoapBindingStub;
import com.totvs.technology.ecm.dm.ws.ECMDashBoardServiceServiceLocator;
import com.totvs.technology.ecm.dm.ws.ECMDashBoardServiceServiceSoapBindingStub;
import com.totvs.technology.ecm.dm.ws.WorkflowProcessDto;
import com.totvs.technology.ecm.foundation.ws.ColleagueDto;
import com.totvs.technology.ecm.foundation.ws.ECMColleagueServiceServiceLocator;
import com.totvs.technology.ecm.foundation.ws.ECMColleagueServiceServiceSoapBindingStub;
import com.totvs.technology.ecm.workflow.ws.ECMWorkflowEngineServiceServiceLocator;
import com.totvs.technology.ecm.workflow.ws.ECMWorkflowEngineServiceServiceSoapBindingStub;

import br.com.grupojcr.dao.AjustePontoDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dao.SolicitacaoCompraDAO;
import br.com.grupojcr.dto.AprovacaoContratoDTO;
import br.com.grupojcr.dto.AprovacaoOrdemCompraDTO;
import br.com.grupojcr.dto.AprovacaoPontoDTO;
import br.com.grupojcr.dto.AprovacaoSolicitacaoCompraDTO;
import br.com.grupojcr.dto.SolicitacaoAprovacaoDTO;
import br.com.grupojcr.dto.ZMDRMFLUIGDTO;
import br.com.grupojcr.entity.AjustePonto;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.util.Preferencias;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.Preferencias.Propriedades;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class FluigBusiness {
	
	protected static Logger LOG = Logger.getLogger(FluigBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private RMDAO daoRM;
	
	@EJB
	private SolicitacaoCompraDAO daoSolicitacaoCompra;
	
	@EJB
	private AjustePontoDAO daoAjustePonto;
	
	private ECMWorkflowEngineServiceServiceSoapBindingStub obterProxyECMWorkFlowEngineService() throws ServiceException {
		try {
			ECMWorkflowEngineServiceServiceLocator locator = new ECMWorkflowEngineServiceServiceLocator();
			ECMWorkflowEngineServiceServiceSoapBindingStub cliente = (ECMWorkflowEngineServiceServiceSoapBindingStub) locator
					.getWorkflowEngineServicePort();
			return cliente;
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw e;
		}
	}
	
	private ECMColleagueServiceServiceSoapBindingStub obterProxyECMColleagueService() throws ServiceException {
		try {
			ECMColleagueServiceServiceLocator locator = new ECMColleagueServiceServiceLocator();
			ECMColleagueServiceServiceSoapBindingStub cliente = (ECMColleagueServiceServiceSoapBindingStub) locator
					.getColleagueServicePort();
			return cliente;
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw e;
		}
	}
	
	private ECMDashBoardServiceServiceSoapBindingStub obterProxyECMDashBoardService() throws ServiceException {
		LOG.info("[obterProxyECMDashBoardService] Método iniciado.");
		try {
			ECMDashBoardServiceServiceLocator locator = new ECMDashBoardServiceServiceLocator();
			ECMDashBoardServiceServiceSoapBindingStub cliente = (ECMDashBoardServiceServiceSoapBindingStub) locator
					.getDashBoardServicePort();
			LOG.info("[obterProxyECMDashBoardService] Stub obtido.");
			return cliente;
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			throw e;
		}
	}
	
	private ECMCardServiceServiceSoapBindingStub obterProxyECMCardService() throws ServiceException {
		try {
			ECMCardServiceServiceLocator locator = new ECMCardServiceServiceLocator();
			ECMCardServiceServiceSoapBindingStub cliente = (ECMCardServiceServiceSoapBindingStub) locator
					.getCardServicePort();
			return cliente;
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw e;
		}
	}
	
	public String[][] iniciarProcessoFluig(String nomeProcesso, String usrAprova, Integer idAtividade, String[][] parametros) throws ApplicationException {
		try {
			ECMWorkflowEngineServiceServiceSoapBindingStub cliente = obterProxyECMWorkFlowEngineService();
			
			return cliente.startProcess(Preferencias.get(Propriedades.USUARIO_FLUIG), Preferencias.get(Propriedades.CHAVE_FLUIG), 1, nomeProcesso, idAtividade,
					new String[] { usrAprova }, null, "fluig_admin", true, null,
					parametros,
					null, false);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcessoFluig" }, e);
		}
	}
	
	public void cancelarProcessoFluig(Long idProcesso, Long idSolicitacao, String motivoCancelamento, String nomeUsuario) throws ApplicationException {
		try {
			ECMWorkflowEngineServiceServiceSoapBindingStub cliente = obterProxyECMWorkFlowEngineService();
			
			String textoCancelamento = "SOLICITAÇÃO DE COMPRA Nº " + idSolicitacao + " cancelada por: " + nomeUsuario + " [MOTIVO: " + motivoCancelamento + "]";
			
			cliente.cancelInstance(Preferencias.get(Propriedades.USUARIO_FLUIG), Preferencias.get(Propriedades.CHAVE_FLUIG), 1, idProcesso.intValue(), "fluig_admin", textoCancelamento);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "cancelarProcessoFluig" }, e);
		}
	}
	
	public SolicitacaoAprovacaoDTO listarSolicitacoesAprovacao(String usuario) {
		try {
			if(TreatString.isNotBlank(usuario)) {
				ECMDashBoardServiceServiceSoapBindingStub cliente = obterProxyECMDashBoardService();
				WorkflowProcessDto[] solicitacoes = cliente.findWorkflowTasks(Preferencias.get(Propriedades.USUARIO_FLUIG), Preferencias.get(Propriedades.CHAVE_FLUIG), 1, usuario);
				
				SolicitacaoAprovacaoDTO solicitacao = new SolicitacaoAprovacaoDTO();
				solicitacao.setContratos(new ArrayList<AprovacaoContratoDTO>());
				solicitacao.setOrdemCompras(new ArrayList<AprovacaoOrdemCompraDTO>());
				solicitacao.setSolicitacoes(new ArrayList<AprovacaoSolicitacaoCompraDTO>());
				solicitacao.setPontos(new ArrayList<AprovacaoPontoDTO>());
				Integer qtdContrato = 0;
				Integer qtdOrdemCompra = 0;
				Integer qtdSolicitacaoCompra = 0;
				Integer qtdPonto = 0;
				
				if(solicitacoes.length > 0) {
					for(int i = 0; i < solicitacoes.length; i++) {
						ZMDRMFLUIGDTO rmFluig = daoRM.obterLigacaoRMFluig(solicitacoes[i].getProcessInstanceId());
						
						if(rmFluig != null) {
							
							if(Util.isNotNull(rmFluig.getIdCnt()) && !Util.isNullOrZero(rmFluig.getIdCnt())) {
								AprovacaoContratoDTO contrato = daoRM.obterContrato(rmFluig.getIdCnt(), rmFluig.getIdColigada());
								if(Util.isNotNull(contrato)) {
									contrato.setIdFluig(solicitacoes[i].getProcessInstanceId());
									contrato.setTipo(solicitacoes[i].getStateDescription());
									contrato.setSequenciaMovimento(solicitacoes[i].getMovementSequence());
									solicitacao.getContratos().add(contrato);
									qtdContrato++;
								}
							} else if(Util.isNotNull(rmFluig.getIdMovimento()) && !Util.isNullOrZero(rmFluig.getIdMovimento())) {
								AprovacaoOrdemCompraDTO ordemCompra = daoRM.obterOrdemCompra(rmFluig.getIdMovimento(), rmFluig.getIdColigada());
								if(Util.isNotNull(ordemCompra)) {
									ordemCompra.setIdFluig(solicitacoes[i].getProcessInstanceId());
									ordemCompra.setTipo(solicitacoes[i].getStateDescription());
									ordemCompra.setSequenciaMovimento(solicitacoes[i].getMovementSequence());
									solicitacao.getOrdemCompras().add(ordemCompra);
									qtdOrdemCompra++;
								}
							}
						} else {
							SolicitacaoCompra solicitacaoCompra = daoSolicitacaoCompra.obterSolicitacaoPorIdFluig(solicitacoes[i].getProcessInstanceId());
							
							if(solicitacaoCompra != null) {
								AprovacaoSolicitacaoCompraDTO solicitacaoCompraDTO = new AprovacaoSolicitacaoCompraDTO();
								solicitacaoCompraDTO.setNumeroSolicitacao(solicitacaoCompra.getId());
								solicitacaoCompraDTO.setCodigoCentroCusto(solicitacaoCompra.getCodigoCentroCusto());
								solicitacaoCompraDTO.setCentroCusto(solicitacaoCompra.getCentroCusto());
								solicitacaoCompraDTO.setNomeColigada(solicitacaoCompra.getColigada().getRazaoSocial());
								solicitacaoCompraDTO.setRequisitante(solicitacaoCompra.getUsuarioSolicitante().getNome());
								solicitacaoCompraDTO.setValorAproximado(TreatNumber.formatMoneyCurrency(solicitacaoCompra.getValorTotalAproximado()));
								solicitacaoCompraDTO.setIdFluig(solicitacoes[i].getProcessInstanceId());
								solicitacaoCompraDTO.setTipo(solicitacoes[i].getStateDescription());
								solicitacaoCompraDTO.setSequenciaMovimento(solicitacoes[i].getMovementSequence());
								solicitacao.getSolicitacoes().add(solicitacaoCompraDTO);
								qtdSolicitacaoCompra++;
							} else {
								AjustePonto ajustePonto = daoAjustePonto.obterAjustePontoPorIdFluig(solicitacoes[i].getProcessInstanceId());
								
								if(ajustePonto != null) {
									AprovacaoPontoDTO pontoDTO = new AprovacaoPontoDTO();
									pontoDTO.setIdFluig(solicitacoes[i].getProcessInstanceId());
									pontoDTO.setNomeFuncionario(ajustePonto.getUsuario().getNome().toUpperCase());
									pontoDTO.setPeriodo(TreatDate.format("dd/MM/yyyy", ajustePonto.getDtPeriodoInicial()) + " - " + TreatDate.format("dd/MM/yyyy", ajustePonto.getDtPeriodoFinal()));
									pontoDTO.setSequenciaMovimento(solicitacoes[i].getMovementSequence());
									solicitacao.getPontos().add(pontoDTO);
									qtdPonto++;
								}
							}
						}
					}
				}
				
				solicitacao.setQtdContratos(qtdContrato);
				solicitacao.setQtdOrdemCompra(qtdOrdemCompra);
				solicitacao.setQtdSolicitacaoCompra(qtdSolicitacaoCompra);
				solicitacao.setQtdPonto(qtdPonto);
				
				if(qtdContrato > 0) {
					solicitacao.setClasseCSSContratos("badge-danger");
				} else {
					solicitacao.setClasseCSSContratos("");
				}
				
				if(qtdSolicitacaoCompra > 0) {
					solicitacao.setClasseCSSSolicitacaoCompra("badge-danger");
				} else {
					solicitacao.setClasseCSSSolicitacaoCompra("");
				}
				
				if(qtdOrdemCompra > 0) {
					solicitacao.setClasseCSSOrdemCompra("badge-danger");
				} else {
					solicitacao.setClasseCSSOrdemCompra("");
				}
				
				if(qtdPonto > 0) {
					solicitacao.setClasseCSSPonto("badge-danger");
				} else {
					solicitacao.setClasseCSSPonto("");
				}
				
				return solicitacao;
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
		return null;
	}
	
	public String[][] getInstanceCardData(int numeroSolicitacao) throws ApplicationException {
		try {
			ECMWorkflowEngineServiceServiceSoapBindingStub cliente = obterProxyECMWorkFlowEngineService();
			
			return cliente.getInstanceCardData(Preferencias.get(Propriedades.USUARIO_FLUIG), Preferencias.get(Propriedades.CHAVE_FLUIG), 1, Preferencias.get(Propriedades.USUARIO_FLUIG), numeroSolicitacao);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "getInstanceCardData" }, e);
		}
	}
	
	public void updateCardData(int numeroFormulario, CardFieldDto[] cardData) throws ApplicationException {
		try {
			ECMCardServiceServiceSoapBindingStub cliente = obterProxyECMCardService();
			cliente.updateCardData(1, Preferencias.get(Propriedades.USUARIO_FLUIG), Preferencias.get(Propriedades.CHAVE_FLUIG), numeroFormulario, cardData);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "getInstanceCardData" }, e);
		}
	}
	
	public ColleagueDto[] getColleague(String colleague) throws ApplicationException {
		try {
			ECMColleagueServiceServiceSoapBindingStub cliente = obterProxyECMColleagueService();
			return cliente.getColleague(Preferencias.get(Propriedades.USUARIO_FLUIG), Preferencias.get(Propriedades.CHAVE_FLUIG), 1, colleague);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "getInstanceCardData" }, e);
		}
	}

}
