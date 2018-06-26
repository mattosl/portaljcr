package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TCTRCMOV")
@XmlAccessorType(XmlAccessType.FIELD)
public class TCTRCMOVXML {
	
	public TCTRCMOVXML() {}
	
	public TCTRCMOVXML(String codColigada, String usuario) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.VALORNOTAS = "0,00";
		this.VALORRATEADO = "0,00";
		this.QUANTIDADENOTAS = "0";
		this.QUANTIDADERATEADAS = "0";
		this.RECCREATEDBY = usuario;
		this.RECCREATEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.RECMODIFIEDBY = usuario;
		this.RECMODIFIEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
	}
	
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "VALORNOTAS")
	private String VALORNOTAS;
	@XmlElement(name = "VALORRATEADO")
	private String VALORRATEADO;
	@XmlElement(name = "QUANTIDADENOTAS")
	private String QUANTIDADENOTAS;
	@XmlElement(name = "QUANTIDADERATEADAS")
	private String QUANTIDADERATEADAS;
	@XmlElement(name = "RECCREATEDBY")
	private String RECCREATEDBY;
	@XmlElement(name = "RECCREATEDON")
	private String RECCREATEDON;
	@XmlElement(name = "RECMODIFIEDBY")
	private String RECMODIFIEDBY;
	@XmlElement(name = "RECMODIFIEDON")
	private String RECMODIFIEDON;
	
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
	public String getVALORNOTAS() {
		return VALORNOTAS;
	}
	public void setVALORNOTAS(String vALORNOTAS) {
		VALORNOTAS = vALORNOTAS;
	}
	public String getVALORRATEADO() {
		return VALORRATEADO;
	}
	public void setVALORRATEADO(String vALORRATEADO) {
		VALORRATEADO = vALORRATEADO;
	}
	public String getQUANTIDADENOTAS() {
		return QUANTIDADENOTAS;
	}
	public void setQUANTIDADENOTAS(String qUANTIDADENOTAS) {
		QUANTIDADENOTAS = qUANTIDADENOTAS;
	}
	public String getQUANTIDADERATEADAS() {
		return QUANTIDADERATEADAS;
	}
	public void setQUANTIDADERATEADAS(String qUANTIDADERATEADAS) {
		QUANTIDADERATEADAS = qUANTIDADERATEADAS;
	}
	public String getRECCREATEDBY() {
		return RECCREATEDBY;
	}
	public void setRECCREATEDBY(String rECCREATEDBY) {
		RECCREATEDBY = rECCREATEDBY;
	}
	public String getRECCREATEDON() {
		return RECCREATEDON;
	}
	public void setRECCREATEDON(String rECCREATEDON) {
		RECCREATEDON = rECCREATEDON;
	}
	public String getRECMODIFIEDBY() {
		return RECMODIFIEDBY;
	}
	public void setRECMODIFIEDBY(String rECMODIFIEDBY) {
		RECMODIFIEDBY = rECMODIFIEDBY;
	}
	public String getRECMODIFIEDON() {
		return RECMODIFIEDON;
	}
	public void setRECMODIFIEDON(String rECMODIFIEDON) {
		RECMODIFIEDON = rECMODIFIEDON;
	}
	
	
}
