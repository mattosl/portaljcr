package br.com.grupojcr.dao;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class SolicitacaoCompraDAO extends GenericDAO<SolicitacaoCompra> {
	
	private static Logger log = Logger.getLogger(SolicitacaoCompraDAO.class);
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer obterQtdSolicitacaoCompra(FiltroSolicitacaoCompra filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT COUNT(solicitacao) FROM SolicitacaoCompra solicitacao ");
			sb.append("LEFT JOIN solicitacao.grupoCotacao grupoCotacao ");
			sb.append("LEFT JOIN grupoCotacao.usuarios usuarios ");
			sb.append("WHERE solicitacao.id != null ");
			
			if(Util.isNotNull(filtro.getSituacao())) {
				sb.append("AND solicitacao.situacao = :situacao ");
			}
			
			if(Util.isNotNull(filtro.getColigada())) {
				sb.append("AND solicitacao.coligada = :coligada ");
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND solicitacao.dtSolicitacao BETWEEN :dtInicio AND :dtFinal ");
			}
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				sb.append("AND solicitacao.usuarioSolicitante = :usuarioSolicitante ");
			}
			
			if(Util.isNotNull(filtro.getUsuarioCotacao())) {
				sb.append("AND (solicitacao.usuarioCotacao = :usuarioCotacao ");
				sb.append("OR usuarios.id = :usuarioCotacao) ");
			}
			
			if(Util.isNotNull(filtro.getNumeroSolicitacao())) {
				sb.append("AND solicitacao.id = :numeroSolicitacao ");
			}
			
			if(Util.isNotNull(filtro.getSituacaoIgnorar())) {
				sb.append("AND solicitacao.situacao NOT IN :situacaoIgnorar ");
			}
			
			TypedQuery<Long> query = manager.createQuery(sb.toString(), Long.class);
			
			if(Util.isNotNull(filtro.getSituacao())) {
				query.setParameter("situacao", filtro.getSituacao());
			}
			
			if(Util.isNotNull(filtro.getColigada())) {
				query.setParameter("coligada", filtro.getColigada());
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
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				query.setParameter("usuarioSolicitante", filtro.getUsuarioLogado());
			}
			
			if(Util.isNotNull(filtro.getUsuarioCotacao())) {
				query.setParameter("usuarioCotacao", filtro.getUsuarioCotacao());
			}
			
			if(Util.isNotNull(filtro.getSituacaoIgnorar())) {
				query.setParameter("situacaoIgnorar", Arrays.asList(filtro.getSituacaoIgnorar()));
			}
			
			if(Util.isNotNull(filtro.getNumeroSolicitacao())) {
				query.setParameter("numeroSolicitacao", filtro.getNumeroSolicitacao());
			}
			
			return query.getSingleResult().intValue();
		} catch (NoResultException nR) {
			return 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterQtdSolicitacaoCompra" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<SolicitacaoCompra> listarSolicitacaoCompraPaginado(int first, int pageSize, FiltroSolicitacaoCompra filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT solicitacao FROM SolicitacaoCompra solicitacao ");
			sb.append("LEFT JOIN FETCH solicitacao.coligada coligada ");
			sb.append("LEFT JOIN FETCH solicitacao.grupoCotacao grupoCotacao ");
			sb.append("LEFT JOIN FETCH grupoCotacao.usuarios usuarios ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioSolicitante usuarioSolicitante ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioCotacao usuarioCotacao ");
			sb.append("WHERE solicitacao.id != null ");
			
			if(Util.isNotNull(filtro.getSituacao())) {
				sb.append("AND solicitacao.situacao = :situacao ");
			}
			
			if(Util.isNotNull(filtro.getColigada())) {
				sb.append("AND coligada = :coligada ");
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND solicitacao.dtSolicitacao BETWEEN :dtInicio AND :dtFinal ");
			}
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				sb.append("AND usuarioSolicitante = :usuarioSolicitante ");
			}
			
			if(Util.isNotNull(filtro.getUsuarioCotacao())) {
				sb.append("AND (solicitacao.usuarioCotacao = :usuarioCotacao ");
				sb.append("OR usuarios.id = :usuarioCotacao) ");
			}
			
			if(Util.isNotNull(filtro.getSituacaoIgnorar())) {
				sb.append("AND solicitacao.situacao NOT IN :situacaoIgnorar ");
			}
			
			if(Util.isNotNull(filtro.getNumeroSolicitacao())) {
				sb.append("AND solicitacao.id = :numeroSolicitacao ");
			}
			
			sb.append("ORDER BY solicitacao.dtSolicitacao DESC ");
			
			TypedQuery<SolicitacaoCompra> query = manager.createQuery(sb.toString(), SolicitacaoCompra.class);
			
			if(Util.isNotNull(filtro.getSituacao())) {
				query.setParameter("situacao", filtro.getSituacao());
			}
			
			if(Util.isNotNull(filtro.getColigada())) {
				query.setParameter("coligada", filtro.getColigada());
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
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				query.setParameter("usuarioSolicitante", filtro.getUsuarioLogado());
			}
			
			if(Util.isNotNull(filtro.getUsuarioCotacao())) {
				query.setParameter("usuarioCotacao", filtro.getUsuarioCotacao());
			}
			
			if(Util.isNotNull(filtro.getSituacaoIgnorar())) {
				query.setParameter("situacaoIgnorar", Arrays.asList(filtro.getSituacaoIgnorar()));
			}
			
			if(Util.isNotNull(filtro.getNumeroSolicitacao())) {
				query.setParameter("numeroSolicitacao", filtro.getNumeroSolicitacao());
			}
			
			if(Util.isNotNull(first) && Util.isNotNull(pageSize)){
				query.setFirstResult(first);
				query.setMaxResults(pageSize);
			}
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "listarSolicitacaoCompraPaginado" }, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<SolicitacaoCompra> listarSolicitacaoCompraPendente(FiltroSolicitacaoCompra filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT DISTINCT solicitacao FROM SolicitacaoCompra solicitacao ");
			sb.append("LEFT JOIN FETCH solicitacao.coligada coligada ");
			sb.append("LEFT JOIN FETCH solicitacao.grupoCotacao grupoCotacao ");
			sb.append("LEFT JOIN FETCH grupoCotacao.usuarios usuarios ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioSolicitante usuarioSolicitante ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioCotacao usuarioCotacao ");
			sb.append("WHERE solicitacao.id != null ");
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				sb.append("AND (usuarioCotacao.id = :idUsuarioLogado ");
				sb.append("OR usuarios.id = :idUsuarioLogado) ");
			}
			
			if(Util.isNotNull(filtro.getSituacao())) {
				sb.append("AND solicitacao.situacao = :situacao ");
			}
			
			if(CollectionUtils.isNotEmpty(filtro.getColigadasUsuario())) {
				sb.append("AND solicitacao.coligada IN :coligadasUsuario ");
			}
			
			sb.append("ORDER BY solicitacao.dtSolicitacao DESC ");
			
			TypedQuery<SolicitacaoCompra> query = manager.createQuery(sb.toString(), SolicitacaoCompra.class);
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				query.setParameter("idUsuarioLogado", filtro.getUsuarioLogado().getId());
			}
			
			if(Util.isNotNull(filtro.getSituacao())) {
				query.setParameter("situacao", filtro.getSituacao());
			}
			
			if(CollectionUtils.isNotEmpty(filtro.getColigadasUsuario())) {
				query.setParameter("coligadasUsuario", filtro.getColigadasUsuario());
			}
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "listarSolicitacaoCompraPendente" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SolicitacaoCompra obterSolicitacao(Long idSolicitacaoCompra) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT DISTINCT solicitacao FROM SolicitacaoCompra solicitacao ");
			sb.append("LEFT JOIN FETCH solicitacao.coligada coligada ");
			sb.append("LEFT JOIN FETCH solicitacao.grupoCotacao grupoCotacao ");
			sb.append("LEFT JOIN FETCH grupoCotacao.usuarios usuarios ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioSolicitante usuarioSolicitante ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioCotacao usuarioCotacao ");
			sb.append("WHERE solicitacao.id = :idSolicitacaoCompra ");
			
			TypedQuery<SolicitacaoCompra> query = manager.createQuery(sb.toString(), SolicitacaoCompra.class);
			query.setParameter("idSolicitacaoCompra", idSolicitacaoCompra);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterSolicitacao" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SolicitacaoCompra obterSolicitacaoPorIdFluig(Integer idFluig) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT DISTINCT solicitacao FROM SolicitacaoCompra solicitacao ");
			sb.append("LEFT JOIN FETCH solicitacao.coligada coligada ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioSolicitante usuarioSolicitante ");
			sb.append("WHERE solicitacao.identificadorFluig = :idFluig ");
			
			TypedQuery<SolicitacaoCompra> query = manager.createQuery(sb.toString(), SolicitacaoCompra.class);
			query.setParameter("idFluig", idFluig.longValue());
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterSolicitacao" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SolicitacaoCompra obterSolicitacaoPorColigadaMovimento(Long idColigada, Long idMovimento) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT DISTINCT solicitacao FROM SolicitacaoCompra solicitacao ");
			sb.append("LEFT JOIN FETCH solicitacao.coligada coligada ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioSolicitante usuarioSolicitante ");
			sb.append("LEFT JOIN FETCH solicitacao.usuarioCotacao usuarioCotacao ");
			sb.append("LEFT JOIN FETCH solicitacao.ordensCompra ordemCompra ");
			sb.append("WHERE coligada.id = :idColigada ");
			sb.append("AND ordemCompra.identificadorRM = :idMovimento ");
			
			TypedQuery<SolicitacaoCompra> query = manager.createQuery(sb.toString(), SolicitacaoCompra.class);
			query.setParameter("idColigada", idColigada);
			query.setParameter("idMovimento", idMovimento.toString());
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterSolicitacaoPorColigadaMovimento" }, e);
		}
	}
	
}
