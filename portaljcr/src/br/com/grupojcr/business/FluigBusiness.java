package br.com.grupojcr.business;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.totvs.technology.ecm.dm.ws.ECMDashBoardServiceServiceLocator;
import com.totvs.technology.ecm.dm.ws.ECMDashBoardServiceServiceSoapBindingStub;
import com.totvs.technology.ecm.dm.ws.WorkflowProcessDto;
import com.totvs.technology.ecm.workflow.ws.ECMWorkflowEngineServiceServiceLocator;
import com.totvs.technology.ecm.workflow.ws.ECMWorkflowEngineServiceServiceSoapBindingStub;

import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dao.SolicitacaoCompraDAO;
import br.com.grupojcr.dto.AprovacaoContratoDTO;
import br.com.grupojcr.dto.AprovacaoOrdemCompraDTO;
import br.com.grupojcr.dto.AprovacaoSolicitacaoCompraDTO;
import br.com.grupojcr.dto.SolicitacaoAprovacaoDTO;
import br.com.grupojcr.dto.ZMDRMFLUIGDTO;
import br.com.grupojcr.entity.SolicitacaoCompra;
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
	
	public String[][] iniciarProcessoFluig(String nomeProcesso, String usrAprova, Integer idAtividade, String[][] parametros) throws ApplicationException {
		try {
			ECMWorkflowEngineServiceServiceSoapBindingStub cliente = obterProxyECMWorkFlowEngineService();
			
			return cliente.startProcess("fluig_admin", "Flu1g@dm1m", 1, nomeProcesso, idAtividade,
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
			
			cliente.cancelInstance("fluig_admin", "Flu1g@dm1m", 1, idProcesso.intValue(), "fluig_admin", textoCancelamento);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "cancelarProcessoFluig" }, e);
		}
	}
	
	public SolicitacaoAprovacaoDTO listarSolicitacoesAprovacao(String usuario) {
		try {
			if(TreatString.isNotBlank(usuario)) {
				ECMDashBoardServiceServiceSoapBindingStub cliente = obterProxyECMDashBoardService();
				WorkflowProcessDto[] solicitacoes = cliente.findWorkflowTasks("fluig_admin", "Flu1g@dm1m", 1, usuario);
				
				SolicitacaoAprovacaoDTO solicitacao = new SolicitacaoAprovacaoDTO();
				solicitacao.setContratos(new ArrayList<AprovacaoContratoDTO>());
				solicitacao.setOrdemCompras(new ArrayList<AprovacaoOrdemCompraDTO>());
				solicitacao.setSolicitacoes(new ArrayList<AprovacaoSolicitacaoCompraDTO>());
				Integer qtdContrato = 0;
				Integer qtdOrdemCompra = 0;
				Integer qtdSolicitacaoCompra = 0;
				
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
							}
						}
					}
				}
				
				solicitacao.setQtdContratos(qtdContrato);
				solicitacao.setQtdOrdemCompra(qtdOrdemCompra);
				solicitacao.setQtdSolicitacaoCompra(qtdSolicitacaoCompra);
				
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
				
				return solicitacao;
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
		return null;
	}

}
