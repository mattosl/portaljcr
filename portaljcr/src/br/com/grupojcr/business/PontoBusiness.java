package br.com.grupojcr.business;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.entity.BatidaPonto;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class PontoBusiness {
	
	private static Logger LOG = Logger.getLogger(PontoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private RMDAO daoRM;
	
	public BatidaPonto salvar() throws ApplicationException {
		try {
			return null;
//		} catch (ApplicationException e) {
//			LOG.info(e.getMessage(), e);
//			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
}
