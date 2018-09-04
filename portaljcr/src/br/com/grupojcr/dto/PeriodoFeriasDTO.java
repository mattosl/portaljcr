package br.com.grupojcr.dto;

import java.util.Date;

public class PeriodoFeriasDTO {
	
	private Date periodoInicial;
	private Date periodoFinal;
	
	public PeriodoFeriasDTO() {}
	
	public PeriodoFeriasDTO(Date dtInicial, Date dtFinal) {
		this.periodoInicial = dtInicial;
		this.periodoFinal = dtFinal;
	}
	
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
