package br.com.grupojcr.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.grupojcr.business.UsuarioBusiness;
import br.com.grupojcr.dto.FiltroUsuario;
import br.com.grupojcr.entity.datamodel.UsuarioDataModel;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewScoped
@ControllerExceptionHandler
public class UsuarioController implements Serializable {

	protected static Logger LOG = Logger.getLogger(UsuarioController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	private static final long serialVersionUID = 764194435849716691L;
	
	private FiltroUsuario filtro;
	
	private Boolean exibirResultado;
	
	@EJB
	private UsuarioBusiness usuarioBusiness;
	
	@Inject
	private UsuarioDataModel dataModel;
	
	
	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setFiltro(new FiltroUsuario());
			getFiltro().setAtivo(3);
//		} catch (ApplicationException e) {
//			LOG.info(e.getMessage(), e);
//			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	/**
	 * Método responsavel por realizar a pesquisa de nfs-e
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @throws ApplicationException
	 */
	public void pesquisar() throws ApplicationException {
		try {
			
			if(usuarioBusiness.obterQtdUsuario(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
				throw new ApplicationException("message.datatable.noRecords", FacesMessage.SEVERITY_WARN);
			}
			
			dataModel.setFiltro(getFiltro());
			setExibirResultado(Boolean.TRUE);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "pesquisar" }, e);
		}
	}
	
	/**
	 * Método responsavel por voltar para tela de pesquisa
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @return String
	 */
	public String voltar() {
		return "/pages/nfse/listar_nfse.xhtml?faces-redirect=true";
	}

	public FiltroUsuario getFiltro() {
		return filtro;
	}
	public void setFiltro(FiltroUsuario filtro) {
		this.filtro = filtro;
	}

	public Boolean getExibirResultado() {
		return exibirResultado;
	}

	public void setExibirResultado(Boolean exibirResultado) {
		this.exibirResultado = exibirResultado;
	}

	public UsuarioDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(UsuarioDataModel dataModel) {
		this.dataModel = dataModel;
	}

}
