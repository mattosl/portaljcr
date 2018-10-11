package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
public class ConsultarPontoController implements Serializable {
	
	private static final long serialVersionUID = -8044745000788576246L;
	protected static Logger LOG = Logger.getLogger(ConsultarPontoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private String chapa;
	private String chave;
	private String justificativa;
	private String horario;
	
	private Calendar periodoInicial;
	private Calendar periodoFinal;
	private Date filtroInicio;
	private Date filtroFim;
	
	private Integer sequenciaEdicao;
	
	private Boolean exibirResultado;
	
	private FuncionarioRM funcionario;
	private Usuario usuario;
	private PeriodoPontoDTO periodoFiltro;

	
	private List<AjustePontoDTO> pontos;
	private List<PeriodoPontoDTO> periodos;
	private List<BatidaPonto> batidas;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private PontoBusiness pontoBusiness;
	
	
	public void iniciarProcesso() throws ApplicationException {
		try {
			iniciarDados();
			setExibirResultado(Boolean.FALSE);
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
	
	public void pesquisar() throws ApplicationException {
		try {
			
			if(Util.isNull(getFiltroInicio())) {
				throw new ApplicationException("consultar.ponto.periodo.obrigatorio", FacesMessage.SEVERITY_WARN);
			}
			
			if(Util.isNull(getFiltroFim())) {
				throw new ApplicationException("consultar.ponto.periodo.obrigatorio", FacesMessage.SEVERITY_WARN);
			}
			
			Integer dias = TreatDate.contarDiferencaEmDias(getFiltroInicio(), getFiltroFim());
			
			if(dias > 180) {
				throw new ApplicationException("consultar.ponto.máximo.dias", FacesMessage.SEVERITY_WARN);
			}
			
			Calendar inicio = Calendar.getInstance();
			inicio.setTime(getFiltroInicio());
			Calendar fim = Calendar.getInstance();
			fim.setTime(getFiltroFim());
			
			setPontos(rmBusiness.obterBatidasUsuarioPeriodo(getUsuario(), getFuncionario().getCodColigada(), getFuncionario().getChapa(), inicio, fim));
			
			if(getPontos().size() == 0) {
				setExibirResultado(Boolean.FALSE);
				throw new ApplicationException("message.datatable.noRecords", FacesMessage.SEVERITY_WARN);
			}
			setBatidas(pontoBusiness.listarBatidaPorPeriodo(getUsuario().getId(), getFiltroInicio(), getFiltroFim()));
			
			setExibirResultado(Boolean.TRUE);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "pesquisar" }, e);
		}
	}
	
	
	private void iniciarDados() throws ApplicationException {
		try {
			
			setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
			if(Util.isBlank(getUsuario().getChapa())) {
				throw new ApplicationException("ajuste.ponto.nao.utiliza", FacesMessage.SEVERITY_FATAL);
			}
			setFuncionario(rmBusiness.obterDadosFuncionario(getUsuario().getChapa()));
			
			carregarPeriodo();
			
			Boolean utilizaPonto = rmBusiness.verificarUtilizaPonto(getFuncionario().getCodColigada(), getFuncionario().getChapa(), getPeriodoFinal().getTime());
			
			if(!utilizaPonto) {
				throw new ApplicationException("ajuste.ponto.nao.utiliza", FacesMessage.SEVERITY_FATAL);
			}
			
			setBatidas(new ArrayList<BatidaPonto>());
			setFiltroInicio(null);
			setFiltroFim(null);
			
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
			PeriodoPontoDTO dto = rmBusiness.obterPeriodoAtualFuncionario(getFuncionario().getCodColigada(), getFuncionario().getChapa());
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
	
	public Date obterDataMaxima() throws ApplicationException {
		try {
			if(Util.isNotNull(getPeriodoInicial())) {
				return getPeriodoInicial().getTime();
			}
			return Calendar.getInstance().getTime();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterDataMaxima" }, e);
		}
	}
	
	public String formatarData(PeriodoPontoDTO periodo) throws ApplicationException {
		try {
			return TreatDate.format("dd/MM/yyyy", periodo.getPeriodoInicial()) + " à " + TreatDate.format("dd/MM/yyyy", periodo.getPeriodoFinal());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "formatarData" }, e);
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

	public FuncionarioRM getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioRM funcionario) {
		this.funcionario = funcionario;
	}


	public Integer getSequenciaEdicao() {
		return sequenciaEdicao;
	}

	public void setSequenciaEdicao(Integer sequenciaEdicao) {
		this.sequenciaEdicao = sequenciaEdicao;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Date getFiltroInicio() {
		return filtroInicio;
	}

	public void setFiltroInicio(Date filtroInicio) {
		this.filtroInicio = filtroInicio;
	}

	public Date getFiltroFim() {
		return filtroFim;
	}

	public void setFiltroFim(Date filtroFim) {
		this.filtroFim = filtroFim;
	}

	public Boolean getExibirResultado() {
		return exibirResultado;
	}

	public void setExibirResultado(Boolean exibirResultado) {
		this.exibirResultado = exibirResultado;
	}

	public List<PeriodoPontoDTO> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<PeriodoPontoDTO> periodos) {
		this.periodos = periodos;
	}

	public PeriodoPontoDTO getPeriodoFiltro() {
		return periodoFiltro;
	}

	public void setPeriodoFiltro(PeriodoPontoDTO periodoFiltro) {
		this.periodoFiltro = periodoFiltro;
	}

	public List<BatidaPonto> getBatidas() {
		return batidas;
	}

	public void setBatidas(List<BatidaPonto> batidas) {
		this.batidas = batidas;
	}

}
