package br.com.grupojcr.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import com.totvs.technology.ecm.dm.ws.CardFieldDto;

import br.com.grupojcr.business.FluigBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.dto.OrdemCompraDTO;
import br.com.grupojcr.dto.ProdutoDTO;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.OrdemCompra;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;
import br.com.grupojcr.rm.CondicaoPagamentoRM;
import br.com.grupojcr.rm.FornecedorRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

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
	private ProdutoDTO produtoDTO;
	
	private List<SolicitacaoCompra> listaSolicitacao;
	private List<CondicaoPagamentoRM> listaCondicaoPagamento;
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private FluigBusiness fluigBusiness;
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			setFiltro(new FiltroSolicitacaoCompra());
			getFiltro().setSituacao(SituacaoSolicitacaoCompra.LIBERADO_ORDEM_COMPRA);
			setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			
			getFiltro().setColigadasUsuario(new ArrayList<Coligada>());
			if (Util.isNotNull(getUsuario().getColigadas())) {
				for (Coligada coligada : getUsuario().getColigadas()) {
					if (coligada.getSituacao()) {
						getFiltro().getColigadasUsuario().add(coligada);
					}
				}
			}
			
			setListaSolicitacao(solicitacaoCompraBusiness.listarSolicitacaoCompraPendente(filtro));
		
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	public void vincularProduto() throws ApplicationException {
		try {
			getProdutoDTO().getCotacaoItem().getSolicitacaoCompraItem().setIdProduto(getProdutoDTO().getProduto().getIdProduto());
			getProdutoDTO().getCotacaoItem().getSolicitacaoCompraItem().setCodigoProduto(getProdutoDTO().getProduto().getCodigoProduto());
			getProdutoDTO().getCotacaoItem().getSolicitacaoCompraItem().setDescricaoProduto(getProdutoDTO().getProduto().getProduto());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "vincularProduto" }, e);
		}
	}
	
	public String iniciarOrdemCompra() throws ApplicationException {
		try {
			setOrdemCompra(new OrdemCompraDTO());
			setProdutoDTO(new ProdutoDTO());
			
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			}
			
			for(Cotacao cotacao : getSolicitacaoCompra().getCotacoes()) {
				if(cotacao.getCotacaoPrincipal()) {
					getOrdemCompra().setCotacao(cotacao);
					getOrdemCompra().setListaProduto(new ArrayList<ProdutoDTO>());
					for(CotacaoItem item : cotacao.getItens()) {
						if(!item.getNaoPossui() ) {
							ProdutoDTO dto = new ProdutoDTO();
							dto.setCotacaoItem(item);
							getOrdemCompra().getListaProduto().add(dto);
						}
					}
				}
			}
			
			getOrdemCompra().setSolicitacaoCompra(getSolicitacaoCompra());
			
			setListaCondicaoPagamento(rmBusiness.listarCondicaoPagamento(getSolicitacaoCompra().getColigada().getId()));
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarOrdemCompra" }, e);
		}
		return "/pages/solicitacaoCompra/cotacao/editar_ordemCompra.xhtml?faces-redirect=true";
	}
	
	public String gerarOrdemCompra() throws ApplicationException {
		try {
			if(Util.isNull(getOrdemCompra().getCondicaoPagamento())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher a condição de pagamento."}, FacesMessage.SEVERITY_WARN);
			}
			
			if(Util.isNull(getOrdemCompra().getFornecedor())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher o Fornecedor do RM."}, FacesMessage.SEVERITY_WARN);
			}
			
			for(ProdutoDTO produto : getOrdemCompra().getListaProduto()) {
				if(Util.isNull(produto.getCotacaoItem().getSolicitacaoCompraItem().getCodigoProduto())) {
					throw new ApplicationException("message.empty", new String[] {"Favor vincular todos os produtos/serviços com o RM."}, FacesMessage.SEVERITY_WARN);
				}
			}
			
			/** Validação de Orçamento */
			solicitacaoCompraBusiness.validarOrcamentoOrdemCompra(getOrdemCompra());
			
			Boolean existeUsuario = rmBusiness.existeUsuario(getUsuario().getUsuario().toLowerCase());
			String usuario = existeUsuario ? getUsuario().getUsuario().toLowerCase() : "portaljcr";
			
			
			if(getOrdemCompra().getSolicitacaoCompra().getModalidade().equals(Modalidade.MATERIAL_SERVICO)) {
				List<String> xmls = new ArrayList<String>();
				List<String> identificadores = new ArrayList<String>();
				List<CotacaoItem> itensMaterial = solicitacaoCompraBusiness.obterItensMaterial(getOrdemCompra());
				List<CotacaoItem> itensServico = solicitacaoCompraBusiness.obterItensServico(getOrdemCompra());
				
				if(CollectionUtils.isNotEmpty(itensMaterial)) {
					
					OrdemCompraDTO material = new OrdemCompraDTO();
					material.setCondicaoPagamento(getOrdemCompra().getCondicaoPagamento());
					material.setCotacao(getOrdemCompra().getCotacao());
					material.setFornecedor(getOrdemCompra().getFornecedor());
					material.setSolicitacaoCompra(getOrdemCompra().getSolicitacaoCompra());
					material.getCotacao().setItens(new HashSet<CotacaoItem>(itensMaterial));
					
					BigDecimal valorTotal = new BigDecimal(0);
					for(CotacaoItem itemCotacao : material.getCotacao().getItens()) {
						valorTotal = valorTotal.add(itemCotacao.getValorTotal());
					}
					material.getCotacao().setValorTotal(valorTotal);
					
					String xml = solicitacaoCompraBusiness.montarXML(material, usuario, Modalidade.MATERIAL);
					
					xmls.add(xml);
				}
				
				if(CollectionUtils.isNotEmpty(itensServico)) {
					OrdemCompraDTO servico = new OrdemCompraDTO();
					servico.setCondicaoPagamento(getOrdemCompra().getCondicaoPagamento());
					servico.setCotacao(getOrdemCompra().getCotacao());
					servico.setFornecedor(getOrdemCompra().getFornecedor());
					servico.setSolicitacaoCompra(getOrdemCompra().getSolicitacaoCompra());
					servico.getCotacao().setItens(new HashSet<CotacaoItem>(itensServico));
					servico.getCotacao().setFrete(new BigDecimal(0));
					
					BigDecimal valorTotal = new BigDecimal(0);
					for(CotacaoItem itemCotacao : servico.getCotacao().getItens()) {
						valorTotal = valorTotal.add(itemCotacao.getValorTotal());
					}
					servico.getCotacao().setValorTotal(valorTotal);
					
					String xml = solicitacaoCompraBusiness.montarXML(servico, usuario, Modalidade.SERVICO);
					
					xmls.add(xml);
				}
				
				if(CollectionUtils.isNotEmpty(xmls)) {
					for(String xml : xmls) {
						String retorno = rmBusiness.saveRecordAuth("MovMovimentoTBCData", xml, "CODCOLIGADA=" + getOrdemCompra().getSolicitacaoCompra().getColigada().getId() +";CODSISTEMA=T;CODUSUARIO=portaljcr", "portaljcr", "portaljcr-123");
						
						String [] arrayRetorno = retorno.split(";");
						
						salvarXml(arrayRetorno, usuario);
						
						identificadores.add(arrayRetorno[1]);
					}
				}
				
				if(CollectionUtils.isNotEmpty(identificadores)) {
					for(String identificador : identificadores) {
						solicitacaoCompraBusiness.criarOrdemCompra(getOrdemCompra(), identificador, getUsuario());
					}
					solicitacaoCompraBusiness.encerrarSolicitacao(getOrdemCompra());
					
					Message.setMessage("gerar.ordem.compras.sucesso", new String[] {identificadores.get(0) + " e " + identificadores.get(1), getOrdemCompra().getSolicitacaoCompra().getColigada().getRazaoSocial()});
				}
				
			} else {
				
				String xml = solicitacaoCompraBusiness.montarXML(getOrdemCompra(), usuario, getOrdemCompra().getSolicitacaoCompra().getModalidade());
				
				String retorno = rmBusiness.saveRecordAuth("MovMovimentoTBCData", xml, "CODCOLIGADA=" + getOrdemCompra().getSolicitacaoCompra().getColigada().getId() +";CODSISTEMA=T;CODUSUARIO=portaljcr", "portaljcr", "portaljcr-123");
				
				String [] arrayRetorno = retorno.split(";");
				
				salvarXml(arrayRetorno, usuario);
				
				OrdemCompra oc = solicitacaoCompraBusiness.encerrar(getOrdemCompra(), arrayRetorno[1], getUsuario());
				solicitacaoCompraBusiness.enviarEmailOrdemCompra(oc);
				
				Message.setMessage("gerar.ordem.compra.sucesso", new String[] {arrayRetorno[1], getOrdemCompra().getSolicitacaoCompra().getColigada().getRazaoSocial()});
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "gerarOrdemCompra" }, e);
		}
		return "/pages/solicitacaoCompra/cotacao/listar_ordemCompra.xhtml?faces-redirect=true";
	}
	
	public void salvarXml(String [] arrayRetorno, String usuario) throws ApplicationException {
		try {
			
			if(arrayRetorno.length == 2) {
				rmBusiness.atualizaCampos(arrayRetorno[0], arrayRetorno[1], usuario);
				
				Long idMovimento = Long.valueOf(arrayRetorno[1]);
				Integer idFluig = rmBusiness.obterIdFluig(getOrdemCompra().getSolicitacaoCompra().getColigada().getId(), idMovimento);
				Integer idFormulario = null;
				if(Util.isNotNull(idFluig)) {
					String[][] dadosFormulario = fluigBusiness.getInstanceCardData(idFluig);
					for(int i = 0; i < dadosFormulario.length; i++) {
						for(int j = 0; j < dadosFormulario[i].length; j++) {
							if(dadosFormulario[i][j].equals("documentid")) {
								try {
									idFormulario = Integer.parseInt((dadosFormulario[i][j + 1]).toString());
									break;
								} catch(NumberFormatException e) {
									break;
								}
							}
						}
					}
					if(Util.isNotNull(idFormulario)) {
						CardFieldDto [] dto = new CardFieldDto[] {new CardFieldDto("solicitante", getOrdemCompra().getSolicitacaoCompra().getUsuarioSolicitante().getNome().toUpperCase())};
						fluigBusiness.updateCardData(idFormulario, dto);
					}
				}
			} else {
				throw new ApplicationException("message.empty", new String[] {arrayRetorno[0]}, FacesMessage.SEVERITY_WARN);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvarXml" }, e);
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
			return rmBusiness.listarProdutosPorNome(getOrdemCompra().getSolicitacaoCompra().getColigada().getId(), nome, getOrdemCompra().getSolicitacaoCompra().getModalidade());
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

	public ProdutoDTO getProdutoDTO() {
		return produtoDTO;
	}

	public void setProdutoDTO(ProdutoDTO produtoDTO) {
		this.produtoDTO = produtoDTO;
	}

	public List<CondicaoPagamentoRM> getListaCondicaoPagamento() {
		return listaCondicaoPagamento;
	}

	public void setListaCondicaoPagamento(List<CondicaoPagamentoRM> listaCondicaoPagamento) {
		this.listaCondicaoPagamento = listaCondicaoPagamento;
	}

}
