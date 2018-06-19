package br.com.grupojcr.dto;

import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;

public class ProdutoDTO {
	
	private ProdutoRM produto;
	private String descricaoProduto;
	private Integer quantidade;
	private UnidadeRM unidade;
	private String observacao;
	private Double valorAproximado;
	private Boolean naoEncontrei;
	
	public ProdutoRM getProduto() {
		return produto;
	}
	public void setProduto(ProdutoRM produto) {
		this.produto = produto;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public UnidadeRM getUnidade() {
		return unidade;
	}
	public void setUnidade(UnidadeRM unidade) {
		this.unidade = unidade;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Boolean getNaoEncontrei() {
		return naoEncontrei;
	}
	public void setNaoEncontrei(Boolean naoEncontrei) {
		this.naoEncontrei = naoEncontrei;
	}
	public String getDescricaoProduto() {
		return descricaoProduto;
	}
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}
	public Double getValorAproximado() {
		return valorAproximado;
	}
	public void setValorAproximado(Double valorAproximado) {
		this.valorAproximado = valorAproximado;
	}
	

}
