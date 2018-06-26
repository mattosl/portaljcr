package br.com.grupojcr.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TITMMOV")
@XmlAccessorType(XmlAccessType.FIELD)
public class TITMMOVXML {
	
	public TITMMOVXML(String codColigada, String codCentroCusto, String centroCusto, String valor) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.CODCCUSTO = codCentroCusto;
		this.NOME = centroCusto;
		this.VALOR = valor;
		this.IDMOVRATCCU = "-1";
	}
	
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "CODCCUSTO")
	private String CODCCUSTO;
	@XmlElement(name = "NOME")
	private String NOME;
	@XmlElement(name = "VALOR")
	private String VALOR;
	@XmlElement(name = "IDMOVRATCCU")
	private String IDMOVRATCCU;
	
	public String getCODCOLIGADA() {
		return CODCOLIGADA;
	}
	public void setCODCOLIGADA(String cODCOLIGADA) {
		CODCOLIGADA = cODCOLIGADA;
	}
	public String getIDMOV() {
		return IDMOV;
	}
	public void setIDMOV(String iDMOV) {
		IDMOV = iDMOV;
	}
	public String getCODCCUSTO() {
		return CODCCUSTO;
	}
	public void setCODCCUSTO(String cODCCUSTO) {
		CODCCUSTO = cODCCUSTO;
	}
	public String getNOME() {
		return NOME;
	}
	public void setNOME(String nOME) {
		NOME = nOME;
	}
	public String getVALOR() {
		return VALOR;
	}
	public void setVALOR(String vALOR) {
		VALOR = vALOR;
	}
	public String getIDMOVRATCCU() {
		return IDMOVRATCCU;
	}
	public void setIDMOVRATCCU(String iDMOVRATCCU) {
		IDMOVRATCCU = iDMOVRATCCU;
	}
	
}
