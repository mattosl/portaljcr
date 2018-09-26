package br.com.grupojcr.serialize;

import com.google.gson.annotations.SerializedName;

public class PontoSerialize {
	
	@SerializedName("idAjustePonto")
	private Long idAjustePonto;
	@SerializedName("motivo")
	private String motivo;
	
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Long getIdAjustePonto() {
		return idAjustePonto;
	}
	public void setIdAjustePonto(Long idAjustePonto) {
		this.idAjustePonto = idAjustePonto;
	}

}
