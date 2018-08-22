package br.com.grupojcr.dto;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.enumerator.Mes;
import br.com.grupojcr.enumerator.PeriodoHolerite;


public class HoleriteDTO {
	
	private Coligada coligada;
	private Integer ano;
	private Mes mes;
	private PeriodoHolerite periodo;
	
	public Coligada getColigada() {
		return coligada;
	}
	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Mes getMes() {
		return mes;
	}
	public void setMes(Mes mes) {
		this.mes = mes;
	}
	public PeriodoHolerite getPeriodo() {
		return periodo;
	}
	public void setPeriodo(PeriodoHolerite periodo) {
		this.periodo = periodo;
	}

}
