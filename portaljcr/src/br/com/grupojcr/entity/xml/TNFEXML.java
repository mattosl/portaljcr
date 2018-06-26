package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TNFE")
@XmlAccessorType(XmlAccessType.FIELD)
public class TNFEXML {
	
	public TNFEXML(String codColigada, String usuario) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.VALORSERVICO = "0,00";
		this.DEDUCAOSERVICO = "0,00";
		this.ALIQUOTAISS = "0,00";
		this.ISSRETIDO = "0";
		this.VALORISS = "0,00";
		this.VALORCREDITOIPTU = "0,00";
		this.BASEDECALCULO = "0,00";
		this.EDITADO = "0";
		this.RECCREATEDBY = usuario;
		this.RECCREATEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.RECMODIFIEDBY = usuario;
		this.RECMODIFIEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
	}
	
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "VALORSERVICO")
	private String VALORSERVICO;
	@XmlElement(name = "DEDUCAOSERVICO")
	private String DEDUCAOSERVICO;
	@XmlElement(name = "ALIQUOTAISS")
	private String ALIQUOTAISS;
	@XmlElement(name = "ISSRETIDO")
	private String ISSRETIDO;
	@XmlElement(name = "VALORISS")
	private String VALORISS;
	@XmlElement(name = "VALORCREDITOIPTU")
	private String VALORCREDITOIPTU;
	@XmlElement(name = "BASEDECALCULO")
	private String BASEDECALCULO;
	@XmlElement(name = "EDITADO")
	private String EDITADO;
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

	public String getVALORSERVICO() {
		return VALORSERVICO;
	}

	public void setVALORSERVICO(String vALORSERVICO) {
		VALORSERVICO = vALORSERVICO;
	}

	public String getDEDUCAOSERVICO() {
		return DEDUCAOSERVICO;
	}

	public void setDEDUCAOSERVICO(String dEDUCAOSERVICO) {
		DEDUCAOSERVICO = dEDUCAOSERVICO;
	}

	public String getALIQUOTAISS() {
		return ALIQUOTAISS;
	}

	public void setALIQUOTAISS(String aLIQUOTAISS) {
		ALIQUOTAISS = aLIQUOTAISS;
	}

	public String getISSRETIDO() {
		return ISSRETIDO;
	}

	public void setISSRETIDO(String iSSRETIDO) {
		ISSRETIDO = iSSRETIDO;
	}

	public String getVALORISS() {
		return VALORISS;
	}

	public void setVALORISS(String vALORISS) {
		VALORISS = vALORISS;
	}

	public String getVALORCREDITOIPTU() {
		return VALORCREDITOIPTU;
	}

	public void setVALORCREDITOIPTU(String vALORCREDITOIPTU) {
		VALORCREDITOIPTU = vALORCREDITOIPTU;
	}

	public String getBASEDECALCULO() {
		return BASEDECALCULO;
	}

	public void setBASEDECALCULO(String bASEDECALCULO) {
		BASEDECALCULO = bASEDECALCULO;
	}

	public String getEDITADO() {
		return EDITADO;
	}

	public void setEDITADO(String eDITADO) {
		EDITADO = eDITADO;
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
