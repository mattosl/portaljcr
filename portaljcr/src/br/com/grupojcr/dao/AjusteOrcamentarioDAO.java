package br.com.grupojcr.dao;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.dto.FiltroOrcamento;
import br.com.grupojcr.entity.AjusteOrcamentario;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class AjusteOrcamentarioDAO extends GenericDAO<AjusteOrcamentario> {
	
	private static Logger log = Logger.getLogger(AjusteOrcamentarioDAO.class);
	private final static String KEY_ERRO = "ERRO:";

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer obterQtdAjusteOrcamentario(FiltroOrcamento filtro) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT COUNT(ajuste) FROM AjusteOrcamentario ajuste ");
			sb.append("LEFT JOIN ajuste.coligada coligada ");
			sb.append("WHERE ajuste.id is not null ");
			
			if(Util.isNotNull(filtro.getColigada())) {
				sb.append("AND coligada.id = :idColigada ");
			}
			
			if(Util.isNotNull(filtro.getCentroCusto())) {
				sb.append("AND ajuste.codigoCentroCusto = :codigoCentroCusto ");
			}
			
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND ajuste.dtAjuste BETWEEN :dtInicio AND :dtFinal ");
			}
			
			TypedQuery<Long> query = manager.createQuery(sb.toString(), Long.class);
			
			if(Util.isNotNull(filtro.getColigada())) {
				query.setParameter("idColigada", filtro.getColigada().getId());
			}
			
			if(Util.isNotNull(filtro.getCentroCusto())) {
				query.setParameter("codigoCentroCusto", filtro.getCentroCusto().getCodigoCentroCusto());
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				Calendar inicio = Calendar.getInstance();
				inicio.setTime(filtro.getPeriodoInicial());
				inicio.set(Calendar.HOUR, 0);
				inicio.set(Calendar.MINUTE, 0);
				inicio.set(Calendar.SECOND, 0);
				query.setParameter("dtInicio", inicio.getTime());
				Calendar fim = Calendar.getInstance();
				fim.setTime(filtro.getPeriodoFinal());
				fim.set(Calendar.HOUR, 23);
				fim.set(Calendar.MINUTE, 59);
				fim.set(Calendar.SECOND, 59);
				query.setParameter("dtFinal", fim.getTime());
			}
			
			return query.getSingleResult().intValue();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterQtdAjusteOrcamentario" }, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AjusteOrcamentario> listarAjusteOrcamentarioPaginado(int first, int pageSize, FiltroOrcamento filtro) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT ajuste FROM AjusteOrcamentario ajuste ");
			sb.append("LEFT JOIN FETCH ajuste.coligada coligada ");
			sb.append("LEFT JOIN FETCH ajuste.usuarioAjuste usuario ");
			sb.append("WHERE ajuste.id is not null ");
			
			if(Util.isNotNull(filtro.getColigada())) {
				sb.append("AND coligada.id = :idColigada ");
			}
			
			if(Util.isNotNull(filtro.getCentroCusto())) {
				sb.append("AND ajuste.codigoCentroCusto = :codigoCentroCusto ");
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND ajuste.dtAjuste BETWEEN :dtInicio AND :dtFinal ");
			}
			
			sb.append("ORDER BY ajuste.dtAjuste DESC ");
			
			TypedQuery<AjusteOrcamentario> query = manager.createQuery(sb.toString(), AjusteOrcamentario.class);
			
			if(Util.isNotNull(filtro.getColigada())) {
				query.setParameter("idColigada", filtro.getColigada().getId());
			}
			
			if(Util.isNotNull(filtro.getCentroCusto())) {
				query.setParameter("codigoCentroCusto", filtro.getCentroCusto().getCodigoCentroCusto());
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				Calendar inicio = Calendar.getInstance();
				inicio.setTime(filtro.getPeriodoInicial());
				inicio.set(Calendar.HOUR, 0);
				inicio.set(Calendar.MINUTE, 0);
				inicio.set(Calendar.SECOND, 0);
				query.setParameter("dtInicio", inicio.getTime());
				Calendar fim = Calendar.getInstance();
				fim.setTime(filtro.getPeriodoFinal());
				fim.set(Calendar.HOUR, 23);
				fim.set(Calendar.MINUTE, 59);
				fim.set(Calendar.SECOND, 59);
				query.setParameter("dtFinal", fim.getTime());
			}
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarAjusteOrcamentarioPaginado" }, e);
		}
	}
	
	
}
