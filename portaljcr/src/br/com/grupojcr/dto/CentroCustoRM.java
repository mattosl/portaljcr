package br.com.grupojcr.dto;

import java.io.Serializable;

import br.com.grupojcr.entity.CentroCustoEntity;

public class CentroCustoRM implements CentroCustoEntity, Serializable {
	
	private static final long serialVersionUID = 8107905232953442774L;
	
	private String codigoCentroCusto;
	private String centroCusto;
	
	public String getCodigoCentroCusto() {
		return codigoCentroCusto;
	}
	public void setCodigoCentroCusto(String codigoCentroCusto) {
		this.codigoCentroCusto = codigoCentroCusto;
	}
	public String getCentroCusto() {
		return centroCusto;
	}
	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

}
