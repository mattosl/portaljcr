package br.com.grupojcr.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class UsuarioDAO extends GenericDAO<Usuario> {
	
	private static Logger log = Logger.getLogger(UsuarioDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Usuario obterUsuario(String usuario) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT usuario FROM Usuario usuario ");
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

}
