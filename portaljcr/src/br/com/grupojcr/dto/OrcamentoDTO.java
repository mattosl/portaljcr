package br.com.grupojcr.dto;

import java.math.BigDecimal;

public class OrcamentoDTO {
	
	private BigDecimal valorOrcado;
	private BigDecimal valorEmpenhado;
	private BigDecimal valorRealizado;
	private BigDecimal valorCedido;
	private BigDecimal valorRecebido;
	private BigDecimal saldo;
	
	public BigDecimal getValorOrcado() {
		return valorOrcado;
	}
	public void setValorOrcado(BigDecimal valorOrcado) {
		this.valorOrcado = valorOrcado;
	}
	public BigDecimal getValorEmpenhado() {
		return valorEmpenhado;
	}
	public void setValorEmpenhado(BigDecimal valorEmpenhado) {
		this.valorEmpenhado = valorEmpenhado;
	}
	public BigDecimal getValorRealizado() {
		return valorRealizado;
	}
	public void setValorRealizado(BigDecimal valorRealizado) {
		this.valorRealizado = valorRealizado;
	}
	public BigDecimal getValorCedido() {
		return valorCedido;
	}
	public void setValorCedido(BigDecimal valorCedido) {
		this.valorCedido = valorCedido;
	}
	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}
	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
}
