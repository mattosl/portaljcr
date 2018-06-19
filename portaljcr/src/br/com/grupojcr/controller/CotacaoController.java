package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
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
public class CotacaoController implements Serializable {
	
	private static final long serialVersionUID = 3176719577745395815L;
	protected static Logger LOG = Logger.getLogger(CotacaoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private FiltroSolicitacaoCompra filtro;
	
	private Boolean exibirResultado;
	private String origem;
	
	private List<SolicitacaoCompra> listaSolicitacao;
	
	private SolicitacaoCompra solicitacaoCompra;
	private Cotacao cotacao;
	private Usuario usuario;
	
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
	
	public void calcular(CotacaoItem item) throws ApplicationException {
		try {
			if(Util.isNotNull(item.getValor())) {
				Double valor = item.getValor();
				Double frete = item.getFrete();
				Double valorTotal = valor;
				if(Util.isNotNull(frete)) {
					valorTotal += frete;
				}
				item.setValorTotal(valorTotal);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcular" }, e);
		}
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
	
	
}
