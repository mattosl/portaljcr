package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AjusteOrcamentarioDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.FiltroOrcamento;
import br.com.grupojcr.entity.AjusteOrcamentario;
import br.com.grupojcr.enumerator.Mes;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class OrcamentoBusiness {
	
	private static Logger LOG = Logger.getLogger(OrcamentoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private AjusteOrcamentarioDAO daoAjuste;
	
	@EJB
	private RMDAO daoRM;

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
			List<AjusteOrcamentarioDTO> listaDTO = new ArrayList<AjusteOrcamentarioDTO>();
			if(CollectionUtils.isNotEmpty(ajustesOrcamentario)) {
				for(AjusteOrcamentario ajuste : ajustesOrcamentario) {
					AjusteOrcamentarioDTO dto = new AjusteOrcamentarioDTO();
					dto.setColigada(ajuste.getColigada());
					dto.setCentroCusto(new CentroCustoRM());
					dto.getCentroCusto().setCodigoCentroCusto(ajuste.getCodigoCentroCusto());
					dto.getCentroCusto().setCentroCusto(ajuste.getCentroCusto());
					dto.setNaturezaOrigem(daoRM.obterNaturezaOrcamentaria(ajuste.getIdNaturezaOrigem()));
					dto.setNaturezaDestino(daoRM.obterNaturezaOrcamentaria(ajuste.getIdNaturezaDestino()));
					dto.setValor(ajuste.getValor());
					dto.setMesOrigem(Mes.obterPorCodigo(ajuste.getMesOrigem()));
					dto.setMesDestino(Mes.obterPorCodigo(ajuste.getMesDestino()));
					dto.setDtAjuste(ajuste.getDtAjuste());
					dto.setUsuario(ajuste.getUsuarioAjuste());
					
					listaDTO.add(dto);
				}
			}
			
			return listaDTO;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarAjusteOrcamentarioPaginado" }, e);
		}
	}
}
