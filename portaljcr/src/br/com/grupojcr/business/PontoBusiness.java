package br.com.grupojcr.business;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

import br.com.grupojcr.dao.AjustePontoDAO;
import br.com.grupojcr.dao.BatidaPontoDAO;
import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.dto.AjustePontoDTO;
import br.com.grupojcr.dto.BatidaDTO;
import br.com.grupojcr.entity.AjustePonto;
import br.com.grupojcr.entity.BatidaPonto;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.SituacaoAjustePonto;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class PontoBusiness {
	
	private static Logger LOG = Logger.getLogger(PontoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private AjustePontoDAO daoAjustePonto;

	@EJB
	private BatidaPontoDAO daoBatidaPonto;
	
	@EJB
	private UsuarioDAO daoUsuario;
	
	public BatidaPonto salvar(Usuario usuario, Date periodoInicial, Date periodoFinal, String codsecao, String secao, BatidaPonto batida, AjustePontoDTO ajustePontoDTO) throws ApplicationException {
		try {
			AjustePonto ajustePonto = daoAjustePonto.obterAjustePonto(usuario.getId(), periodoInicial, periodoFinal);
			
			if(Util.isNull(ajustePonto)) {
				ajustePonto = new AjustePonto();
				ajustePonto.setUsuario(usuario);
				ajustePonto.setChapa(usuario.getChapa());
				ajustePonto.setDtAjuste(Calendar.getInstance().getTime());
				ajustePonto.setDtPeriodoInicial(periodoInicial);
				ajustePonto.setDtPeriodoFinal(periodoFinal);
				ajustePonto.setSituacao(SituacaoAjustePonto.RASCUNHO);
				ajustePonto.setCodigoSecao(codsecao);
				ajustePonto.setSecao(secao);
				
				daoAjustePonto.incluir(ajustePonto);
			}
			
			BatidaPonto batidaExiste = daoBatidaPonto.obterBatida(batida.getDtBatida(), batida.getBatida(), ajustePonto.getId());
			if(Util.isNotNull(batidaExiste)) {
				throw new ApplicationException("ajuste.ponto.batida.existe", FacesMessage.SEVERITY_WARN);
			}
			
			if(validarExisteBatida(ajustePontoDTO, batida.getBatida())) {
				throw new ApplicationException("ajuste.ponto.batida.existe", FacesMessage.SEVERITY_WARN);
			}
			
			batida.setAjuste(ajustePonto);
			
			if(Util.isNotNull(batida.getId())) {
				daoBatidaPonto.alterar(batida);
			} else {
				daoBatidaPonto.incluir(batida);
			}
			
			return batida;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	public Boolean validarExisteBatida(AjustePontoDTO ajustePontoDTO, Integer batida) throws ApplicationException {
		try {
			if(Util.isNotNull(ajustePontoDTO)) {
				for(Integer key : ajustePontoDTO.getBatidas().keySet()) {
					BatidaDTO batidaDTO = ajustePontoDTO.getBatidas().get(key);
					if(Util.isNotNull(batidaDTO.getBatida())) {
						if(batidaDTO.getBatida().equals(batida)) {
							return Boolean.TRUE;
						}
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "validarExisteBatida" }, e);
		}
	}
	
}
