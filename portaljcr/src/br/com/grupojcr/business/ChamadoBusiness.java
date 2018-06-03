package br.com.grupojcr.business;

import java.io.File;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AnexoChamadoDAO;
import br.com.grupojcr.dao.ChamadoDAO;
import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.dto.ArquivoDTO;
import br.com.grupojcr.dto.ChamadoDTO;
import br.com.grupojcr.entity.AnexoChamado;
import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.PrioridadeChamado;
import br.com.grupojcr.enumerator.SituacaoChamado;
import br.com.grupojcr.util.Dominios;
import br.com.grupojcr.util.TreatFile;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class ChamadoBusiness {
	
	private static Logger LOG = Logger.getLogger(ChamadoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private ChamadoDAO daoChamado;
	
	@EJB
	private UsuarioDAO daoUsuario;
	
	@EJB
	private AnexoChamadoDAO daoAnexoChamado;
	
	
	public void salvar(ChamadoDTO dto, Long idUsuario) throws ApplicationException {
		try {
			if(Util.isNull(dto.getSubcategoria())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher os campos obrigatórios (*)"}, FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(dto.getPrioridade())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher os campos obrigatórios (*)"}, FacesMessage.SEVERITY_WARN);
			}
			if(TreatString.isBlank(dto.getTitulo())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher os campos obrigatórios (*)"}, FacesMessage.SEVERITY_WARN);
			}
			if(TreatString.isBlank(dto.getDescricao())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher os campos obrigatórios (*)"}, FacesMessage.SEVERITY_WARN);
			}
			
			Chamado chamado = new Chamado();
			chamado.setCategoria(dto.getSubcategoria().getCategoria().getNome());
			chamado.setSubcategoria(dto.getSubcategoria().getNome());
			chamado.setPrioridade(dto.getPrioridade());
			chamado.setTitulo(dto.getTitulo());
			chamado.setDescricao(dto.getDescricao());
			chamado.setSituacao(SituacaoChamado.ABERTO);
			Calendar calendario = Calendar.getInstance();
			chamado.setDtAbertura(calendario.getTime());
			
			if(dto.getPrioridade().equals(PrioridadeChamado.ALTA)) {
				calendario.add(Calendar.DAY_OF_MONTH, 1);
			} else if(dto.getPrioridade().equals(PrioridadeChamado.MEDIA)) {
				calendario.add(Calendar.DAY_OF_MONTH, 2);
			} else {
				calendario.add(Calendar.DAY_OF_MONTH, 5);
			}
			chamado.setDtPrevisao(calendario.getTime());
			Usuario usuarioLogado = daoUsuario.obterUsuarioPorId(idUsuario);
			chamado.setUsuarioSolicitante(usuarioLogado);
			
			daoChamado.incluir(chamado);
			
			String caminhoServidor = Dominios.SERVER_PATH;
			if(CollectionUtils.isNotEmpty(dto.getAnexos())) {
				for(ArquivoDTO arquivo : dto.getAnexos()) {
					String caminhoArquivo = caminhoServidor + File.separator + "CHAMADOS" + File.separator + chamado.getId() + File.separator;
					TreatFile.saveFileByFolder(arquivo.getData(), caminhoArquivo, arquivo.getNome());
					
					AnexoChamado anexoChamado = new AnexoChamado();
					anexoChamado.setCaminho(caminhoArquivo);
					anexoChamado.setChamado(chamado);
					anexoChamado.setDtInclusao(Calendar.getInstance().getTime());
					anexoChamado.setNome(arquivo.getNome());
					anexoChamado.setUsuario(usuarioLogado);
					
					daoAnexoChamado.incluir(anexoChamado);
					
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}

}
