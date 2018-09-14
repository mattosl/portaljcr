package br.com.grupojcr.dto;

import java.util.Date;

public class PeriodoPontoDTO {
	
	private Date periodoInicial;
	private Date periodoFinal;
	
	public Date getPeriodoInicial() {
		return periodoInicial;
	}
	public void setPeriodoInicial(Date periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	public Date getPeriodoFinal() {
		return periodoFinal;
	}
	public void setPeriodoFinal(Date periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

}
