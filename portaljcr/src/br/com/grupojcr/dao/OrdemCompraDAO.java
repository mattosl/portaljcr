package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.OrdemCompra;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class OrdemCompraDAO extends GenericDAO<OrdemCompra> {
	
	private static Logger log = Logger.getLogger(OrdemCompraDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<OrdemCompra> listarOrdemCompraPorSolicitacao(Long idSolicitacao) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT oc FROM OrdemCompra oc ");
			sb.append("LEFT JOIN FETCH oc.cotacao cotacao ");
			sb.append("WHERE oc.solicitacaoCompra.id = :idSolicitacao ");
			sb.append("ORDER BY oc.dtOrdemCompra DESC ");
			
			TypedQuery<OrdemCompra> query = manager.createQuery(sb.toString(), OrdemCompra.class);
			query.setParameter("idSolicitacao", idSolicitacao);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarOrdemCompraPorSolicitacao" }, e);
		}
	}
}
