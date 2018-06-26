package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TMOVFISCAL")
@XmlAccessorType(XmlAccessType.FIELD)
public class TMOVFISCALXML {
	
	public TMOVFISCALXML(String codColigada, String usuario) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.CONTRIBUINTECREDENCIADO = "0";
		this.OPERACAOCONSUMIDORFINAL = "0";
		this.OPERACAOPRESENCIAL = "0";
		this.RECCREATEDBY = usuario;
		this.RECCREATEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.RECMODIFIEDBY = usuario;
		this.RECMODIFIEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
	}
	
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "CONTRIBUINTECREDENCIADO")
	private String CONTRIBUINTECREDENCIADO;
	@XmlElement(name = "OPERACAOCONSUMIDORFINAL")
	private String OPERACAOCONSUMIDORFINAL;
	@XmlElement(name = "OPERACAOPRESENCIAL")
	private String OPERACAOPRESENCIAL;
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
	public String getCONTRIBUINTECREDENCIADO() {
		return CONTRIBUINTECREDENCIADO;
	}
	public void setCONTRIBUINTECREDENCIADO(String cONTRIBUINTECREDENCIADO) {
		CONTRIBUINTECREDENCIADO = cONTRIBUINTECREDENCIADO;
	}
	public String getOPERACAOCONSUMIDORFINAL() {
		return OPERACAOCONSUMIDORFINAL;
	}
	public void setOPERACAOCONSUMIDORFINAL(String oPERACAOCONSUMIDORFINAL) {
		OPERACAOCONSUMIDORFINAL = oPERACAOCONSUMIDORFINAL;
	}
	public String getOPERACAOPRESENCIAL() {
		return OPERACAOPRESENCIAL;
	}
	public void setOPERACAOPRESENCIAL(String oPERACAOPRESENCIAL) {
		OPERACAOPRESENCIAL = oPERACAOPRESENCIAL;
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
