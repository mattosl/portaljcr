package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import br.com.grupojcr.business.ChamadoBusiness;
import br.com.grupojcr.dto.FiltroRelatorioChamado;
import br.com.grupojcr.entity.datamodel.RelatorioChamadoDataModel;
import br.com.grupojcr.enumerator.CausaChamado;
import br.com.grupojcr.enumerator.PrioridadeChamado;
import br.com.grupojcr.enumerator.SituacaoChamado;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class RelatorioChamadoController implements Serializable {
	
	private static final long serialVersionUID = 934937300949501891L;
	protected static Logger LOG = Logger.getLogger(RelatorioChamadoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private FiltroRelatorioChamado filtro;
	
	private Boolean exibirResultado;
	
	private List<SituacaoChamado> listaSituacaoChamado;
	private List<String> listaCategoria;
	private List<String> listaSubCategoria;
	private List<PrioridadeChamado> listaPrioridadeChamado;
	private List<CausaChamado> listaCausaChamado;
	
	@EJB
	private ChamadoBusiness chamadoBusiness;
	
	@Inject
	private RelatorioChamadoDataModel dataModel;
	
	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setExibirResultado(Boolean.FALSE);
			setFiltro(new FiltroRelatorioChamado());
			carregarDatas();
			
			setListaSituacaoChamado(new ArrayList<SituacaoChamado>(Arrays.asList(SituacaoChamado.values())));
			SituacaoChamado [] situacoes = {SituacaoChamado.ABERTO, SituacaoChamado.EM_ANDAMENTO, SituacaoChamado.RESOLVIDO, SituacaoChamado.FECHADO};
			getFiltro().setSituacao(situacoes);
			
			setListaPrioridadeChamado(new ArrayList<PrioridadeChamado>(Arrays.asList(PrioridadeChamado.values())));
			setListaCausaChamado(new ArrayList<CausaChamado>(Arrays.asList(CausaChamado.values())));
			
			
			if(chamadoBusiness.obterQtdChamadoRelatorio(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
			} else {
				DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tableChamado");
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
	
	private void carregarDatas() throws ApplicationException {
		try {
			Calendar calendarioInicial = Calendar.getInstance();
			calendarioInicial.set(Calendar.DAY_OF_MONTH, calendarioInicial.getActualMinimum(Calendar.DAY_OF_MONTH));
			Calendar calendarioFinal = Calendar.getInstance();
			calendarioFinal.set(Calendar.DAY_OF_MONTH, calendarioFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			getFiltro().setPeriodoInicial(calendarioInicial.getTime());
			getFiltro().setPeriodoFinal(calendarioFinal.getTime());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarDatas" }, e);
		}
	}
	
	public void pesquisar() throws ApplicationException {
		try {
			if(chamadoBusiness.obterQtdChamadoRelatorio(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
				throw new ApplicationException("message.datatable.noRecords", FacesMessage.SEVERITY_WARN);
			}
			
			DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tabelaChamado");
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
	
	public String voltar() throws ApplicationException {
		try {
			return "/pages/suporte/listar_relatorioChamado.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltar" }, e);
		}
	}

	public Boolean getExibirResultado() {
		return exibirResultado;
	}

	public void setExibirResultado(Boolean exibirResultado) {
		this.exibirResultado = exibirResultado;
	}

	public RelatorioChamadoDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(RelatorioChamadoDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public FiltroRelatorioChamado getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroRelatorioChamado filtro) {
		this.filtro = filtro;
	}

	public List<SituacaoChamado> getListaSituacaoChamado() {
		return listaSituacaoChamado;
	}

	public void setListaSituacaoChamado(List<SituacaoChamado> listaSituacaoChamado) {
		this.listaSituacaoChamado = listaSituacaoChamado;
	}

	public List<String> getListaCategoria() {
		return listaCategoria;
	}

	public void setListaCategoria(List<String> listaCategoria) {
		this.listaCategoria = listaCategoria;
	}

	public List<String> getListaSubCategoria() {
		return listaSubCategoria;
	}

	public void setListaSubCategoria(List<String> listaSubCategoria) {
		this.listaSubCategoria = listaSubCategoria;
	}

	public List<PrioridadeChamado> getListaPrioridadeChamado() {
		return listaPrioridadeChamado;
	}

	public void setListaPrioridadeChamado(List<PrioridadeChamado> listaPrioridadeChamado) {
		this.listaPrioridadeChamado = listaPrioridadeChamado;
	}

	public List<CausaChamado> getListaCausaChamado() {
		return listaCausaChamado;
	}

	public void setListaCausaChamado(List<CausaChamado> listaCausaChamado) {
		this.listaCausaChamado = listaCausaChamado;
	}
}
