package br.com.grupojcr.serialize;

import com.google.gson.annotations.SerializedName;

public class SolicitacaoCompraSerialize {
	
	@SerializedName("idSolicitacao")
	private Long idSolicitacao;
	@SerializedName("motivo")
	private String motivo;
	@SerializedName("observacao")
	private String observacao;
	
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Long getIdSolicitacao() {
		return idSolicitacao;
	}
	public void setIdSolicitacao(Long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
