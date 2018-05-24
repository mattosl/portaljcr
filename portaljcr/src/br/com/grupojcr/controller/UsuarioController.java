package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.DualListModel;

import br.com.grupojcr.business.ColigadaBusiness;
import br.com.grupojcr.business.GrupoBusiness;
import br.com.grupojcr.business.LoginBusiness;
import br.com.grupojcr.business.UsuarioBusiness;
import br.com.grupojcr.dto.FiltroUsuario;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Grupo;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.UsuarioDataModel;
import br.com.grupojcr.util.Util;
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
	private Grupo grupo;
	private Coligada coligada;
	
	private DualListModel<Grupo> dualListGrupo;
	private DualListModel<Coligada> dualListColigada;
	
	private List<Grupo> listaGrupo;
	
	private List<Coligada> listaColigada;
	
	
	@EJB
	private UsuarioBusiness usuarioBusiness;
	
	@EJB
	private GrupoBusiness grupoBusiness;
	
	@EJB
	private ColigadaBusiness coligadaBusiness;
	
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
			carregarGrupos();
			carregarColigadas();
			return "/pages/administrador/usuario/editar_usuario.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarEditar" }, e);
		}
	}
	
	public String salvar() throws ApplicationException {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().getFlash().setKeepMessages(true);
			
			usuarioBusiness.alterar(getUsuario());
			
			Message.setMessage("usuario.salvar");
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
		return voltar();
	}
	
	/**
	 * Método responsavel por carregar os grupos do usuário
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	private void carregarGrupos() throws ApplicationException {
		try {
			List<Grupo> grupoSistema = grupoBusiness.listar();
			List<Grupo> grupoDisponivel = new ArrayList<Grupo>();
			
			
			if(Util.isNotNull(getUsuario().getGrupos())) {
				if(CollectionUtils.isNotEmpty(grupoSistema)) {
					for(Grupo g : grupoSistema) {
						if(!getUsuario().getGrupos().contains(g)) {
							grupoDisponivel.add(g);
						}
					}
				}
			} else {
				grupoDisponivel.addAll(grupoSistema);
			}
			
			
			setListaGrupo(grupoDisponivel);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarGrupos" }, e);
		}
	}
	
	public void adicionarGrupo() throws ApplicationException {
		try {
			if(Util.isNull(getGrupo())) {
				throw new ApplicationException("usuario.grupo.obrigatorio", FacesMessage.SEVERITY_WARN);
			}
			
			getUsuario().getGrupos().add(getGrupo());
			
			getListaGrupo().remove(getGrupo());
			setGrupo(null);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarGrupo" }, e);
		}
	}
	
	public void adicionarColigada() throws ApplicationException {
		try {
			if(Util.isNull(getColigada())) {
				throw new ApplicationException("usuario.coligada.obrigatorio", FacesMessage.SEVERITY_WARN);
			}
			
			getUsuario().getColigadas().add(getColigada());
			
			getListaColigada().remove(getColigada());
			setColigada(null);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarColigada" }, e);
		}
	}
	
	public void removerGrupo(Grupo grupo) throws ApplicationException {
		try {
			getUsuario().getGrupos().remove(grupo);
			
			getListaGrupo().add(grupo);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "removerGrupo" }, e);
		}
	}
	
	public void removerColigada(Coligada coligada) throws ApplicationException {
		try {
			getUsuario().getColigadas().remove(coligada);
			
			getListaColigada().add(coligada);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "removerColigada" }, e);
		}
	}
	
	/**
	 * Método responsavel por carregar as coligadas do usuário
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	private void carregarColigadas() throws ApplicationException {
		try {
			List<Coligada> coligadaSistema = coligadaBusiness.listarColigadas();
			List<Coligada> coligadaDisponivel = new ArrayList<Coligada>();
			
			
			if(Util.isNotNull(getUsuario().getGrupos())) {
				if(CollectionUtils.isNotEmpty(coligadaSistema)) {
					for(Coligada c : coligadaSistema) {
						if(!getUsuario().getColigadas().contains(c)) {
							coligadaDisponivel.add(c);
						}
					}
				}
			} else {
				coligadaDisponivel.addAll(coligadaSistema);
			}
			
			
			setListaColigada(coligadaDisponivel);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarColigadas" }, e);
		}
	}
	
	/**
	 * Método responsavel por voltar para tela de pesquisa
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @return String
	 */
	public String voltar() {
		return "/pages/administrador/usuario/listar_usuario.xhtml?faces-redirect=true";
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

	public DualListModel<Grupo> getDualListGrupo() {
		return dualListGrupo;
	}

	public void setDualListGrupo(DualListModel<Grupo> dualListGrupo) {
		this.dualListGrupo = dualListGrupo;
	}

	public DualListModel<Coligada> getDualListColigada() {
		return dualListColigada;
	}

	public void setDualListColigada(DualListModel<Coligada> dualListColigada) {
		this.dualListColigada = dualListColigada;
	}

	public List<Grupo> getListaGrupo() {
		return listaGrupo;
	}

	public void setListaGrupo(List<Grupo> listaGrupo) {
		this.listaGrupo = listaGrupo;
	}

	public List<Coligada> getListaColigada() {
		return listaColigada;
	}

	public void setListaColigada(List<Coligada> listaColigada) {
		this.listaColigada = listaColigada;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Coligada getColigada() {
		return coligada;
	}

	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}

}
