package br.com.grupojcr.dto;

public class BatidaDTO {
	
	private Integer batida;
	private String status;
	private Integer natureza;
	private Boolean falta;
	private Boolean editado;
	
	public Integer getBatida() {
		return batida;
	}
	public void setBatida(Integer batida) {
		this.batida = batida;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getNatureza() {
		return natureza;
	}
	public void setNatureza(Integer natureza) {
		this.natureza = natureza;
	}
	public Boolean getFalta() {
		return falta;
	}
	public void setFalta(Boolean falta) {
		this.falta = falta;
	}
	public Boolean getEditado() {
		return editado;
	}
	public void setEditado(Boolean editado) {
		this.editado = editado;
	}
	

}
