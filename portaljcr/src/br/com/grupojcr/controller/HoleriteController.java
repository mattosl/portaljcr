package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.HoleriteBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.HoleriteDTO;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class HoleriteController implements Serializable {
	
	private static final long serialVersionUID = -5499017085363361305L;
	protected static Logger LOG = Logger.getLogger(HoleriteController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private String chapa;
	private String chave;
	
	private List<HoleriteDTO> listaHolerite;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private HoleriteBusiness holeriteBusiness;

	public String autenticarUsuario() throws ApplicationException {
		try {
			if(Util.isBlank(getChapa())) {
				throw new ApplicationException("message.empty", new String[] {"Login ou Senha inválidas."}, FacesMessage.SEVERITY_WARN);
			}
			
			if(Util.isBlank(getChave())) {
				throw new ApplicationException("message.empty", new String[] {"Chapa ou Senha inválidas."}, FacesMessage.SEVERITY_WARN);
			}
			
			try {
				rmBusiness.autenticarUsuario(getChapa(), getChave());
			} catch (ApplicationException e) {
				throw new ApplicationException("message.empty", new String[] {"Chapa ou Senha inválidas."}, FacesMessage.SEVERITY_WARN);
			}
				
			setListaHolerite(holeriteBusiness.listarHolerite(getChapa(), getChave()));
			
			return "/pages/recursosHumanos/holerite/listar_holerite.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autenticarUsuario" }, e);
		}
	}


	public String getChapa() {
		return chapa;
	}


	public void setChapa(String chapa) {
		this.chapa = chapa;
	}


	public String getChave() {
		return chave;
	}


	public void setChave(String chave) {
		this.chave = chave;
	}


	public List<HoleriteDTO> getListaHolerite() {
		return listaHolerite;
	}


	public void setListaHolerite(List<HoleriteDTO> listaHolerite) {
		this.listaHolerite = listaHolerite;
	}
	

}
