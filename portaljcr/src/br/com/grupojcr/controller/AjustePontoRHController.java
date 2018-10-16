package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.PontoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class AjustePontoRHController implements Serializable {
	
	private static final long serialVersionUID = -7946061706484820602L;
	protected static Logger LOG = Logger.getLogger(AjustePontoRHController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private List<Usuario> listaUsuarioChapa;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private PontoBusiness pontoBusiness;
	
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			setListaUsuarioChapa(pontoBusiness.listarUsuariosUtilizamPonto());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	public String voltar() throws ApplicationException {
		try {
			return "/pages/recursosHumanos/ajustarPonto/rh/listar_ajustarPonto.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltar" }, e);
		}
	}


	public List<Usuario> getListaUsuarioChapa() {
		return listaUsuarioChapa;
	}


	public void setListaUsuarioChapa(List<Usuario> listaUsuarioChapa) {
		this.listaUsuarioChapa = listaUsuarioChapa;
	}

}
