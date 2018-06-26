package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.dto.OrdemCompraDTO;
import br.com.grupojcr.dto.ProdutoDTO;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;
import br.com.grupojcr.rm.FornecedorRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class GerarOrdemCompraController implements Serializable {
	
	private static final long serialVersionUID = 2431820118446661763L;
	protected static Logger LOG = Logger.getLogger(GerarOrdemCompraController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private SolicitacaoCompra solicitacaoCompra;
	private OrdemCompraDTO ordemCompra;
	private FiltroSolicitacaoCompra filtro;
	private Usuario usuario;
	private Integer passo;
	
	private List<SolicitacaoCompra> listaSolicitacao;
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@EJB
	private RMBusiness rmBusiness;
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			setFiltro(new FiltroSolicitacaoCompra());
			getFiltro().setSituacao(SituacaoSolicitacaoCompra.LIBERADO_ORDEM_COMPRA);
			setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			getFiltro().setUsuarioLogado(getUsuario());
			
			setListaSolicitacao(solicitacaoCompraBusiness.listarSolicitacaoCompraPendente(filtro));
		
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	public String iniciarOrdemCompra() throws ApplicationException {
		try {
//			String retorno = rmBusiness.saveRecordAuth("MovMovimentoTBCData", xml, "CODCOLIGADA=7;CODSISTEMA=T;CODUSUARIO=leonan", "leonan", "@careca123");
//			System.out.println(retorno);
			
			setPasso(0);
			setOrdemCompra(new OrdemCompraDTO());
			
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			}
			
			for(Cotacao cotacao : getSolicitacaoCompra().getCotacoes()) {
				if(cotacao.getCotacaoPrincipal()) {
					getOrdemCompra().setCotacao(cotacao);
					getOrdemCompra().setListaProduto(new ArrayList<ProdutoDTO>());
					for(CotacaoItem item : cotacao.getItens()) {
						if(Util.isNull(item.getSolicitacaoCompraItem().getIdProduto())) {
							ProdutoDTO dto = new ProdutoDTO();
							dto.setCotacaoItem(item);
							getOrdemCompra().getListaProduto().add(dto);
						}
					}
				}
			}
			
			getOrdemCompra().setSolicitacaoCompra(getSolicitacaoCompra());
			
//			String xml = solicitacaoCompraBusiness.montarXML(new OrdemCompra());
//			System.out.println(xml);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarOrdemCompra" }, e);
		}
		return "/pages/solicitacaoCompra/cotacao/editar_ordemCompra.xhtml?faces-redirect=true";
	}
	
	public void proximo() throws ApplicationException {
		try {
			
			if(getPasso().equals(0)) {
				if(Util.isNull(getOrdemCompra().getFornecedor())) {
					throw new ApplicationException("message.empty", new String[] {"Favor selecionar um fornecedor do RM."}, FacesMessage.SEVERITY_WARN);
				}
			}
			setPasso(getPasso() + 1);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "proximo" }, e);
		}
	}
	
	public void anterior() throws ApplicationException {
		try {
			setPasso(getPasso() - 1);
//		} catch (ApplicationException e) {
//			LOG.info(e.getMessage(), e);
//			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "anterior" }, e);
		}
	}
	
	public List<FornecedorRM> autocompleteFornecedor(String nome) throws ApplicationException {
		try {
			return rmBusiness.listarFornecedorPorNome(nome);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autocompleteFornecedor" }, e);
		}
	}
	
	public List<ProdutoRM> autocompleteProduto(String nome) throws ApplicationException {
		try {
			return rmBusiness.listarProdutosPorNome(getOrdemCompra().getSolicitacaoCompra().getColigada().getId(), nome);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autocompleteProduto" }, e);
		}
	}
	
	public String voltar() throws ApplicationException {
		try {
			return "/pages/solicitacaoCompra/cotacao/listar_ordemCompra.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltar" }, e);
		}
	}

	public SolicitacaoCompra getSolicitacaoCompra() {
		return solicitacaoCompra;
	}

	public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
		this.solicitacaoCompra = solicitacaoCompra;
	}

	public OrdemCompraDTO getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(OrdemCompraDTO ordemCompra) {
		this.ordemCompra = ordemCompra;
	}

	public FiltroSolicitacaoCompra getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroSolicitacaoCompra filtro) {
		this.filtro = filtro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<SolicitacaoCompra> getListaSolicitacao() {
		return listaSolicitacao;
	}

	public void setListaSolicitacao(List<SolicitacaoCompra> listaSolicitacao) {
		this.listaSolicitacao = listaSolicitacao;
	}

	public Integer getPasso() {
		return passo;
	}

	public void setPasso(Integer passo) {
		this.passo = passo;
	}

}
