package br.com.grupojcr.rm;

import java.math.BigDecimal;
import java.util.Date;

public class HoleriteItensRM {
	
	private Date dtPagamento;
	private String codigo;
	private String descricao;
	private BigDecimal referencia;
	private BigDecimal provento;
	private BigDecimal desconto;
	
	public Date getDtPagamento() {
		return dtPagamento;
	}
	public void setDtPagamento(Date dtPagamento) {
		this.dtPagamento = dtPagamento;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getReferencia() {
		return referencia;
	}
	public void setReferencia(BigDecimal referencia) {
		this.referencia = referencia;
	}
	public BigDecimal getProvento() {
		return provento;
	}
	public void setProvento(BigDecimal provento) {
		this.provento = provento;
	}
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
}
