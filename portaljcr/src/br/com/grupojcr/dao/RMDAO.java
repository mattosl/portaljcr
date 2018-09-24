package br.com.grupojcr.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.grupojcr.dto.AprovacaoContratoDTO;
import br.com.grupojcr.dto.AprovacaoOrdemCompraDTO;
import br.com.grupojcr.dto.AprovadorDTO;
import br.com.grupojcr.dto.CentroCustoDTO;
import br.com.grupojcr.dto.ChapaDTO;
import br.com.grupojcr.dto.ColigadaDTO;
import br.com.grupojcr.dto.HoleriteDTO;
import br.com.grupojcr.dto.HorasPontoDTO;
import br.com.grupojcr.dto.ItemDTO;
import br.com.grupojcr.dto.MovimentoDTO;
import br.com.grupojcr.dto.OrcamentoDTO;
import br.com.grupojcr.dto.PeriodoPontoDTO;
import br.com.grupojcr.dto.ZMDRMFLUIGDTO;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.enumerator.Mes;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.enumerator.PeriodoHolerite;
import br.com.grupojcr.rm.AprovadorRM;
import br.com.grupojcr.rm.BatidaRM;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.CondicaoPagamentoRM;
import br.com.grupojcr.rm.FeriadoRM;
import br.com.grupojcr.rm.FeriasRM;
import br.com.grupojcr.rm.FornecedorRM;
import br.com.grupojcr.rm.FuncionarioHoleriteRM;
import br.com.grupojcr.rm.FuncionarioRM;
import br.com.grupojcr.rm.HoleriteItensRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
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
	public MovimentoDTO obterMovimento(Integer codMovimento, Integer codColigada) {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT DISTINCT ").append("FILIAL.CODCOLIGADA AS CODIGO_EMPRESA, ")
					.append("FILIAL.NOME AS EMPRESA, ").append("USUARIO.NOME AS SOLICITANTE, ")
					.append("TMOV.CODUSUARIO AS USRSOLICITANTE, ").append("TMOV.IDMOV AS IDENTIFICADOR_RM, ")
					.append("TMOV.DATAEMISSAO AS DT_EMISSAO, ").append("FORNECEDOR.CODCFO AS CODIGO_FORNECEDOR, ")
					.append("FORNECEDOR.NOME AS FORNECEDOR, ")
					.append("COND_PAGAMENTO.CODCPG AS CODIGO_COND_PAGAMENTO, ")
					.append("COND_PAGAMENTO.NOME AS CONDICAO_PAGAMENTO, ")
					.append("TMOV.VALORBRUTOORIG AS VALOR_TOTAL, ").append("CCUSTO.CODCCUSTO AS CODIGO_CCUSTO, ")
					.append("CCUSTO.NOME AS CCUSTO, ")
					.append("CAST(HISTORICO.HISTORICOLONGO AS VARCHAR(8000)) AS OBSERVACAO, ")
					.append("TMOV.CODMOEVALORLIQUIDO AS MOEDA, ").append("TIPO_MOV.CODTMV AS CODIGO_MOVIMENTO, ")
					.append("TIPO_MOV.NOME AS TIPO_MOVIMENTO, ").append("TMOV.STATUS AS STATUS ").append("FROM ")
					.append("TMOV AS TMOV (NOLOCK) ")
					.append("JOIN GFILIAL  (NOLOCK) AS FILIAL ON (FILIAL.CODCOLIGADA = TMOV.CODCOLIGADA AND FILIAL.CODFILIAL = TMOV.CODFILIAL) ")
					.append("JOIN GCCUSTO (NOLOCK) AS CCUSTO ON (CCUSTO.CODCCUSTO = TMOV.CODCCUSTO) ")
					.append("JOIN FCFO (NOLOCK) AS FORNECEDOR ON (FORNECEDOR.CODCFO = TMOV.CODCFO) ")
					.append("JOIN GUSUARIO (NOLOCK) AS USUARIO ON (USUARIO.CODUSUARIO LIKE TMOV.CODUSUARIO) ")
					.append("JOIN TCPG (NOLOCK) AS COND_PAGAMENTO ON (COND_PAGAMENTO.CODCPG = TMOV.CODCPG AND COND_PAGAMENTO.CODCOLIGADA = TMOV.CODCOLIGADA) ")
					.append("JOIN TMOVHISTORICO (NOLOCK) AS HISTORICO ON (HISTORICO.CODCOLIGADA = TMOV.CODCOLIGADA AND HISTORICO.IDMOV = TMOV.IDMOV) ")
					.append("JOIN TTMV (NOLOCK) AS TIPO_MOV ON (TIPO_MOV.CODTMV = TMOV.CODTMV) ")
					.append("WHERE TMOV.IDMOV = ? ").append("AND TMOV.CODCOLIGADA = ?");
			ps = conn.prepareStatement(sb.toString());

			ps.setInt(1, codMovimento.intValue());
			ps.setInt(2, codColigada.intValue());

			ResultSet set = ps.executeQuery();

			if (set.next()) {
				MovimentoDTO dto = new MovimentoDTO();
				dto.setIdColigada(Integer.valueOf(set.getInt("CODIGO_EMPRESA")));
				dto.setNomeEmpresa(set.getString("EMPRESA"));
				dto.setSolicitante(set.getString("SOLICITANTE"));
				dto.setIdMov(Integer.valueOf(set.getInt("IDENTIFICADOR_RM")));
				dto.setDataEmissao(TreatDate.format("dd/MM/yyyy", set.getDate("DT_EMISSAO")));
				dto.setIdFornecedor(set.getString("CODIGO_FORNECEDOR"));
				dto.setFornecedor(set.getString("FORNECEDOR"));
				dto.setIdCondicaoPagamento(set.getString("CODIGO_COND_PAGAMENTO"));
				dto.setCondicaoPagamento(set.getString("CONDICAO_PAGAMENTO"));
				BigDecimal valor = set.getBigDecimal("VALOR_TOTAL");
				dto.setValorTotal(TreatNumber.formatMoney(valor).toString());
				dto.setIdCentroCusto(set.getString("CODIGO_CCUSTO"));
				dto.setCentroCusto(set.getString("CCUSTO"));
				dto.setObservacao(set.getString("OBSERVACAO"));
				dto.setMoeda(set.getString("MOEDA"));
				dto.setIdTipoMovimento(set.getString("CODIGO_MOVIMENTO"));
				dto.setTipoMovimento(set.getString("TIPO_MOVIMENTO"));
				dto.setUsrSolicitante(set.getString("USRSOLICITANTE"));
				dto.setStatus(set.getString("STATUS"));
				dto.setListaItem(listarItensMovimento(codMovimento, codColigada));
				return dto;
			}
			return null;
		} catch (Exception e) {
			LOG.error("Erro ao obter movimento");
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AprovacaoOrdemCompraDTO obterOrdemCompra(Integer idMov, Integer idColigada) {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT MOV.IDMOV AS ID, ")
			.append("COLIGADA.NOME AS COLIGADA, ")
			.append("FORNECEDOR.NOME AS FORNECEDOR,")
			.append("CC.CODCCUSTO AS CODIGO_CCUSTO, ")
			.append("CC.NOME AS NOME_CCUSTO, ")
			.append("MOV.VALORBRUTOORIG AS VALOR, ")
			.append("USUARIO.NOME AS NOME_USUARIO ")
			.append("FROM TMOV MOV ")
			.append("LEFT JOIN GCOLIGADA COLIGADA ON (MOV.CODCOLIGADA = COLIGADA.CODCOLIGADA) ")
			.append("LEFT JOIN FCFO FORNECEDOR ON (MOV.CODCFO = FORNECEDOR.CODCFO) ")
			.append("LEFT JOIN GCCUSTO CC on (MOV.CODCOLIGADA = CC.CODCOLIGADA AND MOV.CODCCUSTO = CC.CODCCUSTO) ")
			.append("LEFT JOIN GUSUARIO USUARIO ON (MOV.CODUSUARIO = USUARIO.CODUSUARIO) ")
			.append("WHERE MOV.CODCOLIGADA = ? ")
			.append("AND MOV.IDMOV = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			int idx = 1;
			ps.setInt(idx++, idColigada);
			ps.setInt(idx++, idMov);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				AprovacaoOrdemCompraDTO dto = new AprovacaoOrdemCompraDTO();
				dto.setIdCnt(set.getInt("ID"));
				dto.setNomeColigada(set.getString("COLIGADA"));
				dto.setNomeFornecedor(set.getString("FORNECEDOR"));
				dto.setCodigoCentroCusto(set.getString("CODIGO_CCUSTO"));
				dto.setCentroCusto(set.getString("NOME_CCUSTO"));
				BigDecimal valor = set.getBigDecimal("VALOR");
				dto.setValor(TreatNumber.formatMoney(valor).toString());
				dto.setRequisitante(set.getString("NOME_USUARIO"));
				
				return dto;
			}
		} catch (Exception e) {
			LOG.error("Erro ao obter dados da ordem de compra");
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ItemDTO> listarItensMovimento(Integer codMovimento, Integer codColigada) {
		List<ItemDTO> listaDTO = new ArrayList<ItemDTO>();
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ")
			.append("PRODUTO.CODIGOPRD AS CODIGO_PRODUTO, ")
			.append("PRODUTO.NOMEFANTASIA AS NOME, ")
			.append("ITEM.QUANTIDADETOTAL AS QUANTIDADE, ")
			.append("ITEM.CODUND AS UNIDADE, ")
			.append("ITEM.VALORBRUTOITEMORIG AS VALOR, ")
			.append("CC.CODCCUSTO AS CODIGO_CCUSTO, ")
			.append("CC.NOME AS CENTRO_CUSTO, ")
			.append("ORCAMENTO.CODTBORCAMENTO AS CODIGO_NATUREZA, ")
			.append("ORCAMENTO.DESCRICAO AS NATUREZA, ")
			.append("HISTORICO.HISTORICOLONGO AS OBSERVACAO ")
			.append("FROM ")
			.append("TITMMOV AS ITEM (NOLOCK) ")
			.append("LEFT JOIN TPRODUTO(NOLOCK) AS PRODUTO ON (PRODUTO.IDPRD = ITEM.IDPRD) ")
			.append("LEFT JOIN GCCUSTO(NOLOCK) AS CC ON (CC.CODCCUSTO = ITEM.CODCCUSTO AND CC.CODCOLIGADA = ITEM.CODCOLIGADA) ")
			.append("LEFT JOIN TPRODUTODEF AS PRODUTODEF ON (PRODUTODEF.IDPRD = PRODUTO.IDPRD) ")
			.append("LEFT JOIN TTBORCAMENTO(NOLOCK) AS ORCAMENTO ON (ORCAMENTO.CODTBORCAMENTO = PRODUTODEF.CODTBORCAMENTO) ")
			.append("LEFT JOIN TITMMOVHISTORICO(NOLOCK) AS HISTORICO ON (HISTORICO.IDMOV = ITEM.IDMOV AND HISTORICO.CODCOLIGADA = ITEM.CODCOLIGADA AND HISTORICO.NSEQITMMOV = ITEM.NSEQITMMOV) ")
			.append("WHERE ITEM.CODCOLIGADA = ? ")
			.append("AND ITEM.IDMOV = ? ");
			
			ps = conn.prepareStatement(sb.toString());

			ps.setInt(1, codColigada.intValue());
			ps.setInt(2, codMovimento.intValue());

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				ItemDTO dto = new ItemDTO();
				dto.setIdProduto(set.getString("CODIGO_PRODUTO"));
				dto.setProduto(set.getString("NOME"));
				dto.setQuantidade(Integer.valueOf(set.getInt("QUANTIDADE")));
				dto.setUnidade(set.getString("UNIDADE"));
				BigDecimal valor = set.getBigDecimal("VALOR");
				dto.setPrecoUnitario(TreatNumber.formatMoney(valor).toString());
				dto.setIdCentroCusto(set.getString("CODIGO_CCUSTO"));
				dto.setCentroCusto(set.getString("CENTRO_CUSTO"));
				dto.setIdNaturezaOrcamentaria(set.getString("CODIGO_NATUREZA"));
				dto.setNaturezaOrcamentaria(set.getString("NATUREZA"));
				dto.setObservacao(set.getString("OBSERVACAO"));

				listaDTO.add(dto);
			}
		} catch (Exception e) {
			LOG.error("Erro ao obter itens do movimento");
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
		return listaDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ItemDTO> listarItensContrato(Integer codContrato, Integer codColigada) {
		List<ItemDTO> listaDTO = new ArrayList<ItemDTO>();
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ")
			.append("PRODUTO.CODIGOPRD AS CODIGO_PRODUTO, ")
			.append("PRODUTO.NOMEFANTASIA AS NOME, ")
			.append("ITEM.QUANTIDADE AS QUANTIDADE, ")
			.append("ITEM.CODUND AS UNIDADE, ")
			.append("ITEM.VALORTOTAL AS VALOR, ")
			.append("CC.CODCCUSTO AS CODIGO_CCUSTO, ")
			.append("CC.NOME AS CENTRO_CUSTO, ")
			.append("ORCAMENTO.CODTBORCAMENTO AS CODIGO_NATUREZA, ")
			.append("ORCAMENTO.DESCRICAO AS NATUREZA, ")
			.append("HISTORICO.HISTORICOLONGO AS OBSERVACAO ")
			.append("FROM ")
			.append("TITMCNT AS ITEM (NOLOCK) ")
			.append("LEFT JOIN TPRODUTO(NOLOCK) AS PRODUTO ON (PRODUTO.IDPRD = ITEM.IDPRD) ")
			.append("LEFT JOIN GCCUSTO(NOLOCK) AS CC ON (CC.CODCCUSTO = ITEM.CODCCUSTO AND CC.CODCOLIGADA = ITEM.CODCOLIGADA) ")
			.append("LEFT JOIN TPRODUTODEF AS PRODUTODEF ON (PRODUTODEF.IDPRD = PRODUTO.IDPRD) ")
			.append("LEFT JOIN TTBORCAMENTO(NOLOCK) AS ORCAMENTO ON (ORCAMENTO.CODTBORCAMENTO = PRODUTODEF.CODTBORCAMENTO) ")
			.append("LEFT JOIN TITMCNTHISTORICO(NOLOCK) AS HISTORICO ON (HISTORICO.IDCNT = ITEM.IDCNT AND HISTORICO.CODCOLIGADA = ITEM.CODCOLIGADA AND HISTORICO.NSEQITMCNT = ITEM.NSEQITMCNT) ")
			.append("WHERE ITEM.CODCOLIGADA = ? ")
			.append("AND ITEM.IDCNT = ? ");

			ps = conn.prepareStatement(sb.toString());

			ps.setInt(1, codColigada.intValue());
			ps.setInt(2, codContrato.intValue());

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				ItemDTO dto = new ItemDTO();
				dto.setIdProduto(set.getString("CODIGO_PRODUTO"));
				dto.setProduto(set.getString("NOME"));
				dto.setQuantidade(Integer.valueOf(set.getInt("QUANTIDADE")));

				BigDecimal valor = set.getBigDecimal("VALOR");
				dto.setPrecoUnitario(TreatNumber.formatMoney(valor).toString());
				dto.setIdCentroCusto(set.getString("CODIGO_CCUSTO"));
				dto.setCentroCusto(set.getString("CENTRO_CUSTO"));
				dto.setIdNaturezaOrcamentaria(set.getString("CODIGO_NATUREZA"));
				dto.setNaturezaOrcamentaria(set.getString("NATUREZA"));
				dto.setObservacao(set.getString("OBSERVACAO"));

				listaDTO.add(dto);
			}
		} catch (Exception e) {
			LOG.error("Erro ao obter itens do contrato");
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
		return listaDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AprovacaoContratoDTO obterContrato(Integer idCnt, Integer idColigada) {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CNT.IDCNT AS ID, ")
			.append("COLIGADA.NOME AS COLIGADA, ")
			.append("FORNECEDOR.NOME AS FORNECEDOR,")
			.append("CC.CODCCUSTO AS CODIGO_CCUSTO, ")
			.append("CC.NOME AS NOME_CCUSTO, ")
			.append("CNT.VALORCONTRATO AS VALOR, ")
			.append("USUARIO.NOME AS NOME_USUARIO ")
			.append("FROM TCNT CNT ")
			.append("LEFT JOIN GCOLIGADA COLIGADA ON (CNT.CODCOLIGADA = COLIGADA.CODCOLIGADA) ")
			.append("LEFT JOIN FCFO FORNECEDOR ON (CNT.CODCFO = FORNECEDOR.CODCFO) ")
			.append("LEFT JOIN GCCUSTO CC on (CNT.CODCOLIGADA = CC.CODCOLIGADA AND CNT.CODCCUSTO = CC.CODCCUSTO) ")
			.append("LEFT JOIN GUSUARIO USUARIO ON (CNT.CODUSUARIO = USUARIO.CODUSUARIO) ")
			.append("WHERE CNT.CODCOLIGADA = ? ")
			.append("AND CNT.IDCNT = ? ");
			
			ps = conn.prepareStatement(sb.toString());
			int idx = 1;
			ps.setInt(idx++, idColigada);
			ps.setInt(idx++, idCnt);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				AprovacaoContratoDTO dto = new AprovacaoContratoDTO();
				dto.setIdCnt(set.getInt("ID"));
				dto.setNomeColigada(set.getString("COLIGADA"));
				dto.setNomeFornecedor(set.getString("FORNECEDOR"));
				dto.setCodigoCentroCusto(set.getString("CODIGO_CCUSTO"));
				dto.setCentroCusto(set.getString("NOME_CCUSTO"));
				BigDecimal valor = set.getBigDecimal("VALOR");
				dto.setValor(TreatNumber.formatMoney(valor).toString());
				dto.setRequisitante(set.getString("NOME_USUARIO"));
				
				return dto;
			}
		} catch (Exception e) {
			LOG.error("Erro ao obter dados do contrato");
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ZMDRMFLUIGDTO obterLigacaoRMFluig(Integer idFluig) {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append(
					"SELECT RMFLUIG.CODCOLIGADA, RMFLUIG.IDMOV, RMFLUIG.IDCNT, RMFLUIG.IDFLUIG FROM ZMDRMFLUIG RMFLUIG ");
			sb.append("WHERE RMFLUIG.IDFLUIG = ? ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, idFluig);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				ZMDRMFLUIGDTO dto = new ZMDRMFLUIGDTO();
				dto.setIdColigada(set.getInt("CODCOLIGADA"));
				dto.setIdMovimento(set.getInt("IDMOV"));
				dto.setIdCnt(set.getInt("IDCNT"));
				dto.setIdFluig(set.getInt("IDFLUIG"));
				
				return dto;
			}
		} catch (Exception e) {
			LOG.error("Erro ao obter ligação RM e Fluig");
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
		return null;
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
	public List<ProdutoRM> listarProdutosPorNome(Long idColigada, String nome, Modalidade modalidade) throws ApplicationException {
		/**
		 * SELECT TPRODUTO.IDPRD, TPRODUTO.CODIGOPRD, TPRODUTO.NOMEFANTASIA, TPRDDEF.CODUNDCOMPRA FROM TPRODUTO AS TPRODUTO
		 * LEFT JOIN TPRODUTODEF AS TPRDDEF ON (TPRODUTO.IDPRD = TPRDDEF.IDPRD AND TPRODUTO.CODCOLPRD = TPRDDEF.CODCOLIGADA)
 		 * WHERE TPRODUTO.CODCOLPRD = ?
		 * AND TPRODUTO.INATIVO = 0
		 * AND LEN(TPRODUTO.CODIGOPRD) > 6
		 * AND TPRODUTO.NOMEFANTASIA LIKE ?
		 * AND (TPRODUTO.CODIGOPRD LIKE '02.%' OR TPRODUTO.CODIGOPRD LIKE '90.001%')
		 * ORDER BY TPRODUTO.CODIGOPRD ASC
		 * OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<ProdutoRM> listaProduto = new ArrayList<ProdutoRM>();

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT TPRODUTO.IDPRD, TPRODUTO.CODIGOPRD, TPRODUTO.NOMEFANTASIA, TPRDDEF.CODUNDCOMPRA FROM TPRODUTO AS TPRODUTO ")
			.append("LEFT JOIN TPRODUTODEF AS TPRDDEF ON (TPRODUTO.IDPRD = TPRDDEF.IDPRD AND TPRODUTO.CODCOLPRD = TPRDDEF.CODCOLIGADA) ")
			.append("WHERE TPRODUTO.CODCOLPRD = ? ")
			.append("AND TPRODUTO.INATIVO = 0 ")
			.append("AND LEN(TPRODUTO.CODIGOPRD) > 6 ")
			.append("AND TPRODUTO.NOMEFANTASIA LIKE ? ");
			
			if(Util.isNotNull(modalidade)) {
				if(modalidade.equals(Modalidade.MATERIAL)) {
					sb.append("AND (TPRODUTO.CODIGOPRD LIKE '02.%' OR TPRODUTO.CODIGOPRD LIKE '90.001%') ");
				} else if (modalidade.equals(Modalidade.SERVICO)) {
					sb.append("AND (TPRODUTO.CODIGOPRD LIKE '01.%' OR TPRODUTO.CODIGOPRD LIKE '90.002%') ");
				}
			}
			
			sb.append("ORDER BY TPRODUTO.CODIGOPRD ASC ")
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
				produto.setCodigoUnidadeCompra(set.getString("CODUNDCOMPRA"));
				
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
			.append("ORDER BY DESCRICAO ASC ");
			
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
			.append("ORDER BY CODUND ASC ");
			
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
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CondicaoPagamentoRM> listarCondicaoPagamento(Long idColigada) throws ApplicationException {
		/**
		 * SELECT CODCPG, NOME FROM TCPG
		 * WHERE CODCOLIGADA = ?
		 * AND INATIVO IS NULL
		 * ORDER BY CODCPG ASC
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<CondicaoPagamentoRM> listaCondicaoPagamento = new ArrayList<CondicaoPagamentoRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODCPG, NOME FROM TCPG ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND INATIVO IS NULL ")
			.append("ORDER BY CODCPG ASC ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idColigada);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				CondicaoPagamentoRM condPagamento = new CondicaoPagamentoRM();
				condPagamento.setCodigoCondicaoPagamento(set.getString("CODCPG"));
				condPagamento.setCondicaoPagamento(set.getString("NOME"));
				
				listaCondicaoPagamento.add(condPagamento);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarCondicaoPagamento" }, e);
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
		return listaCondicaoPagamento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoRM obterProduto(Integer idProduto) throws ApplicationException {
		/**
		 * SELECT PRODUTO.IDPRD, PRODUTO.CODIGOPRD, PRODUTO.NOMEFANTASIA, PRODUTO.CODIGOREDUZIDO, PRODUTODEF.CODTBORCAMENTO FROM TPRODUTO AS PRODUTO
		 * LEFT JOIN TPRODUTODEF AS PRODUTODEF ON (PRODUTO.IDPRD = PRODUTODEF.IDPRD) 
		 * WHERE PRODUTO.IDPRD = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT PRODUTO.IDPRD, PRODUTO.CODIGOPRD, PRODUTO.NOMEFANTASIA, PRODUTO.CODIGOREDUZIDO, PRODUTODEF.CODTBORCAMENTO FROM TPRODUTO AS PRODUTO ")
			.append("LEFT JOIN TPRODUTODEF AS PRODUTODEF ON (PRODUTO.IDPRD = PRODUTODEF.IDPRD) ")
			.append("WHERE PRODUTO.IDPRD = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, idProduto);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				ProdutoRM produto = new ProdutoRM();
				produto.setIdProduto(set.getInt("IDPRD"));
				produto.setCodigoProduto(set.getString("CODIGOPRD"));
				produto.setProduto(set.getString("NOMEFANTASIA"));
				produto.setCodigoReduzido(set.getString("CODIGOREDUZIDO"));
				produto.setCodigoNatureza(set.getString("CODTBORCAMENTO"));
				
				return produto;
				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterProduto" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void atualizarMovimento(String idColigada, String idMovimento, String usuario) throws ApplicationException {
		/**
		 * UPDATE TMOV
		 * SET CAMPOLIVRE1 = ?
		 * WHERE CODCOLIGADA = ?
		 * AND IDMOV = ?"
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TMOV ")
			.append("SET CAMPOLIVRE1 = ?, CODUSUARIO = ? ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND IDMOV = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, idMovimento);
			ps.setString(2, usuario);
			ps.setString(3, idColigada);
			ps.setString(4, idMovimento);
			
			ps.execute();
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "atualizarMovimento" }, e);
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
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean existeUsuario(String nomeUsuario) throws ApplicationException {
		/**
		 * SELECT CODUSUARIO FROM GUSUARIO
		 * WHERE CODUSUARIO LIKE ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODUSUARIO FROM GUSUARIO ")
			.append("WHERE CODUSUARIO LIKE ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, nomeUsuario);
			
			ResultSet set = ps.executeQuery();
			
			if(set.next()) {
				return Boolean.TRUE;
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "existeUsuario" }, e);
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
	public String obterLotacaoCentroCusto(String centroCusto, Long idColigada) throws ApplicationException {
		/**
		 * SELECT LOTACAO FROM CCUSTOCOMPL
		 * WHERE CODCCUSTO LIKE ?
		 * AND CODCOLIGADA = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT LOTACAO FROM CCUSTOCOMPL ")
			.append("WHERE CODCCUSTO LIKE ? ")
			.append("AND CODCOLIGADA = ? ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, centroCusto);
			ps.setLong(2, idColigada);
			
			ResultSet set = ps.executeQuery();
			
			if(set.next()) {
				return set.getString("LOTACAO");
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterLotacaoCentroCusto" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AprovadorRM> listarPrimeiroAprovador(String lotacao) throws ApplicationException {
		/**
		 * SELECT APROVADOR.USRAPROV, APROVADOR.VALORDEMOV, APROVADOR.VALORATEMOV FROM ZMDAPROVADOR AS APROVADOR
		 * WHERE APROVADOR.CODCCUSTO LIKE ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<AprovadorRM> listaAprovador = new ArrayList<AprovadorRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT APROVADOR.USRAPROV, APROVADOR.VALORDEMOV, APROVADOR.VALORATEMOV FROM ZMDAPROVADOR AS APROVADOR ")
			.append("WHERE APROVADOR.CODCCUSTO LIKE ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, lotacao);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				AprovadorRM aprovador = new AprovadorRM();
				aprovador.setAprovador(set.getString("USRAPROV"));
				aprovador.setValorMovimentoDe(set.getBigDecimal("VALORDEMOV"));
				aprovador.setValorMovimentoAte(set.getBigDecimal("VALORATEMOV"));
				
				listaAprovador.add(aprovador);
				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarPrimeiroAprovador" }, e);
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
		return listaAprovador;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AprovadorRM> listarSegundoAprovador(String lotacao) throws ApplicationException {
		/**
		 * SELECT APROVADOR.USRAPROV, APROVADOR.VALORDEMOV, APROVADOR.VALORATEMOV FROM ZMDSEGUNDOAPROV AS APROVADOR
		 * WHERE APROVADOR.CODCCUSTO LIKE ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		List<AprovadorRM> listaAprovador = new ArrayList<AprovadorRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT APROVADOR.USRAPROV, APROVADOR.VALORDEMOV, APROVADOR.VALORATEMOV FROM ZMDSEGUNDOAPROV AS APROVADOR ")
			.append("WHERE APROVADOR.CODCCUSTO LIKE ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, lotacao);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				AprovadorRM aprovador = new AprovadorRM();
				aprovador.setAprovador(set.getString("USRAPROV"));
				aprovador.setValorMovimentoDe(set.getBigDecimal("VALORDEMOV"));
				aprovador.setValorMovimentoAte(set.getBigDecimal("VALORATEMOV"));
				
				listaAprovador.add(aprovador);
				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarSegundoAprovador" }, e);
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
		return listaAprovador;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterPeriodoOrcamentoColigada(Long codColigada) throws ApplicationException {
		/**
		 *	SELECT IDPERIODO FROM TPERIODOORCAMENTO
		 *	WHERE STATUS = 0
		 *	AND GETDATE() BETWEEN DATAINICIO AND DATAFIM
		 *	AND CODCOLIGADA = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT IDPERIODO FROM TPERIODOORCAMENTO ")
			.append("WHERE STATUS = 0 ")
			.append("AND GETDATE() BETWEEN DATAINICIO AND DATAFIM ")
			.append("AND CODCOLIGADA = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, codColigada);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getInt("IDPERIODO");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterPeriodoOrcamentoColigada" }, e);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterOrcamento(Integer periodo, Long codColigada, String idNatureza, String idCentroCusto) throws ApplicationException {
		/**
		 *	SELECT IDORCAMENTO FROM TORCAMENTO
		 * 	WHERE CODCOLIGADA = ?
		 * 	AND CODTBORCAMENTO LIKE ?
		 * 	AND IDPERIODO = ?
		 * 	AND CODCCUSTO LIKE ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT IDORCAMENTO FROM TORCAMENTO ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CODTBORCAMENTO LIKE ? ")
			.append("AND IDPERIODO = ? ")
			.append("AND CODCCUSTO LIKE ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, codColigada);
			ps.setString(2, idNatureza);
			ps.setInt(3, periodo);
			ps.setString(4, idCentroCusto);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getInt("IDORCAMENTO");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterOrcamento" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal obterSaldoDisponivelOrcamento(Integer periodo, Long codColigada, Integer idOrcamento, Integer mes) throws ApplicationException {
		/**
		 *	SELECT ((VALORORCADO - (VALORREAL + VALORCEDIDO)) + VALORRECEBIDO) AS SALDO FROM TITMORCAMENTO
		 *	WHERE CODCOLIGADA = ?
		 *	AND IDORCAMENTO = ?
		 *	AND IDPERIODO = ?
		 *	AND IDITMPERIODO = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ((COALESCE(VALORORCADO,0) - (COALESCE(VALORREAL,0) + COALESCE(VALORCEDIDO, 0) + COALESCE(VALOROPCIONAL1, 0))) + VALORRECEBIDO) AS SALDO FROM TITMORCAMENTO ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND IDORCAMENTO = ? ")
			.append("AND IDPERIODO = ? ")
			.append("AND IDITMPERIODO = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, codColigada);
			ps.setInt(2, idOrcamento);
			ps.setInt(3, periodo);
			ps.setInt(4, mes);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getBigDecimal("SALDO");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterSaldoDisponivelOrcamento" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public OrcamentoDTO obterOrcamentoCompleto(Integer periodo, Long codColigada, Integer idOrcamento, Integer mes) throws ApplicationException {
		/**
		 *	SELECT VALORORCADO AS ORCADO, ((VALORORCADO - (VALORREAL + VALORCEDIDO)) + VALORRECEBIDO) AS SALDO FROM TITMORCAMENTO
		 *	WHERE CODCOLIGADA = ?
		 *	AND IDORCAMENTO = ?
		 *	AND IDPERIODO = ?
		 *	AND IDITMPERIODO = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT VALORORCADO AS ORCADO, VALORREAL AS REALIZADO, VALORCEDIDO AS CEDIDO, VALORRECEBIDO AS RECEBIDO, ((COALESCE(VALORORCADO,0) - (COALESCE(VALORREAL,0) + COALESCE(VALORCEDIDO, 0) + COALESCE(VALOROPCIONAL1, 0))) + VALORRECEBIDO) AS SALDO FROM TITMORCAMENTO ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND IDORCAMENTO = ? ")
			.append("AND IDPERIODO = ? ")
			.append("AND IDITMPERIODO = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, codColigada);
			ps.setInt(2, idOrcamento);
			ps.setInt(3, periodo);
			ps.setInt(4, mes);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				OrcamentoDTO dto = new OrcamentoDTO();
				dto.setValorOrcado(set.getBigDecimal("ORCADO"));
				dto.setValorRealizado(set.getBigDecimal("REALIZADO"));
				dto.setValorCedido(set.getBigDecimal("CEDIDO"));
				dto.setValorRecebido(set.getBigDecimal("RECEBIDO"));
				dto.setSaldo(set.getBigDecimal("SALDO"));
				
				return dto;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterOrcamentoCompleto" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public OrcamentoDTO incluirItemOrcamento(Integer periodo, Long codColigada, Integer idOrcamento, Integer mes) throws ApplicationException {
		/**
		 *	INSERT INTO TITMORCAMENTO
		 *	(CODCOLIGADA, IDORCAMENTO, IDPERIODO, IDITMPERIODO, VALORORCADO, VALORREAL, VALOROPCIONAL1, VALOROPCIONAL2, VALORRECEBIDO, VALORCEDIDO, VALOREXCEDENTE, RECCREATEDBY, RECCREATEDON, RECMODIFIEDBY, RECMODIFIEDON)
		 *	VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'mestre', ?, 'mestre', ?)
		 *
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO TITMORCAMENTO ")
			.append("(CODCOLIGADA, IDORCAMENTO, IDPERIODO, IDITMPERIODO, VALORORCADO, VALORREAL, VALOROPCIONAL1, VALOROPCIONAL2, VALORRECEBIDO, VALORCEDIDO, VALOREXCEDENTE, RECCREATEDBY, RECCREATEDON, RECMODIFIEDBY, RECMODIFIEDON) ")
			.append("VALUES(?, ?, ?, ?, 0, 0, 0, 0, 0, 0, 0, 'mestre', ?, 'mestre', ?) ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, codColigada);
			ps.setInt(2, idOrcamento);
			ps.setInt(3, periodo);
			ps.setInt(4, mes);
			ps.setTimestamp(5, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
			ps.setTimestamp(6, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
			
			ps.executeUpdate();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "incluirItemOrcamento" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterIdFluig(Long idMovimento) throws ApplicationException {
		/**
		 * SELECT IDFLUIG FROM ZMDRMFLUIG
		 * WHERE IDMOV = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT IDFLUIG FROM ZMDRMFLUIG ")
			.append("WHERE IDMOV = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idMovimento);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getInt("IDFLUIG");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterIdFluig" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void cederValor(BigDecimal valorCedido, Integer periodo, Long codColigada, Integer idOrcamento, Integer mes) throws ApplicationException {
		/**
		 *	UPDATE TITMORCAMENTO
		 *	SET VALORCEDIDO = (COALESCE(VALORCEDIDO, 0) + ?)
		 *	WHERE CODCOLIGADA = ?
		 *	AND IDORCAMENTO = ?
		 *	AND IDPERIODO = ?
		 *	AND IDITMPERIODO = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TITMORCAMENTO ")
			.append("SET VALORCEDIDO = (COALESCE(VALORCEDIDO, 0) + ?) ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND IDORCAMENTO = ? ")
			.append("AND IDPERIODO = ? ")
			.append("AND IDITMPERIODO = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setBigDecimal(1, valorCedido);
			ps.setLong(2, codColigada);
			ps.setInt(3, idOrcamento);
			ps.setInt(4, periodo);
			ps.setInt(5, mes);
			
			ps.execute();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "cederValor" }, e);
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
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void receberValor(BigDecimal valorRecebido, Integer periodo, Long codColigada, Integer idOrcamento, Integer mes) throws ApplicationException {
		/**
		 *	UPDATE TITMORCAMENTO
		 *	SET VALORRECEBIDO = (COALESCE(VALORRECEBIDO, 0) + ?)
		 *	WHERE CODCOLIGADA = ?
		 *	AND IDORCAMENTO = ?
		 *	AND IDPERIODO = ?
		 *	AND IDITMPERIODO = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TITMORCAMENTO ")
			.append("SET VALORRECEBIDO = (COALESCE(VALORRECEBIDO, 0) + ?) ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND IDORCAMENTO = ? ")
			.append("AND IDPERIODO = ? ")
			.append("AND IDITMPERIODO = ? ");
			
			
			ps = conn.prepareStatement(sb.toString());
			ps.setBigDecimal(1, valorRecebido);
			ps.setLong(2, codColigada);
			ps.setInt(3, idOrcamento);
			ps.setInt(4, periodo);
			ps.setInt(5, mes);
			
			ps.execute();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "cederValor" }, e);
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
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NaturezaOrcamentariaRM obterNaturezaOrcamentaria(String codigoNatureza) throws ApplicationException {
		/**
		 *	SELECT CODTBORCAMENTO, DESCRICAO FROM TTBORCAMENTO
		 *	WHERE CODTBORCAMENTO = ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODTBORCAMENTO, DESCRICAO FROM TTBORCAMENTO ")
			.append("WHERE CODTBORCAMENTO = ? ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, codigoNatureza);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				NaturezaOrcamentariaRM nat = new NaturezaOrcamentariaRM();
				nat.setCodigoNaturezaOrcamentaria(set.getString("CODTBORCAMENTO"));
				nat.setNaturezaOrcamentaria(set.getString("DESCRICAO"));
				return nat;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterNaturezaOrcamentaria" }, e);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterCodigoPessoa(Long idColigada, String chapa) throws ApplicationException {
		/**
		 * SELECT CODPESSOA, CODCOLIGADA, CHAPA FROM PFUNC
		 * WHERE CODCOLIGADA = ?
		 * AND CHAPA LIKE ?
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODPESSOA, CODCOLIGADA, CHAPA FROM PFUNC ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA LIKE ? ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idColigada);
			ps.setString(2, chapa);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getInt("CODPESSOA");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterCodigoPessoa" }, e);
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
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ChapaDTO> listarChapaFuncionario(Integer codPessoa) throws ApplicationException {
		/**
		 * SELECT CODPESSOA, CODCOLIGADA, CHAPA FROM PFUNC
		 * WHERE CODPESSOA = ?
		 * ORDER BY RECCREATEDON DESC
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<ChapaDTO> chapas = new ArrayList<ChapaDTO>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CODPESSOA, CODCOLIGADA, CHAPA FROM PFUNC ")
			.append("WHERE CODPESSOA = ? ")
			.append("ORDER BY RECCREATEDON DESC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, codPessoa);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				ChapaDTO dto = new ChapaDTO();
				dto.setIdColigada(set.getLong("CODCOLIGADA"));
				dto.setChapa(set.getString("CHAPA"));
				
				chapas.add(dto);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarChapaFuncionario" }, e);
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
		return chapas;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HoleriteDTO> listarHoleriteFuncionario(Long idColigada, String chapa) throws ApplicationException {
		/**
		 * SELECT DISTINCT CODCOLIGADA, ANOCOMP, MESCOMP, NROPERIODO FROM PFPERFF
		 * WHERE CHAPA = ?
		 * AND CODCOLIGADA = ?
		 * ORDER BY ANOCOMP DESC, MESCOMP DESC
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<HoleriteDTO> holerites = new ArrayList<HoleriteDTO>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT DISTINCT CODCOLIGADA, ANOCOMP, MESCOMP, NROPERIODO FROM PFPERFF ")
			.append("WHERE CHAPA = ? ")
			.append("AND CODCOLIGADA = ? ")
			.append("ORDER BY ANOCOMP DESC, MESCOMP DESC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, chapa);
			ps.setLong(2, idColigada);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				HoleriteDTO dto = new HoleriteDTO();
				dto.setColigada(new Coligada());
				dto.getColigada().setId(set.getLong("CODCOLIGADA"));
				dto.setAno(set.getInt("ANOCOMP"));
				dto.setMes(Mes.obterPorCodigo(set.getInt("MESCOMP")));
				dto.setPeriodo(PeriodoHolerite.obterPorCodigo(set.getInt("NROPERIODO")));
				
				holerites.add(dto);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarHoleriteFuncionario" }, e);
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
		return holerites;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FuncionarioRM obterDadosFuncionario(Long idColigada, String chapa) throws ApplicationException {
		/**
		 * SELECT
		 * 		GCOLIGADA.CODCOLIGADA AS CODCOLIGADA,
		 * 		GCOLIGADA.NOMEFANTASIA AS RAZAO_SOCIAL,
		 *		GCOLIGADA.CGC AS CNPJ,
		 * 		PFUNC.CHAPA AS CHAPA,
		 * 		PFUNC.NOME AS NOME_FUNCIONARIO,
		 * 		PFUNCAO.NOME AS FUNCAO,
		 * 		PFUNC.DATAADMISSAO AS DATA_ADMISSAO,
		 * 		PPESSOA.RUA AS RUA,
		 * 		PPESSOA.NUMERO AS NUMERO_RUA,
		 * 		PPESSOA.BAIRRO AS BAIRRO,
		 * 		PPESSOA.CIDADE AS CIDADE,
		 * 		PPESSOA.CEP AS CEP,
		 * 		PPESSOA.ESTADO AS ESTADO,
		 * 		PFUNC.PISPASEP AS PISPASEP,
		 * 		PPESSOA.CPF AS CPF,
		 * 		PPESSOA.CARTIDENTIDADE AS IDENTIDADE,
		 * 		GBANCO.NOME AS BANCO,
		 * 		GAGENCIA.NOME AS AGENCIA,
		 * 		GAGENCIA.NUMAGENCIA AS NUMERO_AGENCIA,
		 * 		PFUNC.CONTAPAGAMENTO AS NUMERO_CONTA,
		 * 		PSECAO.CODIGO AS CODIGO_SECAO,
		 * 		PSECAO.DESCRICAO AS DESC_SECAO
		 * FROM PFUNC AS PFUNC
		 * 		LEFT JOIN PPESSOA AS PPESSOA ON (PPESSOA.CODIGO = PFUNC.CODPESSOA)
		 * 		LEFT JOIN PFUNCAO AS PFUNCAO ON (PFUNCAO.CODIGO = PFUNC.CODFUNCAO AND PFUNCAO.CODCOLIGADA = PFUNC.CODCOLIGADA)
		 * 		LEFT JOIN GBANCO AS GBANCO ON (GBANCO.NUMBANCO = PFUNC.CODBANCOPAGTO)
		 * 		LEFT JOIN GAGENCIA AS GAGENCIA ON (GAGENCIA.NUMBANCO = GBANCO.NUMBANCO AND GAGENCIA.NUMAGENCIA = PFUNC.CODAGENCIAPAGTO)
		 * 		LEFT JOIN PSECAO AS SECAO ON (SECAO.CODIGO = PFUNC.CODSECAO AND SECAO.CODCOLIGADA = PFUNC.CODCOLIGADA)
		 * WHERE PFUNC.CODCOLIGADA = ?
		 * AND PFUNC.CHAPA = ?
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ")
					.append("GCOLIGADA.CODCOLIGADA AS CODCOLIGADA, ")
					.append("GCOLIGADA.NOMEFANTASIA AS RAZAO_SOCIAL, ")
					.append("GCOLIGADA.CGC AS CNPJ, ")
					.append("PFUNC.CHAPA AS CHAPA, ")
					.append("PFUNC.NOME AS NOME_FUNCIONARIO, ")
					.append("PFUNCAO.NOME AS FUNCAO, ")
					.append("PFUNC.DATAADMISSAO AS DATA_ADMISSAO, ")
					.append("PPESSOA.RUA AS RUA, ")
					.append("PPESSOA.NUMERO AS NUMERO_RUA, ")
					.append("PPESSOA.BAIRRO AS BAIRRO, ")
					.append("PPESSOA.CIDADE AS CIDADE, ")
					.append("PPESSOA.CEP AS CEP, ")
					.append("PPESSOA.ESTADO AS ESTADO, ")
					.append("PFUNC.PISPASEP AS PISPASEP, ")
					.append("PPESSOA.CPF AS CPF, ")
					.append("PPESSOA.CARTIDENTIDADE AS IDENTIDADE, ")
					.append("GBANCO.NOME AS BANCO, ")
					.append("GAGENCIA.NOME AS AGENCIA, ")
					.append("GAGENCIA.NUMAGENCIA AS NUMERO_AGENCIA, ")
					.append("PFUNC.CONTAPAGAMENTO AS NUMERO_CONTA, ")
					.append("SECAO.CODIGO AS CODIGO_SECAO, ")
					.append("SECAO.DESCRICAO AS DESC_SECAO ")
			.append("FROM PFUNC AS PFUNC ")
					.append("LEFT JOIN PPESSOA AS PPESSOA ON (PPESSOA.CODIGO = PFUNC.CODPESSOA) ")
					.append("LEFT JOIN PFUNCAO AS PFUNCAO ON (PFUNCAO.CODIGO = PFUNC.CODFUNCAO AND PFUNCAO.CODCOLIGADA = PFUNC.CODCOLIGADA) ")
					.append("LEFT JOIN GBANCO AS GBANCO ON (GBANCO.NUMBANCO = PFUNC.CODBANCOPAGTO) ")
					.append("LEFT JOIN GAGENCIA AS GAGENCIA ON (GAGENCIA.NUMBANCO = GBANCO.NUMBANCO AND GAGENCIA.NUMAGENCIA = PFUNC.CODAGENCIAPAGTO) ")
					.append("LEFT JOIN GCOLIGADA AS GCOLIGADA ON (GCOLIGADA.CODCOLIGADA = PFUNC.CODCOLIGADA) ")
					.append("LEFT JOIN PSECAO AS SECAO ON (SECAO.CODIGO = PFUNC.CODSECAO AND SECAO.CODCOLIGADA = PFUNC.CODCOLIGADA) ")
			.append("WHERE PFUNC.CODCOLIGADA = ? ")
			.append("AND PFUNC.CHAPA = ? ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idColigada);
			ps.setString(2, chapa);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				FuncionarioRM funcionario = new FuncionarioRM();
				funcionario.setCodColigada(set.getInt("CODCOLIGADA"));
				funcionario.setEmpresa(set.getString("RAZAO_SOCIAL"));
				funcionario.setCnpj(set.getString("CNPJ"));
				funcionario.setChapa(set.getString("CHAPA"));
				funcionario.setNomeFuncionario(set.getString("NOME_FUNCIONARIO"));
				funcionario.setFuncao(set.getString("FUNCAO"));
				funcionario.setDtAdmissao(set.getDate("DATA_ADMISSAO"));
				funcionario.setRua(set.getString("RUA"));
				funcionario.setNumero(set.getString("NUMERO_RUA"));
				funcionario.setBairro(set.getString("BAIRRO"));
				funcionario.setCidade(set.getString("CIDADE"));
				funcionario.setCep(set.getString("CEP"));
				funcionario.setEstado(set.getString("ESTADO"));
				funcionario.setPispasep(set.getString("PISPASEP"));
				funcionario.setCpf(set.getString("CPF"));
				funcionario.setIdentidade(set.getString("IDENTIDADE"));
				funcionario.setNomeBanco(set.getString("BANCO"));
				funcionario.setAgencia(set.getString("AGENCIA"));
				funcionario.setNumeroAgencia(set.getString("NUMERO_AGENCIA"));
				funcionario.setNumeroConta(set.getString("NUMERO_CONTA"));
				funcionario.setCodSecao(set.getString("CODIGO_SECAO"));
				funcionario.setSecao(set.getString("DESC_SECAO"));
				
				return funcionario;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterDadosFuncionario" }, e);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FuncionarioHoleriteRM obterDadosHoleriteFuncionario(Integer idColigada, String chapa, Integer mes, Integer ano, Integer periodo) throws ApplicationException {
		/**
		 * 
		 * SELECT PFPERFF.BASEFGTS, PFPERFF.BASEFGTS13, PFPERFF.BASEIRRF, PFPERFF.BASEIRRF13, PFPERFF.BASEINSS, PFPERFF.BASEINSS13, PFPERFF.SALARIODECALCULO FROM PFPERFF AS PFPERFF
		 * WHERE PFPERFF.CODCOLIGADA = ?
		 * AND PFPERFF.CHAPA = ?
		 * AND PFPERFF.MESCOMP = ?
		 * AND PFPERFF.ANOCOMP = ?
		 * AND PFPERFF.NROPERIODO = ?
		 * 
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT PFPERFF.BASEFGTS, PFPERFF.BASEFGTS13, PFPERFF.BASEIRRF, PFPERFF.BASEIRRF13, PFPERFF.BASEINSS, PFPERFF.BASEINSS13, PFPERFF.SALARIODECALCULO FROM PFPERFF AS PFPERFF ")
			.append("WHERE PFPERFF.CODCOLIGADA = ? ")
			.append("AND PFPERFF.CHAPA = ? ")
			.append("AND PFPERFF.MESCOMP = ? ")
			.append("AND PFPERFF.ANOCOMP = ? ")
			.append("AND PFPERFF.NROPERIODO = ? ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idColigada);
			ps.setString(2, chapa);
			ps.setInt(3, mes);
			ps.setInt(4, ano);
			ps.setInt(5, periodo);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				FuncionarioHoleriteRM funcionario = new FuncionarioHoleriteRM();
				if(periodo.equals(1)) {
					funcionario.setBaseFGTS(set.getBigDecimal("BASEFGTS13"));
					funcionario.setBaseIRRF(set.getBigDecimal(0));
					funcionario.setBaseINSS(set.getBigDecimal("BASEINSS13"));
				} else if(periodo.equals(2)) {
					funcionario.setBaseFGTS(set.getBigDecimal("BASEFGTS13"));
					funcionario.setBaseIRRF(set.getBigDecimal("BASEIRRF13"));
					funcionario.setBaseINSS(set.getBigDecimal("BASEINSS13"));
				} else {
					funcionario.setBaseFGTS(set.getBigDecimal("BASEFGTS"));
					funcionario.setBaseIRRF(set.getBigDecimal("BASEIRRF"));
					funcionario.setBaseINSS(set.getBigDecimal("BASEINSS"));
				}
				funcionario.setSalarioCalculo(set.getBigDecimal("SALARIODECALCULO"));
				
				return funcionario;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterDadosHoleriteFuncionario" }, e);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HoleriteItensRM> listarItensHolerite(Integer idColigada, String chapa, Integer mes, Integer ano, Integer periodo) throws ApplicationException {
		/**
		 * 
		 * SELECT PFFINANC.DTPAGTO, PEVENTO.CODIGO, PEVENTO.DESCRICAO, PFFINANC.[REF], IIF(PEVENTO.PROVDESCBASE LIKE 'P', PFFINANC.VALOR, 0) AS PROVENTO, IIF(PEVENTO.PROVDESCBASE LIKE 'D', PFFINANC.VALOR, 0) AS DESCONTO FROM PFFINANC AS PFFINANC
		 * LEFT JOIN PEVENTO AS PEVENTO ON (PEVENTO.CODIGO = PFFINANC.CODEVENTO AND PEVENTO.CODCOLIGADA = PFFINANC.CODCOLIGADA)
		 * WHERE PFFINANC.CODCOLIGADA = ?
		 * AND PFFINANC.CHAPA = ?
		 * AND PFFINANC.ANOCOMP = ?
		 * AND PFFINANC.MESCOMP = ?
		 * AND PFFINANC.NROPERIODO = ?
		 * AND (PEVENTO.PROVDESCBASE LIKE 'P' OR PEVENTO.PROVDESCBASE LIKE 'D')
		 * 
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<HoleriteItensRM> listaItens = new ArrayList<HoleriteItensRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT PFFINANC.DTPAGTO, PEVENTO.CODIGO, PEVENTO.DESCRICAO, PFFINANC.[REF], IIF(PEVENTO.PROVDESCBASE LIKE 'P', PFFINANC.VALOR, 0) AS PROVENTO, IIF(PEVENTO.PROVDESCBASE LIKE 'D', PFFINANC.VALOR, 0) AS DESCONTO FROM PFFINANC AS PFFINANC ")
			.append("LEFT JOIN PEVENTO AS PEVENTO ON (PEVENTO.CODIGO = PFFINANC.CODEVENTO AND PEVENTO.CODCOLIGADA = PFFINANC.CODCOLIGADA) ")
			.append("WHERE PFFINANC.CODCOLIGADA = ? ")
			.append("AND PFFINANC.CHAPA = ? ")
			.append("AND PFFINANC.MESCOMP = ? ")
			.append("AND PFFINANC.ANOCOMP = ? ")
			.append("AND PFFINANC.NROPERIODO = ? ")
			.append("AND (PEVENTO.PROVDESCBASE LIKE 'P' OR PEVENTO.PROVDESCBASE LIKE 'D') ")
			.append("ORDER BY PEVENTO.PROVDESCBASE DESC, PEVENTO.CODIGO ASC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idColigada);
			ps.setString(2, chapa);
			ps.setInt(3, mes);
			ps.setInt(4, ano);
			ps.setInt(5, periodo);
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				HoleriteItensRM item = new HoleriteItensRM();
				item.setDtPagamento(set.getDate("DTPAGTO"));
				item.setCodigo(set.getString("CODIGO"));
				item.setDescricao(set.getString("DESCRICAO"));
				item.setReferencia(set.getBigDecimal("REF"));
				item.setProvento(set.getBigDecimal("PROVENTO"));
				item.setDesconto(set.getBigDecimal("DESCONTO"));
				
				listaItens.add(item);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarItensHolerite" }, e);
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
		return listaItens;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterQtdDependenteIRRF(Integer idColigada, String chapa) throws ApplicationException {
		/**
		 * 
		 * SELECT COUNT(*) AS QTD FROM PFDEPEND
		 * WHERE CODCOLIGADA = ?
		 * AND CHAPA = ?
		 * AND INCIRRF = 1
		 * 
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT COUNT(*) AS QTD FROM PFDEPEND ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA = ? ")
			.append("AND INCIRRF = 1 ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idColigada);
			ps.setString(2, chapa);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getInt("QTD");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdDependenteIRRF" }, e);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterQtdDependenteSalFamilia(Integer idColigada, String chapa) throws ApplicationException {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT (COUNT(*) + (SELECT COUNT(*) FROM PFDEPEND ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA = ? ")
			.append("AND INCSALFAM = 1)) AS QTD FROM PFDEPEND ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA = ? ")
			.append("AND DATEDIFF(YEAR, DTNASCIMENTO, GETDATE()) < 14 ")
			.append("AND (GRAUPARENTESCO = '1' OR GRAUPARENTESCO = '3') ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, idColigada);
			ps.setString(2, chapa);
			ps.setLong(3, idColigada);
			ps.setString(4, chapa);
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getInt("QTD");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdDependenteSalFamilia" }, e);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BatidaRM> obterBatidasUsuarioPeriodo(Integer idColigada, String chapa, Date periodoInicial, Date periodoFinal) throws ApplicationException {
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<BatidaRM> batidas = new ArrayList<BatidaRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT [DATA], BATIDA, STATUS, NATUREZA FROM ABATFUN ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA LIKE ? ")
			.append("AND [DATA] BETWEEN ? AND ? ")
			.append("ORDER BY [DATA] DESC, BATIDA ASC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, idColigada);
			ps.setString(2, chapa);
			ps.setDate(3, new java.sql.Date(periodoInicial.getTime()));
			ps.setDate(4, new java.sql.Date(periodoFinal.getTime()));
			
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				BatidaRM batida = new BatidaRM();
				batida.setData(set.getDate("DATA"));
				batida.setBatida(set.getInt("BATIDA"));
				batida.setStatus(set.getString("STATUS"));
				batida.setNatureza(set.getInt("NATUREZA"));
				
				batidas.add(batida);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterBatidasUsuarioPeriodo" }, e);
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
		return batidas;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FeriasRM> obterFeriasFuncionario(Integer idColigada, String chapa) throws ApplicationException {
		
		/** 
		 * SELECT DATAMUDANCA, MOTIVO FROM PFHSTSIT
		 * WHERE CODCOLIGADA = ?
		 * AND CHAPA = ?
		 * AND (MOTIVO LIKE '16' OR MOTIVO LIKE '17')
		 * ORDER BY DATAMUDANCA ASC
		*/
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<FeriasRM> ferias = new ArrayList<FeriasRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT DATAMUDANCA, MOTIVO FROM PFHSTSIT ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA LIKE ? ")
			.append("AND (MOTIVO LIKE '16' OR MOTIVO LIKE '17') ")
			.append("ORDER BY DATAMUDANCA ASC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, idColigada);
			ps.setString(2, chapa);
			
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				FeriasRM f = new FeriasRM();
				f.setData(set.getDate("DATAMUDANCA"));
				f.setMotivo(set.getString("MOTIVO"));
				
				ferias.add(f);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterFeriasFuncionario" }, e);
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
		return ferias;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ColigadaDTO> listarColigadas() {
		Connection conn = null;
		PreparedStatement ps = null;
		List<ColigadaDTO> coligadas = new ArrayList<ColigadaDTO>();

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append(
					"SELECT CODCOLIGADA, NOMEFANTASIA FROM GFILIAL GROUP BY CODCOLIGADA, NOMEFANTASIA ORDER BY CODCOLIGADA");

			ps = conn.prepareStatement(sb.toString());

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				ColigadaDTO dto = new ColigadaDTO();
				dto.setId(Integer.valueOf(set.getInt("CODCOLIGADA")));
				dto.setNome(set.getString("NOMEFANTASIA"));

				coligadas.add(dto);
			}
		} catch (Exception e) {
			LOG.error("Erro ao listar coligadas");
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
		return coligadas;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CentroCustoDTO> listarCentroCusto() {
		Connection conn = null;
		PreparedStatement ps = null;
		List<CentroCustoDTO> centrosCusto = new ArrayList<CentroCustoDTO>();

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append(
					"SELECT CODCCUSTO, NOME FROM GCCUSTO WHERE CODCOLIGADA = 7 ORDER BY CODCCUSTO");

			ps = conn.prepareStatement(sb.toString());

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				CentroCustoDTO dto = new CentroCustoDTO();
				dto.setCodigoCentroCusto(set.getString("CODCCUSTO"));
				dto.setNome(set.getString("NOME"));

				centrosCusto.add(dto);
			}
		} catch (Exception e) {
			LOG.error("Erro ao listar centro de custos");
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
		return centrosCusto;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OrcamentoDTO> listarOrcamento() {
		Connection conn = null;
		PreparedStatement ps = null;
		List<OrcamentoDTO> orcamentos = new ArrayList<OrcamentoDTO>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append(
					"SELECT CODTBORCAMENTO, DESCRICAO FROM TTBORCAMENTO WHERE INATIVO = 0 ORDER BY DESCRICAO");
			
			ps = conn.prepareStatement(sb.toString());
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				OrcamentoDTO dto = new OrcamentoDTO();
				dto.setCodigo(set.getString("CODTBORCAMENTO"));
				dto.setDescricao(set.getString("DESCRICAO"));
				
				orcamentos.add(dto);
			}
		} catch (Exception e) {
			LOG.error("Erro ao listar orçamento");
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
		return orcamentos;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentoDTO> listarAprovacaoPorPeriodo(Integer idColigada, String centroCusto, Date dtInicio, Date dtFim) {
		Connection conn = null;
		PreparedStatement ps = null;
		List<MovimentoDTO> movimentos = new ArrayList<MovimentoDTO>();

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT DISTINCT ")
			.append("FILIAL.CODCOLIGADA AS CODIGO_EMPRESA, ")
			.append("FILIAL.NOME AS EMPRESA, ")
			.append("USUARIO.NOME AS SOLICITANTE, ")
			.append("TMOV.CODUSUARIO AS USRSOLICITANTE, ")
			.append("TMOV.IDMOV AS IDENTIFICADOR_RM, ")
			.append("TMOV.DATAEMISSAO AS DT_EMISSAO, ")
			.append("FORNECEDOR.CODCFO AS CODIGO_FORNECEDOR, ")
			.append("FORNECEDOR.NOME AS FORNECEDOR, ")
			.append("COND_PAGAMENTO.CODCPG AS CODIGO_COND_PAGAMENTO, ")
			.append("COND_PAGAMENTO.NOME AS CONDICAO_PAGAMENTO, ")
			.append("TMOV.VALORBRUTOORIG AS VALOR_TOTAL, ")
			.append("CCUSTO.CODCCUSTO AS CODIGO_CCUSTO, ")
			.append("CCUSTO.NOME AS CCUSTO, ")
			.append("CAST(HISTORICO.HISTORICOLONGO AS VARCHAR(8000)) AS OBSERVACAO, ")
			.append("TMOV.CODMOEVALORLIQUIDO AS MOEDA, ")
			.append("TIPO_MOV.CODTMV AS CODIGO_MOVIMENTO, ")
			.append("'ORDEM DE COMPRA' AS TIPO_MOVIMENTO, ")
			.append("TMOV.STATUS AS STATUS, ")
			.append("MONITOR.SITUACAO AS SITUACAO_APROVACAO, ")
			.append("MONITOR.DATAAPROV AS DATA_ULTIMA_APROVACAO, ")
			.append("CCUSTOCOMPL.LOTACAO AS LOTACAO ")
			.append("FROM ")
			.append("TMOV AS TMOV (NOLOCK) ")
			.append("JOIN GFILIAL  (NOLOCK) AS FILIAL ON (FILIAL.CODCOLIGADA = TMOV.CODCOLIGADA AND FILIAL.CODFILIAL = TMOV.CODFILIAL) ")
			.append("JOIN GCCUSTO (NOLOCK) AS CCUSTO ON (CCUSTO.CODCCUSTO = TMOV.CODCCUSTO) ")
			.append("JOIN FCFO (NOLOCK) AS FORNECEDOR ON (FORNECEDOR.CODCFO = TMOV.CODCFO) ")
			.append("JOIN GUSUARIO (NOLOCK) AS USUARIO ON (USUARIO.CODUSUARIO LIKE TMOV.CODUSUARIO) ")
			.append("JOIN TCPG (NOLOCK) AS COND_PAGAMENTO ON (COND_PAGAMENTO.CODCPG = TMOV.CODCPG AND COND_PAGAMENTO.CODCOLIGADA = TMOV.CODCOLIGADA) ")
			.append("JOIN TMOVHISTORICO (NOLOCK) AS HISTORICO ON (HISTORICO.CODCOLIGADA = TMOV.CODCOLIGADA AND HISTORICO.IDMOV = TMOV.IDMOV) ")
			.append("JOIN TTMV (NOLOCK) AS TIPO_MOV ON (TIPO_MOV.CODTMV = TMOV.CODTMV) ")
			.append("JOIN CCUSTOCOMPL (NOLOCK) AS CCUSTOCOMPL ON (CCUSTOCOMPL.CODCOLIGADA = TMOV.CODCOLIGADA AND CCUSTOCOMPL.CODCCUSTO LIKE TMOV.CODCCUSTO) ")
			.append("JOIN ZMDMONITORAPROVACAO (NOLOCK) AS MONITOR ON (MONITOR.IDMOV = TMOV.IDMOV AND MONITOR.CODCOLIGADA = TMOV.CODCOLIGADA AND MONITOR.IDMOV = TMOV.IDMOV AND MONITOR.CODTMV = TMOV.CODTMV AND MONITOR.DATAAPROV = (SELECT MAX(AUXMON.DATAAPROV) FROM ZMDMONITORAPROVACAO AUXMON WHERE AUXMON.CODTMV = TMOV.CODTMV AND AUXMON.CODCOLIGADA = TMOV.CODCOLIGADA AND AUXMON.IDMOV = TMOV.IDMOV)) ")
			.append("WHERE TMOV.DATAEMISSAO BETWEEN ? AND ? ");
			if (Util.isNotNull(idColigada)) {
				sb.append("AND TMOV.CODCOLIGADA = ? ");
			}
			if(TreatString.isNotBlank(centroCusto)) {
				sb.append("AND CCUSTO.CODCCUSTO LIKE ? ");
			}

			sb.append("UNION ALL ")
			.append("SELECT DISTINCT ")
			.append("FILIAL.CODCOLIGADA AS CODIGO_EMPRESA, ")
			.append("FILIAL.NOME AS EMPRESA, ")
			.append("USUARIO.NOME AS SOLICITANTE, ")
			.append("CNT.CODUSUARIO AS USRSOLICITANTE, ")
			.append("CNT.IDCNT AS IDENTIFICADOR_RM, ")
			.append("CNT.DATACONTRATO AS DT_EMISSAO, ")
			.append("FORNECEDOR.CODCFO AS CODIGO_FORNECEDOR, ")
			.append("FORNECEDOR.NOME AS FORNECEDOR, ")
			.append("COND_PAGAMENTO.CODCPG AS CODIGO_COND_PAGAMENTO, ")
			.append("COND_PAGAMENTO.NOME AS CONDICAO_PAGAMENTO, ")
			.append("CNT.VALORCONTRATO AS VALOR_TOTAL, ")
			.append("CCUSTO.CODCCUSTO AS CODIGO_CCUSTO, ")
			.append("CCUSTO.NOME AS CCUSTO, ")
			.append("CAST(HISTORICO.HISTORICOLONGO AS VARCHAR(8000)) AS OBSERVACAO, ")
			.append("CNT.CODMOEVALORCONTRATO AS MOEDA, ")
			.append("'CONTRATO' AS CODIGO_MOVIMENTO, ")
			.append("'CONTRATO' AS TIPO_MOVIMENTO, ")
			.append("SIT_CONTRATO.DESCRICAO AS STATUS, ")
			.append("MONITOR.SITUACAO AS SITUACAO_APROVACAO, ")
			.append("MONITOR.DATAAPROV AS DATA_ULTIMA_APROVACAO, ")
			.append("CCUSTOCOMPL.LOTACAO AS LOTACAO ")
			.append("FROM ")
			.append("TCNT AS CNT (NOLOCK) ")
			.append("JOIN GFILIAL  (NOLOCK) AS FILIAL ON (FILIAL.CODCOLIGADA = CNT.CODCOLIGADA AND FILIAL.CODFILIAL = CNT.CODFILIAL) ")
			.append("JOIN GCCUSTO (NOLOCK) AS CCUSTO ON (CCUSTO.CODCCUSTO = CNT.CODCCUSTO) ")
			.append("JOIN FCFO (NOLOCK) AS FORNECEDOR ON (FORNECEDOR.CODCFO = CNT.CODCFO) ")
			.append("JOIN GUSUARIO (NOLOCK) AS USUARIO ON (USUARIO.CODUSUARIO LIKE CNT.CODUSUARIO) ")
			.append("JOIN TCPG (NOLOCK) AS COND_PAGAMENTO ON (COND_PAGAMENTO.CODCPG = CNT.CODCPG AND COND_PAGAMENTO.CODCOLIGADA = CNT.CODCOLIGADA) ")
			.append("JOIN TCNTHISTORICO (NOLOCK) AS HISTORICO ON (HISTORICO.CODCOLIGADA = CNT.CODCOLIGADA AND HISTORICO.IDCNT = CNT.IDCNT) ")
			.append("JOIN ZMDMONITORAPROVACAO (NOLOCK) AS MONITOR ON (MONITOR.IDMOV = CNT.IDCNT AND MONITOR.CODTMV LIKE 'CONTRATO' AND MONITOR.CODCOLIGADA = CNT.CODCOLIGADA AND MONITOR.DATAAPROV = (SELECT MAX(AUXMON.DATAAPROV) FROM ZMDMONITORAPROVACAO AUXMON WHERE AUXMON.CODCOLIGADA = CNT.CODCOLIGADA AND AUXMON.IDMOV = CNT.IDCNT)) ")
			.append("JOIN TSTACNT (NOLOCK) AS SIT_CONTRATO ON (SIT_CONTRATO.CODCOLIGADA = CNT.CODCOLIGADA AND SIT_CONTRATO.CODSTACNT LIKE CNT.CODSTACNT) ")
			.append("JOIN CCUSTOCOMPL (NOLOCK) AS CCUSTOCOMPL ON (CCUSTOCOMPL.CODCOLIGADA = CNT.CODCOLIGADA AND CCUSTOCOMPL.CODCCUSTO LIKE CNT.CODCCUSTO) ")
			.append("WHERE CNT.DATACONTRATO BETWEEN ? AND ? ");
			if (Util.isNotNull(idColigada)) {
				sb.append("AND CNT.CODCOLIGADA = ? ");
			}
			if(TreatString.isNotBlank(centroCusto)) {
				sb.append("AND CCUSTO.CODCCUSTO LIKE ? ");
			}
			sb.append("ORDER BY DT_EMISSAO DESC ");

			ps = conn.prepareStatement(sb.toString());

			int idx = 1;
			ps.setDate(idx++, new java.sql.Date(dtInicio.getTime()));
			ps.setDate(idx++, new java.sql.Date(dtFim.getTime()));
			if (Util.isNotNull(idColigada)) {
				ps.setInt(idx++, idColigada.intValue());
			}
			if(TreatString.isNotBlank(centroCusto)) {
				ps.setString(idx++, centroCusto);
			}
			ps.setDate(idx++, new java.sql.Date(dtInicio.getTime()));
			ps.setDate(idx++, new java.sql.Date(dtFim.getTime()));
			if (Util.isNotNull(idColigada)) {
				ps.setInt(idx++, idColigada.intValue());
			}
			if(TreatString.isNotBlank(centroCusto)) {
				ps.setString(idx++, centroCusto);
			}

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				MovimentoDTO dto = new MovimentoDTO();
				dto.setIdColigada(Integer.valueOf(set.getInt("CODIGO_EMPRESA")));
				dto.setNomeEmpresa(set.getString("EMPRESA"));
				dto.setSolicitante(set.getString("SOLICITANTE"));
				dto.setIdMov(Integer.valueOf(set.getInt("IDENTIFICADOR_RM")));
				dto.setDataEmissao(TreatDate.format("dd/MM/yyyy", set.getDate("DT_EMISSAO")));
				dto.setIdFornecedor(set.getString("CODIGO_FORNECEDOR"));
				dto.setFornecedor(set.getString("FORNECEDOR"));
				dto.setIdCondicaoPagamento(set.getString("CODIGO_COND_PAGAMENTO"));
				dto.setCondicaoPagamento(set.getString("CONDICAO_PAGAMENTO"));
				BigDecimal valor = set.getBigDecimal("VALOR_TOTAL");
				dto.setValorTotal(TreatNumber.formatMoney(valor).toString());
				dto.setIdCentroCusto(set.getString("CODIGO_CCUSTO"));
				dto.setCentroCusto(set.getString("CCUSTO"));
				dto.setObservacao(set.getString("OBSERVACAO"));
				dto.setMoeda(set.getString("MOEDA"));
				dto.setIdTipoMovimento(set.getString("CODIGO_MOVIMENTO"));
				dto.setTipoMovimento(set.getString("TIPO_MOVIMENTO"));
				dto.setUsrSolicitante(set.getString("USRSOLICITANTE"));
				dto.setStatus(set.getString("STATUS"));
				dto.setSituacaoMonitor(set.getString("SITUACAO_APROVACAO"));
				dto.setLotacao(set.getString("LOTACAO"));
				Date dtUltimaAprovacao = set.getDate("DATA_ULTIMA_APROVACAO");
				if (Util.isNotNull(dtUltimaAprovacao)) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					dto.setDtUltimaAprovacao(sdf.format(dtUltimaAprovacao));
				}
				movimentos.add(dto);
			}
		} catch (Exception e) {
			LOG.error("Erro ao listar movimento");
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
		return movimentos;
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AprovadorDTO> listarAprovadores(String lotacao) {
		List<AprovadorDTO> listaDTO = new ArrayList<AprovadorDTO>();
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = datasource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ")
			.append("CODCOLIGADA, CODCCUSTO, USRAPROV, VALORDEMOV, VALORATEMOV, VALORDECNT, VALORATECNT ")
			.append("FROM ")
			.append("ZMDAPROVADOR ")
			.append("WHERE CODCCUSTO = ? ")
			.append("UNION ALL ")
			.append("SELECT ")
			.append("CODCOLIGADA, CODCCUSTO, USRAPROV, VALORDEMOV, VALORATEMOV, VALORDECNT, VALORATECNT ")
			.append("FROM ")
			.append("ZMDSEGUNDOAPROV ")
			.append("WHERE CODCCUSTO = ?");

			ps = conn.prepareStatement(sb.toString());

			ps.setString(1, lotacao);
			ps.setString(2, lotacao);

			ResultSet set = ps.executeQuery();

			while (set.next()) {
				AprovadorDTO dto = new AprovadorDTO();
				dto.setIdColigada(Integer.valueOf(set.getInt("CODCOLIGADA")));
				dto.setLotacao(set.getString("CODCCUSTO"));
				dto.setUsuarioAprovacao(set.getString("USRAPROV"));
				dto.setValorInicialMovimento(set.getBigDecimal("VALORDEMOV"));
				dto.setValorFinalMovimento(set.getBigDecimal("VALORATEMOV"));
				dto.setValorInicialContrato(set.getBigDecimal("VALORDECNT"));
				dto.setValorFinalContrato(set.getBigDecimal("VALORATECNT"));

				listaDTO.add(dto);
			}
		} catch (Exception e) {
			LOG.error("Erro ao obter aprovadores");
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
		return listaDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FeriadoRM> obterFeriadosFuncionario(Integer idColigada, String chapa, Date periodoInicial, Date periodoFinal) throws ApplicationException {
		
		/** 
		 * SELECT DIAFERIADO, NOME FROM GFERIADO AS GFERIADO 
		 * WHERE GFERIADO.CODCALENDARIO = 
		 * 	(SELECT PSECAO.CODCALENDARIO FROM PFUNC AS PFUNC
		 * 	LEFT JOIN PSECAO AS PSECAO ON (PSECAO.CODIGO = PFUNC.CODSECAO AND PSECAO.CODCOLIGADA = PFUNC.CODCOLIGADA)
		 * 	WHERE PFUNC.CODCOLIGADA = ?
		 * 	AND PFUNC.CHAPA LIKE ?)
		 * AND GFERIADO.FERIADO LIKE 'T'
		 * AND GFERIADO.DIAFERIADO BETWEEN ? AND ?
		 * ORDER BY GFERIADO.DIAFERIADO ASC
		*/
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<FeriadoRM> feriados = new ArrayList<FeriadoRM>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT GFERIADO.DIAFERIADO, GFERIADO.NOME FROM GFERIADO AS GFERIADO ")
			.append("WHERE GFERIADO.CODCALENDARIO = ")
				.append("(SELECT PSECAO.CODCALENDARIO FROM PFUNC AS PFUNC ")
				.append("LEFT JOIN PSECAO AS PSECAO ON (PSECAO.CODIGO = PFUNC.CODSECAO AND PSECAO.CODCOLIGADA = PFUNC.CODCOLIGADA) ")
				.append("WHERE PFUNC.CODCOLIGADA = ? ")
				.append("AND PFUNC.CHAPA LIKE ?) ")
			.append("AND GFERIADO.FERIADO LIKE 'T' ")
			.append("AND GFERIADO.DIAFERIADO BETWEEN ? AND ? ")
			.append("ORDER BY GFERIADO.DIAFERIADO ASC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, idColigada);
			ps.setString(2, chapa);
			ps.setDate(3, new java.sql.Date(periodoInicial.getTime()));
			ps.setDate(4, new java.sql.Date(periodoFinal.getTime()));
			
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				FeriadoRM f = new FeriadoRM();
				f.setData(set.getDate("DIAFERIADO"));
				f.setNome(set.getString("NOME"));
				
				feriados.add(f);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterFeriadosFuncionario" }, e);
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
		return feriados;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HorasPontoDTO> obterHorasPonto(Integer idColigada, String chapa, Date periodoInicial, Date periodoFinal) throws ApplicationException {
		
		/** 
		 * 	SELECT [DATA], HTRAB, EXTRAEXECUTADO, ATRASO, FALTA, ADICIONAL, ABONO FROM AAFHTFUN
		 *  WHERE CODCOLIGADA = ?
		 *  AND CHAPA = ?
		 * 	AND [DATA] BETWEEN ? AND ?
		 * 	ORDER BY [DATA] ASC
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		List<HorasPontoDTO> horasPonto = new ArrayList<HorasPontoDTO>();
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT [DATA], HTRAB, EXTRAEXECUTADO, ATRASO, FALTA, ADICIONAL, ABONO FROM AAFHTFUN ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA = ? ")
			.append("AND [DATA] BETWEEN ? AND ? ")
			.append("ORDER BY [DATA] ASC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, idColigada);
			ps.setString(2, chapa);
			ps.setDate(3, new java.sql.Date(periodoInicial.getTime()));
			ps.setDate(4, new java.sql.Date(periodoFinal.getTime()));
			
			
			ResultSet set = ps.executeQuery();
			
			while (set.next()) {
				HorasPontoDTO dto = new HorasPontoDTO();
				dto.setData(set.getDate("DATA"));
				dto.setHorasTrabalhada(set.getInt("HTRAB"));
				dto.setHorasExtra(set.getInt("EXTRAEXECUTADO"));
				dto.setHorasAtraso(set.getInt("ATRASO"));
				dto.setHorasFalta(set.getInt("FALTA"));
				dto.setHorasAdicional(set.getInt("ADICIONAL"));
				dto.setHorasAbono(set.getInt("ABONO"));
				
				horasPonto.add(dto);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterHorasPonto" }, e);
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
		return horasPonto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PeriodoPontoDTO obterPeriodoAtualPonto(Integer idColigada, String chapa) throws ApplicationException {
		
		/**
		 * SELECT TOP 1 INICIOPER, FIMPER FROM APERFUN
		 * WHERE CODCOLIGADA = ?
		 * AND CHAPA = ?
		 * ORDER BY ANOCOMP DESC, MESCOMP DESC 
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT TOP 1 INICIOPER, FIMPER FROM APERFUN ")
			.append("WHERE CODCOLIGADA = ? ")
			.append("AND CHAPA = ? ")
			.append("ORDER BY ANOCOMP DESC, MESCOMP DESC ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, idColigada);
			ps.setString(2, chapa);
			
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				PeriodoPontoDTO dto = new PeriodoPontoDTO();
				dto.setPeriodoInicial(set.getDate("INICIOPER"));
				dto.setPeriodoFinal(set.getDate("FIMPER"));
				
				return dto;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterPeriodoAtualPonto" }, e);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Date obterUltimaColetaColigada() throws ApplicationException {
		
		/**
		 * SELECT TOP(1) RECMODIFIEDON FROM GJOBX
		 * WHERE CLASSEPROCESSO = 'PtoProcImportacaoBatidas'
		 * ORDER BY RECMODIFIEDON DESC
		 */
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT TOP(1) RECMODIFIEDON FROM GJOBX ")
			.append("WHERE CLASSEPROCESSO = 'PtoProcImportacaoBatidas' ")
			.append("ORDER BY RECMODIFIEDON DESC ");
			
			ps = conn.prepareStatement(sb.toString());
			
			ResultSet set = ps.executeQuery();
			
			if (set.next()) {
				return set.getTimestamp("RECMODIFIEDON");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterUltimaColetaColigada" }, e);
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
		return null;
	}
	
}
