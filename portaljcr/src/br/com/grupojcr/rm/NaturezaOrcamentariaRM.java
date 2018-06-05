package br.com.grupojcr.rm;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseStringEntity;

public class NaturezaOrcamentariaRM implements BaseStringEntity, Serializable {
	
	private static final long serialVersionUID = -5771868673228973943L;
	
	private String codigoNaturezaOrcamentaria;
	private String naturezaOrcamentaria;

	public String getCodigoNaturezaOrcamentaria() {
		return codigoNaturezaOrcamentaria;
	}

	public void setCodigoNaturezaOrcamentaria(String codigoNaturezaOrcamentaria) {
		this.codigoNaturezaOrcamentaria = codigoNaturezaOrcamentaria;
	}

	public String getNaturezaOrcamentaria() {
		return naturezaOrcamentaria;
	}

	public void setNaturezaOrcamentaria(String naturezaOrcamentaria) {
		this.naturezaOrcamentaria = naturezaOrcamentaria;
	}

	@Override
	public String getId() {
		return codigoNaturezaOrcamentaria;
	}
}
