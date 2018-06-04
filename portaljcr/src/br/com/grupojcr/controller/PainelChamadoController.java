package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.ChamadoBusiness;
import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class PainelChamadoController implements Serializable {
	
	private static final long serialVersionUID = -1877858013712973138L;
	protected static Logger LOG = Logger.getLogger(PainelChamadoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private List<Chamado> listaChamado;
	
	@EJB
	private ChamadoBusiness chamadoBusiness;
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			setListaChamado(chamadoBusiness.listarChamadosPendentes());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}

	public List<Chamado> getListaChamado() {
		return listaChamado;
	}

	public void setListaChamado(List<Chamado> listaChamado) {
		this.listaChamado = listaChamado;
	}

}
