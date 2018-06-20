package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
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

import br.com.grupojcr.business.CotacaoBusiness;
import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.SolicitacaoCompraDataModel;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class CotacaoController implements Serializable {
	
	private static final long serialVersionUID = 3176719577745395815L;
	protected static Logger LOG = Logger.getLogger(CotacaoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private FiltroSolicitacaoCompra filtro;
	private SolicitacaoCompra solicitacaoCompra;
	private Cotacao cotacao;
	private Usuario usuario;
	
	private Boolean exibirResultado;
	private String origem;
	
	private List<SituacaoSolicitacaoCompra> listaSituacao;
	
	private List<Coligada> listaColigada;
	
	private List<SolicitacaoCompra> listaSolicitacao;
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@EJB
	private CotacaoBusiness cotacaoBusiness;
	
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
			setFiltro(new FiltroSolicitacaoCompra());
			setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			getFiltro().setUsuarioCotacao(getUsuario());
			carregarDatas();
			
			setListaColigada(new ArrayList<Coligada>());
			if(Util.isNotNull(getUsuario().getColigadas())) {
				for(Coligada coligada : getUsuario().getColigadas()) {
					if(coligada.getSituacao()) {
						getListaColigada().add(coligada);
					}
				}
			}
			
			setListaSituacao(SituacaoSolicitacaoCompra.listarParaCotacao());
			
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
	
	
	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcessoCotacaoPendente() throws ApplicationException {
		try {
			setFiltro(new FiltroSolicitacaoCompra());
			getFiltro().setSituacao(SituacaoSolicitacaoCompra.APROVADA_COTACAO);
			setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			getFiltro().setUsuarioLogado(getUsuario());
			
			setListaSolicitacao(solicitacaoCompraBusiness.listarSolicitacaoCompraPendente(filtro));
		
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcessoCotacaoPendente" }, e);
		}
	}
	
	public String iniciarCotacao() throws ApplicationException {
		try {
			solicitacaoCompraBusiness.iniciarCotacao(getSolicitacaoCompra(), getUsuario());
			
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			}
			
			Message.setMessage("solicitacao.compra.cotacao.iniciar", new String[] {getSolicitacaoCompra().getId().toString()});
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarCotacao" }, e);
		}
		return "/pages/solicitacaoCompra/cotacao/editar_cotacao.xhtml?faces-redirect=true";
	}
	
	public String cotar() throws ApplicationException {
		try {
			
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarCotacao" }, e);
		}
		return "/pages/solicitacaoCompra/cotacao/editar_cotacao.xhtml?faces-redirect=true";
	}
	
	public String exibir() throws ApplicationException {
		try {
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "exibir" }, e);
		}
		return "/pages/solicitacaoCompra/cotacao/exibir_cotacao.xhtml?faces-redirect=true";
	}
	
	public String validarVencimento(SolicitacaoCompra solicitacao) throws ApplicationException {
		try {
			if(solicitacao.getDtPrazo().before(Calendar.getInstance().getTime())) {
				return "cotacaoVencida";
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "validarVencimento" }, e);
		}
	}
	
	public String novaCotacao() throws ApplicationException {
		try {
			setCotacao(new Cotacao());
			getCotacao().setItens(new HashSet<CotacaoItem>());
			for(SolicitacaoCompraItem item : getSolicitacaoCompra().getItens()) {
				CotacaoItem cotacaoItem = new CotacaoItem();
				cotacaoItem.setSolicitacaoCompraItem(item);
				cotacaoItem.setNaoPossui(Boolean.FALSE);
				getCotacao().getItens().add(cotacaoItem);
			}
			return "/pages/solicitacaoCompra/cotacao/editar_novaCotacao.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "novaCotacao" }, e);
		}
	}

	public String editarCotacao() throws ApplicationException {
		try {
			if(Util.isNotNull(getCotacao())) {
				getCotacao().setItens(new HashSet<CotacaoItem>(cotacaoBusiness.listarItensCotacao(getCotacao().getId())));
			}
			return "/pages/solicitacaoCompra/cotacao/editar_novaCotacao.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "editarCotacao" }, e);
		}
	}
	
	public String salvarCotacao() throws ApplicationException {
		try {
			
			if(TreatString.isBlank(getCotacao().getFornecedor())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(TreatString.isNotBlank(getCotacao().getObservacao())) {
				if(getCotacao().getObservacao().length() > 300) {
					throw new ApplicationException("message.empty", new String[] {"Máximo 300 caracteres para a observação."}, FacesMessage.SEVERITY_WARN);
				}
			}
			
			Boolean produtoCotado = Boolean.FALSE;
			for(CotacaoItem item : getCotacao().getItens()) {
				if(!item.getNaoPossui()) {
					produtoCotado = Boolean.TRUE;
				}
			}
			
			if(!produtoCotado) {
				throw new ApplicationException("message.empty", new String[] {"Nenhum produto/serviço foi cotado. Não é possível continuar sem a cotação de no minímo um item."}, FacesMessage.SEVERITY_WARN);
			}
			
			for(CotacaoItem item : getCotacao().getItens()) {
				if(!item.getNaoPossui()) {
					if(Util.isNull(item.getValorTotal()) || item.getValorTotal().equals(new Double(0))) {
						throw new ApplicationException("message.empty", new String[] {"Itens com valor zerado. Favor inserir o valor total dos itens."}, FacesMessage.SEVERITY_WARN);
					}
				}
			}
			
			cotacaoBusiness.salvar(getSolicitacaoCompra(), getCotacao());
			
			getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			
			Message.setMessage("cotacao.incluida");
			return "/pages/solicitacaoCompra/cotacao/editar_cotacao.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvarCotacao" }, e);
		}
	}

	public void excluirCotacao() throws ApplicationException {
		try {
			cotacaoBusiness.excluir(getCotacao());
			
			getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			
			Message.setMessage("cotacao.excluida");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirCotacao" }, e);
		}
	}
	
	public void calcular(CotacaoItem item) throws ApplicationException {
		try {
			if(Util.isNotNull(item.getValor())) {
				Double valor = item.getValor();
				Double valorTotal = valor * item.getSolicitacaoCompraItem().getQuantidade();
				item.setValorTotal(valorTotal);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcular" }, e);
		}
	}
	
	public String calcularTotal() throws ApplicationException {
		try {
			if(Util.isNotNull(getCotacao().getItens())) {
				Double total = new Double(0);
				for(CotacaoItem item : getCotacao().getItens()) {
					if(Util.isNotNull(item.getValorTotal())) {
						total += item.getValorTotal();
					}
				}
				
				if(Util.isNotNull(getCotacao().getFrete())) {
					total += getCotacao().getFrete();
				}
				
				getCotacao().setValorTotal(total);
				
				return TreatNumber.formatMoney(total);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcularTotal" }, e);
		}
		return "0,00";
	}
	
	public String limparValores(CotacaoItem item) throws ApplicationException {
		try {
			item.setValor(null);
			item.setValorTotal(new Double(0));
			calcular(item);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "limparValores" }, e);
		}
		return "0,00";
	}
	
	public String voltar() throws ApplicationException {
		try {
			if(TreatString.isNotBlank(getOrigem())) {
				if(getOrigem().equals("COTACAO_PENDENTE")) {
					return "/pages/solicitacaoCompra/cotacao/listar_cotacaoPendente.xhtml?faces-redirect=true";
				}
			}
			return "/pages/solicitacaoCompra/cotacao/listar_minhasCotacoes.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltar" }, e);
		}
	}
	
	public String voltarEdicao() throws ApplicationException {
		try {
			return "/pages/solicitacaoCompra/cotacao/editar_cotacao.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltarEdicao" }, e);
		}
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

	public Boolean getExibirResultado() {
		return exibirResultado;
	}

	public void setExibirResultado(Boolean exibirResultado) {
		this.exibirResultado = exibirResultado;
	}

	public List<SolicitacaoCompra> getListaSolicitacao() {
		return listaSolicitacao;
	}

	public void setListaSolicitacao(List<SolicitacaoCompra> listaSolicitacao) {
		this.listaSolicitacao = listaSolicitacao;
	}

	public SolicitacaoCompra getSolicitacaoCompra() {
		return solicitacaoCompra;
	}

	public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
		this.solicitacaoCompra = solicitacaoCompra;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
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
	
	
}