package br.com.grupojcr.business;

import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.totvs.technology.ecm.workflow.ws.ECMWorkflowEngineServiceServiceLocator;
import com.totvs.technology.ecm.workflow.ws.ECMWorkflowEngineServiceServiceSoapBindingStub;

import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class FluigBusiness {
	
	protected static Logger LOG = Logger.getLogger(FluigBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
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

}
