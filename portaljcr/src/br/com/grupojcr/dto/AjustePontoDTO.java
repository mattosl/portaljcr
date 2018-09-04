package br.com.grupojcr.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AjustePontoDTO {
	
	private Date data;
	private String nomeDia;
	
	private Map<Integer, Calendar> batidas;
	
	private Integer horasTrabalhadas;
	private Integer horasExcedente;
	private Integer horasAtraso;
	private Integer horasFalta;
	private Integer horasAdicionalNoturno;
	private Integer horasAbono;
	
	private Boolean ferias;
	private Boolean feriado;
	private Boolean finalSemana;
	private Boolean faltaBatida;
	
	public AjustePontoDTO() {
		this.finalSemana = Boolean.FALSE;
		this.ferias = Boolean.FALSE;
		this.feriado = Boolean.FALSE;
		this.faltaBatida = Boolean.FALSE;
	}

	public Boolean getFerias() {
		return ferias;
	}

	public void setFerias(Boolean ferias) {
		this.ferias = ferias;
	}

	public Boolean getFeriado() {
		return feriado;
	}

	public void setFeriado(Boolean feriado) {
		this.feriado = feriado;
	}

	public Boolean getFinalSemana() {
		return finalSemana;
	}

	public void setFinalSemana(Boolean finalSemana) {
		this.finalSemana = finalSemana;
	}

	public Boolean getFaltaBatida() {
		return faltaBatida;
	}

	public void setFaltaBatida(Boolean faltaBatida) {
		this.faltaBatida = faltaBatida;
	}

	public Map<Integer, Calendar> getBatidas() {
		return batidas;
	}

	public void setBatidas(Map<Integer, Calendar> batidas) {
		this.batidas = batidas;
	}

	public Integer getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(Integer horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public Integer getHorasExcedente() {
		return horasExcedente;
	}

	public void setHorasExcedente(Integer horasExcedente) {
		this.horasExcedente = horasExcedente;
	}

	public Integer getHorasAtraso() {
		return horasAtraso;
	}

	public void setHorasAtraso(Integer horasAtraso) {
		this.horasAtraso = horasAtraso;
	}

	public Integer getHorasFalta() {
		return horasFalta;
	}

	public void setHorasFalta(Integer horasFalta) {
		this.horasFalta = horasFalta;
	}

	public Integer getHorasAdicionalNoturno() {
		return horasAdicionalNoturno;
	}

	public void setHorasAdicionalNoturno(Integer horasAdicionalNoturno) {
		this.horasAdicionalNoturno = horasAdicionalNoturno;
	}

	public Integer getHorasAbono() {
		return horasAbono;
	}

	public void setHorasAbono(Integer horasAbono) {
		this.horasAbono = horasAbono;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNomeDia() {
		return nomeDia;
	}

	public void setNomeDia(String nomeDia) {
		this.nomeDia = nomeDia;
	}

}
