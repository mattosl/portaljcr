package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.ColigadaBusiness;
import br.com.grupojcr.business.OrcamentoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.business.UsuarioBusiness;
import br.com.grupojcr.dto.ResponsavelOrcamentoDTO;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class ResponsavelOrcamentoController implements Serializable {
	
	private static final long serialVersionUID = -5214010860970587430L;
	protected static Logger LOG = Logger.getLogger(ResponsavelOrcamentoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private Boolean exibirResultado;
	
	private Coligada coligada;
	private List<ResponsavelOrcamentoDTO> listaResponsavel;
	private List<Coligada> listaColigada;

	private Usuario usuario;
	private CentroCustoRM centroCusto;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private OrcamentoBusiness orcamentoBusiness;
	
	@EJB
	private ColigadaBusiness coligadaBusiness;
	
	@EJB
	private UsuarioBusiness usuarioBusiness;
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			setColigada(null);
			setExibirResultado(Boolean.FALSE);
			setListaColigada(coligadaBusiness.listarColigadas());
			
			setListaResponsavel(new ArrayList<ResponsavelOrcamentoDTO>());
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	
	public void carregarCentroCusto() throws ApplicationException {
		try {
			setListaResponsavel(new ArrayList<ResponsavelOrcamentoDTO>());
			if(Util.isNotNull(getColigada())) {
				List<CentroCustoRM> centrosCusto = rmBusiness.listarCentroCustoPorColigada(getColigada().getId());
				if(CollectionUtils.isNotEmpty(centrosCusto)) {
					for(CentroCustoRM cc : centrosCusto) {
						ResponsavelOrcamentoDTO dto = new ResponsavelOrcamentoDTO();
						dto.setCentroCusto(cc);
						dto.setListaResponsavel(orcamentoBusiness.listarUsuarioResponsavel(cc.getCodigoCentroCusto(), getColigada().getId()));
						getListaResponsavel().add(dto);
					}
				}
				setExibirResultado(Boolean.TRUE);
			} else {
				setExibirResultado(Boolean.FALSE);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarCentroCusto" }, e);
		}
	}
	
	public void adicionarResponsavel() throws ApplicationException {
		try {
			setListaResponsavel(new ArrayList<ResponsavelOrcamentoDTO>());
			if(Util.isNotNull(getColigada())) {
				List<CentroCustoRM> centrosCusto = rmBusiness.listarCentroCustoPorColigada(getColigada().getId());
				if(CollectionUtils.isNotEmpty(centrosCusto)) {
					for(CentroCustoRM cc : centrosCusto) {
						ResponsavelOrcamentoDTO dto = new ResponsavelOrcamentoDTO();
						dto.setCentroCusto(cc);
						dto.setListaResponsavel(orcamentoBusiness.listarUsuarioResponsavel(cc.getCodigoCentroCusto(), getColigada().getId()));
						getListaResponsavel().add(dto);
					}
				}
				setExibirResultado(Boolean.TRUE);
			} else {
				setExibirResultado(Boolean.FALSE);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarCentroCusto" }, e);
		}
	}
	
	public void adicionarUsuario() throws ApplicationException {
		try {
			if(Util.isNull(getUsuario())) {
				throw new ApplicationException("responsavel.orcamento", FacesMessage.SEVERITY_WARN);
			}
			
			orcamentoBusiness.salvarResponsavel(getCentroCusto(), getUsuario(), getColigada());
			
			Message.setMessage("usuario.adicionado", new String[] {getUsuario().getNome(), getCentroCusto().getCentroCusto()});
			
			setUsuario(null);
			setCentroCusto(null);
			
			adicionarResponsavel();
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarUsuario" }, e);
		}
	}
	
	public void excluirUsuario() throws ApplicationException {
		try {
			orcamentoBusiness.excluirResponsavel(getCentroCusto(), getUsuario(), getColigada());
			
			Message.setMessage("usuario.removido", new String[] {getUsuario().getNome(), getCentroCusto().getCentroCusto()});
			
			setUsuario(null);
			setCentroCusto(null);
			
			adicionarResponsavel();
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirUsuario" }, e);
		}
	}
	
	public List<Usuario> autocompleteUsuario(String nome) throws ApplicationException {
		try {
			return usuarioBusiness.listarUsuarioPorNome(nome);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autocompleteUsuario" }, e);
		}
	}
	
	public String voltar() throws ApplicationException {
		try {
			return "/pages/orcamento/ajusteOrcamentario/listar_ajusteOrcamentario.xhtml?faces-redirect=true";
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

	public List<Coligada> getListaColigada() {
		return listaColigada;
	}

	public void setListaColigada(List<Coligada> listaColigada) {
		this.listaColigada = listaColigada;
	}


	public Coligada getColigada() {
		return coligada;
	}


	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}
	public List<ResponsavelOrcamentoDTO> getListaResponsavel() {
		return listaResponsavel;
	}


	public void setListaResponsavel(List<ResponsavelOrcamentoDTO> listaResponsavel) {
		this.listaResponsavel = listaResponsavel;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public CentroCustoRM getCentroCusto() {
		return centroCusto;
	}


	public void setCentroCusto(CentroCustoRM centroCusto) {
		this.centroCusto = centroCusto;
	}



}
