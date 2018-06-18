package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class SolicitacaoCompraItemDAO extends GenericDAO<SolicitacaoCompraItem> {
	
	private static Logger log = Logger.getLogger(SolicitacaoCompraItemDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<SolicitacaoCompraItem> listarItemPorSolicitacao(Long idSolicitacao) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT item FROM SolicitacaoCompraItem item ");
			sb.append("WHERE item.solicitacaoCompra.id = :idSolicitacao ");
			sb.append("ORDER BY item.dtInclusao DESC ");
			
			TypedQuery<SolicitacaoCompraItem> query = manager.createQuery(sb.toString(), SolicitacaoCompraItem.class);
			query.setParameter("idSolicitacao", idSolicitacao);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarItemPorSolicitacao" }, e);
		}
	}
	
}
