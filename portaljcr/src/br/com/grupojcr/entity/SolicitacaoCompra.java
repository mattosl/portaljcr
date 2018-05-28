package br.com.grupojcr.entity;

import java.io.Serializable;

public class SolicitacaoCompra implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = 8110893893788100221L;
	
	private Coligada coligada;
	
	private GrupoCotacao grupoCotacao;
	
	private Boolean possuiGrupoCotacao;

	@Override
	public Long getId() {
		return 7L;
	}

	public Coligada getColigada() {
		return coligada;
	}

	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}

	public Boolean getPossuiGrupoCotacao() {
		return possuiGrupoCotacao;
	}

	public void setPossuiGrupoCotacao(Boolean possuiGrupoCotacao) {
		this.possuiGrupoCotacao = possuiGrupoCotacao;
	}

	public GrupoCotacao getGrupoCotacao() {
		return grupoCotacao;
	}

	public void setGrupoCotacao(GrupoCotacao grupoCotacao) {
		this.grupoCotacao = grupoCotacao;
	}

}
