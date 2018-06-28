package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.OrdemCompra;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.SolicitacaoCompraDataModel;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class MinhaSolicitacaoCompraController implements Serializable {
	
	private static final long serialVersionUID = 229648246949209283L;
	protected static Logger LOG = Logger.getLogger(MinhaSolicitacaoCompraController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private FiltroSolicitacaoCompra filtro;
	
	private Boolean exibirResultado;
	
	private List<SituacaoSolicitacaoCompra> listaSituacao;
	
	private List<Coligada> listaColigada;
	
	private Usuario usuario;
	
	private String origem;
	
	private SolicitacaoCompra solicitacaoCompra;
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@Inject
	private SolicitacaoCompraDataModel dataModel;
	
	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setExibirResultado(Boolean.FALSE);
			setFiltro(new FiltroSolicitacaoCompra());
			carregarDatas();
			setOrigem(null);
			
			setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			getFiltro().setUsuarioLogado(getUsuario());
			setListaColigada(new ArrayList<Coligada>());
			if(Util.isNotNull(getUsuario().getColigadas())) {
				for(Coligada coligada : getUsuario().getColigadas()) {
					if(coligada.getSituacao()) {
						getListaColigada().add(coligada);
					}
				}
			}
			
			setListaSituacao(Arrays.asList(SituacaoSolicitacaoCompra.values()));
			
			if(solicitacaoCompraBusiness.obterQtdSolicitacaoCompra(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
			} else {
				DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tableSolicitacoes");
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
			if(solicitacaoCompraBusiness.obterQtdSolicitacaoCompra(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
				throw new ApplicationException("message.datatable.noRecords", FacesMessage.SEVERITY_WARN);
			}
			
			DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tableSolicitacoes");
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
	
	public String exibir() throws ApplicationException {
		try {
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setOrdensCompra(new HashSet<OrdemCompra>(solicitacaoCompraBusiness.listarOrdemCompraPorSolicitacao(getSolicitacaoCompra().getId())));
			}
			return "/pages/solicitacaoCompra/solicitacao/exibir_solicitacao.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "exibir" }, e);
		}
	}
	
	public String iniciarCancelamento() throws ApplicationException {
		try {
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
			}
			return "/pages/solicitacaoCompra/solicitacao/exibir_cancelarSolicitacao.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "exibir" }, e);
		}
	}
	
	public String cancelar() throws ApplicationException {
		try {
			if(Util.isBlank(getSolicitacaoCompra().getMotivoCancelamento())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher o motivo do cancelamento"}, FacesMessage.SEVERITY_WARN );
			} else {
				if(getSolicitacaoCompra().getMotivoCancelamento().trim().length() > 300) {
					throw new ApplicationException("message.empty", new String[] {"Máximo 300 caracteres para o motivo do cancelamento."}, FacesMessage.SEVERITY_WARN );
				}
			}
			
			solicitacaoCompraBusiness.cancelar(getSolicitacaoCompra(), getUsuario());
			
			Message.setMessage("solicitacao.compra.cancelada");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "cancelar" }, e);
		}
		return voltar();
	}
	
	public String voltar() throws ApplicationException {
		try {
			if(TreatString.isNotBlank(getOrigem())) {
				if(getOrigem().equals("COTACAO")) {
					return "/pages/solicitacaoCompra/cotacao/listar_cotacaoPendente.xhtml?faces-redirect=true";
				} else if(getOrigem().equals("MINHAS_COTACOES")) {
					return "/pages/solicitacaoCompra/cotacao/listar_minhasCotacoes.xhtml?faces-redirect=true";
				}
			}
			return "/pages/solicitacaoCompra/solicitacao/listar_minhasSolicitacoes.xhtml?faces-redirect=true";
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

	public FiltroSolicitacaoCompra getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroSolicitacaoCompra filtro) {
		this.filtro = filtro;
	}

	public SolicitacaoCompraDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(SolicitacaoCompraDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public List<SituacaoSolicitacaoCompra> getListaSituacao() {
		return listaSituacao;
	}

	public void setListaSituacao(List<SituacaoSolicitacaoCompra> listaSituacao) {
		this.listaSituacao = listaSituacao;
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

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}
}
