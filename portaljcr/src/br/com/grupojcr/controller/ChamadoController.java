package br.com.grupojcr.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
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
import br.com.grupojcr.enumerator.PrioridadeChamado;
import br.com.grupojcr.util.Dominios;
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
	
	private ChamadoDTO chamadoDTO;
	private ArquivoDTO arquivoDTO;
	private Chamado chamado;
	
	private String mensagem;
	
	@EJB
	private CategoriaChamadoBusiness categoriaChamadoBusiness;
	
	@EJB
	private ChamadoBusiness chamadoBusiness;

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
			ArquivoDTO arquivo = new ArquivoDTO(nomeArquivo, event.getFile().getInputstream(), event.getFile());
			getChamadoDTO().getAnexos().add(arquivo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "doUpload" }, e);
		}
	}
	
	public void salvar() throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			chamadoBusiness.salvar(getChamadoDTO(), usuario.getId());
			
			Message.setMessage("chamado.salvar");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	public void excluirAnexo(ArquivoDTO dto) throws ApplicationException {
		try {
			getChamadoDTO().getAnexos().remove(dto);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirAnexo" }, e);
		}
	}
	
	public void atribuir() throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			chamadoBusiness.atribuir(getChamado(), usuario);
			
			Message.setMessage("chamado.atribuido");
			
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
	
	public String exibir() throws ApplicationException {
		try {
			getChamado().setMensagens(new HashSet<ChamadoAcompanhamento>(chamadoBusiness.listarAcompanhamentoChamado(getChamado().getId())));
			getChamado().setAnexos(new HashSet<AnexoChamado>(chamadoBusiness.listarAnexoPorChamado(getChamado().getId())));
			return "/pages/suporte/exibir_chamado.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "exibir" }, e);
		}
	}
	
	public String voltar() throws ApplicationException {
		try {
			return "/pages/suporte/painel_chamado.xhtml?faces-redirect=true";
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

}
