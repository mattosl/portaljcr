package br.com.grupojcr.rm;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseStringEntity;

public class UnidadeRM implements BaseStringEntity, Serializable {
	
	private static final long serialVersionUID = 8107905232953442774L;
	
	private String codigoUnidade;
	private String unidade;
	
	public UnidadeRM() {}
	
	public UnidadeRM(String codigoUnidade, String unidade) {
		this.codigoUnidade = codigoUnidade;
		this.unidade = unidade;
	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoUnidade == null) ? 0 : codigoUnidade.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnidadeRM other = (UnidadeRM) obj;
		if (codigoUnidade == null) {
			if (other.codigoUnidade != null)
				return false;
		} else if (!codigoUnidade.equals(other.codigoUnidade))
			return false;
		return true;
	}

}
