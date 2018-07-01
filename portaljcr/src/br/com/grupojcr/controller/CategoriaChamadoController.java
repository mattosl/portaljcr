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

import br.com.grupojcr.business.CategoriaChamadoBusiness;
import br.com.grupojcr.entity.CategoriaChamado;
import br.com.grupojcr.entity.SubCategoriaChamado;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class CategoriaChamadoController implements Serializable {
	
	private static final long serialVersionUID = 934937300949501891L;
	protected static Logger LOG = Logger.getLogger(CategoriaChamadoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private List<CategoriaChamado> listaCategoria;
	
	private CategoriaChamado categoriaChamado;
	
	private SubCategoriaChamado subCategoria;
	
	@EJB
	private CategoriaChamadoBusiness categoriaChamadoBusiness;

	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setListaCategoria(categoriaChamadoBusiness.listarCategoriaChamado());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	/**
	 * Método responsavel por carregar tela de criação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 23/05/2018
	 * @throws ApplicationException
	 */
	public String novo() throws ApplicationException {
		try {
			setCategoriaChamado(new CategoriaChamado());
			getCategoriaChamado().setSubCategorias(new HashSet<SubCategoriaChamado>());
			setSubCategoria(new SubCategoriaChamado());
			return "/pages/administrador/suporte/categoriaChamado/editar_categoriaChamado.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "novo" }, e);
		}
	}
	
	public void adicionarSubcategoria() throws ApplicationException {
		try {
			if(Util.isBlank(getSubCategoria().getNome())) {
				throw new ApplicationException("categoria.chamado.subcategoria.nome.obrigatorio", FacesMessage.SEVERITY_WARN);
			}
			
			for(SubCategoriaChamado scc : getCategoriaChamado().getSubCategorias() ) {
				if(scc.getNome().equalsIgnoreCase(getSubCategoria().getNome())) {
					throw new ApplicationException("categoria.chamado.subcategoria.existente", FacesMessage.SEVERITY_WARN);
				}
			}

			getCategoriaChamado().getSubCategorias().add(getSubCategoria());
			
			setSubCategoria(new SubCategoriaChamado());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarCategoriaChamado" }, e);
		}
	}
	
	public void excluirSubcategoria(SubCategoriaChamado subcategoria) throws ApplicationException {
		try {
			if(Util.isNotNull(subcategoria)) {
				getCategoriaChamado().getSubCategorias().remove(subcategoria);
			}
			
			setSubCategoria(new SubCategoriaChamado());
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirSubcategoria" }, e);
		}
	}
	
	public String salvar() throws ApplicationException {
		try {
			
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().getFlash().setKeepMessages(true);
			
			Boolean novo = Util.isNull(getCategoriaChamado().getId());
			
			categoriaChamadoBusiness.salvar(getCategoriaChamado());
			
			if(novo) {
				Message.setMessage("categoria.chamado.salvar.incluir");
			} else {
				Message.setMessage("categoria.chamado.salvar.alterar");
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
	
	public String iniciarEditar() throws ApplicationException {
		try {
			setSubCategoria(new SubCategoriaChamado());
			return "/pages/administrador/suporte/categoriaChamado/editar_categoriaChamado.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarEditar" }, e);
		}
	}
	
	public String voltar() {
		return "/pages/administrador/suporte/categoriaChamado/listar_categoriaChamado.xhtml?faces-redirect=true";
	}

	public List<CategoriaChamado> getListaCategoria() {
		return listaCategoria;
	}

	public void setListaCategoria(List<CategoriaChamado> listaCategoria) {
		this.listaCategoria = listaCategoria;
	}

	public CategoriaChamado getCategoriaChamado() {
		return categoriaChamado;
	}

	public void setCategoriaChamado(CategoriaChamado categoriaChamado) {
		this.categoriaChamado = categoriaChamado;
	}

	public SubCategoriaChamado getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoriaChamado subCategoria) {
		this.subCategoria = subCategoria;
	}

}
