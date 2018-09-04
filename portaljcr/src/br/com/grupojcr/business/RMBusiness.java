package br.com.grupojcr.business;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.AjustePontoDTO;
import br.com.grupojcr.dto.OrcamentoDTO;
import br.com.grupojcr.entity.xml.FopEnvelopeXML;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.rm.BatidaRM;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.CondicaoPagamentoRM;
import br.com.grupojcr.rm.FeriasRM;
import br.com.grupojcr.rm.FornecedorRM;
import br.com.grupojcr.rm.FuncionarioRM;
import br.com.grupojcr.rm.HoleriteItensRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;
import br.com.grupojcr.rm.ProdutoRM;
import br.com.grupojcr.rm.UnidadeRM;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.totvs.www.br.WsDataServerLocator;
import br.com.totvs.www.br.WsDataServerSoapStub;

@Stateless
public class RMBusiness {
	
	private static Logger LOG = Logger.getLogger(RMBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private RMDAO daoRM;
	
	
	public List<CentroCustoRM> listarCentroCustoPorColigada(Long idColigada) throws ApplicationException {
		try {
			return daoRM.listaCentroCustoPorColigada(idColigada);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarCentroCustoPorColigada" }, e);
		}
	}
	
	public List<NaturezaOrcamentariaRM> listarNaturezaOrcamentaria() throws ApplicationException {
		try {
			return daoRM.listarNaturezaOrcamentaria();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarNaturezaOrcamentaria" }, e);
		}
	}
	
	public List<ProdutoRM> listarProdutosPorNome(Long idColigada, String nome, Modalidade modalidade) throws ApplicationException {
		try {
			return daoRM.listarProdutosPorNome(idColigada, nome, modalidade);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarProdutosPorNome" }, e);
		}
	}

	public List<FornecedorRM> listarFornecedorPorNome(String nome) throws ApplicationException {
		try {
			return daoRM.listarFornecedorPorNome(nome);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarFornecedorPorNome" }, e);
		}
	}
	
	public List<UnidadeRM> listarUnidade() throws ApplicationException {
		try {
			return daoRM.listarUnidade();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarUnidades" }, e);
		}
	}
	
	public List<CondicaoPagamentoRM> listarCondicaoPagamento(Long idColigada) throws ApplicationException {
		try {
			return daoRM.listarCondicaoPagamento(idColigada);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarCondicaoPagamento" }, e);
		}
	}
	
	public Boolean existeUsuario(String nomeUsuario) throws ApplicationException {
		try {
			return daoRM.existeUsuario(nomeUsuario.trim());
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "existeUsuario" }, e);
		}
	}
	
	public void atualizaCampos(String idColigada, String idMov, String usuario) throws ApplicationException {
		try {
			daoRM.atualizarMovimento(idColigada, idMov, usuario);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "atualizaCampos" }, e);
		}
	}
	
	private WsDataServerSoapStub obterProxyWsDataServerSoapStub() throws ServiceException {
		try {
			WsDataServerLocator locator = new WsDataServerLocator();
			WsDataServerSoapStub cliente = (WsDataServerSoapStub) locator
					.getwsDataServerSoap();
			return cliente;
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw e;
		}
	}
	
	public String readRecordAuth(String dataServerName, String primaryKey, String contexto, String usuario, String senha) throws ApplicationException {
		try {
			WsDataServerSoapStub cliente = obterProxyWsDataServerSoapStub();
			
			return cliente.readRecordAuth(dataServerName, primaryKey, contexto, usuario, senha);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "readRecordAuth" }, e);
		}
	}
	
	public String saveRecordAuth(String dataServerName, String xml, String contexto, String usuario, String senha) throws ApplicationException {
		try {
			WsDataServerSoapStub cliente = obterProxyWsDataServerSoapStub();
			
			return cliente.saveRecordAuth(dataServerName, xml, contexto, usuario, senha);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "saveRecordAuth" }, e);
		}
	}
	
	public String autenticarUsuario(String usr, String chave) throws ApplicationException {
		try {
			WsDataServerSoapStub cliente = obterProxyWsDataServerSoapStub();
			
			return cliente.autenticaAcessoAuth(usr, chave);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autenticarUsuario" }, e);
		}
	}
	
	public Integer obterIdFluig(Long idColigada, Long idMovimento) throws ApplicationException {
		try {
			return daoRM.obterIdFluig(idMovimento);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterIdFluig" }, e);
		}
	}
	
	public Integer obterPeriodoColigada(Long idColigada) throws ApplicationException {
		try {
			return daoRM.obterPeriodoOrcamentoColigada(idColigada);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterPeriodoColigada" }, e);
		}
	}
	
	public Integer obterOrcamento(Integer periodo, Long idColigada, String idNatureza, String idCentroCusto) throws ApplicationException {
		try {
			return daoRM.obterOrcamento(periodo, idColigada, idNatureza, idCentroCusto);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterOrcamento" }, e);
		}
	}

	public OrcamentoDTO obterOrcamentoCompleto(Integer periodo, Long idColigada, Integer idOrcamento, Integer mes) throws ApplicationException {
		try {
			return daoRM.obterOrcamentoCompleto(periodo, idColigada, idOrcamento, mes);
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterOrcamentoCompleto" }, e);
		}
	}
	
	public void ajustarOrcamento(AjusteOrcamentarioDTO ajuste) throws ApplicationException {
		try {
			Integer idOrcamentoOrigem = daoRM.obterOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), ajuste.getNaturezaOrigem().getCodigoNaturezaOrcamentaria(), ajuste.getCentroCusto().getCodigoCentroCusto());
			if(Util.isNotNull(idOrcamentoOrigem)) {
				OrcamentoDTO dto = daoRM.obterOrcamentoCompleto(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoOrigem, ajuste.getMesOrigem().getId());
				if(Util.isNull(dto)) {
					daoRM.incluirItemOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoOrigem, ajuste.getMesOrigem().getId());
				}
				daoRM.cederValor(ajuste.getValor(), ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoOrigem, ajuste.getMesOrigem().getId());
				ajuste.setIdOrcamentoOrigem(idOrcamentoOrigem);
				
			} else {
				Calendar calendarioInicial = Calendar.getInstance();
				calendarioInicial.set(Calendar.MONTH, (ajuste.getMesOrigem().getId() - 1));
				calendarioInicial.set(Calendar.DAY_OF_MONTH, calendarioInicial.getActualMinimum(Calendar.DAY_OF_MONTH));
				Calendar calendarioFinal = Calendar.getInstance();
				calendarioFinal.set(Calendar.MONTH, (ajuste.getMesOrigem().getId() - 1));
				calendarioFinal.set(Calendar.DAY_OF_MONTH, calendarioFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				String xml = 	"<MovOrcamento>" +
									"<TORCAMENTO>" +
									    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
									    "<IDORCAMENTO>0</IDORCAMENTO>" +
									    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
									    "<CODCCUSTO>" + ajuste.getCentroCusto().getCodigoCentroCusto() + "</CODCCUSTO>" +
									    "<CODTBORCAMENTO>" + ajuste.getNaturezaOrigem().getCodigoNaturezaOrcamentaria() + "</CODTBORCAMENTO>" +
								    "</TORCAMENTO>" +
								    "<TITMORCAMENTO>" +
									    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
									    "<IDORCAMENTO>0</IDORCAMENTO>" +
									    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
									    "<IDITMPERIODO>" + ajuste.getMesOrigem().getId() + "</IDITMPERIODO>" +
									    "<VALORORCADO>0,00</VALORORCADO>" +
									    "<VALORREAL>0,00</VALORREAL>" +
									    "<VALOROPCIONAL1>0,00</VALOROPCIONAL1>" +
									    "<VALOROPCIONAL2>0,00</VALOROPCIONAL2>" +
									    "<VALORRECEBIDO>0,00</VALORRECEBIDO>" +
									    "<VALORCEDIDO>" + TreatNumber.formatMoney(ajuste.getValor()) + "</VALORCEDIDO>" +
									    "<VALOREXCEDENTE>0,00</VALOREXCEDENTE>" +
									    "<DATAINICIO>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioInicial.getTime()) + "</DATAINICIO>" +
									    "<DATAFIM>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioFinal.getTime()) + "</DATAFIM>" +
								    "</TITMORCAMENTO>" +
							    "</MovOrcamento>";
				
				String retorno = saveRecordAuth("MovOrcamentoData", xml, "CODCOLIGADA=" + ajuste.getColigada().getId() +";CODSISTEMA=T;CODUSUARIO=portaljcr", "portaljcr", "portaljcr-123");
				String [] arrayRetorno = retorno.split(";");
				if(arrayRetorno.length == 2) {
					ajuste.setIdOrcamentoOrigem(Integer.valueOf(arrayRetorno[1]));
				} else {
					throw new ApplicationException("message.empty", new String[] {arrayRetorno[0]}, FacesMessage.SEVERITY_WARN);
				}
			}
			Integer idOrcamentoDestino = daoRM.obterOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), ajuste.getNaturezaDestino().getCodigoNaturezaOrcamentaria(), ajuste.getCentroCusto().getCodigoCentroCusto());
			if(Util.isNotNull(idOrcamentoDestino)) {
				OrcamentoDTO dto = daoRM.obterOrcamentoCompleto(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoDestino, ajuste.getMesDestino().getId());
				if(Util.isNull(dto)) {
					daoRM.incluirItemOrcamento(ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoDestino, ajuste.getMesDestino().getId());
				}
				daoRM.receberValor(ajuste.getValor(), ajuste.getPeriodo(), ajuste.getColigada().getId(), idOrcamentoDestino, ajuste.getMesDestino().getId());
				ajuste.setIdOrcamentoDestino(idOrcamentoDestino);
			} else {
				Calendar calendarioInicial = Calendar.getInstance();
				calendarioInicial.set(Calendar.MONTH, (ajuste.getMesDestino().getId() - 1));
				calendarioInicial.set(Calendar.DAY_OF_MONTH, calendarioInicial.getActualMinimum(Calendar.DAY_OF_MONTH));
				Calendar calendarioFinal = Calendar.getInstance();
				calendarioFinal.set(Calendar.MONTH, (ajuste.getMesDestino().getId() - 1));
				calendarioFinal.set(Calendar.DAY_OF_MONTH, calendarioFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				String xml = 	"<MovOrcamento>" +
							 		"<TORCAMENTO>" +
									    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
									    "<IDORCAMENTO>0</IDORCAMENTO>" +
									    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
									    "<CODCCUSTO>" + ajuste.getCentroCusto().getCodigoCentroCusto() + "</CODCCUSTO>" +
									    "<CODTBORCAMENTO>" + ajuste.getNaturezaDestino().getCodigoNaturezaOrcamentaria() + "</CODTBORCAMENTO>" +
								    "</TORCAMENTO>" +
								    "<TITMORCAMENTO>" +
									    "<CODCOLIGADA>" + ajuste.getColigada().getId() + "</CODCOLIGADA>" +
									    "<IDORCAMENTO>0</IDORCAMENTO>" +
									    "<IDPERIODO>" + ajuste.getPeriodo() + "</IDPERIODO>" +
									    "<IDITMPERIODO>" + ajuste.getMesDestino().getId() + "</IDITMPERIODO>" +
									    "<VALORORCADO>0,00</VALORORCADO>" +
									    "<VALORREAL>0,00</VALORREAL>" +
									    "<VALOROPCIONAL1>0,00</VALOROPCIONAL1>" +
									    "<VALOROPCIONAL2>0,00</VALOROPCIONAL2>" +
									    "<VALORRECEBIDO>" + TreatNumber.formatMoney(ajuste.getValor()) + "</VALORRECEBIDO>" +
									    "<VALORCEDIDO>0,00</VALORCEDIDO>" +
									    "<VALOREXCEDENTE>0,00</VALOREXCEDENTE>" +
									    "<DATAINICIO>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioInicial.getTime()) + "</DATAINICIO>" +
									    "<DATAFIM>" + TreatDate.format("yyyy-MM-dd'T'HH:mm:ss", calendarioFinal.getTime()) + "</DATAFIM>" +
								    "</TITMORCAMENTO>" +
							    "</MovOrcamento>";
				
				String retorno = saveRecordAuth("MovOrcamentoData", xml, "CODCOLIGADA=" + ajuste.getColigada().getId() +";CODSISTEMA=T;CODUSUARIO=portaljcr", "portaljcr", "portaljcr-123");
				
				String [] arrayRetorno = retorno.split(";");
				if(arrayRetorno.length == 2) {
					ajuste.setIdOrcamentoDestino(Integer.valueOf(arrayRetorno[1]));
				} else {
					throw new ApplicationException("message.empty", new String[] {arrayRetorno[0]}, FacesMessage.SEVERITY_WARN);
				}
				
			}
		} catch (ApplicationException e) {
			LOG.info(e.getStackTrace(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getStackTrace(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "ajustarOrcamento" }, e);
		}
	}
	
	public FuncionarioRM obterDadosFuncionario(String chapa) throws ApplicationException {
		try {
			String chapaCompleta = TreatString.filterOnlyNumber(chapa);
			
			Long idColigada = null;
			String chapaOriginal = null;
			if(chapaCompleta.length() == 7) {
				String coligada = chapaCompleta.substring(0, 1);
				if(TreatString.isNotBlank(coligada)) {
					idColigada = Long.parseLong(coligada);
				}
				chapaOriginal = chapaCompleta.substring(1, chapaCompleta.length());
				
			} else if(chapaCompleta.length() == 8) {
				String coligada = chapaCompleta.substring(0, 2);
				if(TreatString.isNotBlank(coligada)) {
					idColigada = Long.parseLong(coligada);
				}
				chapaOriginal = chapaCompleta.substring(2, chapaCompleta.length());
			}
			
			return daoRM.obterDadosFuncionario(idColigada, chapaOriginal);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterDadosFuncionario" }, e);
		}
	}

	public FopEnvelopeXML obterDadosHolerite(Integer idColigada, String chapa, Integer mes, Integer ano, Integer periodo) throws ApplicationException {
		try {
			
			String retorno = readRecordAuth("FopEnvelopeData", idColigada + ";" + chapa + ";" + ano + ";" + mes + ";" + periodo, "CODSISTEMA=G;CODCOLIGADA="+idColigada+";CODUSUARIO=portaljcr", "portaljcr", "portaljcr-123");
			
			JAXBContext context = null;
			context = JAXBContext.newInstance(FopEnvelopeXML.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller = context.createUnmarshaller();
			StringReader reader = new StringReader(retorno);
			FopEnvelopeXML fopEnvelope = (FopEnvelopeXML) unmarshaller.unmarshal(reader);
			
			return fopEnvelope;
			// return daoRM.obterDadosHoleriteFuncionario(idColigada, chapa, mes, ano, periodo);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterDadosHolerite" }, e);
		}
	}

	public List<HoleriteItensRM> listarItensHolerite(Integer idColigada, String chapa, Integer mes, Integer ano, Integer periodo) throws ApplicationException {
		try {
			return daoRM.listarItensHolerite(idColigada, chapa, mes, ano, periodo);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarItensHolerite" }, e);
		}
	}
	
	public Integer obterQtdDepIRRF(Integer idColigada, String chapa) throws ApplicationException {
		try {
			return daoRM.obterQtdDependenteIRRF(idColigada, chapa);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdDepIRRF" }, e);
		}
	}
	
	public List<AjustePontoDTO> obterBatidasUsuarioPeriodo(Integer idColigada, String chapa, Calendar periodoInicial, Calendar periodoFinal) throws ApplicationException {
		try {
//			List<BatidaRM> batidas = daoRM.obterBatidasUsuarioPeriodo(idColigada, chapa, periodoInicial, periodoFinal);
			List<BatidaRM> batidas = daoRM.obterBatidasUsuarioPeriodo(7, "000040", periodoInicial.getTime(), periodoFinal.getTime());
			
			List<AjustePontoDTO> listaAjuste = new ArrayList<AjustePontoDTO>();
			
			LocalDate dataInicio = periodoInicial.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dataFinal = periodoFinal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			long numOfDaysBetween = ChronoUnit.DAYS.between(dataInicio, dataFinal);
			
			List<LocalDate> datas = IntStream.iterate(0, i -> i + 1)
				      .limit(numOfDaysBetween + 1)
				      .mapToObj(i -> dataInicio.plusDays(i))
				      .collect(Collectors.toList()); 
			
			List<FeriasRM> listaFerias = daoRM.obterFeriasFuncionario(idColigada, chapa);
			
			for(LocalDate localDate : datas) {
				AjustePontoDTO dto = new AjustePontoDTO();
				Date dataAtual = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				
				/* Validar final de semana */
				if(localDate.getDayOfWeek().getValue() == 6 
						|| localDate.getDayOfWeek().getValue() == 7) {
					dto.setFinalSemana(Boolean.TRUE);
				}
				
				/* Validar ferias */
				Map<Date, Date> periodos = new HashMap<Date, Date>();
				for(FeriasRM f : listaFerias) {
					if(f.getMotivo().equals(16)) {
					}
					
				}
				
					
				dto.setData(dataAtual);
				List<Calendar> pontos = new ArrayList<Calendar>();
				for(BatidaRM batida : batidas) {
					if(TreatDate.isMesmaData(batida.getData(), dto.getData())) {
						Integer hora = batida.getBatida() / 60;
						Integer minuto = batida.getBatida() % 60;
						
						Calendar databatida = Calendar.getInstance();
						databatida.setTime(dto.getData());
						databatida.set(Calendar.HOUR_OF_DAY, hora);
						databatida.set(Calendar.MINUTE, minuto);
						databatida.set(Calendar.SECOND, 0);
						databatida.set(Calendar.MILLISECOND, 0);
						
						pontos.add(databatida);
					}
				}
				
				String nomeDia = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
				dto.setNomeDia(nomeDia.toUpperCase().substring(0, 3));
				dto.setBatidas(new HashMap<Integer, Calendar>());
				Integer idx = 1;
				for(Calendar ponto : pontos) {
					dto.getBatidas().put(idx++, ponto);
				}
				StringBuilder sb = new StringBuilder();
				sb.append(TreatDate.format("dd/MM/yyyy", dto.getData()) + " - " + dto.getNomeDia() + " ");

//				dto.getBatidas().forEach((k,v) -> System.out.println("key: " + k + " Value: " + TreatDate.format("dd/MM/yyyy", v.getTime())));
				dto.getBatidas().forEach((k,v) -> sb.append(TreatDate.format("HH:mm", v.getTime()) + " "));
				
				System.out.println(sb.toString());
			}
			
			return listaAjuste;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterBatidasUsuarioPeriodo" }, e);
		}
	}

}
