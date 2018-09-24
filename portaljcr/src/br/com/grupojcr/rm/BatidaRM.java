package br.com.grupojcr.rm;

import java.util.Date;

import br.com.grupojcr.entity.BatidaPonto;

public class BatidaRM {
	
	private Date data;
	private Integer batida;
	private String status;
	private Integer natureza;
	private Boolean editado = Boolean.FALSE;
	private BatidaPonto batidaPonto;
	
	public BatidaRM() {}
	
	public BatidaRM(Date data, Integer batida, String status, Integer natureza, Boolean editado, BatidaPonto batidaPonto) {
		this.data = data;
		this.batida = batida;
		this.status = status;
		this.natureza = natureza;
		this.editado = editado;
		this.batidaPonto = batidaPonto;
	}
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
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

	public Boolean getEditado() {
		return editado;
	}

	public void setEditado(Boolean editado) {
		this.editado = editado;
	}

	public BatidaPonto getBatidaPonto() {
		return batidaPonto;
	}

	public void setBatidaPonto(BatidaPonto batidaPonto) {
		this.batidaPonto = batidaPonto;
	}

}
