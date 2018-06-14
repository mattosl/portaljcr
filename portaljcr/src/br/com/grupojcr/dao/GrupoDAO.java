package br.com.grupojcr.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.Grupo;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class GrupoDAO extends GenericDAO<Grupo> {
	
	private static Logger log = Logger.getLogger(GrupoDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Grupo obterGrupo(String nomeGrupo) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT grupo FROM Grupo grupo ");
			sb.append("LEFT JOIN FETCH grupo.usuarios ");
			sb.append("WHERE grupo.nome like :nomeGrupo ");
			
			TypedQuery<Grupo> query = manager.createQuery(sb.toString(), Grupo.class);
			query.setParameter("nomeGrupo", nomeGrupo.trim());
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterGrupo" }, e);
		}
	}

}
