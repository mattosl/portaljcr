package br.com.grupojcr.business;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.OrcamentoDTO;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.CondicaoPagamentoRM;
import br.com.grupojcr.rm.FornecedorRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.totvs.www.br.WsDataServerLocator;
import br.com.totvs.www.br.WsDataServerSoapStub;

@Stateless
public class RMBusiness {
	
	private static Logger LOG = Logger.getLogger(RMBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private RMDAO daoRM;
	
	
	public List<CentroCustoRM> listarCentroCustoPorColigada(Long idColigada) throws ApplicationException {
		try {
			return daoRM.listaCentroCustoPorColigada(idColigada);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarCentroCustoPorColigada" }, e);
		}
	}
	
	public List<NaturezaOrcamentariaRM> listarNaturezaOrcamentaria() throws ApplicationException {
		try {
			return daoRM.listarNaturezaOrcamentaria();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarNaturezaOrcamentaria" }, e);
		}
	}
	
	public List<ProdutoRM> listarProdutosPorNome(Long idColigada, String nome, Modalidade modalidade) throws ApplicationException {
		try {
			return daoRM.listarProdutosPorNome(idColigada, nome, modalidade);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarProdutosPorNome" }, e);
		}
	}

	public List<FornecedorRM> listarFornecedorPorNome(String nome) throws ApplicationException {
		try {
			return daoRM.listarFornecedorPorNome(nome);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarFornecedorPorNome" }, e);
		}
	}
	
	public List<UnidadeRM> listarUnidade() throws ApplicationException {
		try {
			return daoRM.listarUnidade();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarUnidades" }, e);
		}
	}
	
	public List<CondicaoPagamentoRM> listarCondicaoPagamento(Long idColigada) throws ApplicationException {
		try {
			return daoRM.listarCondicaoPagamento(idColigada);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarCondicaoPagamento" }, e);
		}
	}
	
	public Boolean existeUsuario(String nomeUsuario) throws ApplicationException {
		try {
			return daoRM.existeUsuario(nomeUsuario.trim());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "existeUsuario" }, e);
		}
	}
	
	public void atualizaCampos(String idColigada, String idMov, String usuario) throws ApplicationException {
		try {
			daoRM.atualizarMovimento(idColigada, idMov, usuario);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "atualizaCampos" }, e);
		}
	}
	
	private WsDataServerSoapStub obterProxyWsDataServerSoapStub() throws ServiceException {
		try {
			WsDataServerLocator locator = new WsDataServerLocator();
			WsDataServerSoapStub cliente = (WsDataServerSoapStub) locator
					.getwsDataServerSoap();
			return cliente;
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw e;
		}
	}
	
	public String readRecordAuth(String dataServerName, String primaryKey, String contexto, String usuario, String senha) throws ApplicationException {
		try {
			WsDataServerSoapStub cliente = obterProxyWsDataServerSoapStub();
			
			return cliente.readRecordAuth(dataServerName, primaryKey, contexto, usuario, senha);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "readRecordAuth" }, e);
		}
	}
	
	public String saveRecordAuth(String dataServerName, String xml, String contexto, String usuario, String senha) throws ApplicationException {
		try {
			WsDataServerSoapStub cliente = obterProxyWsDataServerSoapStub();
			
			return cliente.saveRecordAuth(dataServerName, xml, contexto, usuario, senha);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "saveRecordAuth" }, e);
		}
	}
	
	public Integer obterIdFluig(Long idColigada, Long idMovimento) throws ApplicationException {
		try {
			return daoRM.obterIdFluig(idMovimento);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterIdFluig" }, e);
		}
	}
	
	public Integer obterPeriodoColigada(Long idColigada) throws ApplicationException {
		try {
			return daoRM.obterPeriodoOrcamentoColigada(idColigada);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterPeriodoColigada" }, e);
		}
	}
	
	public Integer obterOrcamento(Integer periodo, Long idColigada, String idNatureza, String idCentroCusto) throws ApplicationException {
		try {
			return daoRM.obterOrcamento(periodo, idColigada, idNatureza, idCentroCusto);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterOrcamento" }, e);
		}
	}

	public OrcamentoDTO obterOrcamentoCompleto(Integer periodo, Long idColigada, Integer idOrcamento, Integer mes) throws ApplicationException {
		try {
			return daoRM.obterOrcamentoCompleto(periodo, idColigada, idOrcamento, mes);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterOrcamentoCompleto" }, e);
		}
	}
	
	public void ajustarOrcamento(AjusteOrcamentarioDTO ajuste) throws ApplicationException {
		try {
			Integer idOrcamentoOrigem = daoRM.obterOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), ajuste.getNaturezaOrigem().getCodigoNaturezaOrcamentaria(), ajuste.getCentroCusto().getCodigoCentroCusto());
			if(Util.isNotNull(idOrcamentoOrigem)) {
				OrcamentoDTO dto = daoRM.obterOrcamentoCompleto(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoOrigem, ajuste.getMesOrigem().getId());
				if(Util.isNull(dto)) {
					daoRM.incluirItemOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoOrigem, ajuste.getMesOrigem().getId());
				}
				daoRM.cederValor(ajuste.getValor(), ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoOrigem, ajuste.getMesOrigem().getId());
				ajuste.setIdOrcamentoOrigem(idOrcamentoOrigem);
				
			} else {
				Calendar calendarioInicial = Calendar.getInstance();
				calendarioInicial.set(Calendar.MONTH, (ajuste.getMesOrigem().getId() - 1));
				calendarioInicial.set(Calendar.DAY_OF_MONTH, calendarioInicial.getActualMinimum(Calendar.DAY_OF_MONTH));
				Calendar calendarioFinal = Calendar.getInstance();
				calendarioFinal.set(Calendar.MONTH, (ajuste.getMesOrigem().getId() - 1));
				calendarioFinal.set(Calendar.DAY_OF_MONTH, calendarioFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				String xml = "<MovOrcamento>" +
								"<TORCAMENTO>" +
								    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
								    "<IDORCAMENTO>0</IDORCAMENTO>" +
								    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
								    "<CODCCUSTO>" + ajuste.getCentroCusto().getCodigoCentroCusto() + "</CODCCUSTO>" +
								    "<CODTBORCAMENTO>" + ajuste.getNaturezaOrigem().getCodigoNaturezaOrcamentaria() + "</CODTBORCAMENTO>" +
							  "</TORCAMENTO>" +
							  "<TITMORCAMENTO>" +
								    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
								    "<IDORCAMENTO>0</IDORCAMENTO>" +
								    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
								    "<IDITMPERIODO>" + ajuste.getMesOrigem().getId() + "</IDITMPERIODO>" +
								    "<VALORORCADO>0,00</VALORORCADO>" +
								    "<VALORREAL>0,00</VALORREAL>" +
								    "<VALOROPCIONAL1>0,00</VALOROPCIONAL1>" +
								    "<VALOROPCIONAL2>0,00</VALOROPCIONAL2>" +
								    "<VALORRECEBIDO>0,00</VALORRECEBIDO>" +
								    "<VALORCEDIDO>" + TreatNumber.formatMoney(ajuste.getValor()) + "</VALORCEDIDO>" +
								    "<VALOREXCEDENTE>0,00</VALOREXCEDENTE>" +
								    "<DATAINICIO>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioInicial.getTime()) + "</DATAINICIO>" +
								    "<DATAFIM>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioFinal.getTime()) + "</DATAFIM>" +
							  "</TITMORCAMENTO>";
				
				String retorno = saveRecordAuth("MovOrcamentoData", xml, "CODCOLIGADA=" + ajuste.getColigada().getId() +";CODSISTEMA=T;CODUSUARIO=portaljcr", "portaljcr", "portaljcr-123");
				String [] arrayRetorno = retorno.split(";");
				if(arrayRetorno.length == 2) {
					ajuste.setIdOrcamentoOrigem(Integer.valueOf(arrayRetorno[1]));
				} else {
					throw new ApplicationException("message.empty", new String[] {arrayRetorno[0]}, FacesMessage.SEVERITY_WARN);
				}
			}
			Integer idOrcamentoDestino = daoRM.obterOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), ajuste.getNaturezaDestino().getCodigoNaturezaOrcamentaria(), ajuste.getCentroCusto().getCodigoCentroCusto());
			if(Util.isNotNull(idOrcamentoDestino)) {
				OrcamentoDTO dto = daoRM.obterOrcamentoCompleto(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoDestino, ajuste.getMesDestino().getId());
				if(Util.isNull(dto)) {
					daoRM.incluirItemOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoDestino, ajuste.getMesDestino().getId());
				}
				daoRM.receberValor(ajuste.getValor(), ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoDestino, ajuste.getMesDestino().getId());
				ajuste.setIdOrcamentoDestino(idOrcamentoDestino);
			} else {
				Calendar calendarioInicial = Calendar.getInstance();
				calendarioInicial.set(Calendar.MONTH, (ajuste.getMesDestino().getId() - 1));
				calendarioInicial.set(Calendar.DAY_OF_MONTH, calendarioInicial.getActualMinimum(Calendar.DAY_OF_MONTH));
				Calendar calendarioFinal = Calendar.getInstance();
				calendarioFinal.set(Calendar.MONTH, (ajuste.getMesDestino().getId() - 1));
				calendarioFinal.set(Calendar.DAY_OF_MONTH, calendarioFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				String xml = "<MovOrcamento>" +
								"<TORCAMENTO>" +
								    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
								    "<IDORCAMENTO>0</IDORCAMENTO>" +
								    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
								    "<CODCCUSTO>" + ajuste.getCentroCusto().getCodigoCentroCusto() + "</CODCCUSTO>" +
								    "<CODTBORCAMENTO>" + ajuste.getNaturezaDestino().getCodigoNaturezaOrcamentaria() + "</CODTBORCAMENTO>" +
							  "</TORCAMENTO>" +
							  "<TITMORCAMENTO>" +
								    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
								    "<IDORCAMENTO>0</IDORCAMENTO>" +
								    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
								    "<IDITMPERIODO>" + ajuste.getMesDestino().getId() + "</IDITMPERIODO>" +
								    "<VALORORCADO>0,00</VALORORCADO>" +
								    "<VALORREAL>0,00</VALORREAL>" +
								    "<VALOROPCIONAL1>0,00</VALOROPCIONAL1>" +
								    "<VALOROPCIONAL2>0,00</VALOROPCIONAL2>" +
								    "<VALORRECEBIDO>" + TreatNumber.formatMoney(ajuste.getValor()) + "</VALORRECEBIDO>" +
								    "<VALORCEDIDO>0,00</VALORCEDIDO>" +
								    "<VALOREXCEDENTE>0,00</VALOREXCEDENTE>" +
								    "<DATAINICIO>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioInicial.getTime()) + "</DATAINICIO>" +
								    "<DATAFIM>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioFinal.getTime()) + "</DATAFIM>" +
							  "</TITMORCAMENTO>";
				
				String retorno = saveRecordAuth("MovOrcamentoData", xml, "CODCOLIGADA=" + ajuste.getColigada().getId() +";CODSISTEMA=T;CODUSUARIO=portaljcr", "portaljcr", "portaljcr-123");
				
				String [] arrayRetorno = retorno.split(";");
				if(arrayRetorno.length == 2) {
					ajuste.setIdOrcamentoDestino(Integer.valueOf(arrayRetorno[1]));
				} else {
					throw new ApplicationException("message.empty", new String[] {arrayRetorno[0]}, FacesMessage.SEVERITY_WARN);
				}
				
			}
		} catch (ApplicationException e) {
			LOG.info(e.getStackTrace(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "ajustarOrcamento" }, e);
		}
	}

}
