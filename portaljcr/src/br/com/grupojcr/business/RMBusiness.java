package br.com.grupojcr.business;

import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AjustePontoDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.AjustePontoDTO;
import br.com.grupojcr.dto.AprovadorDTO;
import br.com.grupojcr.dto.BatidaDTO;
import br.com.grupojcr.dto.HorasPontoDTO;
import br.com.grupojcr.dto.ItemDTO;
import br.com.grupojcr.dto.MovimentoDTO;
import br.com.grupojcr.dto.OrcamentoDTO;
import br.com.grupojcr.dto.PeriodoFeriasDTO;
import br.com.grupojcr.dto.PeriodoPontoDTO;
import br.com.grupojcr.entity.AjustePonto;
import br.com.grupojcr.entity.BatidaPonto;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.entity.xml.FopEnvelopeXML;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.rm.BatidaRM;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.CondicaoPagamentoRM;
import br.com.grupojcr.rm.FeriadoRM;
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
	
	@EJB
	private AjustePontoDAO daoAjustePonto;
	
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
	
	public PeriodoPontoDTO obterPeriodoAtualFuncionario(Integer idColigada, String chapa) throws ApplicationException {
		try {
			return daoRM.obterPeriodoAtualPonto(idColigada, chapa);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdDepIRRF" }, e);
		}
	}
	
	public List<PeriodoPontoDTO> listarPeriodoPonto(Integer idColigada, String chapa) throws ApplicationException {
		try {
			return daoRM.listarPeriodosPonto(idColigada, chapa);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarPeriodoPonto" }, e);
		}
	}
	
	public List<AjustePontoDTO> obterBatidasUsuarioPeriodo(Usuario usuarioLogado, Integer idColigada, String chapa, Calendar periodoInicial, Calendar periodoFinal) throws ApplicationException {
		try {
			List<BatidaRM> batidas = daoRM.obterBatidasUsuarioPeriodo(idColigada, chapa, periodoInicial.getTime(), periodoFinal.getTime());
			List<HorasPontoDTO> horasPonto = daoRM.obterHorasPonto(idColigada, chapa, periodoInicial.getTime(), periodoFinal.getTime());
			List<FeriasRM> listaFerias = daoRM.obterFeriasFuncionario(idColigada, chapa);
			List<FeriadoRM> listaFeriado = daoRM.obterFeriadosFuncionario(idColigada, chapa, periodoInicial.getTime(), periodoFinal.getTime());
			
			List<AjustePontoDTO> listaAjuste = new ArrayList<AjustePontoDTO>();
			
			LocalDate dataInicio = periodoInicial.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dataFinal = periodoFinal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			long numOfDaysBetween = ChronoUnit.DAYS.between(dataInicio, dataFinal);
			
			List<LocalDate> datas = IntStream.iterate(0, i -> i + 1)
				      .limit(numOfDaysBetween + 1)
				      .mapToObj(i -> dataInicio.plusDays(i))
				      .collect(Collectors.toList()); 
			
			
			List<PeriodoFeriasDTO> periodos = new ArrayList<PeriodoFeriasDTO>();
			for(int i = 0; i < listaFerias.size(); i++) {
				PeriodoFeriasDTO periodo = new PeriodoFeriasDTO();
				if(listaFerias.get(i).getMotivo().equals("16")) {
					periodo.setPeriodoInicial(listaFerias.get(i).getData());
					
					if(listaFerias.get(i + 1) != null) {
						Calendar dtFinal = Calendar.getInstance();
						dtFinal.setTime(listaFerias.get(i + 1).getData());
						dtFinal.add(Calendar.DAY_OF_MONTH, -1);
						periodo.setPeriodoFinal(dtFinal.getTime());
					}
					periodos.add(periodo);
				}
				i++;
			}
			AjustePonto ajustePontoBanco = daoAjustePonto.obterAjustePonto(usuarioLogado.getId(), periodoInicial.getTime(), periodoFinal.getTime());
			if(Util.isNotNull(ajustePontoBanco)) {
				for(BatidaPonto batida : ajustePontoBanco.getBatidas()) {
					BatidaRM batidaExiste = daoRM.obterBatida(idColigada, chapa, batida.getDtBatida(), batida.getBatida());
					if(Util.isNull(batidaExiste)) {
						batidas.add(new BatidaRM(batida.getDtBatida(), batida.getBatida(), "D", batida.getTipo(), Boolean.TRUE, batida));
					}
				}
			}
			
			batidas.sort(new Comparator<BatidaRM>() {

				@Override
				public int compare(BatidaRM o1, BatidaRM o2) {
					if(TreatDate.isMesmaData(o1.getData(), o2.getData())) {
						if(o1.getBatida() > o2.getBatida()) {
							return 1;
						} else if(o1.getBatida().equals(o2.getBatida())) {
							return 0;
						} else {
							return -1;
						}
					}
					if(o1.getData().after(o2.getData())) {
						return 1;
					} else {
						return -1;
					}
				}
				
			});
			
			
			for(LocalDate localDate : datas) {
				AjustePontoDTO dto = new AjustePontoDTO();
				Date dataAtual = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				
				
				/* Validar final de semana */
				if(localDate.getDayOfWeek().getValue() == 6 
						|| localDate.getDayOfWeek().getValue() == 7) {
					dto.setFinalSemana(Boolean.TRUE);
				}
				
				/* Validar Ferias */
				for(PeriodoFeriasDTO periodo : periodos) {
					if(Util.isNull(periodo.getPeriodoFinal())) {
						if(dataAtual.after(periodo.getPeriodoInicial())) {
							dto.setFerias(Boolean.TRUE);
						}
					} else {
						if(TreatDate.pertenceAoPeriodo(dataAtual, periodo.getPeriodoInicial(), periodo.getPeriodoFinal())) {
							dto.setFerias(Boolean.TRUE);
						}
					}
				}
				
				/* Validar Feriado */
				for(FeriadoRM feriado : listaFeriado) {
					if(TreatDate.isMesmaData(dataAtual, feriado.getData())) {
						dto.setFeriado(Boolean.TRUE);
					}
				}
				
				/* Validar se é hoje */
				if(TreatDate.isMesmaData(dataAtual, Calendar.getInstance().getTime())) {
					dto.setHoje(Boolean.TRUE);
				}
				
				dto.setData(dataAtual);
				List<BatidaDTO> pontos = new ArrayList<BatidaDTO>();
				
				for(HorasPontoDTO hp : horasPonto) {
					if(TreatDate.isMesmaData(dataAtual, hp.getData())) {
						dto.setHorasTrabalhadas(hp.getHorasTrabalhada());
						dto.setHorasExtra(hp.getHorasExtra());
						dto.setHorasAtraso(hp.getHorasAtraso());
						dto.setHorasFalta(hp.getHorasFalta());
						dto.setHorasAdicionalNoturno(hp.getHorasAdicional());
						dto.setHorasAbono(hp.getHorasAbono());
					}
				}
					
				
				dto.setBatidas(new HashMap<Integer, BatidaDTO>());
				Integer idx = 1;
				BatidaDTO ultimaBatida = null;
				for(BatidaRM batida : batidas) {
						
					if(TreatDate.isMesmaData(batida.getData(), dto.getData())) {
						BatidaDTO batidaDTO = new BatidaDTO();
						batidaDTO.setBatida(batida.getBatida());
						batidaDTO.setStatus(batida.getStatus());
						batidaDTO.setEditado(batida.getEditado());
						batidaDTO.setBatidaPonto(batida.getBatidaPonto());
						if(idx.equals(1) || idx.equals(3) || idx.equals(5) || idx.equals(7)) {
							batidaDTO.setNatureza(0);
						} else {
							batidaDTO.setNatureza(1);
						}
						
						dto.getBatidas().put(idx, batidaDTO);
						
						ultimaBatida = batidaDTO;
						pontos.add(batidaDTO);
						idx++;
					}
				}
				
				if(Util.isNotNull(ultimaBatida)) {
					idx--;
					if(ultimaBatida.getNatureza().equals(0)) {
						if(idx.equals(1) || idx.equals(3) || idx.equals(5) || idx.equals(7)) {
							BatidaDTO b = new BatidaDTO();
							b.setFalta(Boolean.TRUE);
							dto.setFaltaBatida(Boolean.TRUE);
							dto.getBatidas().put(++idx, b);
						}
					}
				}
				
				if(dto.getBatidas().size() < 4) {
					if(!dto.getFerias() 
							&& !dto.getFeriado()
							&& !dto.getFinalSemana()
							&& !dto.getHoje()) {
						dto.setAtencao(Boolean.TRUE);
					}
				}
				
				String nomeDia = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
				dto.setNomeDia(nomeDia.toUpperCase().substring(0, 3));
				
				StringBuilder sb = new StringBuilder();
				sb.append(TreatDate.format("dd/MM/yyyy", dto.getData()) + " - " + dto.getNomeDia() + " ");

				listaAjuste.add(dto);
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
	
	public List<MovimentoDTO> listarMovimentos(String usuario, Date dtInicio, Date dtFim, Integer idColigada, String centroCusto, String naturezaOrcamentaria) {
		try {
			LOG.info("[listarMovimentos] Listando aprovações do período...");
			List<MovimentoDTO> movimentos = daoRM.listarAprovacaoPorPeriodo(idColigada, centroCusto, dtInicio, dtFim);
			List<MovimentoDTO> retorno = new ArrayList<MovimentoDTO>();
	
			LOG.info("[listarMovimentos] Verificando movimentos que o usuário tem ligação...");
			for (MovimentoDTO dto : movimentos) {
				Boolean possuiVinculo = Boolean.FALSE;
				List<AprovadorDTO> aprovadores = daoRM.listarAprovadores(dto.getLotacao());
	
				Double valorCompra = Util.removerFomatacaoMoeda(dto.getValorTotal());
				
				for (AprovadorDTO aprv : aprovadores) {
					if(aprv.getUsuarioAprovacao().equals(usuario)) {
						if(dto.getIdTipoMovimento().equals("CONTRATO")) {
							if(aprv.getValorInicialContrato().compareTo(new BigDecimal(valorCompra)) == 0 || aprv.getValorInicialContrato().compareTo(new BigDecimal(valorCompra)) == -1) {
								if(aprv.getValorFinalContrato().compareTo(new BigDecimal(valorCompra)) == 0 || aprv.getValorFinalContrato().compareTo(new BigDecimal(valorCompra)) == 1) {
									possuiVinculo = Boolean.TRUE;
								}
							}
						} else {
							if(aprv.getValorInicialMovimento().compareTo(new BigDecimal(valorCompra)) == 0 || aprv.getValorInicialMovimento().compareTo(new BigDecimal(valorCompra)) == -1) {
								if(aprv.getValorFinalMovimento().compareTo(new BigDecimal(valorCompra)) == 0 || aprv.getValorFinalMovimento().compareTo(new BigDecimal(valorCompra)) == 1) {
									possuiVinculo = Boolean.TRUE;
								}
							}
						}
						
					}
				}
				
				if (possuiVinculo) {
					if(dto.getIdTipoMovimento().equals("CONTRATO")) {
						dto.setListaItem(daoRM.listarItensContrato(dto.getIdMov(), dto.getIdColigada()));
					} else {
						dto.setListaItem(daoRM.listarItensMovimento(dto.getIdMov(), dto.getIdColigada()));
					}
					
					if(TreatString.isNotBlank(naturezaOrcamentaria)) {
						Boolean possuiNatureza = Boolean.FALSE;
						if(Util.isNotNull(dto.getListaItem())) {
							for(ItemDTO item : dto.getListaItem()) {
								if(Util.isNotNull(item.getIdNaturezaOrcamentaria())) {
									if(item.getIdNaturezaOrcamentaria().equals(naturezaOrcamentaria)) {
										possuiNatureza = Boolean.TRUE;
									}
								}
							}
						}
						
						if(possuiNatureza) {
							retorno.add(dto);
						}
						
					} else {
						retorno.add(dto);
					}
				}
			}
			
			LOG.info("[listarMovimentos] Movimentos filtrados.");
			return retorno;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public Date obterUltimaColetaColigada(Integer codColigada) throws ApplicationException {
		try {
			return daoRM.obterUltimaColetaColigada();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterUltimaColetaColigada" }, e);
		}
	}

	public List<FeriadoRM> obterFeriados(Integer codColigada, String chapa, Date periodoInicial, Date periodoFinal) throws ApplicationException {
		try {
			return daoRM.obterFeriadosFuncionario(codColigada, chapa, periodoInicial, periodoFinal);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterFeriados" }, e);
		}
	}
	
	public Boolean verificarUtilizaPonto(Integer codColigada, String chapa, Date periodoFinal) throws ApplicationException {
		try {
			return daoRM.verificarUtilizaPonto(codColigada, chapa, periodoFinal);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "verificarUtilizaPonto" }, e);
		}
	}
}
