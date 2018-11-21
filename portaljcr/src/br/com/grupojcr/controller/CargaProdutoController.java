package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;

import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.ProdutoCargaDTO;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class CargaProdutoController implements Serializable {

	private static final long serialVersionUID = 934937300949501891L;
	protected static Logger LOG = Logger.getLogger(CargaProdutoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	
	@EJB
	private RMDAO daoRM;
	
	@EJB
	private RMBusiness rmBusiness;

	public void doUpload(FileUploadEvent event) throws ApplicationException {
		try {
			List<ProdutoCargaDTO> produtos = new ArrayList<ProdutoCargaDTO>();
			Workbook workbook = WorkbookFactory.create(event.getFile().getInputstream());
			
			Sheet sheet = workbook.getSheetAt(0);
			
			DataFormatter dataFormatter = new DataFormatter();
			
			for (Row row: sheet) {
				if(row.getRowNum() != 0) {
					ProdutoCargaDTO produto = new ProdutoCargaDTO();
					int idx = 0;
		            for(Cell cell: row) {
		            	String cellValue = dataFormatter.formatCellValue(cell);
		            	if(idx == 0) {
		            		produto.setTipo(cellValue);
		            	} else if (idx == 1) {
		            		produto.setImobilizado(cellValue);
		            	} else if (idx == 2) {
		            		produto.setDescricao(cellValue);
		            	} else if (idx == 3) {
		            		produto.setUnidade(cellValue);
		            	} else if (idx == 4) {
		            		produto.setCodigoNaturezaOrcamentaria(cellValue);
		            	}
		            	idx++;
		                System.out.print(cellValue + "\t");
		            }
		            
		            produtos.add(produto);
				}
	        }
			
			workbook.close();
			
			for(ProdutoCargaDTO prod : produtos) {
				String codigoProduto = daoRM.obterUltimoCodigoProdutoColigada(1L);
				String codigoFiltrado = TreatString.filterOnlyNumber(codigoProduto);
				
				Long codigoLong = Long.valueOf(codigoFiltrado);
				codigoLong = codigoLong + 1;
				String codigoFormatado = Util.mascaraGenerica(codigoLong.toString(), "##.###.####");
				
				String xml = "<EstPrdBR>" +
								"<TPRODUTO>" + 
									"<CODCOLPRD>1</CODCOLPRD>" +
									"<CODCOLIGADA>1</CODCOLIGADA>" +
									"<IDPRD>-1</IDPRD>" +
									"<CODIGOPRD>" + codigoFormatado + "</CODIGOPRD>" +
									"<NOMEFANTASIA>" + prod.getDescricao().toUpperCase() + "</NOMEFANTASIA>" +
									"<NUMNOFABRIC>" + codigoFormatado + "</NUMNOFABRIC>" +
									"<ULTIMONIVEL>1</ULTIMONIVEL>" +
									"<TIPO>P</TIPO>" +
									"<DESCRICAO>" + prod.getDescricao().toUpperCase() + "</DESCRICAO>" +
									"<PRECO1>0,00</PRECO1>" +
								    "<PRECO2>0,00</PRECO2>" +
								    "<PRECO3>0,00</PRECO3>" +
								    "<PRECO4>0,00</PRECO4>" +
								    "<PRECO5>0,00</PRECO5>" +
								    "<DATABASEPRECO1>2018-10-24T00:00:00</DATABASEPRECO1>" +
								    "<DATABASEPRECO2>2018-10-24T00:00:00</DATABASEPRECO2>" +
								    "<DATABASEPRECO3>2018-10-24T00:00:00</DATABASEPRECO3>" +
								    "<DATABASEPRECO4>2018-10-24T00:00:00</DATABASEPRECO4>" +
								    "<DATABASEPRECO5>2018-10-24T00:00:00</DATABASEPRECO5>" +
								    "<CODMOEPRECO1>R$</CODMOEPRECO1>" +
								    "<CODUNDCONTROLE>" + prod.getUnidade().toUpperCase() + "</CODUNDCONTROLE>" +
								    "<DTCADASTRAMENTO>2018-10-24T00:00:00</DTCADASTRAMENTO>" +
								    "<CUSTOUNITARIO>0,00</CUSTOUNITARIO>" +
								    "<DTCUSTOUNITARIO>1899-12-30T00:00:00</DTCUSTOUNITARIO>" +
								    "<QTDEVOLUME>1</QTDEVOLUME>" +
								    "<CODUNDCOMPRA>" + prod.getUnidade().toUpperCase() + "</CODUNDCOMPRA>" +
								    "<CODUNDVENDA>" + prod.getUnidade().toUpperCase() + "</CODUNDVENDA>" +
								    "<CUSTOMEDIO>0,00</CUSTOMEDIO>" +
								    "<CUSTOREPOSICAO>0,00</CUSTOREPOSICAO>" +
								    "<CUSTOREPOSICAOB>0,00</CUSTOREPOSICAOB>" +
								    "<USANUMDECPRECO>0</USANUMDECPRECO>" +
								    "<INATIVO>0</INATIVO>" +
								    "<PESAVEL>0</PESAVEL>" +
								    "<DATAULTALTERACAO>2018-10-24T00:00:00</DATAULTALTERACAO>" +
								    "<CONTROLADOPORLOTE>0</CONTROLADOPORLOTE>" +
								    "<TIPOCALCULOCUSTO>0</TIPOCALCULOCUSTO>" +
								    "<USANUMSERIE>0</USANUMSERIE>" +
								    "<CODUSUARIO>mestre</CODUSUARIO>" +
								    "<USUARIOCRIACAO>mestre</USUARIOCRIACAO>" +
								    "<PRODUTOBASE>0</PRODUTOBASE>" +
								    "<SERVICOPRODUTORMOFFICINA>0</SERVICOPRODUTORMOFFICINA>" +
								    "<PRODUTOEPI>0</PRODUTOEPI>" +
								    "<DATAPRIMEIRAALT>2018-10-24T00:00:00</DATAPRIMEIRAALT>" +
								    "<CODTBORCAMENTO>" + prod.getCodigoNaturezaOrcamentaria() + "</CODTBORCAMENTO>" +
								    "<CODCOLTBORCAMENTO>0</CODCOLTBORCAMENTO>" +
								    "<EXIGEFORNQUALIFICADO>0</EXIGEFORNQUALIFICADO>" +
								    "<PRODVISIVELCLICBUSINESS>0</PRODVISIVELCLICBUSINESS>" +
								    "<CODUNDTRIBUTAVELINT />" +
							    "</TPRODUTO>" +
								"<TPrdFil>" +
								    "<CODCOLIGADA>1</CODCOLIGADA>" +
								    "<IDPRD>-1</IDPRD>" +
								    "<CODFILIAL>1</CODFILIAL>" +
								    "<ESTOCAVEL>1</ESTOCAVEL>" +
								    "<CONSIGNADO>0</CONSIGNADO>" +
							  	"</TPrdFil>" +
							  	"<TPrdCompl>" + 
							  		"<CODCOLIGADA>1</CODCOLIGADA>" +
								    "<IDPRD>-1</IDPRD>" +
								    "<RECCREATEDBY>mestre</RECCREATEDBY>" +
								    "<RECCREATEDON>2018-10-24T00:00:00</RECCREATEDON>" +
								    "<RECMODIFIEDBY>mestre</RECMODIFIEDBY>" +
								    "<RECMODIFIEDON>2018-10-24T00:00:00</RECMODIFIEDON>" +
								    "<FLAG>1</FLAG>" +
								"</TPrdCompl>" +
							"</EstPrdBR>";
				
				System.out.println(xml);
				
				rmBusiness.saveRecordAuth("EstPrdDataBR", xml, "CODCOLIGADA=1;CODUSUARIO=mestre;CODSISTEMA=G", "mestre", "asmn*pozx80");
				
				
			}

//		} catch (ApplicationException e) {
//			LOG.info(e.getMessage(), e);
//			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "doUpload" }, e);
		}
	}

}
