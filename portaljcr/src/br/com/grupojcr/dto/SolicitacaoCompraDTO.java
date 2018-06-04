package br.com.grupojcr.dto;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.Usuario;

public class SolicitacaoCompraDTO {
	
	private Coligada coligada;
	private Boolean possuiGrupoCotacao;
	private GrupoCotacao grupoCotacao;
	private CentroCustoRM centroCusto;
	private Usuario usuarioCotacao;
	
	
	public Coligada getColigada() {
		return coligada;
	}
	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}
	public GrupoCotacao getGrupoCotacao() {
		return grupoCotacao;
	}
	public void setGrupoCotacao(GrupoCotacao grupoCotacao) {
		this.grupoCotacao = grupoCotacao;
	}
	public CentroCustoRM getCentroCusto() {
		return centroCusto;
	}
	public void setCentroCusto(CentroCustoRM centroCusto) {
		this.centroCusto = centroCusto;
	}
	public Boolean getPossuiGrupoCotacao() {
		return possuiGrupoCotacao;
	}
	public void setPossuiGrupoCotacao(Boolean possuiGrupoCotacao) {
		this.possuiGrupoCotacao = possuiGrupoCotacao;
	}
	public Usuario getUsuarioCotacao() {
		return usuarioCotacao;
	}
	public void setUsuarioCotacao(Usuario usuarioCotacao) {
		this.usuarioCotacao = usuarioCotacao;
	}

}
