package br.com.grupojcr.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import br.com.grupojcr.business.ColigadaBusiness;
import br.com.grupojcr.business.OrcamentoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.FiltroOrcamento;
import br.com.grupojcr.dto.OrcamentoDTO;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.datamodel.AjusteOrcamentarioDataModel;
import br.com.grupojcr.enumerator.Mes;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class AjusteOrcamentarioController implements Serializable {
	
	private static final long serialVersionUID = -5844434468020235131L;
	protected static Logger LOG = Logger.getLogger(AjusteOrcamentarioController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private Boolean exibirResultado;
	
	private FiltroOrcamento filtro;
	
	private List<Coligada> listaColigada;
	
	private List<CentroCustoRM> listaCentroCusto;
	
	private List<NaturezaOrcamentariaRM> listaNaturezaOrcamentaria;
	
	private List<Mes> listaMes;
	
	private AjusteOrcamentarioDTO ajuste;
	
	private List<AjusteOrcamentarioDTO> listaAjusteOrcamentario;
	
	@Inject
	private AjusteOrcamentarioDataModel dataModel;
	
	@EJB
	private OrcamentoBusiness orcamentoBusiness;
	
	@EJB
	private ColigadaBusiness coligadaBusiness;
	
	@EJB
	private RMBusiness rmBusiness;
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			setExibirResultado(Boolean.FALSE);
			setFiltro(new FiltroOrcamento());
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			getFiltro().setUsuarioLogado(usuario);
			getFiltro().setDtAjuste(Calendar.getInstance().getTime());
			
			setListaColigada(new ArrayList<Coligada>());
			if (Util.isNotNull(usuario.getColigadas())) {
				for (Coligada coligada : usuario.getColigadas()) {
					if (coligada.getSituacao()) {
						getListaColigada().add(coligada);
					}
				}
			}
			
			setListaCentroCusto(new ArrayList<CentroCustoRM>());
			
			if(orcamentoBusiness.obterQtdAjusteOrcamentario(getFiltro()) == 0) {
				setExibirResultado(Boolean.FALSE);
			} else {
				DataTable dt = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("gridForm:tableAjuste");
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
	
	public void carregarCentroCusto(Boolean edicao) throws ApplicationException {
		try {
			if(edicao) {
				if(Util.isNotNull(getAjuste().getColigada())) {
					getAjuste().setPeriodo(rmBusiness.obterPeriodoColigada(getAjuste().getColigada().getId()));
					setListaCentroCusto(rmBusiness.listarCentroCustoPorColigada(getAjuste().getColigada().getId()));
				} else {
					getAjuste().setPeriodo(null);
					setListaCentroCusto(new ArrayList<CentroCustoRM>());
				}
				getAjuste().setCentroCusto(null);
				limparAjustes();
				zerarOrcamento();
			} else {
				if(Util.isNotNull(getFiltro().getColigada())) {
					setListaCentroCusto(rmBusiness.listarCentroCustoPorColigada(getFiltro().getColigada().getId()));
				} else {
					setListaCentroCusto(new ArrayList<CentroCustoRM>());
				}
				getFiltro().setCentroCusto(null);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarCentroCusto" }, e);
		}
	}
	
	public void limparAjustes() throws ApplicationException {
		try {
			getAjuste().setNaturezaOrigem(null);
			getAjuste().setNaturezaDestino(null);
			getAjuste().setMesOrigem(null);
			getAjuste().setMesDestino(null);
			getAjuste().setValor(null);
			zerarOrcamento();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "limparAjustes" }, e);
		}
	}
	
	public void calcularOrcamento() throws ApplicationException {
		try {
			zerarOrcamento();
			if(Util.isNotNull(getAjuste().getColigada()) && Util.isNotNull(getAjuste().getCentroCusto())) {
				if(Util.isNotNull(getAjuste().getPeriodo())) {
					if(Util.isNotNull(getAjuste().getNaturezaOrigem()) &&  Util.isNotNull(getAjuste().getMesOrigem())) {
						Integer idOrcamento = rmBusiness.obterOrcamento(getAjuste().getPeriodo(), getAjuste().getColigada().getId(), getAjuste().getNaturezaOrigem().getCodigoNaturezaOrcamentaria(), getAjuste().getCentroCusto().getCodigoCentroCusto());
						if(Util.isNotNull(idOrcamento)) {
							OrcamentoDTO dto = rmBusiness.obterOrcamentoCompleto(getAjuste().getPeriodo(), getAjuste().getColigada().getId(), idOrcamento, getAjuste().getMesOrigem().getId());
							if(Util.isNotNull(dto.getValorOrcado())) {
								getAjuste().setValorOrcadoOrigem(dto.getValorOrcado());
							}
							if(Util.isNotNull(dto.getSaldo())) {
								getAjuste().setValorSaldoOrigem(dto.getSaldo());
							}
						}
					}
					
					if(Util.isNotNull(getAjuste().getNaturezaDestino()) &&  Util.isNotNull(getAjuste().getMesDestino())) {
						Integer idOrcamento = rmBusiness.obterOrcamento(getAjuste().getPeriodo(), getAjuste().getColigada().getId(), getAjuste().getNaturezaDestino().getCodigoNaturezaOrcamentaria(), getAjuste().getCentroCusto().getCodigoCentroCusto());
						if(Util.isNotNull(idOrcamento)) {
							OrcamentoDTO dto = rmBusiness.obterOrcamentoCompleto(getAjuste().getPeriodo(), getAjuste().getColigada().getId(), idOrcamento, getAjuste().getMesDestino().getId());
							if(Util.isNotNull(dto.getValorOrcado())) {
								getAjuste().setValorOrcadoDestino(dto.getValorOrcado());
							}
							if(Util.isNotNull(dto.getSaldo())) {
								getAjuste().setValorSaldoDestino(dto.getSaldo());
							}
							
							if(Util.isNotNull(getAjuste().getValor())) {
								getAjuste().setValorNovoSaldo(getAjuste().getValorSaldoDestino().add(getAjuste().getValor()));
							} else {
								getAjuste().setValorNovoSaldo(getAjuste().getValorSaldoDestino());
							}
						}
					}
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcularOrcamento" }, e);
		}
	}

	public void adicionarAjuste() throws ApplicationException {
		try {
			if(Util.isNull(getAjuste().getColigada())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(getAjuste().getCentroCusto())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(getAjuste().getNaturezaOrigem())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(getAjuste().getNaturezaDestino())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(getAjuste().getMesOrigem())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(getAjuste().getMesDestino())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			if(getAjuste().getNaturezaOrigem().getCodigoNaturezaOrcamentaria().equals(getAjuste().getNaturezaDestino().getCodigoNaturezaOrcamentaria())
					&& getAjuste().getMesOrigem().getId().equals(getAjuste().getMesDestino().getId())) {
				throw new ApplicationException("ajuste.orcamentario.imcompativel", FacesMessage.SEVERITY_WARN);
				
			}
			
			calcularOrcamento();
			if(getAjuste().getValor().compareTo(getAjuste().getValorSaldoOrigem()) == 1) {
				throw new ApplicationException("ajuste.orcamentario.saldo.insuficiente", new String[] {getAjuste().getMesOrigem().getDescricao().toUpperCase(), getAjuste().getNaturezaOrigem().getNaturezaOrcamentaria().toUpperCase()}, FacesMessage.SEVERITY_WARN);
			}
			
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			rmBusiness.ajustarOrcamento(getAjuste(), usuario);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarAjuste" }, e);
		}
	}
	
	public void zerarOrcamento() throws ApplicationException {
		try {
			if(Util.isNotNull(getAjuste())) {
				getAjuste().setValorOrcadoOrigem(new BigDecimal(0));
				getAjuste().setValorSaldoOrigem(new BigDecimal(0));
				getAjuste().setValorOrcadoDestino(new BigDecimal(0));
				getAjuste().setValorSaldoDestino(new BigDecimal(0));
				getAjuste().setValorNovoSaldo(new BigDecimal(0));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "zerarOrcamento" }, e);
		}
	}
	
	public String iniciarAjusteOrcamentario() throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			setListaColigada(new ArrayList<Coligada>());
			if (Util.isNotNull(usuario.getColigadas())) {
				for (Coligada coligada : usuario.getColigadas()) {
					if (coligada.getSituacao()) {
						getListaColigada().add(coligada);
					}
				}
			}
			
			setListaNaturezaOrcamentaria(rmBusiness.listarNaturezaOrcamentaria());
			setListaMes(Mes.listarAPartir(6));
			
			setAjuste(new AjusteOrcamentarioDTO());
			zerarOrcamento();
			
			return "/pages/orcamento/ajusteOrcamentario/editar_ajusteOrcamentario.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "pesquisar" }, e);
		}
	}
	

	public Boolean getExibirResultado() {
		return exibirResultado;
	}

	public void setExibirResultado(Boolean exibirResultado) {
		this.exibirResultado = exibirResultado;
	}

	public FiltroOrcamento getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroOrcamento filtro) {
		this.filtro = filtro;
	}

	public List<Coligada> getListaColigada() {
		return listaColigada;
	}

	public void setListaColigada(List<Coligada> listaColigada) {
		this.listaColigada = listaColigada;
	}

	public List<CentroCustoRM> getListaCentroCusto() {
		return listaCentroCusto;
	}

	public void setListaCentroCusto(List<CentroCustoRM> listaCentroCusto) {
		this.listaCentroCusto = listaCentroCusto;
	}

	public List<NaturezaOrcamentariaRM> getListaNaturezaOrcamentaria() {
		return listaNaturezaOrcamentaria;
	}

	public void setListaNaturezaOrcamentaria(List<NaturezaOrcamentariaRM> listaNaturezaOrcamentaria) {
		this.listaNaturezaOrcamentaria = listaNaturezaOrcamentaria;
	}

	public AjusteOrcamentarioDTO getAjuste() {
		return ajuste;
	}

	public void setAjuste(AjusteOrcamentarioDTO ajuste) {
		this.ajuste = ajuste;
	}

	public List<Mes> getListaMes() {
		return listaMes;
	}

	public void setListaMes(List<Mes> listaMes) {
		this.listaMes = listaMes;
	}

	public List<AjusteOrcamentarioDTO> getListaAjusteOrcamentario() {
		return listaAjusteOrcamentario;
	}

	public void setListaAjusteOrcamentario(List<AjusteOrcamentarioDTO> listaAjusteOrcamentario) {
		this.listaAjusteOrcamentario = listaAjusteOrcamentario;
	}


}
