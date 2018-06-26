package br.com.grupojcr.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.rm.CentroCustoRM;
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
	
	public List<ProdutoRM> listarProdutosPorNome(Long idColigada, String nome) throws ApplicationException {
		try {
			return daoRM.listarProdutosPorNome(idColigada, nome);
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

}
