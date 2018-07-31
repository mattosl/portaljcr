package br.com.grupojcr.business;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AjusteOrcamentarioDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.entity.AjusteOrcamentario;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class AjusteOrcamentarioBusiness {
	
	private static Logger LOG = Logger.getLogger(AjusteOrcamentarioBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private AjusteOrcamentarioDAO daoAjusteOrcamentario;
	
	
	public AjusteOrcamentario salvar(AjusteOrcamentarioDTO dto, Usuario usuario) throws ApplicationException {
		try {
			AjusteOrcamentario ajuste = new AjusteOrcamentario();
			ajuste.setCodigoCentroCusto(dto.getCentroCusto().getCodigoCentroCusto());
			ajuste.setCentroCusto(dto.getCentroCusto().getCentroCusto());
			ajuste.setDtAjuste(Calendar.getInstance().getTime());
			ajuste.setIdPeriodo(dto.getPeriodo());
			ajuste.setUsuarioAjuste(usuario);
			ajuste.setColigada(dto.getColigada());
			ajuste.setIdNaturezaOrigem(dto.getNaturezaOrigem().getCodigoNaturezaOrcamentaria());
			ajuste.setIdOrcamentoOrigem(dto.getIdOrcamentoOrigem());
			ajuste.setMesOrigem(dto.getMesOrigem().getId());
			ajuste.setIdNaturezaDestino(dto.getNaturezaDestino().getCodigoNaturezaOrcamentaria());
			ajuste.setIdOrcamentoDestino(dto.getIdOrcamentoDestino());
			ajuste.setMesDestino(dto.getMesDestino().getId());
			ajuste.setValor(dto.getValor());
			
			daoAjusteOrcamentario.incluir(ajuste);
			
			return ajuste;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	

}
