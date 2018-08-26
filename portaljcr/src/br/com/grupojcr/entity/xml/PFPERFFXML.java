package br.com.grupojcr.entity.xml;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PFPERFF")
@XmlAccessorType(XmlAccessType.FIELD)
public class PFPERFFXML {
	
	public PFPERFFXML() {
		
	}
	@XmlElement(name = "CODCOLIGADA")
	private Integer codColigada;
	
	@XmlElement(name = "CHAPA")
	private String chapa;
	
	@XmlElement(name = "BASEINSS")
	private BigDecimal baseInss;
	
	@XmlElement(name = "BASEINSS13")
	private BigDecimal baseInss13;
	
	@XmlElement(name = "INCRAIS")
	private BigDecimal incRais;
	
	@XmlElement(name = "BASEFGTS")
	private BigDecimal baseFgts;
	
	@XmlElement(name = "BASEFGTS13")
	private BigDecimal baseFgts13;
	
	@XmlElement(name = "TOTALPROVENTOS")
	private BigDecimal totalProventos;
	
	@XmlElement(name = "TOTALDESCONTOS")
	private BigDecimal totalDescontos;
	
	@XmlElement(name = "BASEIRRF")
	private BigDecimal baseIrrf;
	
	@XmlElement(name = "BASEIRRF13")
	private BigDecimal baseIrrf13;
	
	@XmlElement(name = "LIQUIDO")
	private BigDecimal liquido;
	
	@XmlElement(name = "SALARIODECALCULO")
	private BigDecimal salarioCalculo;
	
	@XmlElement(name = "FGTS")
	private BigDecimal fgts;
	
	@XmlElement(name = "FGTS13")
	private BigDecimal fgts13;
	
	@XmlElement(name = "NRODEPENDIRRF")
	private Integer nroDepIrrf;
	
	@XmlElement(name = "NRODEPENDSALFAM")
	private Integer nroDepSal;
	
	public Integer getCodColigada() {
		return codColigada;
	}

	public void setCodColigada(Integer codColigada) {
		this.codColigada = codColigada;
	}

	public BigDecimal getBaseInss() {
		return baseInss;
	}

	public void setBaseInss(BigDecimal baseInss) {
		this.baseInss = baseInss;
	}

	public BigDecimal getBaseInss13() {
		return baseInss13;
	}

	public void setBaseInss13(BigDecimal baseInss13) {
		this.baseInss13 = baseInss13;
	}

	public String getChapa() {
		return chapa;
	}

	public void setChapa(String chapa) {
		this.chapa = chapa;
	}

	public BigDecimal getIncRais() {
		return incRais;
	}

	public void setIncRais(BigDecimal incRais) {
		this.incRais = incRais;
	}

	public BigDecimal getBaseFgts() {
		return baseFgts;
	}

	public void setBaseFgts(BigDecimal baseFgts) {
		this.baseFgts = baseFgts;
	}

	public BigDecimal getBaseFgts13() {
		return baseFgts13;
	}

	public void setBaseFgts13(BigDecimal baseFgts13) {
		this.baseFgts13 = baseFgts13;
	}

	public BigDecimal getTotalProventos() {
		return totalProventos;
	}

	public void setTotalProventos(BigDecimal totalProventos) {
		this.totalProventos = totalProventos;
	}

	public BigDecimal getTotalDescontos() {
		return totalDescontos;
	}

	public void setTotalDescontos(BigDecimal totalDescontos) {
		this.totalDescontos = totalDescontos;
	}

	public BigDecimal getBaseIrrf() {
		return baseIrrf;
	}

	public void setBaseIrrf(BigDecimal baseIrrf) {
		this.baseIrrf = baseIrrf;
	}

	public BigDecimal getBaseIrrf13() {
		return baseIrrf13;
	}

	public void setBaseIrrf13(BigDecimal baseIrrf13) {
		this.baseIrrf13 = baseIrrf13;
	}

	public BigDecimal getLiquido() {
		return liquido;
	}

	public void setLiquido(BigDecimal liquido) {
		this.liquido = liquido;
	}

	public BigDecimal getSalarioCalculo() {
		return salarioCalculo;
	}

	public void setSalarioCalculo(BigDecimal salarioCalculo) {
		this.salarioCalculo = salarioCalculo;
	}

	public BigDecimal getFgts() {
		return fgts;
	}

	public void setFgts(BigDecimal fgts) {
		this.fgts = fgts;
	}

	public BigDecimal getFgts13() {
		return fgts13;
	}

	public void setFgts13(BigDecimal fgts13) {
		this.fgts13 = fgts13;
	}

	public Integer getNroDepIrrf() {
		return nroDepIrrf;
	}

	public void setNroDepIrrf(Integer nroDepIrrf) {
		this.nroDepIrrf = nroDepIrrf;
	}

	public Integer getNroDepSal() {
		return nroDepSal;
	}

	public void setNroDepSal(Integer nroDepSal) {
		this.nroDepSal = nroDepSal;
	}

}
