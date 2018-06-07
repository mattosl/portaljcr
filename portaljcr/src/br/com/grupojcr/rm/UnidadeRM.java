package br.com.grupojcr.rm;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseStringEntity;

public class UnidadeRM implements BaseStringEntity, Serializable {
	
	private static final long serialVersionUID = 8107905232953442774L;
	
	private String codigoUnidade;
	private String unidade;
	
	@Override
	public String getId() {
		return codigoUnidade;
	}

	public String getCodigoUnidade() {
		return codigoUnidade;
	}

	public void setCodigoUnidade(String codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

}
