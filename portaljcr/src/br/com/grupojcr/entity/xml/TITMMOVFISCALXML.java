package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TITMMOVFISCAL")
@XmlAccessorType(XmlAccessType.FIELD)
public class TITMMOVFISCALXML {
	
	public TITMMOVFISCALXML() {}
	
	public TITMMOVFISCALXML(String codColigada, String sequencial, String usuario) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.NSEQITMMOV = sequencial;
		this.QTDECONTRATADA = "0,00";
		this.VLRTOTTRIB = "0,00";
		this.RECCREATEDBY = usuario;
		this.RECCREATEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.RECMODIFIEDBY = usuario;
		this.RECMODIFIEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
	}
	
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "NSEQITMMOV")
	private String NSEQITMMOV;
	@XmlElement(name = "QTDECONTRATADA")
	private String QTDECONTRATADA;
	@XmlElement(name = "VLRTOTTRIB")
	private String VLRTOTTRIB;
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
	public String getNSEQITMMOV() {
		return NSEQITMMOV;
	}
	public void setNSEQITMMOV(String nSEQITMMOV) {
		NSEQITMMOV = nSEQITMMOV;
	}
	public String getQTDECONTRATADA() {
		return QTDECONTRATADA;
	}
	public void setQTDECONTRATADA(String qTDECONTRATADA) {
		QTDECONTRATADA = qTDECONTRATADA;
	}
	public String getVLRTOTTRIB() {
		return VLRTOTTRIB;
	}
	public void setVLRTOTTRIB(String vLRTOTTRIB) {
		VLRTOTTRIB = vLRTOTTRIB;
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
