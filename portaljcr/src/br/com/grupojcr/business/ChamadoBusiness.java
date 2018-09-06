package br.com.grupojcr.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AnexoChamadoDAO;
import br.com.grupojcr.dao.ChamadoAcompanhamentoDAO;
import br.com.grupojcr.dao.ChamadoDAO;
import br.com.grupojcr.dao.GrupoDAO;
import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.dto.ArquivoDTO;
import br.com.grupojcr.dto.ChamadoDTO;
import br.com.grupojcr.dto.FiltroChamado;
import br.com.grupojcr.dto.FiltroRelatorioChamado;
import br.com.grupojcr.email.EmailChamado;
import br.com.grupojcr.entity.AnexoChamado;
import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.entity.ChamadoAcompanhamento;
import br.com.grupojcr.entity.Grupo;
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
	
	@EJB
	private ChamadoAcompanhamentoDAO daoChamadoAcompanhamento;
	
	@EJB
	private GrupoDAO daoGrupo;
	
	
	public Chamado salvar(ChamadoDTO dto, Long idUsuario) throws ApplicationException {
		try {
			if(Util.isNull(dto.getSubcategoria())) {
				throw new ApplicationException("message.empty", new String[] {"Favor preencher os campos obrigatórios (*)"}, FacesMessage.SEVERITY_WARN);
			}
			if(Util.isNull(dto.getLocalizacao())) {
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
			} else {
				if(dto.getDescricao().length() > 500) {
					throw new ApplicationException("message.empty", new String[] {"Máximo 500 caracteres para a descrição do chamado."}, FacesMessage.SEVERITY_WARN);
				}
			}
			
			Chamado chamado = new Chamado();
			chamado.setCategoria(dto.getSubcategoria().getCategoria().getNome());
			chamado.setSubcategoria(dto.getSubcategoria().getNome());
			chamado.setPrioridade(dto.getPrioridade());
			chamado.setTitulo(dto.getTitulo());
			chamado.setDescricao(dto.getDescricao());
			chamado.setSituacao(SituacaoChamado.ABERTO);
			chamado.setLocalizacao(dto.getLocalizacao().getDescricao());
			Calendar calendario = Calendar.getInstance();
			
			while(calendario.get(Calendar.DAY_OF_WEEK) == 1 || calendario.get(Calendar.DAY_OF_WEEK) == 7) {
				calendario.add(Calendar.DAY_OF_MONTH, 1);
			}
			
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
			
			if(CollectionUtils.isNotEmpty(dto.getAnexos())) {
				for(ArquivoDTO arquivo : dto.getAnexos()) {
					adicionarAnexoChamado(chamado, usuarioLogado, arquivo);
				}
			}
			
			return chamado;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	@Asynchronous
	public void enviarEmailNovoChamado(Chamado chamado) throws ApplicationException {
		try {
			Grupo grupo = daoGrupo.obterGrupo("SUPORTE");
			List<String> destinatarios = new ArrayList<String>();
			if(TreatString.isNotBlank(chamado.getUsuarioSolicitante().getEmail())) {
				destinatarios.add(chamado.getUsuarioSolicitante().getEmail().trim());
			}
	
			if(CollectionUtils.isNotEmpty(grupo.getUsuarios())) {
				for(Usuario user : grupo.getUsuarios()) {
					if(TreatString.isNotBlank(user.getEmail())) {
						destinatarios.add(user.getEmail());
					}
				}
			}
			
			if(CollectionUtils.isNotEmpty(destinatarios)) {
				EmailChamado email = new EmailChamado();
				email.novoChamado("[SUPORTE] NOVO CHAMADO Nº " + chamado.getId(), destinatarios, chamado);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Asynchronous
	public void enviarEmailAtribuido(Chamado chamado) throws ApplicationException {
		try {
			List<String> destinatarios = new ArrayList<String>();
			if(TreatString.isNotBlank(chamado.getUsuarioSolicitante().getEmail())) {
				destinatarios.add(chamado.getUsuarioSolicitante().getEmail().trim());
				destinatarios.add(chamado.getUsuarioResponsavel().getEmail().trim());
			}
	
			if(CollectionUtils.isNotEmpty(destinatarios)) {
				EmailChamado email = new EmailChamado();
				email.atribuido("[SUPORTE] CHAMADO Nº " + chamado.getId() + " EM ANDAMENTO", destinatarios, chamado);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Asynchronous
	public void enviarEmailResolvido(Chamado chamado) throws ApplicationException {
		try {
			List<String> destinatarios = new ArrayList<String>();
			if(TreatString.isNotBlank(chamado.getUsuarioSolicitante().getEmail())) {
				destinatarios.add(chamado.getUsuarioSolicitante().getEmail().trim());
				destinatarios.add(chamado.getUsuarioResponsavel().getEmail().trim());
			}
	
			if(CollectionUtils.isNotEmpty(destinatarios)) {
				EmailChamado email = new EmailChamado();
				email.resolvido("[SUPORTE] CHAMADO Nº " + chamado.getId() + " RESOLVIDO", destinatarios, chamado);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Asynchronous
	public void enviarEmailAcompanhamento(Chamado chamado, String mensagem, Usuario usuarioLogado, Usuario usuarioDestino) throws ApplicationException {
		try {
			List<String> destinatarios = new ArrayList<String>();
			destinatarios.add(usuarioDestino.getEmail());
	
			if(CollectionUtils.isNotEmpty(destinatarios)) {
				EmailChamado email = new EmailChamado();
				email.novaMensagem("[SUPORTE] NOVA MENSAGEM NO CHAMADO Nº " + chamado.getId(), destinatarios, chamado, usuarioLogado, mensagem);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public AnexoChamado adicionarAnexoChamado(Chamado chamado, Usuario usuarioLogado, ArquivoDTO dto) throws ApplicationException {
		try {
			String caminhoServidor = Dominios.SERVER_PATH;
			String caminhoArquivo = caminhoServidor + File.separator + "CHAMADOS" + File.separator + chamado.getId() + File.separator;
			TreatFile.saveFileByFolder(dto.getData(), caminhoArquivo, dto.getNome());
			
			AnexoChamado anexoChamado = new AnexoChamado();
			anexoChamado.setCaminho(caminhoArquivo);
			anexoChamado.setChamado(chamado);
			anexoChamado.setDtInclusao(Calendar.getInstance().getTime());
			anexoChamado.setNome(dto.getNome());
			anexoChamado.setUsuario(usuarioLogado);
			
			daoAnexoChamado.incluir(anexoChamado);
			
			return anexoChamado;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "adicionarAnexoChamado" }, e);
		}
	}
	
	public void excluirAnexoChamado(AnexoChamado anexo) throws ApplicationException {
		try {
			Boolean deletado = new File(anexo.getCaminho() + File.separator + anexo.getNome()).delete();
			if(deletado) {
				daoAnexoChamado.excluir(anexo);
			} else {
				throw new ApplicationException("message.empty", new String[] {"Arquivo não pode ser excluído, pois está aberto."}, FacesMessage.SEVERITY_WARN);
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirAnexoChamado" }, e);
		}
	}
	
	public List<Chamado> listarChamadosPendentes() throws ApplicationException {
		try {
			return daoChamado.listarChamadosPendentes();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarChamadosPendentes" }, e);
		}
	}
	
	public List<AnexoChamado> listarAnexoPorChamado(Long idChamado) throws ApplicationException {
		try {
			return daoAnexoChamado.listarAnexoPorChamado(idChamado);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarAnexoPorChamado" }, e);
		}
	}
	
	public List<ChamadoAcompanhamento> listarAcompanhamentoChamado(Long idChamado) throws ApplicationException {
		try {
			return daoChamadoAcompanhamento.listarAcompanhamentoPorChamado(idChamado);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarAcompanhamentoChamado" }, e);
		}
	}
	
	public Chamado atribuir(Chamado chamado, Usuario usuario) throws ApplicationException {
		try {
			chamado.setUsuarioResponsavel(usuario);
			chamado.setSituacao(SituacaoChamado.EM_ANDAMENTO);
			daoChamado.alterar(chamado);
			return chamado;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "atribuir" }, e);
		}
	}
	
	public void solucionar(Chamado chamado) throws ApplicationException {
		try {
			chamado.setDtResolucao(Calendar.getInstance().getTime());
			chamado.setSituacao(SituacaoChamado.RESOLVIDO);
			daoChamado.alterar(chamado);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "solucionar" }, e);
		}
	}
	
	public void encerrar(Chamado chamado) throws ApplicationException {
		try {
			chamado.setDtFechamento(Calendar.getInstance().getTime());
			chamado.setSituacao(SituacaoChamado.FECHADO);
			daoChamado.alterar(chamado);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "encerrar" }, e);
		}
	}
	
	public void enviarMensagem(String mensagem, Usuario usuario, Chamado chamado) throws ApplicationException {
		try {
			ChamadoAcompanhamento acompanhamento = new ChamadoAcompanhamento();
			acompanhamento.setChamado(chamado);
			acompanhamento.setDtAcompanhamento(Calendar.getInstance().getTime());
			acompanhamento.setMensagem(mensagem);
			acompanhamento.setUsuario(usuario);
			daoChamadoAcompanhamento.incluir(acompanhamento);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "enviarMensagem" }, e);
		}
	}
	
	public Integer obterQtdChamado(FiltroChamado filtro) throws ApplicationException {
		try {
			return daoChamado.obterQtdChamado(filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdChamado" }, e);
		}
	}
	
	public Integer obterQtdChamadoRelatorio(FiltroRelatorioChamado filtro) throws ApplicationException {
		try {
			return daoChamado.obterQtdChamadoRelatorio(filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdChamado" }, e);
		}
	}
	
	public List<Chamado> listarChamadoPaginado(int first, int pageSize, FiltroChamado filtro) throws ApplicationException {
		try {
			return daoChamado.listarChamadoPaginado(first, pageSize, filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarChamadoPaginado" }, e);
		}
	}
	
	public List<Chamado> listarChamadoPaginadoRelatorio(int first, int pageSize, FiltroRelatorioChamado filtro) throws ApplicationException {
		try {
			return daoChamado.listarChamadoPaginadoRelatorio(first, pageSize, filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarChamadoPaginado" }, e);
		}
	}
	
	public Chamado obterChamado(Long idChamado) throws ApplicationException {
		try {
			return daoChamado.obterChamado(idChamado);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterChamado" }, e);
		}
	}
	
	public Long obterQtdChamadoPorTipo(FiltroRelatorioChamado filtro, SituacaoChamado situacao) throws ApplicationException {
		try {
			return daoChamado.obterQtdPorTipo(filtro, situacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdChamadoPorTipo" }, e);
		}
	}
	
	public Long obterQtdChamadoPorTipo(SituacaoChamado situacao) throws ApplicationException {
		try {
			return daoChamado.obterQtdPorTipo(situacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdChamadoPorTipo" }, e);
		}
	}
	
	public void fecharChamados() throws ApplicationException {
		try {
			List<Chamado> chamados = daoChamado.listarChamadosParaFechar();
			
			if(Util.isNotNull(chamados)) {
				for(Chamado chamado : chamados) {
					chamado.setSituacao(SituacaoChamado.FECHADO);
					chamado.setDtFechamento(Calendar.getInstance().getTime());
					daoChamado.alterar(chamado);
				}
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "fecharChamados" }, e);
		}
	}

}
