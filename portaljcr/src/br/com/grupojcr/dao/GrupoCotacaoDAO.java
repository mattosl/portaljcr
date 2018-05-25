package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class GrupoCotacaoDAO extends GenericDAO<GrupoCotacao> {
	
	private static Logger log = Logger.getLogger(GrupoCotacaoDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<GrupoCotacao> listarGrupoCotacao() throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT gc FROM GrupoCotacao gc ");
			sb.append("LEFT JOIN FETCH gc.usuarios ");
			sb.append("ORDER BY gc.nome ASC ");
			
			TypedQuery<GrupoCotacao> query = manager.createQuery(sb.toString(), GrupoCotacao.class);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarGrupoCotacao" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Boolean verificarGrupoCotacaoExiste(String nome) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT gc FROM GrupoCotacao gc ");
			sb.append("WHERE upper(gc.nome) like :nome ");
			
			TypedQuery<GrupoCotacao> query = manager.createQuery(sb.toString(), GrupoCotacao.class);
			query.setParameter("nome", nome.toUpperCase().trim());
			
			if(query.getResultList().size() > 0) {
				return Boolean.TRUE;
			}
		} catch (NoResultException nR) {
			return Boolean.FALSE;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "verificarGrupoCotacaoExiste" }, e);
		}
		return Boolean.FALSE;
	}

}
