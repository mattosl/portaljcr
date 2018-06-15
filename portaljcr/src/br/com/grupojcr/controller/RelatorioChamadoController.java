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

import br.com.grupojcr.business.CategoriaChamadoBusiness;
import br.com.grupojcr.business.ChamadoBusiness;
import br.com.grupojcr.dto.FiltroRelatorioChamado;
import br.com.grupojcr.entity.CategoriaChamado;
import br.com.grupojcr.entity.SubCategoriaChamado;
import br.com.grupojcr.entity.datamodel.RelatorioChamadoDataModel;
import br.com.grupojcr.enumerator.CausaChamado;
import br.com.grupojcr.enumerator.LocalizacaoChamado;
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
	private List<CategoriaChamado> listaCategoria;
	private List<SubCategoriaChamado> listaSubCategoria;
	private List<PrioridadeChamado> listaPrioridadeChamado;
	private List<CausaChamado> listaCausaChamado;
	private List<LocalizacaoChamado> listaLocalizacaoChamado;
	
	private Long totalAberto;
	private Long totalEmAndamento;
	private Long totalResolvidos;
	private Long totalFechados;
	
	@EJB
	private ChamadoBusiness chamadoBusiness;
	
	@EJB
	private CategoriaChamadoBusiness categoriaChamadoBusiness;
	
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
			setListaLocalizacaoChamado(new ArrayList<LocalizacaoChamado>(Arrays.asList(LocalizacaoChamado.values())));
			setListaCategoria(categoriaChamadoBusiness.listarCategoriaChamado());
			calcular();
			
			
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
	
	public void carregarSubcategorias() throws ApplicationException {
		try {
			if(Util.isNotNull(getFiltro().getCategoria())) {
				setListaSubCategoria(new ArrayList<SubCategoriaChamado>(getFiltro().getCategoria().getSubCategorias()));
			}
			getFiltro().setSubCategoria(null);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarSubcategorias" }, e);
		}
	}
	
	private void calcular() throws ApplicationException {
		try {
			setTotalAberto(chamadoBusiness.obterQtdChamadoPorTipo(getFiltro(), SituacaoChamado.ABERTO));
			setTotalEmAndamento(chamadoBusiness.obterQtdChamadoPorTipo(getFiltro(), SituacaoChamado.EM_ANDAMENTO));
			setTotalResolvidos(chamadoBusiness.obterQtdChamadoPorTipo(getFiltro(), SituacaoChamado.RESOLVIDO));
			setTotalFechados(chamadoBusiness.obterQtdChamadoPorTipo(getFiltro(), SituacaoChamado.FECHADO));
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
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

	public Long getTotalAberto() {
		return totalAberto;
	}

	public void setTotalAberto(Long totalAberto) {
		this.totalAberto = totalAberto;
	}

	public Long getTotalEmAndamento() {
		return totalEmAndamento;
	}

	public void setTotalEmAndamento(Long totalEmAndamento) {
		this.totalEmAndamento = totalEmAndamento;
	}

	public Long getTotalResolvidos() {
		return totalResolvidos;
	}

	public void setTotalResolvidos(Long totalResolvidos) {
		this.totalResolvidos = totalResolvidos;
	}

	public Long getTotalFechados() {
		return totalFechados;
	}

	public void setTotalFechados(Long totalFechados) {
		this.totalFechados = totalFechados;
	}

	public List<LocalizacaoChamado> getListaLocalizacaoChamado() {
		return listaLocalizacaoChamado;
	}

	public void setListaLocalizacaoChamado(List<LocalizacaoChamado> listaLocalizacaoChamado) {
		this.listaLocalizacaoChamado = listaLocalizacaoChamado;
	}

	public List<CategoriaChamado> getListaCategoria() {
		return listaCategoria;
	}

	public void setListaCategoria(List<CategoriaChamado> listaCategoria) {
		this.listaCategoria = listaCategoria;
	}

	public List<SubCategoriaChamado> getListaSubCategoria() {
		return listaSubCategoria;
	}

	public void setListaSubCategoria(List<SubCategoriaChamado> listaSubCategoria) {
		this.listaSubCategoria = listaSubCategoria;
	}
}
