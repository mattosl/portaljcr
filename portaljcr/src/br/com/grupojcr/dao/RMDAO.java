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

import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.FornecedorRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;
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

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FornecedorRM> listarFornecedorPorNome(String nome) throws ApplicationException {
		/**
		 * SELECT CODCFO, NOMEFANTASIA, CGCCFO FROM FCFO
		 * WHERE CODCOLIGADA = 0
		 * AND NOMEFANTASIA LIKE ?
		 * ORDER BY NOMEFANTASIA ASC
		 * OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY
		 *
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<FornecedorRM> listaFornecedor = new ArrayList<FornecedorRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODCFO, NOMEFANTASIA, CGCCFO FROM FCFO ")
			.append("WHERE CODCOLIGADA = 0 ")
			.append("AND NOMEFANTASIA LIKE ? ")
			.append("ORDER BY NOMEFANTASIA ASC ")
			.append("OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY ");
			
			ps = conn.prepareStatement(sb.toString());
			
			ps.setString(1,  "%" + nome + "%");
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				FornecedorRM fornecedor = new FornecedorRM();
				fornecedor.setCodigoFornecedor(set.getString("CODCFO"));
				fornecedor.setFornecedor(set.getString("NOMEFANTASIA"));
				fornecedor.setCnpjCpf(set.getString("CGCCFO"));
				
				listaFornecedor.add(fornecedor);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarFornecedorPorNome" }, e);
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
		return listaFornecedor;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoRM> listarProdutosPorNome(Long idColigada, String nome) throws ApplicationException {
		/**
		 * SELECT IDPRD, CODIGOPRD, NOMEFANTASIA FROM TPRODUTO
		 * WHERE CODCOLPRD = ?
		 * AND INATIVO = 0
		 * AND LEN(CODIGOPRD) > 6
		 * AND NOMEFANTASIA LIKE ?
		 * ORDER BY CODIGOPRD ASC
		 * OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<ProdutoRM> listaProduto = new ArrayList<ProdutoRM>();

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT IDPRD, CODIGOPRD, NOMEFANTASIA FROM TPRODUTO ")
			.append("WHERE CODCOLPRD = ? ")
			.append("AND INATIVO = 0 ")
			.append("AND LEN(CODIGOPRD) > 6 ")
			.append("AND NOMEFANTASIA LIKE ? ")
			.append("ORDER BY CODIGOPRD ASC ")
			.append("OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY ");

			ps = conn.prepareStatement(sb.toString());

			ps.setLong(1, idColigada);
			ps.setString(2,  "%" + nome + "%");

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				ProdutoRM produto = new ProdutoRM();
				produto.setIdProduto(set.getInt("IDPRD"));
				produto.setCodigoProduto(set.getString("CODIGOPRD"));
				produto.setProduto(set.getString("NOMEFANTASIA"));
				
				listaProduto.add(produto);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarProdutosPorNome" }, e);
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
		return listaProduto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NaturezaOrcamentariaRM> listarNaturezaOrcamentaria() throws ApplicationException {
		/**
		 *  SELECT CODTBORCAMENTO, DESCRICAO FROM TTBORCAMENTO
		 *  WHERE INATIVO = 0
		 *  ORDER BY CODTBORCAMENTO ASC	
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<NaturezaOrcamentariaRM> listaNaturezaOrcamentaria = new ArrayList<NaturezaOrcamentariaRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODTBORCAMENTO, DESCRICAO FROM TTBORCAMENTO ")
			.append("WHERE INATIVO = 0 ")
			.append("ORDER BY CODTBORCAMENTO ASC ");
			
			ps = conn.prepareStatement(sb.toString());
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				NaturezaOrcamentariaRM natureza = new NaturezaOrcamentariaRM();
				natureza.setCodigoNaturezaOrcamentaria(set.getString("CODTBORCAMENTO"));
				natureza.setNaturezaOrcamentaria(set.getString("DESCRICAO"));
				
				listaNaturezaOrcamentaria.add(natureza);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listaNaturezaOrcamentaria" }, e);
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
		return listaNaturezaOrcamentaria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeRM> listarUnidade() throws ApplicationException {
		/**
		 * SELECT CODUND, DESCRICAO FROM TUND
		 * ORDER BY DESCRICAO ASC
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<UnidadeRM> listaUnidade = new ArrayList<UnidadeRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODUND, DESCRICAO FROM TUND ")
			.append("ORDER BY DESCRICAO ASC ");
			
			ps = conn.prepareStatement(sb.toString());
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				UnidadeRM unidade = new UnidadeRM();
				unidade.setCodigoUnidade(set.getString("CODUND"));
				unidade.setUnidade(set.getString("DESCRICAO"));
				
				listaUnidade.add(unidade);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarUnidade" }, e);
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
		return listaUnidade;
	}

}
