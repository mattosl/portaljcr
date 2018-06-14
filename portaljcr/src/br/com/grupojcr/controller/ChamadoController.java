package br.com.grupojcr.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.grupojcr.business.CategoriaChamadoBusiness;
import br.com.grupojcr.business.ChamadoBusiness;
import br.com.grupojcr.dto.ArquivoDTO;
import br.com.grupojcr.dto.ChamadoDTO;
import br.com.grupojcr.entity.AnexoChamado;
import br.com.grupojcr.entity.CategoriaChamado;
import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.entity.ChamadoAcompanhamento;
import br.com.grupojcr.entity.SubCategoriaChamado;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.CausaChamado;
import br.com.grupojcr.enumerator.LocalizacaoChamado;
import br.com.grupojcr.enumerator.PrioridadeChamado;
import br.com.grupojcr.util.Dominios;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatFile;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;
import br.com.grupojcr.util.exception.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class ChamadoController implements Serializable {
	
	private static final long serialVersionUID = 934937300949501891L;
	protected static Logger LOG = Logger.getLogger(ChamadoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private List<SelectItem> listaCategoria;
	private List<PrioridadeChamado> listaPrioridade;
	private List<CausaChamado> listaCausaChamado;
	private List<LocalizacaoChamado> listaLocalizacao;
	
	private ChamadoDTO chamadoDTO;
	private ArquivoDTO arquivoDTO;
	private Chamado chamado;
	
	private String mensagem;
	private String origem;
	
	@EJB
	private CategoriaChamadoBusiness categoriaChamadoBusiness;
	
	@EJB
	private ChamadoBusiness chamadoBusiness;
	
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
			carregarCategorias();
			setListaPrioridade(Arrays.asList(PrioridadeChamado.values()));
			setListaLocalizacao(Arrays.asList(LocalizacaoChamado.values()));
			setChamadoDTO(new ChamadoDTO());
			getChamadoDTO().setPrioridade(PrioridadeChamado.MEDIA);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}
	
	private void carregarCategorias() throws ApplicationException {
		try {
			setListaCategoria(new ArrayList<SelectItem>());
			List<CategoriaChamado> categorias = categoriaChamadoBusiness.listarCategoriaChamado();
			
			if(Util.isNotNull(categorias)) {
				for(CategoriaChamado cc : categorias) {
					SelectItemGroup grupo = new SelectItemGroup(cc.getNome());
					
					List<SelectItem> itens = new ArrayList<SelectItem>();
					for(SubCategoriaChamado sc : cc.getSubCategorias()) {
						itens.add(new SelectItem(sc, sc.getNome()));
					}
					
					SelectItem [] selectItem = new SelectItem[itens.size()];
					itens.toArray(selectItem);
					grupo.setSelectItems(selectItem);
					
					getListaCategoria().add(grupo);
					
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarCategorias" }, e);
		}
	}
	
	public void doUpload(FileUploadEvent event) throws ApplicationException {
		try {
			String nomeArquivo = Util.converterEncoding(event.getFile().getFileName(), 
					Dominios.ENCODING_ISO_8859_1, Dominios.ENCODING_UTF_8);
			
			Integer idx = 1;
			while(existeAnexo(nomeArquivo, Boolean.TRUE).equals(Boolean.TRUE)) {
				nomeArquivo = idx + nomeArquivo;
				idx++;
			}
			ArquivoDTO arquivo = new ArquivoDTO(nomeArquivo, event.getFile().getInputstream(), event.getFile());
			getChamadoDTO().getAnexos().add(arquivo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "doUpload" }, e);
		}
	}
	
	private Boolean existeAnexo(String nomeArquivo, Boolean chamadoNovo) throws ApplicationException {
		try {
			if(chamadoNovo) {
				
				for(ArquivoDTO dto : getChamadoDTO().getAnexos()) {
					if(dto.getNome().equalsIgnoreCase(nomeArquivo)) {
						return Boolean.TRUE;
					}
				}
			} else {
				for(AnexoChamado anexo : getChamado().getAnexos()) {
					if(anexo.getNome().equalsIgnoreCase(nomeArquivo)) {
						return Boolean.TRUE;
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "existeAnexo" }, e);
		}
	}
	
	public void doUploadDireto(FileUploadEvent event) throws ApplicationException {
		try {
			String nomeArquivo = Util.converterEncoding(event.getFile().getFileName(), 
					Dominios.ENCODING_ISO_8859_1, Dominios.ENCODING_UTF_8);
			
			Integer idx = 1;
			while(existeAnexo(nomeArquivo, Boolean.FALSE).equals(Boolean.TRUE)) {
				nomeArquivo = idx + nomeArquivo;
				idx++;
			}
			ArquivoDTO arquivo = new ArquivoDTO(nomeArquivo, event.getFile().getInputstream(), event.getFile());
			
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			AnexoChamado anexoChamado = chamadoBusiness.adicionarAnexoChamado(getChamado(), usuario, arquivo);
			
			adicionarMensagem("Adicionei um anexo: " + nomeArquivo);
			
			getChamado().getAnexos().add(anexoChamado);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "doUpload" }, e);
		}
	}
	
	public String salvar() throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			Chamado chamado = chamadoBusiness.salvar(getChamadoDTO(), usuario.getId());
			
			chamadoBusiness.enviarEmailNovoChamado(chamado);
			
			Message.setMessage("chamado.salvar");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
		return "/pages/suporte/listar_chamado.xhtml?faces-redirect=true";
	}
	
	public void excluirAnexo(ArquivoDTO dto) throws ApplicationException {
		try {
			getChamadoDTO().getAnexos().remove(dto);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirAnexo" }, e);
		}
	}
	
	public void excluirAnexo(AnexoChamado anexo) throws ApplicationException {
		try {
			getChamado().getAnexos().remove(anexo);
			chamadoBusiness.excluirAnexoChamado(anexo);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirAnexo" }, e);
		}
	}
	
	public void atribuir() throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			Chamado chamado = chamadoBusiness.atribuir(getChamado(), usuario);
			
			chamadoBusiness.enviarEmailAtribuido(chamado);
			
			Message.setMessage("chamado.atribuido");
			
			adicionarMensagem("Olá, vou atender o seu chamado e a partir de agora sou responsável por ele!");
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "atribuir" }, e);
		}
	}
	
	public StreamedContent download(AnexoChamado anexo) throws ApplicationException {
		try {
			return new DefaultStreamedContent(new ByteArrayInputStream(TreatFile.fileToByte(new File(anexo.getCaminho() + File.separator + anexo.getNome()))), null, anexo.getNome());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "download" }, e);
		}
	}
	
	public void enviarMensagem() throws ApplicationException {
		try {
			if(TreatString.isNotBlank(mensagem)) {
				Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
				chamadoBusiness.enviarMensagem(getMensagem(), usuario, getChamado());
				
				if(chamado.getUsuarioSolicitante().equals(usuario)) {
					if(Util.isNotNull(getChamado().getUsuarioResponsavel())) {
						chamadoBusiness.enviarEmailAcompanhamento(getChamado(), mensagem, usuario, getChamado().getUsuarioResponsavel());
					}
				} else {
					chamadoBusiness.enviarEmailAcompanhamento(getChamado(), mensagem, usuario, getChamado().getUsuarioSolicitante());
				}
				
				setMensagem(null);
				getChamado().setMensagens(new HashSet<ChamadoAcompanhamento>(chamadoBusiness.listarAcompanhamentoChamado(getChamado().getId())));
			} else {
				throw new ApplicationException("message.empty", new String[] {"Escreva uma mensagem."}, FacesMessage.SEVERITY_WARN);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "enviarMensagem" }, e);
		}
	}
	
	public String editar() throws ApplicationException {
		try {
			getChamado().setMensagens(new HashSet<ChamadoAcompanhamento>(chamadoBusiness.listarAcompanhamentoChamado(getChamado().getId())));
			getChamado().setAnexos(new HashSet<AnexoChamado>(chamadoBusiness.listarAnexoPorChamado(getChamado().getId())));
			return "/pages/suporte/editar_chamado.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "editar" }, e);
		}
	}
	
	public String carregarDadosParametros() throws ApplicationException {
		try {
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			String parametro = params.get("idChamado");
			if(Util.isNotNull(parametro)) {
				try {
					Long idChamado = Long.parseLong(parametro);
					Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
					setChamado(chamadoBusiness.obterChamado(idChamado));
					
					if(!loginController.hasGroup(Arrays.asList("ADMINISTRADOR", "SUPORTE"))) {
						if(!getChamado().getUsuarioSolicitante().getId().equals(usuario.getId())) {
							Message.setMessage("message.empty", new String[] { "Você não possui permissão para visualizar este chamado." }, FacesMessage.SEVERITY_FATAL);
							return "/pages/suporte/listar_chamado.xhtml?faces-redirect=true";
						}
					}
					editar();
				} catch (NumberFormatException e) {
					Message.setMessage("message.empty", new String[] { "Chamado não existe." }, FacesMessage.SEVERITY_FATAL);
					return "/pages/suporte/listar_chamado.xhtml?faces-redirect=true";
				}
			}
			
			if(Util.isNull(getChamado())) {
				Message.setMessage("message.empty", new String[] { "Chamado não existe." }, FacesMessage.SEVERITY_FATAL);
				return "/pages/suporte/listar_chamado.xhtml?faces-redirect=true"; 
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarDadosParametros" }, e);
		}
		return null;
	}
	
	public void carregarCausas() throws ApplicationException {
		try {
			setListaCausaChamado(new ArrayList<CausaChamado>(Arrays.asList(CausaChamado.values())));
			getChamado().setCausa(CausaChamado.NORMAL);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarCausas" }, e);
		}
	}
	
	public String solucionarChamado() throws ApplicationException {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().getFlash().setKeepMessages(true);
			
			if(Util.isNull(getChamado().getCausa())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			}
			
			if(TreatString.isBlank(getChamado().getSolucao())) {
				throw new ApplicationException("message.campos.obrigatorios", FacesMessage.SEVERITY_WARN);
			} else {
				if(getChamado().getSolucao().length() > 300) {
					throw new ApplicationException("message.empty", new String[] {"Máximo 300 caracteres para a Solução."}, FacesMessage.SEVERITY_WARN);
				}
			}
			
			chamadoBusiness.solucionar(getChamado());
			
			chamadoBusiness.enviarEmailResolvido(getChamado());
			
			Message.setMessage("chamado.resolvido", new String[] {getChamado().getId().toString()});
			
			adicionarMensagem("Adicionei uma solução ao seu chamado: " + getChamado().getSolucao());
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "solucionarChamado" }, e);
		}
		return voltar();
	}
	
	public String encerrarChamado() throws ApplicationException {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().getFlash().setKeepMessages(true);
			
			if(TreatString.isNotBlank(getChamado().getFeedback())) {
				if(getChamado().getSolucao().length() > 200) {
					throw new ApplicationException("message.empty", new String[] {"Máximo 200 caracteres para o feedback."}, FacesMessage.SEVERITY_WARN);
				}
			}
			
			chamadoBusiness.encerrar(getChamado());
			
			Message.setMessage("chamado.encerrado", new String[] {getChamado().getId().toString()});
			
			if(TreatString.isNotBlank(getChamado().getFeedback())) {
				adicionarMensagem("Chamado encerrado. Feedback: " + getChamado().getFeedback());
			} else {
				adicionarMensagem("Chamado encerrado. Obrigado!");
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "encerrarChamado" }, e);
		}
		return voltar();
	}
	
	private void adicionarMensagem(String mensagem) throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			chamadoBusiness.enviarMensagem(mensagem, usuario, getChamado());
			
			getChamado().setMensagens(new HashSet<ChamadoAcompanhamento>(chamadoBusiness.listarAcompanhamentoChamado(getChamado().getId())));
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarMensagem" }, e);
		}
	}
	
	public Integer calcularTempoResolucao(Chamado chamado) throws ApplicationException {
		try {
			if(Util.isNotNull(chamado.getDtResolucao())) {
				return TreatDate.contarDiferencaEmDias(chamado.getDtAbertura(), chamado.getDtResolucao()); 
			} else {
				return TreatDate.contarDiferencaEmDias(chamado.getDtAbertura(), Calendar.getInstance().getTime());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "calcularTempoResolucao" }, e);
		}
	}
	
	public String voltar() throws ApplicationException {
		try {
			if(TreatString.isNotBlank(origem)) {
				if(origem.equals("PAINEL")) {
					return "/pages/suporte/painel_chamado.xhtml?faces-redirect=true";
				}
			}
			return "/pages/suporte/listar_chamado.xhtml?faces-redirect=true";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "voltar" }, e);
		}
	}
	
	public List<SelectItem> getListaCategoria() {
		return listaCategoria;
	}

	public void setListaCategoria(List<SelectItem> listaCategoria) {
		this.listaCategoria = listaCategoria;
	}

	public List<PrioridadeChamado> getListaPrioridade() {
		return listaPrioridade;
	}

	public void setListaPrioridade(List<PrioridadeChamado> listaPrioridade) {
		this.listaPrioridade = listaPrioridade;
	}

	public ChamadoDTO getChamadoDTO() {
		return chamadoDTO;
	}

	public void setChamadoDTO(ChamadoDTO chamadoDTO) {
		this.chamadoDTO = chamadoDTO;
	}

	public ArquivoDTO getArquivoDTO() {
		return arquivoDTO;
	}

	public void setArquivoDTO(ArquivoDTO arquivoDTO) {
		this.arquivoDTO = arquivoDTO;
	}

	public Chamado getChamado() {
		return chamado;
	}

	public void setChamado(Chamado chamado) {
		this.chamado = chamado;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public List<CausaChamado> getListaCausaChamado() {
		return listaCausaChamado;
	}

	public void setListaCausaChamado(List<CausaChamado> listaCausaChamado) {
		this.listaCausaChamado = listaCausaChamado;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public List<LocalizacaoChamado> getListaLocalizacao() {
		return listaLocalizacao;
	}

	public void setListaLocalizacao(List<LocalizacaoChamado> listaLocalizacao) {
		this.listaLocalizacao = listaLocalizacao;
	}

}
