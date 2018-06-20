package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class CotacaoItemDAO extends GenericDAO<CotacaoItem> {
	
	private static Logger log = Logger.getLogger(CotacaoItemDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<CotacaoItem> listarItensCotacao(Long idCotacao) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT item FROM CotacaoItem item ");
			sb.append("LEFT JOIN FETCH item.solicitacaoCompraItem sci ");
			sb.append("WHERE item.cotacao.id = :idCotacao ");
			sb.append("ORDER BY item.id ASC ");
			
			TypedQuery<CotacaoItem> query = manager.createQuery(sb.toString(), CotacaoItem.class);
			query.setParameter("idCotacao", idCotacao);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarItensCotacao" }, e);
		}
	}
}
