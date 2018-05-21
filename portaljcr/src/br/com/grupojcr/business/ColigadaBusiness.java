package br.com.grupojcr.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.ColigadaDAO;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class ColigadaBusiness {
	
	private static Logger LOG = Logger.getLogger(ColigadaBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private ColigadaDAO daoColigada;
	
	/**
	 * Método responsavel por listar coligadas ativas
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @return List<Coligada>
	 * @throws ApplicationException
	 */
	public List<Coligada> listarColigadas() throws ApplicationException {
		try {
			return daoColigada.listar();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarColigadas" }, e);
		}
	}

}
