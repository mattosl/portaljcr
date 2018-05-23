package br.com.grupojcr.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import br.com.grupojcr.business.LoginBusiness;
import br.com.grupojcr.business.UsuarioBusiness;
import br.com.grupojcr.dto.FiltroUsuario;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.UsuarioDataModel;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class UsuarioController implements Serializable {

	protected static Logger LOG = Logger.getLogger(UsuarioController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	private static final long serialVersionUID = 764194435849716691L;
	
	private FiltroUsuario filtro;
	
	private Boolean exibirResultado;
	
	private Usuario usuario;
	
	@EJB
	private UsuarioBusiness usuarioBusiness;
	
	@EJB
	private LoginBusiness loginBusiness;
	
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
			
			DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tableUsuario");
			dt.setFirst(0);
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
	 * Método responsavel por sincronizar com AD os usuários
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @throws ApplicationException
	 */
	public void sincronizar() throws ApplicationException {
		try {
			loginBusiness.sincronizarUsuarios();
			Message.setMessage("usuario.sincronizado");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "sincronizar" }, e);
		}
	}
	
	/**
	 * Método responsavel por carregar tela de edição
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	public String iniciarEditar() throws ApplicationException {
		try {
			return "/pages/administrador/usuario/editar_usuario.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarEditar" }, e);
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
