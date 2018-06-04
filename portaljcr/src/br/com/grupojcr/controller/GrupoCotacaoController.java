package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.GrupoCotacaoBusiness;
import br.com.grupojcr.business.UsuarioBusiness;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class GrupoCotacaoController implements Serializable {

	private static final long serialVersionUID = -3922718803256194459L;
	
	protected static Logger LOG = Logger.getLogger(GrupoCotacaoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private List<GrupoCotacao> listaGrupoCotacao;
	private List<Usuario> listaUsuario;
	
	private GrupoCotacao grupoCotacao;
	private Usuario usuario;
	
	@EJB
	private UsuarioBusiness usuarioBusiness;
	
	@EJB
	private GrupoCotacaoBusiness grupoCotacaoBusiness;
	
	
	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 21/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setListaGrupoCotacao(grupoCotacaoBusiness.listar());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	/**
	 * Método responsavel por salvar grupos de cotação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 21/05/2018
	 * @throws ApplicationException
	 */
	public String salvar() throws ApplicationException {
		try {
			
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().getFlash().setKeepMessages(true);
			
			Boolean novo = Util.isNull(getGrupoCotacao().getId());
			
			grupoCotacaoBusiness.salvar(getGrupoCotacao());
			
			if(novo) {
				Message.setMessage("grupo.cotacao.salvar.incluir");
			} else {
				Message.setMessage("grupo.cotacao.salvar.alterar");
			}
			
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
	 * Método responsavel por carregar tela de criação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	public String novo() throws ApplicationException {
		try {
			setGrupoCotacao(new GrupoCotacao());
			getGrupoCotacao().setUsuarios(new HashSet<Usuario>());
			return "/pages/administrador/grupoCotacao/editar_grupoCotacao.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "novo" }, e);
		}
	}
	
	/**
	 * Método responsavel pelo autocomplete de usuário
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	public List<Usuario> autocompleteUsuario(String nome) throws ApplicationException {
		try {
			return usuarioBusiness.listarUsuarioPorNome(nome);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autocompleteUsuario" }, e);
		}
	}

	/**
	 * Método responsavel por adicionar usuário do grupo de cotação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	public void adicionarUsuario() throws ApplicationException {
		try {
			if(Util.isNull(getUsuario())) {
				throw new ApplicationException("grupo.cotacao.usuario.obrigatorio", FacesMessage.SEVERITY_WARN);
			}

			if(getGrupoCotacao().getUsuarios().contains(getUsuario())) {
				throw new ApplicationException("grupo.cotacao.usuario.vinculado", FacesMessage.SEVERITY_WARN);
			}
			getGrupoCotacao().getUsuarios().add(getUsuario());
			
			setUsuario(null);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarUsuario" }, e);
		}
	}

	/**
	 * Método responsavel por remover usuário do grupo de cotação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	public void excluirUsuario(Usuario usuario) throws ApplicationException {
		try {
			if(Util.isNotNull(usuario)) {
				getGrupoCotacao().getUsuarios().remove(usuario);
			}
			
			// TODO Fazer regra de solicitação de compra para cotação
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirUsuario" }, e);
		}
	}
	
	/**
	 * Método responsavel por iniciar a edição do grupo de cotação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	public String iniciarEditar() throws ApplicationException {
		try {
			return "/pages/administrador/grupoCotacao/editar_grupoCotacao.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarEditar" }, e);
		}
	}
	
	/**
	 * Método responsavel por ativar/inativar
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 22/05/2018
	 * @param coligada : Coligada
	 * @throws ApplicationException
	 */
	public void ativarInativar(GrupoCotacao grupoCotacao) throws ApplicationException {
		try {
			if(grupoCotacao.getSituacao()) {
				grupoCotacaoBusiness.ativar(grupoCotacao);
				Message.setMessage("grupo.cotacao.ativar");
			} else {
				grupoCotacaoBusiness.inativar(grupoCotacao);
				Message.setMessage("grupo.cotacao.inativar");
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "ativarInativar" }, e);
		}
	}
	
	/**
	 * Método responsavel por voltar para tela de pesquisa
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @return String
	 */
	public String voltar() {
		return "/pages/administrador/grupoCotacao/listar_grupoCotacao.xhtml?faces-redirect=true";
	}

	public List<GrupoCotacao> getListaGrupoCotacao() {
		return listaGrupoCotacao;
	}

	public void setListaGrupoCotacao(List<GrupoCotacao> listaGrupoCotacao) {
		this.listaGrupoCotacao = listaGrupoCotacao;
	}

	public GrupoCotacao getGrupoCotacao() {
		return grupoCotacao;
	}

	public void setGrupoCotacao(GrupoCotacao grupoCotacao) {
		this.grupoCotacao = grupoCotacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<Usuario> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}
	
}
