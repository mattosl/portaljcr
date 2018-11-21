package br.com.grupojcr.dto;

public class ProdutoCargaDTO {
	
	private String tipo;
	private String imobilizado;
	private String descricao;
	private String unidade;
	private String codigoNaturezaOrcamentaria;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getImobilizado() {
		return imobilizado;
	}
	public void setImobilizado(String imobilizado) {
		this.imobilizado = imobilizado;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	public String getCodigoNaturezaOrcamentaria() {
		return codigoNaturezaOrcamentaria;
	}
	public void setCodigoNaturezaOrcamentaria(String codigoNaturezaOrcamentaria) {
		this.codigoNaturezaOrcamentaria = codigoNaturezaOrcamentaria;
	}

}
