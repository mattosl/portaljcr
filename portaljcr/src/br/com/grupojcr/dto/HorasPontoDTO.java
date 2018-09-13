package br.com.grupojcr.dto;

import java.util.Date;

public class HorasPontoDTO {
	
	private Date data;
	private Integer horasTrabalhada;
	private Integer horasExtra;
	private Integer horasFalta;
	private Integer horasAtraso;
	private Integer horasAdicional;
	private Integer horasAbono;
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Integer getHorasTrabalhada() {
		return horasTrabalhada;
	}
	public void setHorasTrabalhada(Integer horasTrabalhada) {
		this.horasTrabalhada = horasTrabalhada;
	}
	public Integer getHorasExtra() {
		return horasExtra;
	}
	public void setHorasExtra(Integer horasExtra) {
		this.horasExtra = horasExtra;
	}
	public Integer getHorasAtraso() {
		return horasAtraso;
	}
	public void setHorasAtraso(Integer horasAtraso) {
		this.horasAtraso = horasAtraso;
	}
	public Integer getHorasAdicional() {
		return horasAdicional;
	}
	public void setHorasAdicional(Integer horasAdicional) {
		this.horasAdicional = horasAdicional;
	}
	public Integer getHorasAbono() {
		return horasAbono;
	}
	public void setHorasAbono(Integer horasAbono) {
		this.horasAbono = horasAbono;
	}
	public Integer getHorasFalta() {
		return horasFalta;
	}
	public void setHorasFalta(Integer horasFalta) {
		this.horasFalta = horasFalta;
	}

}
