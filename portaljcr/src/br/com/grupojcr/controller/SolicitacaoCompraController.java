package br.com.grupojcr.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import br.com.grupojcr.business.GrupoCotacaoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.ProdutoDTO;
import br.com.grupojcr.dto.SolicitacaoCompraDTO;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.enumerator.PrioridadeSolicitacaoCompra;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

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
	private List<UnidadeRM> listaUnidade;

	private SolicitacaoCompraItem solicitacaoItem;
	private SolicitacaoCompraDTO solicitacaoCompraDTO;
	private Usuario usuario;

	private Boolean voltar = Boolean.FALSE;
	private Boolean edicaoProduto;

	@EJB
	private GrupoCotacaoBusiness grupoCotacaoBusiness;

	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;

	@EJB
	private RMBusiness rmBusiness;

	/**
	 * Método responsavel por iniciar processo
	 * 
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			if (!voltar) {

				setSolicitacaoCompraDTO(new SolicitacaoCompraDTO());
				getSolicitacaoCompraDTO().setPossuiGrupoCotacao(Boolean.TRUE);
				setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.get("usuario"));
				setListaColigada(new ArrayList<Coligada>());
				if (Util.isNotNull(getUsuario().getColigadas())) {
					for (Coligada coligada : getUsuario().getColigadas()) {
						if (coligada.getSituacao()) {
							getListaColigada().add(coligada);
						}
					}
				}
				setListaGrupoCotacao(grupoCotacaoBusiness.listarGruposAtivos());
			}
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
	 * 
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public String prosseguir() throws ApplicationException {
		try {
			if (Util.isNull(solicitacaoCompraDTO.getColigada())) {
				throw new ApplicationException("message.empty",
						new String[] { "Favor selecionar para qual empresa será realizada a cotação." },
						FacesMessage.SEVERITY_WARN);
			}
			if (Util.isNull(solicitacaoCompraDTO.getPossuiGrupoCotacao())) {
				throw new ApplicationException("message.empty",
						new String[] { "Favor selecionar quem irá realizar a cotação." }, FacesMessage.SEVERITY_WARN);
			}
			if (solicitacaoCompraDTO.getPossuiGrupoCotacao()) {
				if (Util.isNull(solicitacaoCompraDTO.getGrupoCotacao())) {
					throw new ApplicationException("message.empty",
							new String[] { "Favor selecionar qual grupo de cotação irá realizar as cotações." },
							FacesMessage.SEVERITY_WARN);
				}
			} else {
				getSolicitacaoCompraDTO().setUsuarioCotacao((Usuario) FacesContext.getCurrentInstance()
						.getExternalContext().getSessionMap().get("usuario"));
			}

			setListaCentroCusto(
					rmBusiness.listarCentroCustoPorColigada(getSolicitacaoCompraDTO().getColigada().getId()));
			setListaNaturezaOrcamentaria(rmBusiness.listarNaturezaOrcamentaria());
			setListaModalidade(new ArrayList<Modalidade>(Arrays.asList(Modalidade.values())));
			setListaPrioridade(
					new ArrayList<PrioridadeSolicitacaoCompra>(Arrays.asList(PrioridadeSolicitacaoCompra.values())));
			getSolicitacaoCompraDTO().setModalidade(Modalidade.MATERIAL);
			getSolicitacaoCompraDTO().setPrioridade(PrioridadeSolicitacaoCompra.MEDIA);
			getSolicitacaoCompraDTO().setCentroCusto(null);
			getSolicitacaoCompraDTO().setMotivoCompra(null);
			getSolicitacaoCompraDTO().setSugestaoFornecedor(null);
			getSolicitacaoCompraDTO().setLocalEntrega(null);
			getSolicitacaoCompraDTO().setItens(new ArrayList<SolicitacaoCompraItem>());
			return "/pages/solicitacaoCompra/solicitacao/nova_solicitacao.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "prosseguir" }, e);
		}
	}

	public void novoItem() throws ApplicationException {
		try {
			setEdicaoProduto(Boolean.FALSE);
			getSolicitacaoCompraDTO().setProduto(new ProdutoDTO());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "novoItem" }, e);
		}
	}

	public String salvar() throws ApplicationException {
		try {
			Boolean validado = Boolean.TRUE;
			if (Util.isNull(getSolicitacaoCompraDTO().getPrioridade())) {
				validado = Boolean.FALSE;
			}
			if (Util.isNull(getSolicitacaoCompraDTO().getCentroCusto())) {
				validado = Boolean.FALSE;
			}
			if (Util.isNull(getSolicitacaoCompraDTO().getModalidade())) {
				validado = Boolean.FALSE;
			}
			if (Util.isBlank(getSolicitacaoCompraDTO().getMotivoCompra())) {
				validado = Boolean.FALSE;
			} else {
				if (getSolicitacaoCompraDTO().getMotivoCompra().length() > 500) {
					throw new ApplicationException("message.empty",
							new String[] { "Máximo 500 caracteres para o Motivo da Compra." },
							FacesMessage.SEVERITY_WARN);
				}
			}
			if (CollectionUtils.isEmpty(getSolicitacaoCompraDTO().getItens())) {
				throw new ApplicationException("message.empty",
						new String[] { "A solicitação deve conter no minímo 1 item." }, FacesMessage.SEVERITY_WARN);
			}

			if (!validado) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			
			solicitacaoCompraBusiness.validarOrcamentoSolicitacaoCompra(getSolicitacaoCompraDTO());
			solicitacaoCompraBusiness.salvar(getSolicitacaoCompraDTO(), getUsuario());

			Message.setMessage("solicitacao.compra.sucesso");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
		return "/pages/solicitacaoCompra/solicitacao/listar_minhasSolicitacoes.xhtml?faces-redirect=true";
	}

	public void adicionarProduto() throws ApplicationException {
		try {
			Boolean camposObrigatorios = Boolean.FALSE;
			if (!getSolicitacaoCompraDTO().getProduto().getNaoEncontrei()) {
				if (Util.isNull(getSolicitacaoCompraDTO().getProduto().getProduto())) {
					camposObrigatorios = Boolean.TRUE;
				}
			} else {
				if (Util.isBlank(getSolicitacaoCompraDTO().getProduto().getDescricaoProduto())) {
					camposObrigatorios = Boolean.TRUE;
				}
			}

			if (Util.isBlank(getSolicitacaoCompraDTO().getProduto().getObservacao())) {
				camposObrigatorios = Boolean.TRUE;
			} else {
				if (getSolicitacaoCompraDTO().getProduto().getObservacao().length() > 300) {
					throw new ApplicationException("message.empty",
							new String[] { "Máximo 500 caracteres na observação." }, FacesMessage.SEVERITY_WARN);
				}
			}

			if (Util.isNull(getSolicitacaoCompraDTO().getProduto().getQuantidade())) {
				camposObrigatorios = Boolean.TRUE;
			}

//			if (Util.isNull(getSolicitacaoCompraDTO().getProduto().getUnidade())) {
//				camposObrigatorios = Boolean.TRUE;
//			}
			
			if(Util.isNullOrZero(getSolicitacaoCompraDTO().getProduto().getValorAproximado())) {
				camposObrigatorios = Boolean.TRUE;
			}

			if (camposObrigatorios) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}

//			if (!getSolicitacaoCompraDTO().getProduto().getNaoEncontrei()) {
//				for (SolicitacaoCompraItem item : getSolicitacaoCompraDTO().getItens()) {
//					if (!item.getProdutoNaoEncontrado()) {
//						if (item.getIdProduto().equals(getSolicitacaoCompraDTO().getProduto().getProduto().getId())) {
//							throw new ApplicationException("message.empty",
//									new String[] { "Este produto ja foi incluído." }, FacesMessage.SEVERITY_WARN);
//						}
//					}
//				}
//			}

			SolicitacaoCompraItem item = new SolicitacaoCompraItem();
			if (!getSolicitacaoCompraDTO().getProduto().getNaoEncontrei()) {
				item.setIdProduto(getSolicitacaoCompraDTO().getProduto().getProduto().getIdProduto());
				item.setCodigoProduto(getSolicitacaoCompraDTO().getProduto().getProduto().getCodigoProduto());
				item.setDescricaoProduto(getSolicitacaoCompraDTO().getProduto().getProduto().getProduto());
			} else {
				item.setDescricaoProduto(getSolicitacaoCompraDTO().getProduto().getDescricaoProduto());
			}

			item.setProdutoNaoEncontrado(getSolicitacaoCompraDTO().getProduto().getNaoEncontrei());
			item.setQuantidade(getSolicitacaoCompraDTO().getProduto().getQuantidade());
			item.setCodigoUnidade(getSolicitacaoCompraDTO().getProduto().getProduto().getCodigoUnidadeCompra());
			item.setUnidade(getSolicitacaoCompraDTO().getProduto().getProduto().getCodigoUnidadeCompra());
			item.setValorAproximado(getSolicitacaoCompraDTO().getProduto().getValorAproximado());
			item.setObservacao(getSolicitacaoCompraDTO().getProduto().getObservacao());
			item.setDtInclusao(Calendar.getInstance().getTime());
			item.setUsuarioInclusao(getUsuario());

			getSolicitacaoCompraDTO().getItens().add(item);

			PrimeFaces.current().executeScript("PF('modalProduto').hide();");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarProduto" }, e);
		}
	}

	public void editarProduto() throws ApplicationException {
		try {
			Boolean camposObrigatorios = Boolean.FALSE;
			if (!getSolicitacaoCompraDTO().getProduto().getNaoEncontrei()) {
				if (Util.isNull(getSolicitacaoCompraDTO().getProduto().getProduto())) {
					camposObrigatorios = Boolean.TRUE;
				}
			} else {
				if (Util.isBlank(getSolicitacaoCompraDTO().getProduto().getDescricaoProduto())) {
					camposObrigatorios = Boolean.TRUE;
				}
			}

			if (Util.isBlank(getSolicitacaoCompraDTO().getProduto().getObservacao())) {
				camposObrigatorios = Boolean.TRUE;
			} else {
				if (getSolicitacaoCompraDTO().getProduto().getObservacao().length() > 300) {
					throw new ApplicationException("message.empty",
							new String[] { "Máximo 500 caracteres na observação." }, FacesMessage.SEVERITY_WARN);
				}
			}

			if (Util.isNull(getSolicitacaoCompraDTO().getProduto().getQuantidade())) {
				camposObrigatorios = Boolean.TRUE;
			}

//			if (Util.isNull(getSolicitacaoCompraDTO().getProduto().getUnidade())) {
//				camposObrigatorios = Boolean.TRUE;
//			}

			if (camposObrigatorios) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}

			getSolicitacaoCompraDTO().getItens().remove(getSolicitacaoItem());
			if (!getSolicitacaoCompraDTO().getProduto().getNaoEncontrei()) {
				getSolicitacaoItem().setIdProduto(getSolicitacaoCompraDTO().getProduto().getProduto().getIdProduto());
				getSolicitacaoItem()
						.setCodigoProduto(getSolicitacaoCompraDTO().getProduto().getProduto().getCodigoProduto());
				getSolicitacaoItem()
						.setDescricaoProduto(getSolicitacaoCompraDTO().getProduto().getProduto().getProduto());
			} else {
				getSolicitacaoItem().setDescricaoProduto(getSolicitacaoCompraDTO().getProduto().getDescricaoProduto());
			}

			getSolicitacaoItem().setProdutoNaoEncontrado(getSolicitacaoCompraDTO().getProduto().getNaoEncontrei());
			getSolicitacaoItem().setQuantidade(getSolicitacaoCompraDTO().getProduto().getQuantidade());
			getSolicitacaoItem()
					.setCodigoUnidade(getSolicitacaoCompraDTO().getProduto().getProduto().getCodigoUnidadeCompra());
			getSolicitacaoItem().setUnidade(getSolicitacaoCompraDTO().getProduto().getProduto().getCodigoUnidadeCompra());
			getSolicitacaoItem().setValorAproximado(getSolicitacaoCompraDTO().getProduto().getValorAproximado());
			getSolicitacaoItem().setObservacao(getSolicitacaoCompraDTO().getProduto().getObservacao());
			getSolicitacaoItem().setDtInclusao(Calendar.getInstance().getTime());
			getSolicitacaoItem().setUsuarioInclusao(getUsuario());

			getSolicitacaoCompraDTO().getItens().add(getSolicitacaoItem());

			PrimeFaces.current().executeScript("PF('modalProduto').hide();");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "editarProduto" }, e);
		}
	}

	public void excluirItem(SolicitacaoCompraItem item) throws ApplicationException {
		try {
			getSolicitacaoCompraDTO().getItens().remove(item);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirItem" }, e);
		}
	}

	public void iniciarEdicaoItem() throws ApplicationException {
		try {
			setEdicaoProduto(Boolean.TRUE);
			getSolicitacaoCompraDTO().setProduto(new ProdutoDTO());
			getSolicitacaoCompraDTO().getProduto().setNaoEncontrei(getSolicitacaoItem().getProdutoNaoEncontrado());
			if (getSolicitacaoCompraDTO().getProduto().getNaoEncontrei()) {
				getSolicitacaoCompraDTO().getProduto().setDescricaoProduto(getSolicitacaoItem().getDescricaoProduto());
			} else {
				getSolicitacaoCompraDTO().getProduto().setProduto(new ProdutoRM(getSolicitacaoItem().getIdProduto(),
						getSolicitacaoItem().getCodigoProduto(), getSolicitacaoItem().getDescricaoProduto()));
			}

			getSolicitacaoCompraDTO().getProduto().setQuantidade(getSolicitacaoItem().getQuantidade());
			getSolicitacaoCompraDTO().getProduto().getProduto().setCodigoUnidadeCompra(getSolicitacaoItem().getCodigoUnidade());
			getSolicitacaoCompraDTO().getProduto().setValorAproximado(getSolicitacaoItem().getValorAproximado());
			getSolicitacaoCompraDTO().getProduto().setObservacao(getSolicitacaoItem().getObservacao());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarEdicaoItem" }, e);
		}
	}
	
	public void limparProdutos() throws ApplicationException {
		try {
			getSolicitacaoCompraDTO().setItens(new ArrayList<SolicitacaoCompraItem>());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "limparProdutos" }, e);
		}
	}

	public List<ProdutoRM> autocompleteProduto(String nome) throws ApplicationException {
		try {
			return rmBusiness.listarProdutosPorNome(getSolicitacaoCompraDTO().getColigada().getId(), nome, getSolicitacaoCompraDTO().getModalidade());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autocompleteProduto" }, e);
		}
	}
	
	public String calcularTotal() throws ApplicationException {
		try {
			if(Util.isNotNull(getSolicitacaoCompraDTO().getProduto())) {
				if(Util.isNotNull(getSolicitacaoCompraDTO().getProduto().getQuantidade())) {
					if(Util.isNotNull(getSolicitacaoCompraDTO().getProduto().getValorAproximado())) {
						BigDecimal valor = getSolicitacaoCompraDTO().getProduto().getValorAproximado().multiply(getSolicitacaoCompraDTO().getProduto().getQuantidade());
						return TreatNumber.formatMoney(valor);
					}
				} else {
					if(Util.isNotNull(getSolicitacaoCompraDTO().getProduto().getValorAproximado())) {
						return TreatNumber.formatMoney(getSolicitacaoCompraDTO().getProduto().getValorAproximado());
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcularTotal" }, e);
		}
		return "0,00";
	}

	public String voltar() throws ApplicationException {
		try {
			setVoltar(Boolean.TRUE);
			return "/pages/solicitacaoCompra/solicitacao/nova_solicitacao_pre.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltar" }, e);
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

	public List<UnidadeRM> getListaUnidade() {
		return listaUnidade;
	}

	public void setListaUnidade(List<UnidadeRM> listaUnidade) {
		this.listaUnidade = listaUnidade;
	}

	public Boolean getVoltar() {
		return voltar;
	}

	public void setVoltar(Boolean voltar) {
		this.voltar = voltar;
	}

	public Boolean getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Boolean edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public SolicitacaoCompraItem getSolicitacaoItem() {
		return solicitacaoItem;
	}

	public void setSolicitacaoItem(SolicitacaoCompraItem solicitacaoItem) {
		this.solicitacaoItem = solicitacaoItem;
	}

}
