package br.com.grupojcr.dao;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.AjustePonto;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class AjustePontoDAO extends GenericDAO<AjustePonto> {
	
	private static Logger log = Logger.getLogger(AjustePontoDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AjustePonto obterAjustePonto(Long idUsuarioAjuste, Date periodoInicial, Date periodoFinal) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT ajustePonto FROM AjustePonto ajustePonto ");
			sb.append("LEFT JOIN ajustePonto.usuario usuario ");
			sb.append("LEFT JOIN ajustePonto.batidas batida ");
			sb.append("WHERE ajustePonto.usuario.id = :idUsuario ");
			sb.append("AND ajustePonto.dtPeriodoInicial = :dtPeriodoInicial ");
			sb.append("AND ajustePonto.dtPeriodoFinal = :dtPeriodoFinal ");
			
			TypedQuery<AjustePonto> query = manager.createQuery(sb.toString(), AjustePonto.class);
			
			query.setParameter("idUsuario", idUsuarioAjuste);
			query.setParameter("dtPeriodoInicial", periodoInicial);
			query.setParameter("dtPeriodoFinal", periodoFinal);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterAjustePonto" }, e);
		}
	}

}
