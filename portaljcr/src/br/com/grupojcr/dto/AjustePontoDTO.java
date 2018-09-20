package br.com.grupojcr.dto;

import java.util.Date;
import java.util.HashMap;

public class AjustePontoDTO {
	
	private Date data;
	private String nomeDia;
	
	private HashMap<Integer, BatidaDTO> batidas;
	
	private Integer horasTrabalhadas;
	private Integer horasExtra;
	private Integer horasAtraso;
	private Integer horasFalta;
	private Integer horasAdicionalNoturno;
	private Integer horasAbono;
	
	private Boolean ferias;
	private Boolean feriado;
	private Boolean finalSemana;
	private Boolean faltaBatida;
	private Boolean hoje;
	private Boolean atencao;
	
	private Integer ultimoPontoFalta;
	
	public AjustePontoDTO() {
		this.finalSemana = Boolean.FALSE;
		this.ferias = Boolean.FALSE;
		this.feriado = Boolean.FALSE;
		this.faltaBatida = Boolean.FALSE;
		this.hoje = Boolean.FALSE;
		this.atencao = Boolean.FALSE;
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

	public HashMap<Integer, BatidaDTO> getBatidas() {
		return batidas;
	}

	public void setBatidas(HashMap<Integer, BatidaDTO> batidas) {
		this.batidas = batidas;
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

	public Integer getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(Integer horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
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

	public Boolean getHoje() {
		return hoje;
	}

	public void setHoje(Boolean hoje) {
		this.hoje = hoje;
	}

	public Integer getUltimoPontoFalta() {
		return ultimoPontoFalta;
	}

	public void setUltimoPontoFalta(Integer ultimoPontoFalta) {
		this.ultimoPontoFalta = ultimoPontoFalta;
	}

	public Boolean getAtencao() {
		return atencao;
	}

	public void setAtencao(Boolean atencao) {
		this.atencao = atencao;
	}



}
