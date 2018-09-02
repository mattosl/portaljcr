package br.com.grupojcr.rm;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseIntegerEntity;

public class ProdutoRM implements BaseIntegerEntity, Serializable {
	

	private static final long serialVersionUID = 8107905232953442774L;
	
	private Integer idProduto;
	private String codigoProduto;
	private String produto;
	private String codigoReduzido;
	private String codigoNatureza;
	private String codigoUnidadeCompra;
	
	public ProdutoRM() {}

	public ProdutoRM(Integer idProduto, String codigoProduto, String produto) {
		this.idProduto = idProduto;
		this.codigoProduto = codigoProduto;
		this.produto = produto;
	}

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

	public String getCodigoReduzido() {
		return codigoReduzido;
	}

	public void setCodigoReduzido(String codigoReduzido) {
		this.codigoReduzido = codigoReduzido;
	}

	public String getCodigoNatureza() {
		return codigoNatureza;
	}

	public void setCodigoNatureza(String codigoNatureza) {
		this.codigoNatureza = codigoNatureza;
	}

	public String getCodigoUnidadeCompra() {
		return codigoUnidadeCompra;
	}

	public void setCodigoUnidadeCompra(String codigoUnidadeCompra) {
		this.codigoUnidadeCompra = codigoUnidadeCompra;
	}

}
