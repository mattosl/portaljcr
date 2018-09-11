package br.com.grupojcr.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import br.com.grupojcr.business.ColigadaBusiness;
import br.com.grupojcr.business.CotacaoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.OrdemCompra;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.SolicitacaoCompraDataModel;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;
import br.com.grupojcr.rm.UnidadeRM;
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
	private OrdemCompra ordemCompra;
	
	private Boolean exibirResultado;
	private Boolean possuiMinimoCotacao;
	private String origem;
	
	private List<SituacaoSolicitacaoCompra> listaSituacao;
	
	private List<Coligada> listaColigada;
	
	private List<SolicitacaoCompra> listaSolicitacao;
	
	private List<UnidadeRM> listaUnidade;
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@EJB
	private CotacaoBusiness cotacaoBusiness;
	
	@EJB
	private ColigadaBusiness coligadaBusiness;
	
	@EJB
	private RMBusiness rmBusiness;
	
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
			getFiltro().setSituacaoIgnorar(new SituacaoSolicitacaoCompra[] {SituacaoSolicitacaoCompra.AGUARDANDO_APRV, SituacaoSolicitacaoCompra.APROVADA_COTACAO});
			carregarDatas();
			
			setListaColigada(coligadaBusiness.listarColigadasAtivas());
			
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
	
	public String concluirCotacao() throws ApplicationException {
		try {
			if(CollectionUtils.isEmpty(getSolicitacaoCompra().getCotacoes())) {
				throw new ApplicationException("cotacao.nenhuma", FacesMessage.SEVERITY_WARN);
			}
			
			if(getSolicitacaoCompra().getCotacoes().size() < 3) {
				setPossuiMinimoCotacao(Boolean.FALSE);
			} else {
				setPossuiMinimoCotacao(Boolean.TRUE);
			}
			return "/pages/solicitacaoCompra/cotacao/editar_concluirCotacao.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "concluirCotacao" }, e);
		}
	}
	
	public void atribuirParaMim() throws ApplicationException {
		try {
			solicitacaoCompraBusiness.atribuir(getSolicitacaoCompra(), getUsuario());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "atribuirParaMim" }, e);
		}
	}
	
	public String liberarCotacao() throws ApplicationException {
		try {
			setCotacao(null);
			if(getSolicitacaoCompra().getCotacoes().size() < 3) {
				setPossuiMinimoCotacao(Boolean.FALSE);
			} else {
				setPossuiMinimoCotacao(Boolean.TRUE);
			}
			return "/pages/solicitacaoCompra/cotacao/editar_liberarCompra.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "liberarCotacao" }, e);
		}
	}
	
	public String liberar() throws ApplicationException {
		try {
			String justificativa = getSolicitacaoCompra().getJustificativaLiberacao();
			Boolean selecionadoPrincipal = Boolean.FALSE;
			for(Cotacao cotacao : getSolicitacaoCompra().getCotacoes()) {
				if(cotacao.getCotacaoPrincipal()) {
					selecionadoPrincipal = Boolean.TRUE;
					if(!(cotacao.getCotacaoPrincipal() && cotacao.getMelhorOpcao())) {
						if(TreatString.isBlank(justificativa)) {
							throw new ApplicationException("message.empty", new String[] {"A melhor opção não foi selecionada, justifique."}, FacesMessage.SEVERITY_WARN);
						} else {
							if(justificativa.length() > 300) {
								throw new ApplicationException("message.empty", new String[] {"Máximo 300 caracteres para a justificativa."}, FacesMessage.SEVERITY_WARN);
							}
						}
					}
					Boolean reenviar = solicitacaoCompraBusiness.liberar(getSolicitacaoCompra(), cotacao, justificativa);
					if(!reenviar) {
						Message.setMessage("cotacao.aprovada");
						solicitacaoCompraBusiness.enviarEmailLiberacao(getSolicitacaoCompra(), cotacao);
					} else {
						Message.setMessage("cotacao.reenviada");
					}
					break;
				}
			}
			
			if(!selecionadoPrincipal) {
				throw new ApplicationException("cotacao.selecionada", FacesMessage.SEVERITY_WARN);
			}
			
			return "/pages/solicitacaoCompra/solicitacao/listar_minhasSolicitacoes.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "liberar" }, e);
		}
	}
	
	public String solicitarCotacoes() throws ApplicationException {
		try {
			
			solicitacaoCompraBusiness.solicitarCotacoes(getSolicitacaoCompra().getId());
			
			Message.setMessage("cotacao.mais.cotacoes");
			return "/pages/solicitacaoCompra/solicitacao/listar_minhasSolicitacoes.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "liberar" }, e);
		}
	}
	
	public void selecionarCotacao(Cotacao cotacao) throws ApplicationException {
		try {
			for(Cotacao cot : getSolicitacaoCompra().getCotacoes()) {
				if(cot.getId().equals(cotacao.getId())) {
					cot.setCotacaoPrincipal(Boolean.TRUE);
				} else {
					cot.setCotacaoPrincipal(Boolean.FALSE);
				}
				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "selecionarCotacao" }, e);
		}
	}
	
	public String concluir() throws ApplicationException {
		try {
			
			if(getSolicitacaoCompra().getCotacoes().size() < 3) {
				if(TreatString.isBlank(getSolicitacaoCompra().getJustificativa())) {
					throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
				}
				if(TreatString.isBlank(getSolicitacaoCompra().getJustificativa())) {
					throw new ApplicationException("message.empty", new String[] {"Máximo 300 caracteres para a justificativa."}, FacesMessage.SEVERITY_WARN);
				}
			}
			
			solicitacaoCompraBusiness.concluir(getSolicitacaoCompra());
			solicitacaoCompraBusiness.enviarEmailConcluir(getSolicitacaoCompra());
			
			Message.setMessage("cotacao.concluida");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "concluir" }, e);
		}
		return voltar();
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
	
	public void iniciarProcessoOrdemCompra() throws ApplicationException {
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
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcessoOrdemCompra" }, e);
		}
	}
	
	public String iniciarCotacao() throws ApplicationException {
		try {
			solicitacaoCompraBusiness.iniciarCotacao(getSolicitacaoCompra(), getUsuario());
			
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setOrdensCompra(new HashSet<OrdemCompra>());
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
	
	public String iniciarOrdemCompra() throws ApplicationException {
		try {
			setOrdemCompra(new OrdemCompra());
			
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			}
			
			for(Cotacao cotacao : getSolicitacaoCompra().getCotacoes()) {
				if(cotacao.getCotacaoPrincipal()) {
					getOrdemCompra().setCotacao(cotacao);
				}
			}
			
			getOrdemCompra().setSolicitacaoCompra(getSolicitacaoCompra());
			getOrdemCompra().setUsuario(getUsuario());
			
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarOrdemCompra" }, e);
		}
		return "/pages/solicitacaoCompra/cotacao/editar_ordemCompra.xhtml?faces-redirect=true";
	}
	
	public String cotar() throws ApplicationException {
		try {
			
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setOrdensCompra(new HashSet<OrdemCompra>(solicitacaoCompraBusiness.listarOrdemCompraPorSolicitacao(getSolicitacaoCompra().getId())));
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
				getSolicitacaoCompra().setOrdensCompra(new HashSet<OrdemCompra>(solicitacaoCompraBusiness.listarOrdemCompraPorSolicitacao(getSolicitacaoCompra().getId())));
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
				cotacaoItem.setQuantidade(item.getQuantidade());
				cotacaoItem.setCodigoUnidade(item.getCodigoUnidade());
				cotacaoItem.setValorTotal(new BigDecimal(0));
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
			if(TreatString.isBlank(getCotacao().getNomeContato())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(TreatString.isBlank(getCotacao().getContatoFornecedor())) {
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
					if(Util.isNullOrZero(item.getValor())) {
						throw new ApplicationException("message.empty", new String[] {"Itens com valor zerado. Favor inserir o valor total dos itens."}, FacesMessage.SEVERITY_WARN);
					}
					if(TreatString.isBlank(item.getCodigoUnidade())) {
						throw new ApplicationException("message.empty", new String[] {"Itens sem unidade. Favor inserir a unidade dos itens."}, FacesMessage.SEVERITY_WARN);
					}
					if(Util.isNullOrZero(item.getQuantidade())) {
						throw new ApplicationException("message.empty", new String[] {"Itens sem quantidade. Favor inserir a quantidade dos itens."}, FacesMessage.SEVERITY_WARN);
					}
					if(TreatString.isNotBlank(item.getObservacao())) {
						if(item.getObservacao().length() > 300) {
							throw new ApplicationException("message.empty", new String[] {"Máximo 300 caracteres para a observação dos itens."}, FacesMessage.SEVERITY_WARN);
						}
					} else {
						throw new ApplicationException("message.empty", new String[] {"Itens sem observação. Favor inserir a observação dos itens."}, FacesMessage.SEVERITY_WARN);
					}
				}
			}
			
			cotacaoBusiness.salvar(getSolicitacaoCompra(), getCotacao());
			
			solicitacaoCompraBusiness.calcularMelhorOpcao(getSolicitacaoCompra());
			
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
			
			solicitacaoCompraBusiness.calcularMelhorOpcao(getSolicitacaoCompra());
			
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
				BigDecimal valor = item.getValor();
				BigDecimal quantidade = item.getQuantidade();
				if(Util.isNotNull(quantidade)) {
					BigDecimal valorTotal = valor.multiply(quantidade);
					item.setValorTotal(valorTotal);
				} else {
					item.setValorTotal(new BigDecimal(0));
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcular" }, e);
		}
	}
	
	public String calcularTotal() throws ApplicationException {
		try {
			if(Util.isNotNull(getCotacao().getItens())) {
				BigDecimal total = new BigDecimal(0);
				for(CotacaoItem item : getCotacao().getItens()) {
					if(Util.isNotNull(item.getValorTotal())) {
						total = total.add(item.getValorTotal());
					}
				}
				
				if(Util.isNotNull(getCotacao().getDesconto())) {
					total = total.subtract(getCotacao().getDesconto());
				}
				
				if(Util.isNotNull(getCotacao().getFrete())) {
					total = total.add(getCotacao().getFrete());
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
			item.setQuantidade(new BigDecimal(1));
			item.setCodigoUnidade(null);
			item.setValorTotal(new BigDecimal(0));
			item.setObservacao("");
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
				if(getOrigem().equals("MINHA_SOLICITACAO")) {
					return "/pages/solicitacaoCompra/solicitacao/listar_minhasSolicitacoes.xhtml?faces-redirect=true";
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
			if(Util.isNotNull(getSolicitacaoCompra())) {
				getSolicitacaoCompra().setItens(new HashSet<SolicitacaoCompraItem>(solicitacaoCompraBusiness.listarItensPorSolicitacao(getSolicitacaoCompra().getId())));
				getSolicitacaoCompra().setCotacoes(new HashSet<Cotacao>(solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(getSolicitacaoCompra().getId())));
			}
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

	public List<UnidadeRM> getListaUnidade() {
		return listaUnidade;
	}

	public void setListaUnidade(List<UnidadeRM> listaUnidade) {
		this.listaUnidade = listaUnidade;
	}

	public Boolean getPossuiMinimoCotacao() {
		return possuiMinimoCotacao;
	}

	public void setPossuiMinimoCotacao(Boolean possuiMinimoCotacao) {
		this.possuiMinimoCotacao = possuiMinimoCotacao;
	}

	public OrdemCompra getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(OrdemCompra ordemCompra) {
		this.ordemCompra = ordemCompra;
	}
	
	
}
