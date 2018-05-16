package br.com.grupojcr.dto;

import java.util.Date;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.enumerator.MunicipioIBGE;

public class FiltroConsultaNFSE {

	private Coligada coligada;
	private MunicipioIBGE municipio;
	private Long numeroNota;
	private Date dtInicial;
	private Date dtFinal;
	private Integer situacao;

	public Coligada getColigada() {
		return coligada;
	}

	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}

	public MunicipioIBGE getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioIBGE municipio) {
		this.municipio = municipio;
	}

	public Long getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}
}
