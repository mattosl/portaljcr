package br.com.grupojcr.dto;

import java.math.BigDecimal;

import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;

public class ProdutoDTO {
	
	private ProdutoRM produto;
	private String descricaoProduto;
	private BigDecimal quantidade;
	private UnidadeRM unidade;
	private String observacao;
	private BigDecimal valorAproximado;
	private Boolean naoEncontrei;
	private CotacaoItem cotacaoItem;
	
	public ProdutoRM getProduto() {
		return produto;
	}
	public void setProduto(ProdutoRM produto) {
		this.produto = produto;
	}
	public BigDecimal getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(BigDecimal quantidade) {
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
	public BigDecimal getValorAproximado() {
		return valorAproximado;
	}
	public void setValorAproximado(BigDecimal valorAproximado) {
		this.valorAproximado = valorAproximado;
	}
	public CotacaoItem getCotacaoItem() {
		return cotacaoItem;
	}
	public void setCotacaoItem(CotacaoItem cotacaoItem) {
		this.cotacaoItem = cotacaoItem;
	}
	

}
