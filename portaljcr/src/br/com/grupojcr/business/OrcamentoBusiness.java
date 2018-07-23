package br.com.grupojcr.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AjusteOrcamentarioDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.FiltroOrcamento;
import br.com.grupojcr.entity.AjusteOrcamentario;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class OrcamentoBusiness {
	
	private static Logger LOG = Logger.getLogger(OrcamentoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private AjusteOrcamentarioDAO daoAjuste;

	public Integer obterQtdAjusteOrcamentario(FiltroOrcamento filtro) throws ApplicationException {
		try {
			return daoAjuste.obterQtdAjusteOrcamentario(filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdAjusteOrcamentario" }, e);
		}
	}
	
	public List<AjusteOrcamentarioDTO> listarAjusteOrcamentarioPaginado(int first, int pageSize, FiltroOrcamento filtro) throws ApplicationException {
		try {
			List<AjusteOrcamentario> ajustesOrcamentario = daoAjuste.listarAjusteOrcamentarioPaginado(first, pageSize, filtro);
			return null;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarAjusteOrcamentarioPaginado" }, e);
		}
	}
}
