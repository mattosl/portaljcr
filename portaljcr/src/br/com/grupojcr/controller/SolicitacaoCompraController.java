package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.GrupoCotacaoBusiness;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class SolicitacaoCompraController implements Serializable {
	
	private static final long serialVersionUID = 901951337689947636L;
	
	protected static Logger LOG = Logger.getLogger(SolicitacaoCompraController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private List<Coligada> listaColigada;
	private List<GrupoCotacao> listaGrupoCotacao;
	
	private SolicitacaoCompra solicitacaoCompra;
	
	private Usuario usuario;
	
	@EJB
	private GrupoCotacaoBusiness grupoCotacaoBusiness;

	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setSolicitacaoCompra(new SolicitacaoCompra());
			getSolicitacaoCompra().setPossuiGrupoCotacao(Boolean.TRUE);
			setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			setListaColigada(new ArrayList<Coligada>());
			if(Util.isNotNull(getUsuario().getColigadas())) {
				for(Coligada coligada : getUsuario().getColigadas()) {
					if(coligada.getSituacao()) {
						getListaColigada().add(coligada);
					}
				}
			}
			setListaGrupoCotacao(grupoCotacaoBusiness.listarGruposAtivos());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	/**
	 * Método responsavel por prosseguir solicitação de compra
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public String prosseguir() throws ApplicationException {
		try {
			return "/pages/solicitacaoCompra/solicitacao/nova_solicitacao.xhtml?faces-redirect=true";
//		} catch (ApplicationException e) {
//			LOG.info(e.getMessage(), e);
//			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "prosseguir" }, e);
		}
	}

	public List<Coligada> getListaColigada() {
		return listaColigada;
	}

	public void setListaColigada(List<Coligada> listaColigada) {
		this.listaColigada = listaColigada;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public SolicitacaoCompra getSolicitacaoCompra() {
		return solicitacaoCompra;
	}

	public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
		this.solicitacaoCompra = solicitacaoCompra;
	}

	public List<GrupoCotacao> getListaGrupoCotacao() {
		return listaGrupoCotacao;
	}

	public void setListaGrupoCotacao(List<GrupoCotacao> listaGrupoCotacao) {
		this.listaGrupoCotacao = listaGrupoCotacao;
	}

}
