package br.com.grupojcr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.grupojcr.dto.CentroCustoRM;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class RMDAO {
	
	protected static Logger LOG = Logger.getLogger(RMDAO.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@PersistenceContext(unitName = "corporeRM")
	protected EntityManager manager;
	
	@Resource(mappedName="java:jboss/datasources/corporeRMDS")
	protected DataSource datasource;

	/**
	 * Método responsável por testar conexão
	 * 
	 * @author Leonan Yglecias Mattos - <mattosl@grupojcr.com.br>
	 * @since 16/04/2018
	 * @return Boolean
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean testarConexao() throws ApplicationException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT SYSDATETIME();");
			ps = conn.prepareStatement(sb.toString());

			ResultSet set = ps.executeQuery();

			if (set.next()) {
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			LOG.error("Erro ao testar conexão");
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "testarConexao" }, e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					LOG.error(e.getMessage(), e);
				} finally {
					ps = null;
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error(e.getMessage(), e);
				} finally {
					conn = null;
				}
			}
		}
		return Boolean.FALSE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CentroCustoRM> listaCentroCustoPorColigada(Long idColigada) throws ApplicationException {
		/**
		 *  SELECT CODCCUSTO, NOME FROM GCCUSTO
		 *	WHERE CODCOLIGADA = ?
		 *	AND ATIVO = 'T'
		 *	AND PERMITELANC = 'T';
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<CentroCustoRM> listaCentroCusto = new ArrayList<CentroCustoRM>();

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODCCUSTO, NOME FROM GCCUSTO ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND ATIVO = 'T' ")
			.append("AND PERMITELANC = 'T' ")
			.append("ORDER BY CODCCUSTO ASC ");

			ps = conn.prepareStatement(sb.toString());

			ps.setLong(1, idColigada);

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				CentroCustoRM ccusto = new CentroCustoRM();
				ccusto.setCodigoCentroCusto(set.getString("CODCCUSTO"));
				ccusto.setCentroCusto(set.getString("NOME"));
				
				listaCentroCusto.add(ccusto);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listaCentroCustoPorColigada" }, e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					LOG.error(e.getMessage(), e);
				} finally {
					ps = null;
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error(e.getMessage(), e);
				} finally {
					conn = null;
				}
			}
		}
		return listaCentroCusto;
	}

}
