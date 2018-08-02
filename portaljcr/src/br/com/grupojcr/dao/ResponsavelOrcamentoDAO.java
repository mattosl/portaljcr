package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.ResponsavelOrcamento;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class ResponsavelOrcamentoDAO extends GenericDAO<ResponsavelOrcamento> {
	
	private static Logger log = Logger.getLogger(ResponsavelOrcamentoDAO.class);
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Usuario> listarUsuarioResponsavel(String codigoCentroCusto, Long idColigada) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT ro.usuarioResponsavel FROM ResponsavelOrcamento ro ");
			sb.append("LEFT JOIN ro.usuarioResponsavel usuario ");
			sb.append("WHERE ro.id is not null ");
			
			if(TreatString.isNotBlank(codigoCentroCusto)) {
				sb.append("AND ro.codigoCentroCusto = :codigoCentroCusto ");
			}
			
			if(Util.isNotNull(idColigada)) {
				sb.append("AND ro.coligada.id = :idColigada ");
			}
			
			TypedQuery<Usuario> query = manager.createQuery(sb.toString(), Usuario.class);
			
			if(TreatString.isNotBlank(codigoCentroCusto)) {
				query.setParameter("codigoCentroCusto", codigoCentroCusto);
			}
			
			if(Util.isNotNull(idColigada)) {
				query.setParameter("idColigada", idColigada);
			}
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "listarUsuarioResponsavel" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Coligada> listarColigadaResponsavel(Usuario usuario) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT ro.coligada FROM ResponsavelOrcamento ro ");
			sb.append("LEFT JOIN ro.usuarioResponsavel responsavel ");
			sb.append("WHERE ro.id is not null ");
			
			if(Util.isNotNull(usuario)) {
				sb.append("AND responsavel.id = :idUsuario ");
			}
			
			TypedQuery<Coligada> query = manager.createQuery(sb.toString(), Coligada.class);
			
			if(Util.isNotNull(usuario)) {
				query.setParameter("idUsuario", usuario.getId());
			}
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "listarColigadaResponsavel" }, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ResponsavelOrcamento obterResponsavel(String codigoCentroCusto, Coligada coligada, Usuario usuario) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT ro FROM ResponsavelOrcamento ro ");
			sb.append("WHERE ro.usuarioResponsavel.id = :idUsuario ");
			sb.append("AND ro.coligada.id = :idColigada ");
			sb.append("AND ro.codigoCentroCusto = :codigoCentroCusto ");
			
			TypedQuery<ResponsavelOrcamento> query = manager.createQuery(sb.toString(), ResponsavelOrcamento.class);
			
			query.setParameter("idUsuario", usuario.getId());
			query.setParameter("idColigada", coligada.getId());
			query.setParameter("codigoCentroCusto", codigoCentroCusto);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "obterResponsavel" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ResponsavelOrcamento> listarResponsavelPorUsuario(Usuario usuario) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT ro FROM ResponsavelOrcamento ro ");
			sb.append("WHERE ro.usuarioResponsavel.id = :idUsuario ");
			
			TypedQuery<ResponsavelOrcamento> query = manager.createQuery(sb.toString(), ResponsavelOrcamento.class);
			
			query.setParameter("idUsuario", usuario.getId());
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "listarResponsavelPorUsuario" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ResponsavelOrcamento> listarResponsavelPorUsuarioColigada(Coligada coligada, Usuario usuario) throws ApplicationException {
		try {
			
			StringBuilder sb = new StringBuilder("SELECT ro FROM ResponsavelOrcamento ro ");
			sb.append("LEFT JOIN FETCH ro.usuarioResponsavel usuarioResponsavel ");
			sb.append("LEFT JOIN FETCH ro.coligada coligada ");
			sb.append("WHERE usuarioResponsavel.id = :idUsuario ");
			sb.append("AND coligada.id = :idColigada ");
			
			TypedQuery<ResponsavelOrcamento> query = manager.createQuery(sb.toString(), ResponsavelOrcamento.class);
			
			query.setParameter("idUsuario", usuario.getId());
			query.setParameter("idColigada", coligada.getId());
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException("message.default.erro", new String[] { "listarResponsavelPorUsuarioColigada" }, e);
		}
	}
	
}
