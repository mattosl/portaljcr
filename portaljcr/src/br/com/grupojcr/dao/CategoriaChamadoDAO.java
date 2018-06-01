package br.com.grupojcr.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.grupojcr.entity.CategoriaChamado;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class CategoriaChamadoDAO extends GenericDAO<CategoriaChamado> {
	
	private static Logger log = Logger.getLogger(CategoriaChamadoDAO.class);
	private final static String KEY_ERRO = "ERRO:";

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<CategoriaChamado> listarCategorias() throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT categoriaChamado FROM CategoriaChamado categoriaChamado ");
			sb.append("LEFT JOIN FETCH categoriaChamado.subCategorias ");
			sb.append("ORDER BY categoriaChamado.nome ASC ");
			
			TypedQuery<CategoriaChamado> query = manager.createQuery(sb.toString(), CategoriaChamado.class);
			
			return query.getResultList();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "listar" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CategoriaChamado obterCategoriaChamado(Long idCategoria) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT categoriaChamado FROM CategoriaChamado categoriaChamado ");
			sb.append("LEFT JOIN FETCH categoriaChamado.subCategorias ");
			sb.append("WHERE categoriaChamado.id = :idCategoria ");
			
			TypedQuery<CategoriaChamado> query = manager.createQuery(sb.toString(), CategoriaChamado.class);
			query.setParameter("idCategoria", idCategoria);
			
			return query.getSingleResult();
		} catch (NoResultException nR) {
			return null;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "obterCategoriaChamado" }, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Boolean verificarCategoriaExiste(String nome, Long idCategoria) throws ApplicationException {
		try{
			StringBuilder sb = new StringBuilder("SELECT DISTINCT cc FROM CategoriaChamado cc ");
			sb.append("WHERE upper(cc.nome) like :nome ");
			
			if(Util.isNotNull(idCategoria)) {
				sb.append("AND cc.id != :idCategoria ");
			}
			
			TypedQuery<CategoriaChamado> query = manager.createQuery(sb.toString(), CategoriaChamado.class);
			query.setParameter("nome", nome.toUpperCase().trim());
			
			if(Util.isNotNull(idCategoria)) {
				query.setParameter("idCategoria", idCategoria);
			}
			
			if(query.getResultList().size() > 0) {
				return Boolean.TRUE;
			}
		} catch (NoResultException nR) {
			return Boolean.FALSE;
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new ApplicationException("message.default.erro", new String[] { "verificarCategoriaExiste" }, e);
		}
		return Boolean.FALSE;
	}
}
