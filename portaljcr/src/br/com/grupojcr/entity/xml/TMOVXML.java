package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TMOV")
@XmlAccessorType(XmlAccessType.FIELD)
public class TMOVXML {
	
	public TMOVXML() {
		
//		this.CODCOLIGADA = cODCOLIGADA;
		this.IDMOV = "-1";
		this.CODFILIAL = "1";
		this.CODLOC = "01.001";
//		this.CODCFO = cODCFO;
		this.NUMEROMOV = "0";
		this.SERIE = "AP";
//		this.CODTMV = cODTMV;
		this.TIPO = "A";
		this.STATUS = "A";
		this.MOVIMPRESSO = "0";
		this.DOCIMPRESSO = "0";
		this.FATIMPRESSA = "0";
		this.DATAEMISSAO = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.COMISSAOREPRES = "0,00";
//		this.CODCPG = "";
//		this.VALORBRUTO = vALORBRUTO;
//		this.VALORLIQUIDO = vALORLIQUIDO;
//		this.VALOROUTROS = vALOROUTROS;
		this.PERCENTUALFRETE = "0,00";
		this.VALORFRETE = "0,00";
		this.PERCENTUALSEGURO = "0,00";
		this.VALORSEGURO = "0,00";
		this.PERCENTUALDESC = "0,00";
		this.VALORDESC = "0,00";
		this.PERCENTUALDESP = "0,00";
		this.VALORDESP = "0,00";
		this.PERCCOMISSAO = "0,00";
		this.PESOLIQUIDO = "0,00";
		this.PESOBRUTO = "0,00";
		this.CODMOEVALORLIQUIDO = "R$";
		this.DATAMOVIMENTO = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.GEROUFATURA = "0";
		this.FRETECIFOUFOB = "1";
//		this.CODCFOAUX = "";
//		this.CODCCUSTO = cODCCUSTO;
		this.PERCCOMISSAOVEN2 = "0,00";
		this.CODCOLCFO = "0";
//		this.CODUSUARIO = cODUSUARIO;
		this.CODFILIALDESTINO = "1";
		this.GERADOPORLOTE = "0";
		this.STATUSEXPORTCONT = "0";
		this.CAMPOLIVRE1 = "-1";
		this.GEROUCONTATRABALHO = "0";
		this.GERADOPORCONTATRABALHO = "0";
		this.HORULTIMAALTERACAO = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.INDUSOOBJ = "0,00";
		this.INTEGRADOBONUM = "0";
		this.FLAGPROCESSADO = "0";
		this.ABATIMENTOICMS = "0,00";
//		this.USUARIOCRIACAO = uSUARIOCRIACAO;
		this.DATACRIACAO = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.STSEMAIL = "0";
//		this.VALORBRUTOINTERNO = vALORBRUTOINTERNO;
		this.VINCULADOESTOQUEFL = "0";
		this.VRBASEINSSOUTRAEMPRESA = "0,00";
		this.VALORDESCCONDICIONAL = "0,00";
		this.VALORDESPCONDICIONAL = "0,00";
		this.INTEGRADOAUTOMACAO = "0";
		this.INTEGRAAPLICACAO = "T";
		this.DATALANCAMENTO = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.RECIBONFESTATUS = "0";
		this.IDMOVCFO = "0";
		this.VALORMERCADORIAS = "0,00";
		this.USARATEIOVALORFIN = "1";
		this.CODCOLCFOAUX = "0";
//		this.HISTORICOLONGO = "";
//		this.RATEIOCCUSTODEPTO = rATEIOCCUSTODEPTO;
//		this.VALORBRUTOORIG = vALORBRUTOORIG;
//		this.VALORLIQUIDOORIG = vALORLIQUIDOORIG;
//		this.VALOROUTROSORIG = vALOROUTROSORIG;
		this.FLAGCONCLUSAO = "0";
		this.STATUSPARADIGMA = "N";
		this.STATUSINTEGRACAO = "N";
//		this.RECCREATEDBY = "";
		this.RECCREATEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
//		this.RECMODIFIEDBY = "";
		this.RECMODIFIEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.STATUSMOVINCLUSAOCOLAB = "0";
//		this.CODCOLIGADA1 = "";
		this.IDMOVHST = "-1";
	}


	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "CODFILIAL")
	private String CODFILIAL;
	@XmlElement(name = "CODLOC")
	private String CODLOC;
	@XmlElement(name = "CODCFO")
	private String CODCFO;
	@XmlElement(name = "NUMEROMOV")
	private String NUMEROMOV;
	@XmlElement(name = "SERIE")
	private String SERIE;
	@XmlElement(name = "CODTMV")
	private String CODTMV;
	@XmlElement(name = "TIPO")
	private String TIPO;
	@XmlElement(name = "STATUS")
	private String STATUS;
	@XmlElement(name = "MOVIMPRESSO")
	private String MOVIMPRESSO;
	@XmlElement(name = "DOCIMPRESSO")
	private String DOCIMPRESSO;
	@XmlElement(name = "FATIMPRESSA")
	private String FATIMPRESSA;
	@XmlElement(name = "DATAEMISSAO")
	private String DATAEMISSAO;
	@XmlElement(name = "COMISSAOREPRES")
	private String COMISSAOREPRES;
	@XmlElement(name = "CODCPG")
	private String CODCPG;
	@XmlElement(name = "VALORBRUTO")
	private String VALORBRUTO;
	@XmlElement(name = "VALORLIQUIDO")
	private String VALORLIQUIDO;
	@XmlElement(name = "VALOROUTROS")
	private String VALOROUTROS;
	@XmlElement(name = "PERCENTUALFRETE")
	private String PERCENTUALFRETE;
	@XmlElement(name = "VALORFRETE")
	private String VALORFRETE;
	@XmlElement(name = "PERCENTUALSEGURO")
	private String PERCENTUALSEGURO;
	@XmlElement(name = "VALORSEGURO")
	private String VALORSEGURO;
	@XmlElement(name = "PERCENTUALDESC")
	private String PERCENTUALDESC;
	@XmlElement(name = "VALORDESC")
	private String VALORDESC;
	@XmlElement(name = "PERCENTUALDESP")
	private String PERCENTUALDESP;
	@XmlElement(name = "VALORDESP")
	private String VALORDESP;
	@XmlElement(name = "PERCCOMISSAO")
	private String PERCCOMISSAO;
	@XmlElement(name = "PESOLIQUIDO")
	private String PESOLIQUIDO;
	@XmlElement(name = "PESOBRUTO")
	private String PESOBRUTO;
	@XmlElement(name = "CODMOEVALORLIQUIDO")
	private String CODMOEVALORLIQUIDO;
	@XmlElement(name = "DATAMOVIMENTO")
	private String DATAMOVIMENTO;
	@XmlElement(name = "GEROUFATURA")
	private String GEROUFATURA;
	@XmlElement(name = "FRETECIFOUFOB")
	private String FRETECIFOUFOB;
	@XmlElement(name = "CODCFOAUX")
	private String CODCFOAUX;
	@XmlElement(name = "CODCCUSTO")
	private String CODCCUSTO;
	@XmlElement(name = "PERCCOMISSAOVEN2")
	private String PERCCOMISSAOVEN2;
	@XmlElement(name = "CODCOLCFO")
	private String CODCOLCFO;
	@XmlElement(name = "CODUSUARIO")
	private String CODUSUARIO;
	@XmlElement(name = "CODFILIALDESTINO")
	private String CODFILIALDESTINO;
	@XmlElement(name = "GERADOPORLOTE")
	private String GERADOPORLOTE;
	@XmlElement(name = "STATUSEXPORTCONT")
	private String STATUSEXPORTCONT;
	@XmlElement(name = "CAMPOLIVRE1")
	private String CAMPOLIVRE1;
	@XmlElement(name = "GEROUCONTATRABALHO")
	private String GEROUCONTATRABALHO;
	@XmlElement(name = "GERADOPORCONTATRABALHO")
	private String GERADOPORCONTATRABALHO;
	@XmlElement(name = "HORULTIMAALTERACAO")
	private String HORULTIMAALTERACAO;
	@XmlElement(name = "INDUSOOBJ")
	private String INDUSOOBJ;
	@XmlElement(name = "INTEGRADOBONUM")
	private String INTEGRADOBONUM;
	@XmlElement(name = "FLAGPROCESSADO")
	private String FLAGPROCESSADO;
	@XmlElement(name = "ABATIMENTOICMS")
	private String ABATIMENTOICMS;
	@XmlElement(name = "USUARIOCRIACAO")
	private String USUARIOCRIACAO;
	@XmlElement(name = "DATACRIACAO")
	private String DATACRIACAO;
	@XmlElement(name = "STSEMAIL")
	private String STSEMAIL;
	@XmlElement(name = "VALORBRUTOINTERNO")
	private String VALORBRUTOINTERNO;
	@XmlElement(name = "VINCULADOESTOQUEFL")
	private String VINCULADOESTOQUEFL;
	@XmlElement(name = "VRBASEINSSOUTRAEMPRESA")
	private String VRBASEINSSOUTRAEMPRESA;
	@XmlElement(name = "VALORDESCCONDICIONAL")
	private String VALORDESCCONDICIONAL;
	@XmlElement(name = "VALORDESPCONDICIONAL")
	private String VALORDESPCONDICIONAL;
	@XmlElement(name = "INTEGRADOAUTOMACAO")
	private String INTEGRADOAUTOMACAO;
	@XmlElement(name = "INTEGRAAPLICACAO")
	private String INTEGRAAPLICACAO;
	@XmlElement(name = "DATALANCAMENTO")
	private String DATALANCAMENTO;
	@XmlElement(name = "RECIBONFESTATUS")
	private String RECIBONFESTATUS;
	@XmlElement(name = "IDMOVCFO")
	private String IDMOVCFO;
	@XmlElement(name = "VALORMERCADORIAS")
	private String VALORMERCADORIAS;
	@XmlElement(name = "USARATEIOVALORFIN")
	private String USARATEIOVALORFIN;
	@XmlElement(name = "CODCOLCFOAUX")
	private String CODCOLCFOAUX;
	@XmlElement(name = "HISTORICOLONGO")
	private String HISTORICOLONGO;
	@XmlElement(name = "RATEIOCCUSTODEPTO")
	private String RATEIOCCUSTODEPTO;
	@XmlElement(name = "VALORBRUTOORIG")
	private String VALORBRUTOORIG;
	@XmlElement(name = "VALORLIQUIDOORIG")
	private String VALORLIQUIDOORIG;
	@XmlElement(name = "VALOROUTROSORIG")
	private String VALOROUTROSORIG;
	@XmlElement(name = "FLAGCONCLUSAO")
	private String FLAGCONCLUSAO;
	@XmlElement(name = "STATUSPARADIGMA")
	private String STATUSPARADIGMA;
	@XmlElement(name = "STATUSINTEGRACAO")
	private String STATUSINTEGRACAO;
	@XmlElement(name = "RECCREATEDBY")
	private String RECCREATEDBY;
	@XmlElement(name = "RECCREATEDON")
	private String RECCREATEDON;
	@XmlElement(name = "RECMODIFIEDBY")
	private String RECMODIFIEDBY;
	@XmlElement(name = "RECMODIFIEDON")
	private String RECMODIFIEDON;
	@XmlElement(name = "STATUSMOVINCLUSAOCOLAB")
	private String STATUSMOVINCLUSAOCOLAB;
	@XmlElement(name = "CODCOLIGADA1")
	private String CODCOLIGADA1;
	@XmlElement(name = "IDMOVHST")
	private String IDMOVHST;
	
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
	public String getCODFILIAL() {
		return CODFILIAL;
	}
	public void setCODFILIAL(String cODFILIAL) {
		CODFILIAL = cODFILIAL;
	}
	public String getCODLOC() {
		return CODLOC;
	}
	public void setCODLOC(String cODLOC) {
		CODLOC = cODLOC;
	}
	public String getCODCFO() {
		return CODCFO;
	}
	public void setCODCFO(String cODCFO) {
		CODCFO = cODCFO;
	}
	public String getNUMEROMOV() {
		return NUMEROMOV;
	}
	public void setNUMEROMOV(String nUMEROMOV) {
		NUMEROMOV = nUMEROMOV;
	}
	public String getSERIE() {
		return SERIE;
	}
	public void setSERIE(String sERIE) {
		SERIE = sERIE;
	}
	public String getCODTMV() {
		return CODTMV;
	}
	public void setCODTMV(String cODTMV) {
		CODTMV = cODTMV;
	}
	public String getTIPO() {
		return TIPO;
	}
	public void setTIPO(String tIPO) {
		TIPO = tIPO;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getMOVIMPRESSO() {
		return MOVIMPRESSO;
	}
	public void setMOVIMPRESSO(String mOVIMPRESSO) {
		MOVIMPRESSO = mOVIMPRESSO;
	}
	public String getDOCIMPRESSO() {
		return DOCIMPRESSO;
	}
	public void setDOCIMPRESSO(String dOCIMPRESSO) {
		DOCIMPRESSO = dOCIMPRESSO;
	}
	public String getFATIMPRESSA() {
		return FATIMPRESSA;
	}
	public void setFATIMPRESSA(String fATIMPRESSA) {
		FATIMPRESSA = fATIMPRESSA;
	}
	public String getDATAEMISSAO() {
		return DATAEMISSAO;
	}
	public void setDATAEMISSAO(String dATAEMISSAO) {
		DATAEMISSAO = dATAEMISSAO;
	}
	public String getCOMISSAOREPRES() {
		return COMISSAOREPRES;
	}
	public void setCOMISSAOREPRES(String cOMISSAOREPRES) {
		COMISSAOREPRES = cOMISSAOREPRES;
	}
	public String getCODCPG() {
		return CODCPG;
	}
	public void setCODCPG(String cODCPG) {
		CODCPG = cODCPG;
	}
	public String getVALORBRUTO() {
		return VALORBRUTO;
	}
	public void setVALORBRUTO(String vALORBRUTO) {
		VALORBRUTO = vALORBRUTO;
	}
	public String getVALORLIQUIDO() {
		return VALORLIQUIDO;
	}
	public void setVALORLIQUIDO(String vALORLIQUIDO) {
		VALORLIQUIDO = vALORLIQUIDO;
	}
	public String getVALOROUTROS() {
		return VALOROUTROS;
	}
	public void setVALOROUTROS(String vALOROUTROS) {
		VALOROUTROS = vALOROUTROS;
	}
	public String getPERCENTUALFRETE() {
		return PERCENTUALFRETE;
	}
	public void setPERCENTUALFRETE(String pERCENTUALFRETE) {
		PERCENTUALFRETE = pERCENTUALFRETE;
	}
	public String getVALORFRETE() {
		return VALORFRETE;
	}
	public void setVALORFRETE(String vALORFRETE) {
		VALORFRETE = vALORFRETE;
	}
	public String getPERCENTUALSEGURO() {
		return PERCENTUALSEGURO;
	}
	public void setPERCENTUALSEGURO(String pERCENTUALSEGURO) {
		PERCENTUALSEGURO = pERCENTUALSEGURO;
	}
	public String getVALORSEGURO() {
		return VALORSEGURO;
	}
	public void setVALORSEGURO(String vALORSEGURO) {
		VALORSEGURO = vALORSEGURO;
	}
	public String getPERCENTUALDESC() {
		return PERCENTUALDESC;
	}
	public void setPERCENTUALDESC(String pERCENTUALDESC) {
		PERCENTUALDESC = pERCENTUALDESC;
	}
	public String getVALORDESC() {
		return VALORDESC;
	}
	public void setVALORDESC(String vALORDESC) {
		VALORDESC = vALORDESC;
	}
	public String getPERCENTUALDESP() {
		return PERCENTUALDESP;
	}
	public void setPERCENTUALDESP(String pERCENTUALDESP) {
		PERCENTUALDESP = pERCENTUALDESP;
	}
	public String getVALORDESP() {
		return VALORDESP;
	}
	public void setVALORDESP(String vALORDESP) {
		VALORDESP = vALORDESP;
	}
	public String getPERCCOMISSAO() {
		return PERCCOMISSAO;
	}
	public void setPERCCOMISSAO(String pERCCOMISSAO) {
		PERCCOMISSAO = pERCCOMISSAO;
	}
	public String getPESOLIQUIDO() {
		return PESOLIQUIDO;
	}
	public void setPESOLIQUIDO(String pESOLIQUIDO) {
		PESOLIQUIDO = pESOLIQUIDO;
	}
	public String getPESOBRUTO() {
		return PESOBRUTO;
	}
	public void setPESOBRUTO(String pESOBRUTO) {
		PESOBRUTO = pESOBRUTO;
	}
	public String getCODMOEVALORLIQUIDO() {
		return CODMOEVALORLIQUIDO;
	}
	public void setCODMOEVALORLIQUIDO(String cODMOEVALORLIQUIDO) {
		CODMOEVALORLIQUIDO = cODMOEVALORLIQUIDO;
	}
	public String getDATAMOVIMENTO() {
		return DATAMOVIMENTO;
	}
	public void setDATAMOVIMENTO(String dATAMOVIMENTO) {
		DATAMOVIMENTO = dATAMOVIMENTO;
	}
	public String getGEROUFATURA() {
		return GEROUFATURA;
	}
	public void setGEROUFATURA(String gEROUFATURA) {
		GEROUFATURA = gEROUFATURA;
	}
	public String getFRETECIFOUFOB() {
		return FRETECIFOUFOB;
	}
	public void setFRETECIFOUFOB(String fRETECIFOUFOB) {
		FRETECIFOUFOB = fRETECIFOUFOB;
	}
	public String getCODCFOAUX() {
		return CODCFOAUX;
	}
	public void setCODCFOAUX(String cODCFOAUX) {
		CODCFOAUX = cODCFOAUX;
	}
	public String getCODCCUSTO() {
		return CODCCUSTO;
	}
	public void setCODCCUSTO(String cODCCUSTO) {
		CODCCUSTO = cODCCUSTO;
	}
	public String getPERCCOMISSAOVEN2() {
		return PERCCOMISSAOVEN2;
	}
	public void setPERCCOMISSAOVEN2(String pERCCOMISSAOVEN2) {
		PERCCOMISSAOVEN2 = pERCCOMISSAOVEN2;
	}
	public String getCODUSUARIO() {
		return CODUSUARIO;
	}
	public void setCODUSUARIO(String cODUSUARIO) {
		CODUSUARIO = cODUSUARIO;
	}
	public String getCODFILIALDESTINO() {
		return CODFILIALDESTINO;
	}
	public void setCODFILIALDESTINO(String cODFILIALDESTINO) {
		CODFILIALDESTINO = cODFILIALDESTINO;
	}
	public String getGERADOPORLOTE() {
		return GERADOPORLOTE;
	}
	public void setGERADOPORLOTE(String gERADOPORLOTE) {
		GERADOPORLOTE = gERADOPORLOTE;
	}
	public String getSTATUSEXPORTCONT() {
		return STATUSEXPORTCONT;
	}
	public void setSTATUSEXPORTCONT(String sTATUSEXPORTCONT) {
		STATUSEXPORTCONT = sTATUSEXPORTCONT;
	}
	public String getCAMPOLIVRE1() {
		return CAMPOLIVRE1;
	}
	public void setCAMPOLIVRE1(String cAMPOLIVRE1) {
		CAMPOLIVRE1 = cAMPOLIVRE1;
	}
	public String getGEROUCONTATRABALHO() {
		return GEROUCONTATRABALHO;
	}
	public void setGEROUCONTATRABALHO(String gEROUCONTATRABALHO) {
		GEROUCONTATRABALHO = gEROUCONTATRABALHO;
	}
	public String getGERADOPORCONTATRABALHO() {
		return GERADOPORCONTATRABALHO;
	}
	public void setGERADOPORCONTATRABALHO(String gERADOPORCONTATRABALHO) {
		GERADOPORCONTATRABALHO = gERADOPORCONTATRABALHO;
	}
	public String getHORULTIMAALTERACAO() {
		return HORULTIMAALTERACAO;
	}
	public void setHORULTIMAALTERACAO(String hORULTIMAALTERACAO) {
		HORULTIMAALTERACAO = hORULTIMAALTERACAO;
	}
	public String getINDUSOOBJ() {
		return INDUSOOBJ;
	}
	public void setINDUSOOBJ(String iNDUSOOBJ) {
		INDUSOOBJ = iNDUSOOBJ;
	}
	public String getINTEGRADOBONUM() {
		return INTEGRADOBONUM;
	}
	public void setINTEGRADOBONUM(String iNTEGRADOBONUM) {
		INTEGRADOBONUM = iNTEGRADOBONUM;
	}
	public String getFLAGPROCESSADO() {
		return FLAGPROCESSADO;
	}
	public void setFLAGPROCESSADO(String fLAGPROCESSADO) {
		FLAGPROCESSADO = fLAGPROCESSADO;
	}
	public String getABATIMENTOICMS() {
		return ABATIMENTOICMS;
	}
	public void setABATIMENTOICMS(String aBATIMENTOICMS) {
		ABATIMENTOICMS = aBATIMENTOICMS;
	}
	public String getUSUARIOCRIACAO() {
		return USUARIOCRIACAO;
	}
	public void setUSUARIOCRIACAO(String uSUARIOCRIACAO) {
		USUARIOCRIACAO = uSUARIOCRIACAO;
	}
	public String getDATACRIACAO() {
		return DATACRIACAO;
	}
	public void setDATACRIACAO(String dATACRIACAO) {
		DATACRIACAO = dATACRIACAO;
	}
	public String getSTSEMAIL() {
		return STSEMAIL;
	}
	public void setSTSEMAIL(String sTSEMAIL) {
		STSEMAIL = sTSEMAIL;
	}
	public String getVALORBRUTOINTERNO() {
		return VALORBRUTOINTERNO;
	}
	public void setVALORBRUTOINTERNO(String vALORBRUTOINTERNO) {
		VALORBRUTOINTERNO = vALORBRUTOINTERNO;
	}
	public String getVINCULADOESTOQUEFL() {
		return VINCULADOESTOQUEFL;
	}
	public void setVINCULADOESTOQUEFL(String vINCULADOESTOQUEFL) {
		VINCULADOESTOQUEFL = vINCULADOESTOQUEFL;
	}
	public String getVRBASEINSSOUTRAEMPRESA() {
		return VRBASEINSSOUTRAEMPRESA;
	}
	public void setVRBASEINSSOUTRAEMPRESA(String vRBASEINSSOUTRAEMPRESA) {
		VRBASEINSSOUTRAEMPRESA = vRBASEINSSOUTRAEMPRESA;
	}
	public String getVALORDESCCONDICIONAL() {
		return VALORDESCCONDICIONAL;
	}
	public void setVALORDESCCONDICIONAL(String vALORDESCCONDICIONAL) {
		VALORDESCCONDICIONAL = vALORDESCCONDICIONAL;
	}
	public String getVALORDESPCONDICIONAL() {
		return VALORDESPCONDICIONAL;
	}
	public void setVALORDESPCONDICIONAL(String vALORDESPCONDICIONAL) {
		VALORDESPCONDICIONAL = vALORDESPCONDICIONAL;
	}
	public String getINTEGRADOAUTOMACAO() {
		return INTEGRADOAUTOMACAO;
	}
	public void setINTEGRADOAUTOMACAO(String iNTEGRADOAUTOMACAO) {
		INTEGRADOAUTOMACAO = iNTEGRADOAUTOMACAO;
	}
	public String getINTEGRAAPLICACAO() {
		return INTEGRAAPLICACAO;
	}
	public void setINTEGRAAPLICACAO(String iNTEGRAAPLICACAO) {
		INTEGRAAPLICACAO = iNTEGRAAPLICACAO;
	}
	public String getDATALANCAMENTO() {
		return DATALANCAMENTO;
	}
	public void setDATALANCAMENTO(String dATALANCAMENTO) {
		DATALANCAMENTO = dATALANCAMENTO;
	}
	public String getRECIBONFESTATUS() {
		return RECIBONFESTATUS;
	}
	public void setRECIBONFESTATUS(String rECIBONFESTATUS) {
		RECIBONFESTATUS = rECIBONFESTATUS;
	}
	public String getIDMOVCFO() {
		return IDMOVCFO;
	}
	public void setIDMOVCFO(String iDMOVCFO) {
		IDMOVCFO = iDMOVCFO;
	}
	public String getVALORMERCADORIAS() {
		return VALORMERCADORIAS;
	}
	public void setVALORMERCADORIAS(String vALORMERCADORIAS) {
		VALORMERCADORIAS = vALORMERCADORIAS;
	}
	public String getUSARATEIOVALORFIN() {
		return USARATEIOVALORFIN;
	}
	public void setUSARATEIOVALORFIN(String uSARATEIOVALORFIN) {
		USARATEIOVALORFIN = uSARATEIOVALORFIN;
	}
	public String getCODCOLCFOAUX() {
		return CODCOLCFOAUX;
	}
	public void setCODCOLCFOAUX(String cODCOLCFOAUX) {
		CODCOLCFOAUX = cODCOLCFOAUX;
	}
	public String getHISTORICOLONGO() {
		return HISTORICOLONGO;
	}
	public void setHISTORICOLONGO(String hISTORICOLONGO) {
		HISTORICOLONGO = hISTORICOLONGO;
	}
	public String getRATEIOCCUSTODEPTO() {
		return RATEIOCCUSTODEPTO;
	}
	public void setRATEIOCCUSTODEPTO(String rATEIOCCUSTODEPTO) {
		RATEIOCCUSTODEPTO = rATEIOCCUSTODEPTO;
	}
	public String getVALORBRUTOORIG() {
		return VALORBRUTOORIG;
	}
	public void setVALORBRUTOORIG(String vALORBRUTOORIG) {
		VALORBRUTOORIG = vALORBRUTOORIG;
	}
	public String getVALORLIQUIDOORIG() {
		return VALORLIQUIDOORIG;
	}
	public void setVALORLIQUIDOORIG(String vALORLIQUIDOORIG) {
		VALORLIQUIDOORIG = vALORLIQUIDOORIG;
	}
	public String getVALOROUTROSORIG() {
		return VALOROUTROSORIG;
	}
	public void setVALOROUTROSORIG(String vALOROUTROSORIG) {
		VALOROUTROSORIG = vALOROUTROSORIG;
	}
	public String getFLAGCONCLUSAO() {
		return FLAGCONCLUSAO;
	}
	public void setFLAGCONCLUSAO(String fLAGCONCLUSAO) {
		FLAGCONCLUSAO = fLAGCONCLUSAO;
	}
	public String getSTATUSPARADIGMA() {
		return STATUSPARADIGMA;
	}
	public void setSTATUSPARADIGMA(String sTATUSPARADIGMA) {
		STATUSPARADIGMA = sTATUSPARADIGMA;
	}
	public String getSTATUSINTEGRACAO() {
		return STATUSINTEGRACAO;
	}
	public void setSTATUSINTEGRACAO(String sTATUSINTEGRACAO) {
		STATUSINTEGRACAO = sTATUSINTEGRACAO;
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
	public String getSTATUSMOVINCLUSAOCOLAB() {
		return STATUSMOVINCLUSAOCOLAB;
	}
	public void setSTATUSMOVINCLUSAOCOLAB(String sTATUSMOVINCLUSAOCOLAB) {
		STATUSMOVINCLUSAOCOLAB = sTATUSMOVINCLUSAOCOLAB;
	}
	public String getCODCOLIGADA1() {
		return CODCOLIGADA1;
	}
	public void setCODCOLIGADA1(String cODCOLIGADA1) {
		CODCOLIGADA1 = cODCOLIGADA1;
	}
	public String getIDMOVHST() {
		return IDMOVHST;
	}
	public void setIDMOVHST(String iDMOVHST) {
		IDMOVHST = iDMOVHST;
	}
	public String getCODCOLCFO() {
		return CODCOLCFO;
	}
	public void setCODCOLCFO(String cODCOLCFO) {
		CODCOLCFO = cODCOLCFO;
	}
	
	public void preencherValores(String valor) {
		this.VALORBRUTO = valor;
		this.VALORLIQUIDO = valor;
		this.VALOROUTROS = valor;
		this.VALORBRUTOINTERNO = valor;
		this.RATEIOCCUSTODEPTO = valor;
		this.VALORBRUTOORIG = valor;
		this.VALORLIQUIDOORIG = valor;
		this.VALOROUTROSORIG = valor;
	}
	
	public void preencherUsuario(String usuario) {
		this.CODUSUARIO = usuario;
		this.USUARIOCRIACAO = usuario;
		this.RECCREATEDBY = usuario;
		this.RECMODIFIEDBY = usuario;
	}
	
}
