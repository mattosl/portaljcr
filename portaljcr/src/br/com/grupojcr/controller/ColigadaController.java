package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.grupojcr.business.ColigadaBusiness;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewScoped
@ControllerExceptionHandler
public class ColigadaController implements Serializable {

	protected static Logger LOG = Logger.getLogger(ColigadaController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	private static final long serialVersionUID = 764194435849716691L;
	
	@EJB
	private ColigadaBusiness coligadaBusiness;
	
	private List<Coligada> listaColigada;
	
	
	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 21/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setListaColigada(coligadaBusiness.listarColigadas());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	/**
	 * Método responsavel por ativar/inativar
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 22/05/2018
	 * @param coligada : Coligada
	 * @throws ApplicationException
	 */
	public void ativarInativar(Coligada coligada) throws ApplicationException {
		try {
			if(coligada.getSituacao()) {
				coligadaBusiness.ativar(coligada);
				Message.setMessage("coligada.ativar");
			} else {
				coligadaBusiness.inativar(coligada);
				Message.setMessage("coligada.inativar");
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "ativarInativar" }, e);
		}
	}
	
	public List<Coligada> getListaColigada() {
		return listaColigada;
	}


	public void setListaColigada(List<Coligada> listaColigada) {
		this.listaColigada = listaColigada;
	}
	

}
