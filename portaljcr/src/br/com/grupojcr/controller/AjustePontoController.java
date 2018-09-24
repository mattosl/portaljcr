package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import br.com.grupojcr.business.PontoBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.AjustePontoDTO;
import br.com.grupojcr.dto.BatidaDTO;
import br.com.grupojcr.dto.PeriodoPontoDTO;
import br.com.grupojcr.entity.BatidaPonto;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.rm.FuncionarioRM;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class AjustePontoController implements Serializable {
	
	private static final long serialVersionUID = -8044745000788576246L;
	protected static Logger LOG = Logger.getLogger(AjustePontoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private String chapa;
	private String chave;
	private String justificativa;
	private String horario;
	
	private Calendar periodoInicial;
	private Calendar periodoFinal;
	private Date ultimaColeta;
	private Date dtEdicao;
	
	private FuncionarioRM funcionario;
	private BatidaDTO batidaEdicao;
	private Integer sequenciaEdicao;
	private AjustePontoDTO pontoEdicao;
	
	private List<AjustePontoDTO> pontos;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private PontoBusiness pontoBusiness;
	
	
	public String autenticarUsuario() throws ApplicationException {
		try {
			iniciarAjustePonto();
				
			return "/pages/recursosHumanos/ajustarPonto/editar_ajustarPonto.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			setChapa(null);
			setChave(null);
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autenticarUsuario" }, e);
		}
	}
	
	private void iniciarAjustePonto() throws ApplicationException {
		try {
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			setFuncionario(rmBusiness.obterDadosFuncionario("7-000040"));
			carregarPeriodo();
			setUltimaColeta(rmBusiness.obterUltimaColetaColigada(7));
			List<AjustePontoDTO> ponto = rmBusiness.obterBatidasUsuarioPeriodo(usuario, 7, "000040", getPeriodoInicial(), getPeriodoFinal());
			
			setPontos(ponto);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarAjustePonto" }, e);
		}
	}
	
	private void carregarPeriodo() throws ApplicationException {
		try {
			PeriodoPontoDTO dto = rmBusiness.obterPeriodoAtualFuncionario(7, "000040");
			Calendar periodoInicial = Calendar.getInstance();
			periodoInicial.setTime(dto.getPeriodoInicial());
			Calendar periodoFinal = Calendar.getInstance();
			periodoFinal.setTime(dto.getPeriodoFinal());
			
			setPeriodoInicial(periodoInicial);
			setPeriodoFinal(periodoFinal);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "carregarPeriodo" }, e);
		}
	}
	
	public BatidaDTO obterObjetoMap(HashMap<Integer, BatidaDTO> hash, Integer key) throws ApplicationException {
		try {
			if(Util.isNotNull(hash)) {
				BatidaDTO batida = hash.get(key);
				if(Util.isNotNull(batida)) {
					return batida;
				}
			}
			return new BatidaDTO();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterObjetoMap" }, e);
		}
	}
	
	public Boolean podeEditar(BatidaDTO batida, Integer seq, AjustePontoDTO ponto) throws ApplicationException {
		try {
			if(TreatNumber.isNullOrZero(batida.getBatida())) {
				if(!ponto.getFerias()) {
					if(Util.dataMenor(ponto.getData(), getUltimaColeta())) {
						return Boolean.TRUE;
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "podeEditar" }, e);
		}
	}
	
	public String obterHoras(BatidaDTO batida) throws ApplicationException {
		try {
			if(Util.isNotNull(batida)) {
				if(TreatNumber.isNotNullOrZero(batida.getBatida())) {
					Integer hora = batida.getBatida() / 60;
					Integer minuto = batida.getBatida() % 60;
					
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, hora);
					calendar.set(Calendar.MINUTE, minuto);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					
					return TreatDate.format("HH:mm", calendar.getTime());
				}
			}
			return "";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterHoras" }, e);
		}
	}
	
	public String obterHorasBatida(Integer batida) throws ApplicationException {
		try {
			if(TreatNumber.isNotNullOrZero(batida)) {
				Integer hora = batida / 60;
				Integer minuto = batida % 60;
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, hora);
				calendar.set(Calendar.MINUTE, minuto);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				
				return TreatDate.format("HH:mm", calendar.getTime());
			}
			return "";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterHorasBatida" }, e);
		}
	}
	
	public String obterPeriodoFormatado() {
		return TreatDate.format("dd/MM/yyyy", getPeriodoInicial().getTime()) + " à " + TreatDate.format("dd/MM/yyyy", getPeriodoFinal().getTime()); 
	}
	
	public String obterUltimaColeta() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getUltimaColeta());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return TreatDate.format("dd/MM/yyyy HH:mm", calendar.getTime()); 
	}
	
	public void iniciarModalAjuste(BatidaDTO batida, Integer seq, AjustePontoDTO ponto) throws ApplicationException {
		try {
			if(Util.isNotNull(batida)) {
				setBatidaEdicao(batida);
			}
			if(Util.isNotNull(ponto)) {
				setPontoEdicao(ponto);
			}
			setSequenciaEdicao(seq);
			setHorario(null);
			setJustificativa(null);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarModalAjuste" }, e);
		}
	}

	public void iniciarModalAjusteEdicao(BatidaDTO batida, Integer seq, AjustePontoDTO ponto) throws ApplicationException {
		try {
			if(Util.isNotNull(batida)) {
				setBatidaEdicao(batida);
			}
			if(Util.isNotNull(ponto)) {
				setPontoEdicao(ponto);
			}
			setSequenciaEdicao(seq);
			setHorario(obterHorasBatida(batida.getBatidaPonto().getBatida()));
			setJustificativa(batida.getBatidaPonto().getJustificativa());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarModalAjuste" }, e);
		}
	}
	
	public String obterBatidaFormatada() throws ApplicationException {
		try {
			if(Util.isNotNull(getPontoEdicao())) {
				return TreatDate.format("dd/MM/yyyy", getPontoEdicao().getData());
			}
			return "";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarModalAjuste" }, e);
		}
	}
	
	public void salvarBatida() throws ApplicationException {
		try {
			if(Util.isNull(getHorario())) {
				if(Util.isBlank(getHorario())) {
					throw new ApplicationException("ajuste.ponto.campos", FacesMessage.SEVERITY_WARN);
				}
			} else {
				String horasString = getHorario().substring(0, 2);
				String minutosString = getHorario().substring(3, 5);
				
				Integer horas = Integer.valueOf(horasString);
				Integer minutos = Integer.valueOf(minutosString);
				
				if(horas > new Integer(23)) {
					throw new ApplicationException("ajuste.ponto.horario.invalido", FacesMessage.SEVERITY_WARN);
				}
				if(minutos > new Integer(59)) {
					throw new ApplicationException("ajuste.ponto.horario.invalido", FacesMessage.SEVERITY_WARN);
				}
				
			}
			
			if(Util.isNull(getJustificativa())) {
				if(Util.isBlank(getJustificativa())) {
					throw new ApplicationException("ajuste.ponto.campos", FacesMessage.SEVERITY_WARN);
				}
			} else {
				if(getJustificativa().length() > 1000) {
					throw new ApplicationException("message.empty", new String[] {"Máximo 1000 caracteres para a Justificativa."}, FacesMessage.SEVERITY_WARN);
				}
			}
			
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			BatidaPonto batida = null;
			if(Util.isNotNull(getBatidaEdicao().getBatidaPonto())) {
				batida = getBatidaEdicao().getBatidaPonto();
			} else {
				batida = new BatidaPonto();
			}
			batida.setDtBatida(getPontoEdicao().getData());
			batida.setSequencia(getSequenciaEdicao());
			batida.setJustificativa(getJustificativa());
			if(getSequenciaEdicao().equals(1) || getSequenciaEdicao().equals(3) || getSequenciaEdicao().equals(5) || getSequenciaEdicao().equals(7)) {
				batida.setTipo(0);
			} else {
				batida.setTipo(1);
			}
			
			String horasString = getHorario().substring(0, 2);
			String minutosString = getHorario().substring(3, 5);
			
			Integer horas = Integer.valueOf(horasString);
			Integer minutos = Integer.valueOf(minutosString);
			
			Integer inteiroHora = horas * 60;
			Double minutoDec = minutos / new Double(60);
			Integer inteiroMinuto = (int) (minutoDec * 60);
			
			batida.setBatida(inteiroHora + inteiroMinuto);
			
			pontoBusiness.salvar(usuario, getPeriodoInicial().getTime(), getPeriodoFinal().getTime(), getFuncionario().getCodSecao(), getFuncionario().getSecao(), batida,  getPontoEdicao());
			
			PrimeFaces.current().executeScript("PF('modalBatida').hide();");
			PrimeFaces.current().ajax().update("ajustarPontoForm");
			
			iniciarAjustePonto();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvarBatida" }, e);
		}
	}


	public String getChapa() {
		return chapa;
	}

	public void setChapa(String chapa) {
		this.chapa = chapa;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Calendar getPeriodoInicial() {
		return periodoInicial;
	}

	public void setPeriodoInicial(Calendar periodoInicial) {
		this.periodoInicial = periodoInicial;
	}

	public Calendar getPeriodoFinal() {
		return periodoFinal;
	}

	public void setPeriodoFinal(Calendar periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

	public List<AjustePontoDTO> getPontos() {
		return pontos;
	}

	public void setPontos(List<AjustePontoDTO> pontos) {
		this.pontos = pontos;
	}

	public Date getUltimaColeta() {
		return ultimaColeta;
	}

	public void setUltimaColeta(Date ultimaColeta) {
		this.ultimaColeta = ultimaColeta;
	}

	public FuncionarioRM getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioRM funcionario) {
		this.funcionario = funcionario;
	}

	public BatidaDTO getBatidaEdicao() {
		return batidaEdicao;
	}

	public void setBatidaEdicao(BatidaDTO batidaEdicao) {
		this.batidaEdicao = batidaEdicao;
	}

	public Integer getSequenciaEdicao() {
		return sequenciaEdicao;
	}

	public void setSequenciaEdicao(Integer sequenciaEdicao) {
		this.sequenciaEdicao = sequenciaEdicao;
	}

	public AjustePontoDTO getPontoEdicao() {
		return pontoEdicao;
	}

	public void setPontoEdicao(AjustePontoDTO pontoEdicao) {
		this.pontoEdicao = pontoEdicao;
	}

	public Date getDtEdicao() {
		return dtEdicao;
	}

	public void setDtEdicao(Date dtEdicao) {
		this.dtEdicao = dtEdicao;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

}
