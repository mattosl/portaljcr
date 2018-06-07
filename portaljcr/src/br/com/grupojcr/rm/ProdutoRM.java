package br.com.grupojcr.rm;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseIntegerEntity;

public class ProdutoRM implements BaseIntegerEntity, Serializable {
	
	private static final long serialVersionUID = 8107905232953442774L;
	
	private Integer idProduto;
	private String codigoProduto;
	private String produto;

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}
	
	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
		
	@Override
	public Integer getId() {
		return idProduto;
	}
	
	public String getProdutoFormatado() {
		return codigoProduto + " - " + produto;
	}

}
