package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.AnexoChamado;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class AnexoChamadoDAO extends GenericDAO<AnexoChamado> {
	
	private static Logger log = Logger.getLogger(AnexoChamadoDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AnexoChamado> listarAnexoPorChamado(Long idChamado) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT anexo FROM AnexoChamado anexo ");
			sb.append("WHERE anexo.chamado.id = :idChamado ");
			sb.append("ORDER BY anexo.dtInclusao DESC ");
			
			TypedQuery<AnexoChamado> query = manager.createQuery(sb.toString(), AnexoChamado.class);
			query.setParameter("idChamado", idChamado);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarAnexoPorChamado" }, e);
		}
	}

}
