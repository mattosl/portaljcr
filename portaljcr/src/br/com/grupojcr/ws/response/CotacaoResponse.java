package br.com.grupojcr.ws.response;

import java.util.List;

public class CotacaoResponse {
	
	private Long idCotacao;
	private Boolean melhorOpcao;
	private String fornecedor;
	private String valorTotalCotacao;
	
	private List<CotacaoResponse> listaItens;
	
	private String produtoServico;
	private Integer quantidade;
	private String unidade;
	private String valorUnitario;
	private String valorTotalItem;
	private String valorFrete;
	
	public String getProdutoServico() {
		return produtoServico;
	}
	public void setProdutoServico(String produtoServico) {
		this.produtoServico = produtoServico;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public String getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public String getValorTotalItem() {
		return valorTotalItem;
	}
	public void setValorTotalItem(String valorTotalItem) {
		this.valorTotalItem = valorTotalItem;
	}
	public String getValorFrete() {
		return valorFrete;
	}
	public void setValorFrete(String valorFrete) {
		this.valorFrete = valorFrete;
	}
	public String getValorTotalCotacao() {
		return valorTotalCotacao;
	}
	public void setValorTotalCotacao(String valorTotalCotacao) {
		this.valorTotalCotacao = valorTotalCotacao;
	}
	public Boolean getMelhorOpcao() {
		return melhorOpcao;
	}
	public void setMelhorOpcao(Boolean melhorOpcao) {
		this.melhorOpcao = melhorOpcao;
	}
	public Long getIdCotacao() {
		return idCotacao;
	}
	public void setIdCotacao(Long idCotacao) {
		this.idCotacao = idCotacao;
	}
	public List<CotacaoResponse> getListaItens() {
		return listaItens;
	}
	public void setListaItens(List<CotacaoResponse> listaItens) {
		this.listaItens = listaItens;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

}
