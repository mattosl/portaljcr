package br.com.grupojcr.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.BatidaPonto;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class BatidaPontoDAO extends GenericDAO<BatidaPonto> {
	
	private static Logger log = Logger.getLogger(BatidaPontoDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BatidaPonto obterBatida(Date dtBatida, Integer batida, Long idAjuste, Long idBatida) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT batida FROM BatidaPonto batida ");
			sb.append("WHERE batida.batida = :batida ");
			sb.append("AND batida.dtBatida = :dtBatida ");
			sb.append("AND batida.ajuste.id = :idAjuste ");
			
			if(Util.isNotNull(idBatida)) {
				sb.append("AND batida.id != :idBatida");
			}
			
			TypedQuery<BatidaPonto> query = manager.createQuery(sb.toString(), BatidaPonto.class);
			
			query.setParameter("batida", batida);
			query.setParameter("dtBatida", dtBatida);
			query.setParameter("idAjuste", idAjuste);
			
			if(Util.isNotNull(idBatida)) {
				query.setParameter("idBatida", idBatida);
			}
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterBatida" }, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<BatidaPonto> listarBatidaPorPeriodo(Date periodoInicial, Date periodoFinal, Long idUsuario) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT batida FROM BatidaPonto batida ");
			sb.append("WHERE batida.dtBatida BETWEEN :periodoInicial AND :periodoFinal ");
			sb.append("AND batida.ajuste.usuario.id = :idUsuario ");
			sb.append("ORDER BY dtBatida DESC ");
			
			TypedQuery<BatidaPonto> query = manager.createQuery(sb.toString(), BatidaPonto.class);
			
			query.setParameter("periodoInicial", periodoInicial);
			query.setParameter("periodoFinal", periodoFinal);
			query.setParameter("idUsuario", idUsuario);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarBatidaPorPeriodo" }, e);
		}
	}
	
}
