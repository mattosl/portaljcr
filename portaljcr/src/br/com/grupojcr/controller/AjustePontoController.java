package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.AjustePontoDTO;
import br.com.grupojcr.dto.BatidaDTO;
import br.com.grupojcr.dto.PeriodoPontoDTO;
import br.com.grupojcr.rm.FuncionarioRM;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class AjustePontoController implements Serializable {
	
	private static final long serialVersionUID = -8044745000788576246L;
	protected static Logger LOG = Logger.getLogger(AjustePontoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private String chapa;
	private String chave;
	
	private Calendar periodoInicial;
	private Calendar periodoFinal;
	private Date ultimaColeta;
	
	private FuncionarioRM funcionario;
	
	private List<AjustePontoDTO> pontos;
	
	@EJB
	private RMBusiness rmBusiness;
	
	
	public String autenticarUsuario() throws ApplicationException {
		try {
			iniciarAjustePonto();
				
			return "/pages/recursosHumanos/ajustarPonto/editar_ajustarPonto.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			setChapa(null);
			setChave(null);
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autenticarUsuario" }, e);
		}
	}
	
	private void iniciarAjustePonto() throws ApplicationException {
		try {
			setFuncionario(rmBusiness.obterDadosFuncionario("7-000040"));
			carregarPeriodo();
			setUltimaColeta(rmBusiness.obterUltimaColetaColigada(7));
			List<AjustePontoDTO> ponto = rmBusiness.obterBatidasUsuarioPeriodo(7, "000040", getPeriodoInicial(), getPeriodoFinal());
			
			setPontos(ponto);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarAjustePonto" }, e);
		}
	}
	
	private void carregarPeriodo() throws ApplicationException {
		try {
			PeriodoPontoDTO dto = rmBusiness.obterPeriodoAtualFuncionario(7, "000040");
			Calendar periodoInicial = Calendar.getInstance();
			periodoInicial.setTime(dto.getPeriodoInicial());
			Calendar periodoFinal = Calendar.getInstance();
			periodoFinal.setTime(dto.getPeriodoFinal());
			
			setPeriodoInicial(periodoInicial);
			setPeriodoFinal(periodoFinal);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarPeriodo" }, e);
		}
	}
	
	public BatidaDTO obterObjetoMap(HashMap<Integer, BatidaDTO> hash, Integer key) throws ApplicationException {
		try {
			if(Util.isNotNull(hash)) {
				BatidaDTO batida = hash.get(key);
				if(Util.isNotNull(batida)) {
					return batida;
				}
			}
			return new BatidaDTO();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterObjetoMap" }, e);
		}
	}
	
	public Boolean podeEditar(BatidaDTO batida, Integer seq, AjustePontoDTO ponto) throws ApplicationException {
		try {
			if(TreatNumber.isNullOrZero(batida.getBatida())) {
				if(!ponto.getFerias()) {
					if(Util.dataMenor(ponto.getData(), getUltimaColeta())) {
						return Boolean.TRUE;
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "podeEditar" }, e);
		}
	}
	
	public String obterHoras(BatidaDTO batida) throws ApplicationException {
		try {
			if(Util.isNotNull(batida)) {
				if(TreatNumber.isNotNullOrZero(batida.getBatida())) {
					Integer hora = batida.getBatida() / 60;
					Integer minuto = batida.getBatida() % 60;
					
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, hora);
					calendar.set(Calendar.MINUTE, minuto);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					
					return TreatDate.format("HH:mm", calendar.getTime());
				}
			}
			return "";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterHoras" }, e);
		}
	}
	
	public String obterHorasBatida(Integer batida) throws ApplicationException {
		try {
			if(TreatNumber.isNotNullOrZero(batida)) {
				Integer hora = batida / 60;
				Integer minuto = batida % 60;
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, hora);
				calendar.set(Calendar.MINUTE, minuto);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				
				return TreatDate.format("HH:mm", calendar.getTime());
			}
			return "";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterHorasBatida" }, e);
		}
	}
	
	public String obterPeriodoFormatado() {
		return TreatDate.format("dd/MM/yyyy", getPeriodoInicial().getTime()) + " à " + TreatDate.format("dd/MM/yyyy", getPeriodoFinal().getTime()); 
	}
	
	public String obterUltimaColeta() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getUltimaColeta());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return TreatDate.format("dd/MM/yyyy HH:mm", calendar.getTime()); 
	}

	public String getChapa() {
		return chapa;
	}

	public void setChapa(String chapa) {
		this.chapa = chapa;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Calendar getPeriodoInicial() {
		return periodoInicial;
	}

	public void setPeriodoInicial(Calendar periodoInicial) {
		this.periodoInicial = periodoInicial;
	}

	public Calendar getPeriodoFinal() {
		return periodoFinal;
	}

	public void setPeriodoFinal(Calendar periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

	public List<AjustePontoDTO> getPontos() {
		return pontos;
	}

	public void setPontos(List<AjustePontoDTO> pontos) {
		this.pontos = pontos;
	}

	public Date getUltimaColeta() {
		return ultimaColeta;
	}

	public void setUltimaColeta(Date ultimaColeta) {
		this.ultimaColeta = ultimaColeta;
	}

	public FuncionarioRM getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioRM funcionario) {
		this.funcionario = funcionario;
	}

}
