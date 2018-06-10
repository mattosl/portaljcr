package br.com.grupojcr.dao;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.dto.FiltroChamado;
import br.com.grupojcr.dto.FiltroRelatorioChamado;
import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.enumerator.SituacaoChamado;
import br.com.grupojcr.util.Util;
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
			sb.append("WHERE chamado.situacao != :situacaoResolvido AND chamado.situacao != :situacaoFechado  ");
			sb.append("ORDER BY chamado.dtAbertura DESC ");
			
			TypedQuery<Chamado> query = manager.createQuery(sb.toString(), Chamado.class);
			query.setParameter("situacaoResolvido", SituacaoChamado.RESOLVIDO);
			query.setParameter("situacaoFechado", SituacaoChamado.FECHADO);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarChamadosPendentes" }, e);
		}
	}
	
	public Chamado obterChamado(Long idChamado) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT chamado FROM Chamado chamado ");
			sb.append("LEFT JOIN FETCH chamado.usuarioSolicitante solicitante ");
			sb.append("LEFT JOIN FETCH chamado.usuarioResponsavel responsavel ");
			sb.append("WHERE chamado.id = :idChamado ");
			
			TypedQuery<Chamado> query = manager.createQuery(sb.toString(), Chamado.class);
			query.setParameter("idChamado", idChamado);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterChamado" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer obterQtdChamado(FiltroChamado filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT COUNT(chamado) FROM Chamado chamado ");
			sb.append("WHERE chamado.id != null ");
			
			if(Util.isNotNull(filtro.getSituacao())) {
				if(filtro.getSituacao().equals(1)) {
					sb.append("AND chamado.situacao != :situacao ");
				} else if(filtro.getSituacao().equals(2)) {
					sb.append("AND chamado.situacao = :situacao ");
				}
			}
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				sb.append("AND chamado.usuarioSolicitante = :usuarioSolicitante ");
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND chamado.dtAbertura BETWEEN :dtInicio AND :dtFinal ");
			}
			
			TypedQuery<Long> query = manager.createQuery(sb.toString(), Long.class);
			if(Util.isNotNull(filtro.getSituacao())) {
				query.setParameter("situacao", SituacaoChamado.FECHADO);
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				query.setParameter("dtInicio", filtro.getPeriodoInicial());
				query.setParameter("dtFinal", filtro.getPeriodoFinal());
			}
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				query.setParameter("usuarioSolicitante", filtro.getUsuarioLogado());
			}
			
			return query.getSingleResult().intValue();
		} catch (NoResultException nR) {
			return 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterQtdChamado" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer obterQtdChamadoRelatorio(FiltroRelatorioChamado filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT COUNT(chamado) FROM Chamado chamado ");
			sb.append("WHERE chamado.id != null ");
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND chamado.dtAbertura BETWEEN :dtInicio AND :dtFinal ");
			}
			
			if(Util.isNotNull(filtro.getSituacao()) && filtro.getSituacao().length != 0) {
				sb.append("AND chamado.situacao IN :situacoes ");
			}
			
			TypedQuery<Long> query = manager.createQuery(sb.toString(), Long.class);
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				query.setParameter("dtInicio", filtro.getPeriodoInicial());
				query.setParameter("dtFinal", filtro.getPeriodoFinal());
			}
			
			if(Util.isNotNull(filtro.getSituacao()) && filtro.getSituacao().length != 0) {
				query.setParameter("situacoes", Arrays.asList(filtro.getSituacao()));
			}
			
			return query.getSingleResult().intValue();
		} catch (NoResultException nR) {
			return 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterQtdChamado" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Chamado> listarChamadoPaginado(int first, int pageSize, FiltroChamado filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT chamado FROM Chamado chamado ");
			sb.append("LEFT JOIN FETCH chamado.usuarioSolicitante solicitante ");
			sb.append("LEFT JOIN FETCH chamado.usuarioResponsavel responsavel ");
			sb.append("WHERE chamado.id != null ");
			
			if(Util.isNotNull(filtro.getSituacao())) {
				if(filtro.getSituacao().equals(1)) {
					sb.append("AND chamado.situacao != :situacao ");
				} else if(filtro.getSituacao().equals(2)) {
					sb.append("AND chamado.situacao = :situacao ");
				}
			}
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				sb.append("AND chamado.usuarioSolicitante = :usuarioSolicitante ");
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND chamado.dtAbertura BETWEEN :dtInicio AND :dtFinal ");
			}
			
			sb.append("ORDER BY chamado.dtAbertura DESC ");
			
			TypedQuery<Chamado> query = manager.createQuery(sb.toString(), Chamado.class);
			if(Util.isNotNull(filtro.getSituacao())) {
				query.setParameter("situacao", SituacaoChamado.FECHADO);
			}
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				query.setParameter("dtInicio", filtro.getPeriodoInicial());
				query.setParameter("dtFinal", filtro.getPeriodoFinal());
			}
			
			if(Util.isNotNull(filtro.getUsuarioLogado())) {
				query.setParameter("usuarioSolicitante", filtro.getUsuarioLogado());
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
			throw new ApplicationException("message.default.erro", new String[] { "listarChamadoPaginado" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Chamado> listarChamadoPaginadoRelatorio(int first, int pageSize, FiltroRelatorioChamado filtro) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT chamado FROM Chamado chamado ");
			sb.append("LEFT JOIN FETCH chamado.usuarioSolicitante solicitante ");
			sb.append("LEFT JOIN FETCH chamado.usuarioResponsavel responsavel ");
			sb.append("WHERE chamado.id != null ");
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND chamado.dtAbertura BETWEEN :dtInicio AND :dtFinal ");
			}
			
			if(Util.isNotNull(filtro.getSituacao()) && filtro.getSituacao().length != 0) {
				sb.append("AND chamado.situacao IN :situacoes ");
			}
			
			sb.append("ORDER BY chamado.dtAbertura DESC ");
			
			TypedQuery<Chamado> query = manager.createQuery(sb.toString(), Chamado.class);
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				query.setParameter("dtInicio", filtro.getPeriodoInicial());
				query.setParameter("dtFinal", filtro.getPeriodoFinal());
			}
			
			if(Util.isNotNull(filtro.getSituacao()) && filtro.getSituacao().length != 0) {
				query.setParameter("situacoes", Arrays.asList(filtro.getSituacao()));
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
			throw new ApplicationException("message.default.erro", new String[] { "listarChamadoPaginado" }, e);
		}
	}
}
