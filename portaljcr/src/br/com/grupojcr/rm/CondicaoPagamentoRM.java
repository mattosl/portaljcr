package br.com.grupojcr.rm;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseStringEntity;

public class CondicaoPagamentoRM implements BaseStringEntity, Serializable {
	
	private static final long serialVersionUID = 4215369266519853586L;
	
	private String codigoCondicaoPagamento;
	private String condicaoPagamento;
	
	public CondicaoPagamentoRM() {}
	
	public CondicaoPagamentoRM(String codigoCondicaoPagamento, String condicaoPagamento) {
		this.codigoCondicaoPagamento = codigoCondicaoPagamento;
		this.condicaoPagamento = condicaoPagamento;
	}

	@Override
	public String getId() {
		return codigoCondicaoPagamento;
	}

	public String getCodigoCondicaoPagamento() {
		return codigoCondicaoPagamento;
	}

	public void setCodigoCondicaoPagamento(String codigoCondicaoPagamento) {
		this.codigoCondicaoPagamento = codigoCondicaoPagamento;
	}

	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

}
