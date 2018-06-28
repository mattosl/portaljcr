package br.com.grupojcr.dto;

import java.util.List;

import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.rm.CondicaoPagamentoRM;
import br.com.grupojcr.rm.FornecedorRM;

public class OrdemCompraDTO {

	private CondicaoPagamentoRM condicaoPagamento;
	private FornecedorRM fornecedor;
	private SolicitacaoCompra solicitacaoCompra;
	private String identificadorMovimento;
	private Cotacao cotacao;
	private List<ProdutoDTO> listaProduto;
	
	public FornecedorRM getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(FornecedorRM fornecedor) {
		this.fornecedor = fornecedor;
	}
	public SolicitacaoCompra getSolicitacaoCompra() {
		return solicitacaoCompra;
	}
	public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
		this.solicitacaoCompra = solicitacaoCompra;
	}
	public Cotacao getCotacao() {
		return cotacao;
	}
	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}
	public List<ProdutoDTO> getListaProduto() {
		return listaProduto;
	}
	public void setListaProduto(List<ProdutoDTO> listaProduto) {
		this.listaProduto = listaProduto;
	}
	public CondicaoPagamentoRM getCondicaoPagamento() {
		return condicaoPagamento;
	}
	public void setCondicaoPagamento(CondicaoPagamentoRM condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}
	public String getIdentificadorMovimento() {
		return identificadorMovimento;
	}
	public void setIdentificadorMovimento(String identificadorMovimento) {
		this.identificadorMovimento = identificadorMovimento;
	}

}
