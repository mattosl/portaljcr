package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.AjustePontoDTO;
import br.com.grupojcr.util.TreatDate;
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
	
	@EJB
	private RMBusiness rmBusiness;
	
	public String autenticarUsuario() throws ApplicationException {
		try {
			iniciarAjustePonto();
				
			return "/pages/recursosHumanos/ajustePonto/editar_ajustePonto.xhtml?faces-redirect=true";
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
			carregarPeriodo();
			List<AjustePontoDTO> ponto = rmBusiness.obterBatidasUsuarioPeriodo(7, "000040", getPeriodoInicial(), getPeriodoFinal());
			
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
			Calendar calendarioAtual = Calendar.getInstance();
			Calendar periodoInicial = Calendar.getInstance();
			Calendar periodoFinal = Calendar.getInstance();
			Integer diaMes = calendarioAtual.get(Calendar.DAY_OF_MONTH);
			
			if(diaMes <= 14) {
				periodoInicial.set(Calendar.DAY_OF_MONTH, 15);
				periodoInicial.add(Calendar.MONTH, -2);
				System.out.println("Período Inicial: " + TreatDate.format("dd/MM/yyyy", periodoInicial.getTime()));
				
				periodoFinal.set(Calendar.DAY_OF_MONTH, 14);
				periodoFinal.add(Calendar.MONTH, -1);
				System.out.println("Período Final: " + TreatDate.format("dd/MM/yyyy", periodoFinal.getTime()));
			} else {
				periodoInicial.set(Calendar.DAY_OF_MONTH, 15);
				System.out.println("Período Inicial: " + TreatDate.format("dd/MM/yyyy", periodoInicial.getTime()));
				
				periodoFinal.set(Calendar.DAY_OF_MONTH, 14);
				periodoInicial.add(Calendar.MONTH, +1);
				System.out.println("Período Final: " + TreatDate.format("dd/MM/yyyy", periodoFinal.getTime()));
			}
			
			
			setPeriodoInicial(periodoInicial);
			setPeriodoFinal(periodoFinal);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarPeriodo" }, e);
		}
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

}
