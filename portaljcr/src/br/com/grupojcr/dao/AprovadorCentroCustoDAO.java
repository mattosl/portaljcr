package br.com.grupojcr.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.AprovadorCentroCusto;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class AprovadorCentroCustoDAO extends GenericDAO<AprovadorCentroCustoDAO> {
	
	private static Logger log = Logger.getLogger(AprovadorCentroCustoDAO.class);
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AprovadorCentroCusto obterAprovadorCentroCusto(Long idColigada, String codigoCentroCusto, Integer nivel) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT aprovador FROM AprovadorCentroCusto aprovador ");
			sb.append("WHERE aprovador.codigoCentroCusto like :codigoCentroCusto ");
			sb.append("AND aprovador.nivel = :nivel ");
			sb.append("AND aprovador.codigoColigada = :idColigada ");
			
			TypedQuery<AprovadorCentroCusto> query = manager.createQuery(sb.toString(), AprovadorCentroCusto.class);
			query.setParameter("codigoCentroCusto", codigoCentroCusto);
			query.setParameter("nivel", nivel);
			query.setParameter("idColigada", idColigada.intValue());
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterAprovadorCentroCusto" }, e);
		}
	}
}
