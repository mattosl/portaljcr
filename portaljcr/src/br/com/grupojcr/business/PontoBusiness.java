package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

import br.com.grupojcr.dao.AjustePontoDAO;
import br.com.grupojcr.dao.BatidaPontoDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.dto.AjustePontoDTO;
import br.com.grupojcr.dto.BatidaDTO;
import br.com.grupojcr.dto.PeriodoPontoDTO;
import br.com.grupojcr.entity.AjustePonto;
import br.com.grupojcr.entity.BatidaPonto;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.SituacaoAjustePonto;
import br.com.grupojcr.rm.BatidaRM;
import br.com.grupojcr.rm.FuncionarioRM;
import br.com.grupojcr.util.Preferencias;
import br.com.grupojcr.util.Preferencias.Propriedades;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class PontoBusiness {
	
	private static Logger LOG = Logger.getLogger(PontoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	private final static String PROCESSO_APROVACAO_PONTO = Preferencias.get(Propriedades.FLUIG_APROVACAO_PONTO);

	@EJB
	private AjustePontoDAO daoAjustePonto;

	@EJB
	private BatidaPontoDAO daoBatidaPonto;
	
	@EJB
	private UsuarioDAO daoUsuario;
	
	@EJB
	private FluigBusiness fluigBusiness;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private RMDAO daoRM;
	
	public BatidaPonto salvar(Usuario usuario, Date periodoInicial, Date periodoFinal, String codsecao, String secao, BatidaPonto batida, AjustePontoDTO ajustePontoDTO) throws ApplicationException {
		try {
			AjustePonto ajustePonto = daoAjustePonto.obterAjustePonto(usuario.getId(), periodoInicial, periodoFinal);
			
			if(Util.isNull(ajustePonto)) {
				ajustePonto = new AjustePonto();
				ajustePonto.setUsuario(usuario);
				ajustePonto.setChapa(usuario.getChapa());
				ajustePonto.setDtAjuste(Calendar.getInstance().getTime());
				ajustePonto.setDtPeriodoInicial(periodoInicial);
				ajustePonto.setDtPeriodoFinal(periodoFinal);
				ajustePonto.setSituacao(SituacaoAjustePonto.RASCUNHO);
				ajustePonto.setCodigoSecao(codsecao);
				ajustePonto.setSecao(secao);
				
				daoAjustePonto.incluir(ajustePonto);
			}
			
			BatidaPonto batidaExiste = daoBatidaPonto.obterBatida(batida.getDtBatida(), batida.getBatida(), ajustePonto.getId(), batida.getId());
			if(Util.isNotNull(batidaExiste)) {
				throw new ApplicationException("ajuste.ponto.batida.existe", FacesMessage.SEVERITY_WARN);
			}
			
			if(validarExisteBatida(ajustePontoDTO, batida.getBatida())) {
				throw new ApplicationException("ajuste.ponto.batida.existe", FacesMessage.SEVERITY_WARN);
			}
			
			batida.setAjuste(ajustePonto);
			
			if(Util.isNotNull(batida.getId())) {
				daoBatidaPonto.alterar(batida);
			} else {
				daoBatidaPonto.incluir(batida);
			}
			
			return batida;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	public void excluir(BatidaPonto batida) throws ApplicationException {
		try {
			BatidaPonto batidaBanco = daoBatidaPonto.obter(batida.getId());
			daoBatidaPonto.excluir(batidaBanco);
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluir" }, e);
		}
	}
	
	public Boolean validarExisteBatida(AjustePontoDTO ajustePontoDTO, Integer batida) throws ApplicationException {
		try {
			if(Util.isNotNull(ajustePontoDTO)) {
				for(Integer key : ajustePontoDTO.getBatidas().keySet()) {
					BatidaDTO batidaDTO = ajustePontoDTO.getBatidas().get(key);
					if(Util.isNotNull(batidaDTO.getBatida())) {
						if(batidaDTO.getBatida().equals(batida)) {
							if(Util.isNull(batidaDTO.getBatidaPonto())) {
								return Boolean.TRUE;
							}
						}
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "validarExisteBatida" }, e);
		}
	}
	
	public void enviarPontoAprovacao(List<AjustePontoDTO> ajustes, Date periodoInicial, Date periodoFinal, Usuario usuario, FuncionarioRM funcionario) throws ApplicationException {
		try {
			if(Util.isBlank(usuario.getUsuarioFluigGestor())) {
				throw new ApplicationException("message.empty", new String[] {"Usuário não possui gestor responsável cadastrado."}, FacesMessage.SEVERITY_WARN);
			}
			AjustePonto ajustePonto = obterAjustePonto(usuario.getId(), periodoInicial, periodoFinal);
			
			if(Util.isNull(ajustePonto)) {
				ajustePonto = new AjustePonto();
				ajustePonto.setUsuario(usuario);
				ajustePonto.setChapa(usuario.getChapa());
				ajustePonto.setDtAjuste(Calendar.getInstance().getTime());
				ajustePonto.setDtPeriodoInicial(periodoInicial);
				ajustePonto.setDtPeriodoFinal(periodoFinal);
				ajustePonto.setSituacao(SituacaoAjustePonto.RASCUNHO);
				ajustePonto.setCodigoSecao(funcionario.getCodSecao());
				ajustePonto.setSecao(funcionario.getSecao());
				
				daoAjustePonto.incluir(ajustePonto);
			}
			
			if(!ajustePonto.getSituacao().equals(SituacaoAjustePonto.RASCUNHO)) {
				throw new ApplicationException("message.empty", new String[] {"Cartão Ponto não pode ser enviado novamente para aprovação."}, FacesMessage.SEVERITY_WARN);
			}
			
			StringBuilder itens = new StringBuilder("{\"pontos\": [");
			int tamanho = itens.length();
			int maxQtdPonto = 0;
			
			for(AjustePontoDTO dto : ajustes) {
				if(dto.getBatidas().size() > maxQtdPonto) {
					maxQtdPonto = dto.getBatidas().size();
				}
				
				if(itens.length() > tamanho) {
					itens.append(",");
				}
				
				itens.append("{\"dataPonto\": \"" + TreatDate.format("dd/MM/yyyy", dto.getData()) + 
							"\", \"nomeDia\": \"" + dto.getNomeDia() +
							"\", \"finalSemana\": \"" + dto.getFinalSemana() +
							"\", \"feriado\": \"" + dto.getFeriado() +
							"\", \"ferias\": \"" + dto.getFerias() + "\",");
					
				for(Integer key : dto.getBatidas().keySet()) {
					itens.append("\"batida" + key + "\": {" + 
									"\"horario\": \"" + obterHorasBatida(dto.getBatidas().get(key).getBatida()) + "\"," +
									"\"editado\": \"" + dto.getBatidas().get(key).getEditado() + "\"," +
									"\"justificativa\": \"" + (dto.getBatidas().get(key).getEditado() ? dto.getBatidas().get(key).getBatidaPonto().getJustificativa().toUpperCase().replaceAll("(\r\n|\n)", "&#010;") : "")  +
								"\"},");
				}
				
				Integer idx = dto.getBatidas().keySet().size();
				idx++;
				for(int i = idx; i <= 10; i++) {
					itens.append("\"batida" + i + "\": {" + 
									"\"horario\": \"" + "" + "\"," +
									"\"editado\": \"" + "" +
								"\"}");
					
					if(i != 10) {
						itens.append(",");
					}
					
				}
				
				itens.append("}");
			}
			
			itens.append("]}");
			
			Integer qtdPonto = maxQtdPonto / 2;
			
			String [][] parametros = new String[][] { 
				{ "idAjustePonto", ajustePonto.getId().toString()}, 
				{ "empresa", funcionario.getEmpresa()},
				{ "nomeFuncionario", funcionario.getNomeFuncionario()},
				{ "cargo", funcionario.getFuncao()}, 
				{ "dtInicio", TreatDate.format("dd/MM/yyyy", periodoInicial)},
				{ "dtFim", TreatDate.format("dd/MM/yyyy", periodoFinal)},
				{ "qtdPonto", qtdPonto.toString()},
				{ "itens", itens.toString()} 
			};
			
			// Inicia processo do Fluig
			String[][] resultado = fluigBusiness.iniciarProcessoFluig(PROCESSO_APROVACAO_PONTO, usuario.getUsuarioFluigGestor(), 5, parametros);

			for(int i = 0; i < resultado.length; i++) {
				for(int j = 0; j < resultado[i].length; j++) {
					if(resultado[i][j].equals("iProcess")) {
						try {
							Long idFluig = Long.parseLong((resultado[i][j + 1]).toString());
							ajustePonto.setIdentificadorFluig(idFluig);
							ajustePonto.setUsrFluig(usuario.getUsuarioFluigGestor());
							ajustePonto.setSituacao(SituacaoAjustePonto.AGUARDANDO_APROVACAO);
							
							daoAjustePonto.alterar(ajustePonto);
							
							break;
						} catch(NumberFormatException e) {
						}
					}
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "enviarPontoAprovacao" }, e);
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
	
	public void aprovar(Long idAjustePonto) throws ApplicationException {
		try {
			AjustePonto ajuste = daoAjustePonto.obterAjustePontoPorId(idAjustePonto);
			
			if(CollectionUtils.isNotEmpty(ajuste.getBatidas())) {
				FuncionarioRM funcionario = rmBusiness.obterDadosFuncionario(ajuste.getChapa());
				
				for(BatidaPonto batida : ajuste.getBatidas()) {
					BatidaRM batidaExiste = daoRM.obterBatida(funcionario.getCodColigada(), funcionario.getChapa(), batida.getDtBatida(), batida.getBatida());
					if(Util.isNull(batidaExiste)) {
						daoRM.incluirBatida(funcionario.getCodColigada(), funcionario.getChapa(), batida.getDtBatida(), batida.getBatida(), batida.getTipo());
					}
				}
				
				for(BatidaPonto batida : ajuste.getBatidas()) {
					List<BatidaRM> batidasDia = daoRM.obterBatidasUsuarioDia(funcionario.getCodColigada(), funcionario.getChapa(), batida.getDtBatida());
					
					Integer tipo = 0;
					for(BatidaRM brm : batidasDia) {
						daoRM.atualizarTipoBatida(funcionario.getCodColigada(), funcionario.getChapa(), brm.getData(), brm.getBatida(), tipo);
						if(tipo.equals(0)) {
							tipo = 1;
						} else if (tipo.equals(1)) {
							tipo = 0;
						}
					}
					
				}
			}
			ajuste.setSituacao(SituacaoAjustePonto.APROVADO);
			
			daoAjustePonto.alterar(ajuste);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "aprovar" }, e);
		}
	}
	
	public void recusar(Long idAjustePonto, String motivo) throws ApplicationException {
		try {
			AjustePonto ajuste = daoAjustePonto.obter(idAjustePonto);
			ajuste.setSituacao(SituacaoAjustePonto.RASCUNHO);
			ajuste.setMotivoRecusa(motivo);
			
			daoAjustePonto.alterar(ajuste);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "recusar" }, e);
		}
	}
	
	public AjustePonto obterAjustePonto(Long idUsuario, Date periodoInicial, Date periodoFinal) throws ApplicationException {
		try {
			return daoAjustePonto.obterAjustePonto(idUsuario, periodoInicial, periodoFinal);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterAjustePonto" }, e);
		}
	}
	
	public List<BatidaPonto> listarBatidaPorPeriodo(Long idUsuario, Date periodoInicial, Date periodoFinal) throws ApplicationException {
		try {
			return daoBatidaPonto.listarBatidaPorPeriodo(periodoInicial, periodoFinal, idUsuario);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarBatidaPorPeriodo" }, e);
		}
	}
	
	public List<Usuario> listarUsuariosUtilizamPonto() throws ApplicationException {
		try {
			List<Usuario> usuariosComChapa = daoUsuario.listarUsuarioComChapa();
			List<Usuario> usuariosFiltrados = new ArrayList<Usuario>();
			
			for(Usuario usuario : usuariosComChapa) {
				FuncionarioRM funcionario = rmBusiness.obterDadosFuncionario(usuario.getChapa());
				if(Util.isNotNull(funcionario)) {
					PeriodoPontoDTO periodo = rmBusiness.obterPeriodoAtualFuncionario(funcionario.getCodColigada(), funcionario.getChapa());
					if(Util.isNotNull(periodo)) {
						Boolean utiliza = rmBusiness.verificarUtilizaPonto(funcionario.getCodColigada(), funcionario.getChapa(), periodo.getPeriodoFinal());
						if(utiliza) {
							usuariosFiltrados.add(usuario);
						}
					}
				}
			}
			
			return usuariosFiltrados;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarUsuariosUtilizamPonto" }, e);
		}
	}
	
}
