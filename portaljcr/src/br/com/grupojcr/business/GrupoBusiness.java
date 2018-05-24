package br.com.grupojcr.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.GrupoDAO;
import br.com.grupojcr.entity.Grupo;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class GrupoBusiness {
	
	protected static Logger LOG = Logger.getLogger(GrupoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private GrupoDAO daoGrupo;

	public List<Grupo> listar() throws ApplicationException {
		try {
			return daoGrupo.listar();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listar" }, e);
		}
	}

}
