package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TMOVTRANSP")
@XmlAccessorType(XmlAccessType.FIELD)
public class TMOVTRANSPXML {
	
	public TMOVTRANSPXML() {}
	
	public TMOVTRANSPXML(String codColigada, String usuario) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.RETIRAMERCADORIA = "0";
		this.TIPOCTE = "0";
		this.TIPOSERVICOCTE = "0";
		this.TOMADORTIPO = "0";
		this.TIPOEMITENTEMDFE = "0";
		this.LOTACAO = "1";
		this.TIPOTRANSPORTADORMDFE = "0";
		this.OBSGLOBALIZADO = "";
		this.TIPOBPE = "0";
		this.RECCREATEDBY = usuario;
		this.RECCREATEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.RECMODIFIEDBY = usuario;
		this.RECMODIFIEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
	}
	
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "RETIRAMERCADORIA")
	private String RETIRAMERCADORIA;
	@XmlElement(name = "TIPOCTE")
	private String TIPOCTE;
	@XmlElement(name = "TIPOSERVICOCTE")
	private String TIPOSERVICOCTE;
	@XmlElement(name = "TOMADORTIPO")
	private String TOMADORTIPO;
	@XmlElement(name = "TIPOEMITENTEMDFE")
	private String TIPOEMITENTEMDFE;
	@XmlElement(name = "LOTACAO")
	private String LOTACAO;
	@XmlElement(name = "TIPOTRANSPORTADORMDFE")
	private String TIPOTRANSPORTADORMDFE;
	@XmlElement(name = "OBSGLOBALIZADO")
	private String OBSGLOBALIZADO;
	@XmlElement(name = "TIPOBPE")
	private String TIPOBPE;
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
	public String getRETIRAMERCADORIA() {
		return RETIRAMERCADORIA;
	}
	public void setRETIRAMERCADORIA(String rETIRAMERCADORIA) {
		RETIRAMERCADORIA = rETIRAMERCADORIA;
	}
	public String getTIPOCTE() {
		return TIPOCTE;
	}
	public void setTIPOCTE(String tIPOCTE) {
		TIPOCTE = tIPOCTE;
	}
	public String getTIPOSERVICOCTE() {
		return TIPOSERVICOCTE;
	}
	public void setTIPOSERVICOCTE(String tIPOSERVICOCTE) {
		TIPOSERVICOCTE = tIPOSERVICOCTE;
	}
	public String getTOMADORTIPO() {
		return TOMADORTIPO;
	}
	public void setTOMADORTIPO(String tOMADORTIPO) {
		TOMADORTIPO = tOMADORTIPO;
	}
	public String getTIPOEMITENTEMDFE() {
		return TIPOEMITENTEMDFE;
	}
	public void setTIPOEMITENTEMDFE(String tIPOEMITENTEMDFE) {
		TIPOEMITENTEMDFE = tIPOEMITENTEMDFE;
	}
	public String getLOTACAO() {
		return LOTACAO;
	}
	public void setLOTACAO(String lOTACAO) {
		LOTACAO = lOTACAO;
	}
	public String getTIPOTRANSPORTADORMDFE() {
		return TIPOTRANSPORTADORMDFE;
	}
	public void setTIPOTRANSPORTADORMDFE(String tIPOTRANSPORTADORMDFE) {
		TIPOTRANSPORTADORMDFE = tIPOTRANSPORTADORMDFE;
	}
	public String getOBSGLOBALIZADO() {
		return OBSGLOBALIZADO;
	}
	public void setOBSGLOBALIZADO(String oBSGLOBALIZADO) {
		OBSGLOBALIZADO = oBSGLOBALIZADO;
	}
	public String getTIPOBPE() {
		return TIPOBPE;
	}
	public void setTIPOBPE(String tIPOBPE) {
		TIPOBPE = tIPOBPE;
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
