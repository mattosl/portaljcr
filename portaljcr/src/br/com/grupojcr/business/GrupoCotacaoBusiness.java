package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.GrupoCotacaoDAO;
import br.com.grupojcr.dao.GrupoDAO;
import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.entity.Grupo;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class GrupoCotacaoBusiness {

	protected static Logger LOG = Logger.getLogger(GrupoCotacaoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private GrupoCotacaoDAO daoGrupoCotacao;
	
	@EJB
	private GrupoDAO daoGrupo;

	@EJB
	private UsuarioDAO daoUsuario;

	public List<GrupoCotacao> listar() throws ApplicationException {
		try {
			return daoGrupoCotacao.listarGrupoCotacao();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listar" }, e);
		}
	}
	
	public List<GrupoCotacao> listarGruposAtivos() throws ApplicationException {
		try {
			return daoGrupoCotacao.listarGrupoCotacaoAtivos();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarGruposAtivos" }, e);
		}
	}
	
	public void salvar(GrupoCotacao gc) throws ApplicationException {
		try {
			if(TreatString.isBlank(gc.getNome())) {
				throw new ApplicationException("message.empty", new String[] {"Nome do grupo de cotação é obrigatório"},  FacesMessage.SEVERITY_ERROR);
			}
			if(CollectionUtils.isEmpty(gc.getUsuarios())) {
				throw new ApplicationException("grupo.cotacao.usuario.vazio", FacesMessage.SEVERITY_WARN);
			}
			if(daoGrupoCotacao.verificarGrupoCotacaoExiste(gc.getNome(), gc.getId())) {
				throw new ApplicationException("grupo.cotacao.existente", FacesMessage.SEVERITY_WARN);
			}
			
			Grupo grupoCotador = daoGrupo.obterGrupo("COTACAO");
			if(Util.isNotNull(gc.getId())) {
				GrupoCotacao grupoAtual = daoGrupoCotacao.obterGrupoCotacao(gc.getId());
				if(CollectionUtils.isNotEmpty(grupoAtual.getUsuarios())) {
					for(Usuario usuario : grupoAtual.getUsuarios()) {
						if(!gc.getUsuarios().contains(usuario)) {
							Usuario usr = daoUsuario.obterUsuarioPorId(usuario.getId());
							if(CollectionUtils.isNotEmpty(usr.getGrupos())) {
								if(Util.isNotNull(grupoCotador)) {
									if(usr.getGrupos().contains(grupoCotador)) {
										usr.getGrupos().remove(grupoCotador);
									}
								}
							}
						}
					}
				}
				
				for(Usuario usr : gc.getUsuarios()) {
					Usuario u = daoUsuario.obterUsuarioPorId(usr.getId());
					if(Util.isNotNull(grupoCotador)) {
						if(!u.getGrupos().contains(grupoCotador)) {
							u.getGrupos().add(grupoCotador);
							daoUsuario.alterar(u);
						}
					}
				}
				
				daoGrupoCotacao.alterar(gc);
			} else {
				GrupoCotacao grupoCotacao = new GrupoCotacao();
				grupoCotacao.setNome(gc.getNome());
				grupoCotacao.setSituacao(Boolean.TRUE);
				grupoCotacao.setUsuarios(new HashSet<Usuario>());
				List<Usuario> usuarios = new ArrayList<Usuario>();
				for(Usuario usr : gc.getUsuarios()) {
					Usuario u = daoUsuario.obterUsuarioPorId(usr.getId());
					
					if(Util.isNotNull(grupoCotador)) {
						if(!u.getGrupos().contains(grupoCotador)) {
							u.getGrupos().add(grupoCotador);
							daoUsuario.alterar(u);
						}
						
					}
					usuarios.add(u);
				}
				grupoCotacao.getUsuarios().addAll(usuarios);
				daoGrupoCotacao.incluir(grupoCotacao);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	/**
	 * Método responsavel por ativar grupo de cotação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 22/05/2018
	 * @param coligada : Coligada
	 * @throws ApplicationException
	 */
	public void ativar(GrupoCotacao gc) throws ApplicationException {
		try {
			gc.setSituacao(Boolean.TRUE);
			daoGrupoCotacao.alterar(gc);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "ativar" }, e);
		}
	}
	
	/**
	 * Método responsavel por inativar grupo de cotação
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 22/05/2018
	 * @param coligada : Coligada
	 * @throws ApplicationException
	 */
	public void inativar(GrupoCotacao gc) throws ApplicationException {
		try {
			gc.setSituacao(Boolean.FALSE);
			daoGrupoCotacao.alterar(gc);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "inativar" }, e);
		}
	}
}
