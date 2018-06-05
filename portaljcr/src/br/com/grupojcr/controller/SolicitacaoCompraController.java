package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.GrupoCotacaoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.CentroCustoRM;
import br.com.grupojcr.dto.SolicitacaoCompraDTO;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.enumerator.PrioridadeSolicitacaoCompra;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
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
	private List<CentroCustoRM> listaCentroCusto;
	private List<NaturezaOrcamentariaRM> listaNaturezaOrcamentaria;
	private List<Modalidade> listaModalidade;
	private List<PrioridadeSolicitacaoCompra> listaPrioridade;
	
	private SolicitacaoCompraDTO solicitacaoCompraDTO;
	private Usuario usuario;
	
	@EJB
	private GrupoCotacaoBusiness grupoCotacaoBusiness;
	
	@EJB
	private RMBusiness rmBusiness;

	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setSolicitacaoCompraDTO(new SolicitacaoCompraDTO());
			getSolicitacaoCompraDTO().setPossuiGrupoCotacao(Boolean.TRUE);
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
			if(Util.isNull(solicitacaoCompraDTO.getColigada())) {
				throw new ApplicationException("message.empty", new String[] {"Favor selecionar para qual empresa será realizada a cotação."}, FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(solicitacaoCompraDTO.getPossuiGrupoCotacao())) {
				throw new ApplicationException("message.empty", new String[] {"Favor selecionar quem irá realizar a cotação."}, FacesMessage.SEVERITY_WARN);
			}
			if(solicitacaoCompraDTO.getPossuiGrupoCotacao()) {
				if(Util.isNull(solicitacaoCompraDTO.getGrupoCotacao())) {
					throw new ApplicationException("message.empty", new String[] {"Favor selecionar qual grupo de cotação irá realizar as cotações."}, FacesMessage.SEVERITY_WARN);
				}
			} else {
				getSolicitacaoCompraDTO().setUsuarioCotacao((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			}
			
			setListaCentroCusto(rmBusiness.listarCentroCustoPorColigada(getSolicitacaoCompraDTO().getColigada().getId()));
			setListaNaturezaOrcamentaria(rmBusiness.listarNaturezaOrcamentaria());
			setListaModalidade(new ArrayList<Modalidade>(Arrays.asList(Modalidade.values())));
			setListaPrioridade(new ArrayList<PrioridadeSolicitacaoCompra>(Arrays.asList(PrioridadeSolicitacaoCompra.values())));
			getSolicitacaoCompraDTO().setModalidade(Modalidade.MATERIAL);
			return "/pages/solicitacaoCompra/solicitacao/nova_solicitacao.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
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

	public List<GrupoCotacao> getListaGrupoCotacao() {
		return listaGrupoCotacao;
	}

	public void setListaGrupoCotacao(List<GrupoCotacao> listaGrupoCotacao) {
		this.listaGrupoCotacao = listaGrupoCotacao;
	}

	public List<CentroCustoRM> getListaCentroCusto() {
		return listaCentroCusto;
	}

	public void setListaCentroCusto(List<CentroCustoRM> listaCentroCusto) {
		this.listaCentroCusto = listaCentroCusto;
	}

	public SolicitacaoCompraDTO getSolicitacaoCompraDTO() {
		return solicitacaoCompraDTO;
	}

	public void setSolicitacaoCompraDTO(SolicitacaoCompraDTO solicitacaoCompraDTO) {
		this.solicitacaoCompraDTO = solicitacaoCompraDTO;
	}

	public List<NaturezaOrcamentariaRM> getListaNaturezaOrcamentaria() {
		return listaNaturezaOrcamentaria;
	}

	public void setListaNaturezaOrcamentaria(List<NaturezaOrcamentariaRM> listaNaturezaOrcamentaria) {
		this.listaNaturezaOrcamentaria = listaNaturezaOrcamentaria;
	}

	public List<Modalidade> getListaModalidade() {
		return listaModalidade;
	}

	public void setListaModalidade(List<Modalidade> listaModalidade) {
		this.listaModalidade = listaModalidade;
	}

	public List<PrioridadeSolicitacaoCompra> getListaPrioridade() {
		return listaPrioridade;
	}

	public void setListaPrioridade(List<PrioridadeSolicitacaoCompra> listaPrioridade) {
		this.listaPrioridade = listaPrioridade;
	}

}
