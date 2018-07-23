package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import br.com.grupojcr.business.ColigadaBusiness;
import br.com.grupojcr.business.OrcamentoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.FiltroOrcamento;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.AjusteOrcamentarioDataModel;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class AjusteOrcamentarioController implements Serializable {
	
	private static final long serialVersionUID = -5844434468020235131L;
	protected static Logger LOG = Logger.getLogger(AjusteOrcamentarioController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private Boolean exibirResultado;
	
	private FiltroOrcamento filtro;
	
	private List<Coligada> listaColigada;
	
	private List<CentroCustoRM> listaCentroCusto;
	
	private List<NaturezaOrcamentariaRM> listaNaturezaOrcamentaria;
	
	private AjusteOrcamentarioDTO itemAjuste;
	
	@Inject
	private AjusteOrcamentarioDataModel dataModel;
	
	@EJB
	private OrcamentoBusiness orcamentoBusiness;
	
	@EJB
	private ColigadaBusiness coligadaBusiness;
	
	@EJB
	private RMBusiness rmBusiness;
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			setExibirResultado(Boolean.FALSE);
			setFiltro(new FiltroOrcamento());
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			getFiltro().setUsuarioLogado(usuario);
			getFiltro().setDtAjuste(Calendar.getInstance().getTime());
			
			setListaColigada(new ArrayList<Coligada>());
			if (Util.isNotNull(usuario.getColigadas())) {
				for (Coligada coligada : usuario.getColigadas()) {
					if (coligada.getSituacao()) {
						getListaColigada().add(coligada);
					}
				}
			}
			
			setListaCentroCusto(new ArrayList<CentroCustoRM>());
			
			if(orcamentoBusiness.obterQtdAjusteOrcamentario(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
			} else {
				DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tableAjuste");
				if(Util.isNotNull(dt)) {
					dt.setFirst(0);
				}
				dataModel.setFiltro(getFiltro());
				setExibirResultado(Boolean.TRUE);
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	public void carregarCentroCusto() throws ApplicationException {
		try {
			if(Util.isNotNull(getFiltro().getColigada())) {
				setListaCentroCusto(rmBusiness.listarCentroCustoPorColigada(getFiltro().getColigada().getId()));
			} else {
				setListaCentroCusto(new ArrayList<CentroCustoRM>());
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarCentroCusto" }, e);
		}
	}
	
	public String iniciarAjusteOrcamentario() throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			setListaColigada(new ArrayList<Coligada>());
			if (Util.isNotNull(usuario.getColigadas())) {
				for (Coligada coligada : usuario.getColigadas()) {
					if (coligada.getSituacao()) {
						getListaColigada().add(coligada);
					}
				}
			}
			
			setListaNaturezaOrcamentaria(rmBusiness.listarNaturezaOrcamentaria());
			
			return "/pages/orcamento/ajusteOrcamentario/editar_ajusteOrcamentario.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "pesquisar" }, e);
		}
	}
	

	public Boolean getExibirResultado() {
		return exibirResultado;
	}

	public void setExibirResultado(Boolean exibirResultado) {
		this.exibirResultado = exibirResultado;
	}

	public FiltroOrcamento getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroOrcamento filtro) {
		this.filtro = filtro;
	}

	public List<Coligada> getListaColigada() {
		return listaColigada;
	}

	public void setListaColigada(List<Coligada> listaColigada) {
		this.listaColigada = listaColigada;
	}

	public List<CentroCustoRM> getListaCentroCusto() {
		return listaCentroCusto;
	}

	public void setListaCentroCusto(List<CentroCustoRM> listaCentroCusto) {
		this.listaCentroCusto = listaCentroCusto;
	}

	public List<NaturezaOrcamentariaRM> getListaNaturezaOrcamentaria() {
		return listaNaturezaOrcamentaria;
	}

	public void setListaNaturezaOrcamentaria(List<NaturezaOrcamentariaRM> listaNaturezaOrcamentaria) {
		this.listaNaturezaOrcamentaria = listaNaturezaOrcamentaria;
	}

	public AjusteOrcamentarioDTO getItemAjuste() {
		return itemAjuste;
	}

	public void setItemAjuste(AjusteOrcamentarioDTO itemAjuste) {
		this.itemAjuste = itemAjuste;
	}


}
