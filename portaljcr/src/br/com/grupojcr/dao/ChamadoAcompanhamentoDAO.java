package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.ChamadoAcompanhamento;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class ChamadoAcompanhamentoDAO extends GenericDAO<ChamadoAcompanhamento> {
	
	private static Logger log = Logger.getLogger(ChamadoAcompanhamentoDAO.class);
	private final static String KEY_ERRO = "ERRO:";

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ChamadoAcompanhamento> listarAcompanhamentoPorChamado(Long idChamado) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT ca FROM ChamadoAcompanhamento ca ");
			sb.append("LEFT JOIN FETCH ca.usuario usuario ");
			sb.append("WHERE ca.chamado.id = :idChamado ");
			sb.append("ORDER BY ca.dtAcompanhamento DESC ");
			
			TypedQuery<ChamadoAcompanhamento> query = manager.createQuery(sb.toString(), ChamadoAcompanhamento.class);
			query.setParameter("idChamado", idChamado);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarAcompanhamentoPorChamado" }, e);
		}
	}
}
