package br.com.grupojcr.rm;

import java.math.BigDecimal;

public class AprovadorRM {
	
	private String aprovador;
	private BigDecimal valorMovimentoDe;
	private BigDecimal valorMovimentoAte;
	
	public String getAprovador() {
		return aprovador;
	}
	public void setAprovador(String aprovador) {
		this.aprovador = aprovador;
	}
	public BigDecimal getValorMovimentoDe() {
		return valorMovimentoDe;
	}
	public void setValorMovimentoDe(BigDecimal valorMovimentoDe) {
		this.valorMovimentoDe = valorMovimentoDe;
	}
	public BigDecimal getValorMovimentoAte() {
		return valorMovimentoAte;
	}
	public void setValorMovimentoAte(BigDecimal valorMovimentoAte) {
		this.valorMovimentoAte = valorMovimentoAte;
	}
	

}
