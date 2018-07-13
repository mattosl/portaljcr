package br.com.grupojcr.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.ControleOrcamento;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class ControleOrcamentoDAO extends GenericDAO<ControleOrcamento> {
	
	private static Logger log = Logger.getLogger(ControleOrcamentoDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ControleOrcamento obterControleOrcamento(Long idColigada, String codigoCentroCusto) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT controle FROM ControleOrcamento controle ");
			sb.append("WHERE controle.coligada.id = :idColigada ");
			sb.append("AND controle.codigoCentroCusto = :codigoCentroCusto ");
			
			TypedQuery<ControleOrcamento> query = manager.createQuery(sb.toString(), ControleOrcamento.class);
			query.setParameter("idColigada", idColigada);
			query.setParameter("codigoCentroCusto", codigoCentroCusto);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterControleOrcamento" }, e);
		}
	}

}
