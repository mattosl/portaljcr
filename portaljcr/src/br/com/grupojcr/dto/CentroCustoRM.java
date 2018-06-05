package br.com.grupojcr.dto;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseStringEntity;

public class CentroCustoRM implements BaseStringEntity, Serializable {
	
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
	@Override
	public String getId() {
		return codigoCentroCusto;
	}

}
