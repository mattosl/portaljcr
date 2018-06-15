package br.com.grupojcr.dao;

import java.util.Arrays;
import java.util.Calendar;
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
import br.com.grupojcr.util.TreatString;
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
			
			if(Util.isNotNull(filtro.getPrioridadeChamado())) {
				sb.append("AND chamado.prioridade = :prioridade ");
			}
			
			if(Util.isNotNull(filtro.getCausaChamado())) {
				sb.append("AND chamado.causa = :causa ");
			}
			
			if(TreatString.isNotBlank(filtro.getLocalizacao())) {
				sb.append("AND chamado.localizacao like :localizacao ");
			}
			
			if(Util.isNotNull(filtro.getCategoria())) {
				sb.append("AND chamado.categoria like :categoria ");
			}
			
			if(Util.isNotNull(filtro.getSubCategoria())) {
				sb.append("AND chamado.subcategoria like :subcategoria ");
			}
			
			TypedQuery<Long> query = manager.createQuery(sb.toString(), Long.class);
			
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
			
			if(Util.isNotNull(filtro.getSituacao()) && filtro.getSituacao().length != 0) {
				query.setParameter("situacoes", Arrays.asList(filtro.getSituacao()));
			}
			
			if(Util.isNotNull(filtro.getPrioridadeChamado())) {
				query.setParameter("prioridade", filtro.getPrioridadeChamado());
			}
			
			if(Util.isNotNull(filtro.getCausaChamado())) {
				query.setParameter("causa", filtro.getCausaChamado());
			}
			
			if(TreatString.isNotBlank(filtro.getLocalizacao())) {
				query.setParameter("localizacao", filtro.getLocalizacao());
			}
			
			if(Util.isNotNull(filtro.getCategoria())) {
				query.setParameter("categoria", filtro.getCategoria().getNome());
			}
			
			if(Util.isNotNull(filtro.getSubCategoria())) {
				query.setParameter("subcategoria", filtro.getSubCategoria().getNome());
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
			
			if(Util.isNotNull(filtro.getPrioridadeChamado())) {
				sb.append("AND chamado.prioridade = :prioridade ");
			}
			
			if(Util.isNotNull(filtro.getCausaChamado())) {
				sb.append("AND chamado.causa = :causa ");
			}
			
			if(TreatString.isNotBlank(filtro.getLocalizacao())) {
				sb.append("AND chamado.localizacao like :localizacao ");
			}
			
			if(Util.isNotNull(filtro.getCategoria())) {
				sb.append("AND chamado.categoria like :categoria ");
			}
			
			if(Util.isNotNull(filtro.getSubCategoria())) {
				sb.append("AND chamado.subcategoria like :subcategoria ");
			}
			
			sb.append("ORDER BY chamado.dtAbertura DESC ");
			
			TypedQuery<Chamado> query = manager.createQuery(sb.toString(), Chamado.class);
			
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
			
			if(Util.isNotNull(filtro.getSituacao()) && filtro.getSituacao().length != 0) {
				query.setParameter("situacoes", Arrays.asList(filtro.getSituacao()));
			}
			
			if(Util.isNotNull(filtro.getPrioridadeChamado())) {
				query.setParameter("prioridade", filtro.getPrioridadeChamado());
			}
			
			if(Util.isNotNull(filtro.getCausaChamado())) {
				query.setParameter("causa", filtro.getCausaChamado());
			}
			
			if(TreatString.isNotBlank(filtro.getLocalizacao())) {
				query.setParameter("localizacao", filtro.getLocalizacao());
			}
			
			if(Util.isNotNull(filtro.getCategoria())) {
				query.setParameter("categoria", filtro.getCategoria().getNome());
			}
			
			if(Util.isNotNull(filtro.getSubCategoria())) {
				query.setParameter("subcategoria", filtro.getSubCategoria().getNome());
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
	public Long obterQtdPorTipo(FiltroRelatorioChamado filtro, SituacaoChamado situacao) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT COUNT(chamado) FROM Chamado chamado ");
			sb.append("WHERE chamado.id != null ");
			
			if(Util.isNotNull(filtro.getPeriodoInicial()) && Util.isNotNull(filtro.getPeriodoFinal())) {
				sb.append("AND chamado.dtAbertura BETWEEN :dtInicio AND :dtFinal ");
			}
			
			if(Util.isNotNull(situacao)) {
				sb.append("AND chamado.situacao = :situacao ");
			}
			
			TypedQuery<Long> query = manager.createQuery(sb.toString(), Long.class);
			
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
			
			if(Util.isNotNull(situacao)) {
				query.setParameter("situacao", situacao);
			}
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterQtdPorTipo" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Long obterQtdPorTipo(SituacaoChamado situacao) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT COUNT(chamado) FROM Chamado chamado ");
			sb.append("WHERE chamado.id != null ");
			
			if(Util.isNotNull(situacao)) {
				sb.append("AND chamado.situacao = :situacao ");
			}
			
			TypedQuery<Long> query = manager.createQuery(sb.toString(), Long.class);
			
			if(Util.isNotNull(situacao)) {
				query.setParameter("situacao", situacao);
			}
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterQtdPorTipo" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Chamado> listarChamadosParaFechar() throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT chamado FROM Chamado chamado ");
			sb.append("WHERE chamado.situacao = :situacaoResolvido ");
			sb.append("AND chamado.dtResolucao < :dtAtual ");
			
			TypedQuery<Chamado> query = manager.createQuery(sb.toString(), Chamado.class);
			query.setParameter("situacaoResolvido", SituacaoChamado.RESOLVIDO);
			Calendar calendario = Calendar.getInstance();
			calendario.add(Calendar.DAY_OF_MONTH, -1);
			query.setParameter("dtAtual", calendario.getTime());
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listarChamadosParaFechar" }, e);
		}
	}
}
