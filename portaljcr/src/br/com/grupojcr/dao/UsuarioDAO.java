package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.dto.FiltroUsuario;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class UsuarioDAO extends GenericDAO<Usuario> {
	
	private static Logger log = Logger.getLogger(UsuarioDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Usuario obterUsuario(String usuario) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT usuario FROM Usuario usuario ");
			sb.append("LEFT JOIN FETCH usuario.grupos ");
			sb.append("LEFT JOIN FETCH usuario.coligadas ");
			sb.append("WHERE usuario.usuario like :usuario ");
			
			TypedQuery<Usuario> query = manager.createQuery(sb.toString(), Usuario.class);
			query.setParameter("usuario", usuario);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterUsuario" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Usuario obterAdministrador() throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT usuario FROM Usuario usuario ");
			sb.append("WHERE usuario.usuario like 'admin' ");
			
			TypedQuery<Usuario> query = manager.createQuery(sb.toString(), Usuario.class);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterAdministrador" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer obterQtdUsuario(FiltroUsuario filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT usuario FROM Usuario usuario ");
			sb.append("WHERE usuario.nome != null ");
			
			if(Util.isNotNull(filtro.getNome())) {
				sb.append("AND usuario.nome like :nome ");
			}
			
			if(Util.isNotNull(filtro.getUsuario())) {
				sb.append("AND usuario.usuario = :usuario ");
			}
			
			if(Util.isNotNull(filtro.getSituacao())) {
				sb.append("AND usuario.situacao = :situacao ");
			}
			
			TypedQuery<Usuario> query = manager.createQuery(sb.toString(), Usuario.class);
			
			if(Util.isNotNull(filtro.getNome())) {
				query.setParameter("nome", "%" + filtro.getNome().trim() + "%");
			}
			
			if(Util.isNotNull(filtro.getUsuario())) {
				query.setParameter("usuario", filtro.getUsuario());
			}
			
			if(Util.isNotNull(filtro.getSituacao())) {
				query.setParameter("situacao", filtro.getSituacao());
			}
			
			return query.getResultList().size();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterQtdUsuario" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Usuario> listarUsuarioPaginado(int first, int pageSize, FiltroUsuario filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT usuario FROM Usuario usuario ");
			sb.append("LEFT JOIN FETCH usuario.grupos ");
			sb.append("LEFT JOIN FETCH usuario.coligadas ");
			sb.append("WHERE usuario.nome != null ");
			
			if(Util.isNotNull(filtro.getNome())) {
				sb.append("AND usuario.nome like :nome ");
			}
			
			if(Util.isNotNull(filtro.getUsuario())) {
				sb.append("AND usuario.usuario = :usuario ");
			}
			
			if(Util.isNotNull(filtro.getSituacao())) {
				sb.append("AND usuario.situacao = :situacao ");
			}
			
			TypedQuery<Usuario> query = manager.createQuery(sb.toString(), Usuario.class);
			
			if(Util.isNotNull(filtro.getNome())) {
				query.setParameter("nome", "%" + filtro.getNome().trim() + "%");
			}
			
			if(Util.isNotNull(filtro.getUsuario())) {
				query.setParameter("usuario", filtro.getUsuario());
			}
			
			if(Util.isNotNull(filtro.getSituacao())) {
				query.setParameter("situacao", filtro.getSituacao());
			}
			
			if(Util.isNotNull(first) && Util.isNotNull(pageSize)){
				query.setFirstResult(first);
				query.setMaxResults(pageSize);
			}
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "listarUsuarioPaginado" }, e);
		}
	}
	

}
