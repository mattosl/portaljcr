package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.enumerator.SituacaoChamado;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class ChamadoDAO extends GenericDAO<Chamado> {
	
	private static Logger log = Logger.getLogger(ChamadoDAO.class);
	private final static String KEY_ERRO = "ERRO:";

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Chamado> listarChamadosPendentes() throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT chamado FROM Chamado chamado ");
			sb.append("LEFT JOIN FETCH chamado.usuarioSolicitante solicitante ");
			sb.append("LEFT JOIN FETCH chamado.usuarioResponsavel responsavel ");
			sb.append("WHERE chamado.situacao != :situacao ");
			sb.append("ORDER BY chamado.dtAbertura DESC ");
			
			TypedQuery<Chamado> query = manager.createQuery(sb.toString(), Chamado.class);
			query.setParameter("situacao", SituacaoChamado.RESOLVIDO);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarChamadosPendentes" }, e);
		}
	}
}
