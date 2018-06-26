package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TTRBITMMOV")
@XmlAccessorType(XmlAccessType.FIELD)
public class TTRBITMMOVXML {
	
	public TTRBITMMOVXML() {}
	
	public TTRBITMMOVXML(String codColigada, String sequencial, String tributo, String precoTotal, String usuario) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.NSEQITMMOV = sequencial;
		this.CODTRB = tributo;
		this.BASEDECALCULO = precoTotal;
		this.ALIQUOTA = "0,00";
		this.VALOR = "0,00";
		this.FATORREDUCAO = "0,00";
		this.FATORSUBSTTRIB = "0,00";
		this.BASEDECALCULOCALCULADA = precoTotal;
		this.EDITADO = "0";
		this.PERCDIFERIMENTOPARCIALICMS = "0,00";
		this.BASECHEIA = "0,00";
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
	@XmlElement(name = "CODTRB")
	private String CODTRB;
	@XmlElement(name = "BASEDECALCULO")
	private String BASEDECALCULO;
	@XmlElement(name = "ALIQUOTA")
	private String ALIQUOTA;
	@XmlElement(name = "VALOR")
	private String VALOR;
	@XmlElement(name = "FATORREDUCAO")
	private String FATORREDUCAO;
	@XmlElement(name = "FATORSUBSTTRIB")
	private String FATORSUBSTTRIB;
	@XmlElement(name = "BASEDECALCULOCALCULADA")
	private String BASEDECALCULOCALCULADA;
	@XmlElement(name = "EDITADO")
	private String EDITADO;
	@XmlElement(name = "PERCDIFERIMENTOPARCIALICMS")
	private String PERCDIFERIMENTOPARCIALICMS;
	@XmlElement(name = "BASECHEIA")
	private String BASECHEIA;
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
	public String getCODTRB() {
		return CODTRB;
	}
	public void setCODTRB(String cODTRB) {
		CODTRB = cODTRB;
	}
	public String getBASEDECALCULO() {
		return BASEDECALCULO;
	}
	public void setBASEDECALCULO(String bASEDECALCULO) {
		BASEDECALCULO = bASEDECALCULO;
	}
	public String getALIQUOTA() {
		return ALIQUOTA;
	}
	public void setALIQUOTA(String aLIQUOTA) {
		ALIQUOTA = aLIQUOTA;
	}
	public String getVALOR() {
		return VALOR;
	}
	public void setVALOR(String vALOR) {
		VALOR = vALOR;
	}
	public String getFATORREDUCAO() {
		return FATORREDUCAO;
	}
	public void setFATORREDUCAO(String fATORREDUCAO) {
		FATORREDUCAO = fATORREDUCAO;
	}
	public String getFATORSUBSTTRIB() {
		return FATORSUBSTTRIB;
	}
	public void setFATORSUBSTTRIB(String fATORSUBSTTRIB) {
		FATORSUBSTTRIB = fATORSUBSTTRIB;
	}
	public String getBASEDECALCULOCALCULADA() {
		return BASEDECALCULOCALCULADA;
	}
	public void setBASEDECALCULOCALCULADA(String bASEDECALCULOCALCULADA) {
		BASEDECALCULOCALCULADA = bASEDECALCULOCALCULADA;
	}
	public String getEDITADO() {
		return EDITADO;
	}
	public void setEDITADO(String eDITADO) {
		EDITADO = eDITADO;
	}
	public String getPERCDIFERIMENTOPARCIALICMS() {
		return PERCDIFERIMENTOPARCIALICMS;
	}
	public void setPERCDIFERIMENTOPARCIALICMS(String pERCDIFERIMENTOPARCIALICMS) {
		PERCDIFERIMENTOPARCIALICMS = pERCDIFERIMENTOPARCIALICMS;
	}
	public String getBASECHEIA() {
		return BASECHEIA;
	}
	public void setBASECHEIA(String bASECHEIA) {
		BASECHEIA = bASECHEIA;
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
