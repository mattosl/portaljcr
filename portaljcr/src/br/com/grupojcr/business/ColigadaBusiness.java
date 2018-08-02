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
	
	/**
	 * Método responsavel por ativar coligada
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 22/05/2018
	 * @param coligada : Coligada
	 * @throws ApplicationException
	 */
	public void ativar(Coligada coligada) throws ApplicationException {
		try {
			coligada.setSituacao(Boolean.TRUE);
			daoColigada.alterar(coligada);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "ativar" }, e);
		}
	}
	
	/**
	 * Método responsavel por inativar coligada
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 22/05/2018
	 * @param coligada : Coligada
	 * @throws ApplicationException
	 */
	public void inativar(Coligada coligada) throws ApplicationException {
		try {
			coligada.setSituacao(Boolean.FALSE);
			daoColigada.alterar(coligada);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "inativar" }, e);
		}
	}
	
	public List<Coligada> listarColigadasAtivas() throws ApplicationException {
		try {
			return daoColigada.listarColigadasAtivas();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarColigadasAtivas" }, e);
		}
	}

}
