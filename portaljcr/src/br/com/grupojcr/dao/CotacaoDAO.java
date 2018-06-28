package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class CotacaoDAO extends GenericDAO<Cotacao> {
	
	private static Logger log = Logger.getLogger(CotacaoDAO.class);
	private final static String KEY_ERRO = "ERRO:";
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Cotacao> listarCotacoesPorSolicitacao(Long idSolicitacao) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT cotacao FROM Cotacao cotacao ");
			sb.append("LEFT JOIN FETCH cotacao.usuarioCotacao usuarioCotacao ");
			sb.append("LEFT JOIN FETCH cotacao.itens item ");
			sb.append("LEFT JOIN FETCH item.solicitacaoCompraItem itemSolicitacao ");
			sb.append("WHERE cotacao.solicitacaoCompra.id = :idSolicitacao ");
			sb.append("ORDER BY cotacao.dtCotacao DESC ");
			
			TypedQuery<Cotacao> query = manager.createQuery(sb.toString(), Cotacao.class);
			query.setParameter("idSolicitacao", idSolicitacao);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarCotacoesPorSolicitacao" }, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Cotacao obterCotacao(Long idCotacao) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT cotacao FROM Cotacao cotacao ");
			sb.append("LEFT JOIN FETCH cotacao.usuarioCotacao usuarioCotacao ");
			sb.append("LEFT JOIN FETCH cotacao.itens item ");
			sb.append("LEFT JOIN FETCH item.solicitacaoCompraItem itemSolicitacao ");
			sb.append("WHERE cotacao.id = :idCotacao ");
			
			TypedQuery<Cotacao> query = manager.createQuery(sb.toString(), Cotacao.class);
			query.setParameter("idCotacao", idCotacao);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterCotacao" }, e);
		}
	}
	
	
}
