package br.com.grupojcr.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.OrcamentoDTO;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.CondicaoPagamentoRM;
import br.com.grupojcr.rm.FornecedorRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;
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
	
	public void ajustarOrcamento(AjusteOrcamentarioDTO ajuste, Usuario usuario) throws ApplicationException {
		try {
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "ajustarOrcamento" }, e);
		}
	}

}
