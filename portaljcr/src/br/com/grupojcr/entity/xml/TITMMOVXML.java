package br.com.grupojcr.entity.xml;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.grupojcr.util.TreatDate;

@XmlRootElement(name="TITMMOV")
@XmlAccessorType(XmlAccessType.FIELD)
public class TITMMOVXML {
	
	public TITMMOVXML(){}
	
	public TITMMOVXML(String codColigada, String sequencial, String idPrd, String codigoPrd, String nomeFantasia,
			String codigoReduzido, String qtd, String precoUnitario, String precoTotal, String unidade, String codCentroCusto, 
			String codNaturezaOrcamentaria, String historicoLongo, String usuario) {
		this.CODCOLIGADA = codColigada;
		this.IDMOV = "-1";
		this.NSEQITMMOV = sequencial;
		this.CODFILIAL = "1";
		this.NUMEROSEQUENCIAL = sequencial;
		this.IDPRD = idPrd;
		this.CODIGOPRD = codigoPrd;
		this.NOMEFANTASIA = nomeFantasia;
		this.CODIGOREDUZIDO = codigoReduzido;
		this.NUMNOFABRIC = codigoPrd;
		this.QUANTIDADE = qtd;
		this.PRECOUNITARIO = precoUnitario;
		this.PRECOTABELA = "0,00";
		this.DATAEMISSAO = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.CODUND = unidade;
		this.QUANTIDADEARECEBER = qtd;
		Calendar dataEntrega = Calendar.getInstance();
		dataEntrega.add(Calendar.DAY_OF_MONTH, +1);
		this.DATAENTREGA = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", dataEntrega.getTime());
		this.VALORUNITARIO = "0,00";
		this.VALORFINANCEIRO = "0,00";
		this.CODCCUSTO = codCentroCusto;
		this.ALIQORDENACAO = "0,00";
		this.QUANTIDADEORIGINAL = qtd;
		this.FLAG = "0";
		this.FATORCONVUND = "0,00";
		this.VALORBRUTOITEM = precoTotal;
		this.VALORTOTALITEM = "0,00";
		this.QUANTIDADESEPARADA = "0,00";
		this.PERCENTCOMISSAO = "0,000";
		this.COMISSAOREPRES = "0,00";
		this.VALORESCRITURACAO = "0,00";
		this.VALORFINPEDIDO = "0,00";
		this.VALOROPFRM1 = "0,00";
		this.VALOROPFRM2 = "0,00";
		this.PRECOEDITADO = "0";
		this.QTDEVOLUMEUNITARIO = "1";
		this.PRECOTOTALEDITADO = "0";
		this.VALORDESCCONDICONALITM = "0,00";
		this.VALORDESPCONDICIONALITM = "0,00";
		this.DATAORCAMENTO = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.CODTBORCAMENTO = codNaturezaOrcamentaria;
		this.CODCOLTBORCAMENTO = "0";
		this.RATEIOFRETE = "0,00";
		this.RATEIOSEGURO = "0,00";
		this.RATEIODESC = "0,00";
		this.RATEIODESP = "0,00";
		this.RATEIOEXTRA1 = "0,00";
		this.RATEIOEXTRA2 = "0,00";
		this.RATEIODEDMAT = "0,00";
		this.RATEIODEDSUB = "0,00";
		this.RATEIODEDOUT = "0,00";
		this.VALORUNTORCAMENTO = VALORUNITARIO;
		this.VALSERVICONFE = "0,00";
		this.CODLOC = "01.001";
		this.VALORBEM = "0,00";
		this.VALORLIQUIDO = precoTotal;
		this.HISTORICOLONGO = historicoLongo;
		this.RATEIOCCUSTODEPTO = precoTotal;
		this.VALORBRUTOITEMORIG = precoTotal;
		this.QUANTIDADETOTAL = qtd;
		this.PRODUTOSUBSTITUTO = "0";
		this.PRECOUNITARIOSELEC = "1";
		this.INTEGRAAPLICACAO = "T";
		this.CODBEMSIGAMNT = "";
		this.RECCREATEDBY = usuario;
		this.RECCREATEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.RECMODIFIEDBY = usuario;
		this.RECMODIFIEDON = TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", Calendar.getInstance().getTime());
		this.CODCOLIGADA1 = codColigada;
		this.IDMOVHST = "-1";
		this.NSEQITMMOV1 = sequencial;
	}
	
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;
	@XmlElement(name = "IDMOV")
	private String IDMOV;
	@XmlElement(name = "NSEQITMMOV")
	private String NSEQITMMOV;
	@XmlElement(name = "CODFILIAL")
	private String CODFILIAL;
	@XmlElement(name = "NUMEROSEQUENCIAL")
	private String NUMEROSEQUENCIAL;
	@XmlElement(name = "IDPRD")
	private String IDPRD;
	@XmlElement(name = "CODIGOPRD")
	private String CODIGOPRD;
	@XmlElement(name = "NOMEFANTASIA")
	private String NOMEFANTASIA;
	@XmlElement(name = "CODIGOREDUZIDO")
	private String CODIGOREDUZIDO;
	@XmlElement(name = "NUMNOFABRIC")
	private String NUMNOFABRIC;
	@XmlElement(name = "QUANTIDADE")
	private String QUANTIDADE;
	@XmlElement(name = "PRECOUNITARIO")
	private String PRECOUNITARIO;
	@XmlElement(name = "PRECOTABELA")
	private String PRECOTABELA;
	@XmlElement(name = "DATAEMISSAO")
	private String DATAEMISSAO;
	@XmlElement(name = "CODUND")
	private String CODUND;
	@XmlElement(name = "QUANTIDADEARECEBER")
	private String QUANTIDADEARECEBER;
	@XmlElement(name = "DATAENTREGA")
	private String DATAENTREGA;
	@XmlElement(name = "VALORUNITARIO")
	private String VALORUNITARIO;
	@XmlElement(name = "VALORFINANCEIRO")
	private String VALORFINANCEIRO;
	@XmlElement(name = "CODCCUSTO")
	private String CODCCUSTO;
	@XmlElement(name = "ALIQORDENACAO")
	private String ALIQORDENACAO;
	@XmlElement(name = "QUANTIDADEORIGINAL")
	private String QUANTIDADEORIGINAL;
	@XmlElement(name = "FLAG")
	private String FLAG;
	@XmlElement(name = "FATORCONVUND")
	private String FATORCONVUND;
	@XmlElement(name = "VALORBRUTOITEM")
	private String VALORBRUTOITEM;
	@XmlElement(name = "VALORTOTALITEM")
	private String VALORTOTALITEM;
	@XmlElement(name = "QUANTIDADESEPARADA")
	private String QUANTIDADESEPARADA;
	@XmlElement(name = "PERCENTCOMISSAO")
	private String PERCENTCOMISSAO;
	@XmlElement(name = "COMISSAOREPRES")
	private String COMISSAOREPRES;
	@XmlElement(name = "VALORESCRITURACAO")
	private String VALORESCRITURACAO;
	@XmlElement(name = "VALORFINPEDIDO")
	private String VALORFINPEDIDO;
	@XmlElement(name = "VALOROPFRM1")
	private String VALOROPFRM1;
	@XmlElement(name = "VALOROPFRM2")
	private String VALOROPFRM2;
	@XmlElement(name = "PRECOEDITADO")
	private String PRECOEDITADO;
	@XmlElement(name = "QTDEVOLUMEUNITARIO")
	private String QTDEVOLUMEUNITARIO;
	@XmlElement(name = "PRECOTOTALEDITADO")
	private String PRECOTOTALEDITADO;
	@XmlElement(name = "VALORDESCCONDICONALITM")
	private String VALORDESCCONDICONALITM;
	@XmlElement(name = "VALORDESPCONDICIONALITM")
	private String VALORDESPCONDICIONALITM;
	@XmlElement(name = "DATAORCAMENTO")
	private String DATAORCAMENTO;
	@XmlElement(name = "CODTBORCAMENTO")
	private String CODTBORCAMENTO;
	@XmlElement(name = "CODCOLTBORCAMENTO")
	private String CODCOLTBORCAMENTO;
	@XmlElement(name = "RATEIOFRETE")
	private String RATEIOFRETE;
	@XmlElement(name = "RATEIOSEGURO")
	private String RATEIOSEGURO;
	@XmlElement(name = "RATEIODESC")
	private String RATEIODESC;
	@XmlElement(name = "RATEIODESP")
	private String RATEIODESP;
	@XmlElement(name = "RATEIOEXTRA1")
	private String RATEIOEXTRA1;
	@XmlElement(name = "RATEIOEXTRA2")
	private String RATEIOEXTRA2;
	@XmlElement(name = "RATEIODEDMAT")
	private String RATEIODEDMAT;
	@XmlElement(name = "RATEIODEDSUB")
	private String RATEIODEDSUB;
	@XmlElement(name = "RATEIODEDOUT")
	private String RATEIODEDOUT;
	@XmlElement(name = "VALORUNTORCAMENTO")
	private String VALORUNTORCAMENTO;
	@XmlElement(name = "VALSERVICONFE")
	private String VALSERVICONFE;
	@XmlElement(name = "CODLOC")
	private String CODLOC;
	@XmlElement(name = "VALORBEM")
	private String VALORBEM;
	@XmlElement(name = "VALORLIQUIDO")
	private String VALORLIQUIDO;
	@XmlElement(name = "HISTORICOLONGO")
	private String HISTORICOLONGO;
	@XmlElement(name = "RATEIOCCUSTODEPTO")
	private String RATEIOCCUSTODEPTO;
	@XmlElement(name = "VALORBRUTOITEMORIG")
	private String VALORBRUTOITEMORIG;
	@XmlElement(name = "QUANTIDADETOTAL")
	private String QUANTIDADETOTAL;
	@XmlElement(name = "PRODUTOSUBSTITUTO")
	private String PRODUTOSUBSTITUTO;
	@XmlElement(name = "PRECOUNITARIOSELEC")
	private String PRECOUNITARIOSELEC;
	@XmlElement(name = "INTEGRAAPLICACAO")
	private String INTEGRAAPLICACAO;
	@XmlElement(name = "CODBEMSIGAMNT")
	private String CODBEMSIGAMNT;
	@XmlElement(name = "RECCREATEDBY")
	private String RECCREATEDBY;
	@XmlElement(name = "RECCREATEDON")
	private String RECCREATEDON;
	@XmlElement(name = "RECMODIFIEDBY")
	private String RECMODIFIEDBY;
	@XmlElement(name = "RECMODIFIEDON")
	private String RECMODIFIEDON;
	@XmlElement(name = "CODCOLIGADA1")
	private String CODCOLIGADA1;
	@XmlElement(name = "IDMOVHST")
	private String IDMOVHST;
	@XmlElement(name = "NSEQITMMOV1")
	private String NSEQITMMOV1;
}
