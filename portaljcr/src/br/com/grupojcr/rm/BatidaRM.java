package br.com.grupojcr.rm;

import java.util.Date;

public class BatidaRM {
	
	private Date data;
	private Integer batida;
	private String status;
	private Integer natureza;
	
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

}
