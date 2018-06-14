package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import br.com.grupojcr.business.ChamadoBusiness;
import br.com.grupojcr.dto.FiltroChamado;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.ChamadoDataModel;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class MeusChamadosController implements Serializable {
	
	private static final long serialVersionUID = 934937300949501891L;
	protected static Logger LOG = Logger.getLogger(MeusChamadosController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	private final static Integer ABERTO = 1;
	
	private FiltroChamado filtro;
	
	private Boolean exibirResultado;
	
	@EJB
	private ChamadoBusiness chamadoBusiness;
	
	@Inject
	private ChamadoDataModel dataModel;
	
	@Inject
	private LoginController loginController;

	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setExibirResultado(Boolean.FALSE);
			setFiltro(new FiltroChamado());
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			if(!loginController.hasGroup(Arrays.asList("ADMINISTRADOR","SUPORTE"))) {
				getFiltro().setUsuarioLogado(usuario);
			}
			getFiltro().setSituacao(ABERTO);
			carregarDatas();
			
			if(chamadoBusiness.obterQtdChamado(getFiltro()) == 0) {
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
			if(chamadoBusiness.obterQtdChamado(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
				throw new ApplicationException("message.datatable.noRecords", FacesMessage.SEVERITY_WARN);
			}
			
			DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tableChamado");
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
			return "/pages/suporte/listar_chamado.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltar" }, e);
		}
	}

	public FiltroChamado getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroChamado filtro) {
		this.filtro = filtro;
	}

	public Boolean getExibirResultado() {
		return exibirResultado;
	}

	public void setExibirResultado(Boolean exibirResultado) {
		this.exibirResultado = exibirResultado;
	}

	public ChamadoDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(ChamadoDataModel dataModel) {
		this.dataModel = dataModel;
	}
}
