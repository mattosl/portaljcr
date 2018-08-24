package br.com.grupojcr.rm;

import java.math.BigDecimal;

public class FuncionarioHoleriteRM {
	
	private BigDecimal baseFGTS;
	private BigDecimal baseIRRF;
	private BigDecimal baseINSS;
	private BigDecimal salarioCalculo;
	
	public BigDecimal getBaseFGTS() {
		return baseFGTS;
	}
	public void setBaseFGTS(BigDecimal baseFGTS) {
		this.baseFGTS = baseFGTS;
	}
	public BigDecimal getBaseIRRF() {
		return baseIRRF;
	}
	public void setBaseIRRF(BigDecimal baseIRRF) {
		this.baseIRRF = baseIRRF;
	}
	public BigDecimal getBaseINSS() {
		return baseINSS;
	}
	public void setBaseINSS(BigDecimal baseINSS) {
		this.baseINSS = baseINSS;
	}
	public BigDecimal getSalarioCalculo() {
		return salarioCalculo;
	}
	public void setSalarioCalculo(BigDecimal salarioCalculo) {
		this.salarioCalculo = salarioCalculo;
	}

}
