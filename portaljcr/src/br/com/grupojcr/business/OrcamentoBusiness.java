package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.AjusteOrcamentarioDAO;
import br.com.grupojcr.dao.GrupoDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dao.ResponsavelOrcamentoDAO;
import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.FiltroOrcamento;
import br.com.grupojcr.entity.AjusteOrcamentario;
import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Grupo;
import br.com.grupojcr.entity.ResponsavelOrcamento;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.Mes;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class OrcamentoBusiness {
	
	private static Logger LOG = Logger.getLogger(OrcamentoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	private final static String GRUPO_RESPONSAVEL = "RESPONSAVEL ORCAMENTO";
	
	@EJB
	private AjusteOrcamentarioDAO daoAjuste;
	
	@EJB
	private RMDAO daoRM;
	
	@EJB
	private ResponsavelOrcamentoDAO daoResponsavelOrcamento;
	
	@EJB
	private GrupoDAO daoGrupo;
	
	@EJB
	private UsuarioDAO daoUsuario;

	public Integer obterQtdAjusteOrcamentario(FiltroOrcamento filtro) throws ApplicationException {
		try {
			return daoAjuste.obterQtdAjusteOrcamentario(filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdAjusteOrcamentario" }, e);
		}
	}
	
	public List<AjusteOrcamentarioDTO> listarAjusteOrcamentarioPaginado(int first, int pageSize, FiltroOrcamento filtro) throws ApplicationException {
		try {
			List<AjusteOrcamentario> ajustesOrcamentario = daoAjuste.listarAjusteOrcamentarioPaginado(first, pageSize, filtro);
			List<AjusteOrcamentarioDTO> listaDTO = new ArrayList<AjusteOrcamentarioDTO>();
			if(CollectionUtils.isNotEmpty(ajustesOrcamentario)) {
				for(AjusteOrcamentario ajuste : ajustesOrcamentario) {
					AjusteOrcamentarioDTO dto = new AjusteOrcamentarioDTO();
					dto.setColigada(ajuste.getColigada());
					dto.setCentroCusto(new CentroCustoRM());
					dto.getCentroCusto().setCodigoCentroCusto(ajuste.getCodigoCentroCusto());
					dto.getCentroCusto().setCentroCusto(ajuste.getCentroCusto());
					dto.setNaturezaOrigem(daoRM.obterNaturezaOrcamentaria(ajuste.getIdNaturezaOrigem()));
					dto.setNaturezaDestino(daoRM.obterNaturezaOrcamentaria(ajuste.getIdNaturezaDestino()));
					dto.setValor(ajuste.getValor());
					dto.setMesOrigem(Mes.obterPorCodigo(ajuste.getMesOrigem()));
					dto.setMesDestino(Mes.obterPorCodigo(ajuste.getMesDestino()));
					dto.setDtAjuste(ajuste.getDtAjuste());
					dto.setUsuario(ajuste.getUsuarioAjuste());
					
					listaDTO.add(dto);
				}
			}
			
			return listaDTO;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarAjusteOrcamentarioPaginado" }, e);
		}
	}
	
	public List<Usuario> listarUsuarioResponsavel(String codigoCentroCusto, Long idColigada) throws ApplicationException {
		try {
			return daoResponsavelOrcamento.listarUsuarioResponsavel(codigoCentroCusto, idColigada);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarUsuarioResponsavel" }, e);
		}
	}
	
	public List<Coligada> listarColigadaResponsavel(Usuario usuario) throws ApplicationException {
		try {
			return daoResponsavelOrcamento.listarColigadaResponsavel(usuario);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarColigadaResponsavel" }, e);
		}
	}
	
	public List<CentroCustoRM> listarCentroCustoResponsavel(Coligada coligada, Usuario usuario) throws ApplicationException {
		try {
			List<ResponsavelOrcamento> listaResponsabilidade = daoResponsavelOrcamento.listarResponsavelPorUsuarioColigada(coligada, usuario);
			List<CentroCustoRM> listaCentroCusto = new ArrayList<CentroCustoRM>();
			for(ResponsavelOrcamento ro : listaResponsabilidade) {
				CentroCustoRM centroCusto = new CentroCustoRM();
				centroCusto.setCodigoCentroCusto(ro.getCodigoCentroCusto());
				centroCusto.setCentroCusto(ro.getCentroCusto());
				
				listaCentroCusto.add(centroCusto);
			}
			
			return listaCentroCusto;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarCentroCustoResponsavel" }, e);
		}
	}

	public ResponsavelOrcamento salvarResponsavel(CentroCustoRM centroCusto, Usuario usuario, Coligada coligada) throws ApplicationException {
		try {
			ResponsavelOrcamento roExiste = daoResponsavelOrcamento.obterResponsavel(centroCusto.getCodigoCentroCusto(), coligada, usuario);
			if(Util.isNull(roExiste)) {
				ResponsavelOrcamento responsavelOrcamento = new ResponsavelOrcamento();
				responsavelOrcamento.setColigada(coligada);
				responsavelOrcamento.setCodigoCentroCusto(centroCusto.getCodigoCentroCusto());
				responsavelOrcamento.setCentroCusto(centroCusto.getCentroCusto());
				responsavelOrcamento.setDtInclusao(Calendar.getInstance().getTime());
				responsavelOrcamento.setUsuarioResponsavel(usuario);
				
				daoResponsavelOrcamento.incluir(responsavelOrcamento);
				Grupo grupo = daoGrupo.obterGrupo(GRUPO_RESPONSAVEL);
				Usuario usr = daoUsuario.obterUsuarioPorId(usuario.getId());
				if(Util.isNotNull(grupo)) {
					if(!usr.getGrupos().contains(grupo)) {
						usr.getGrupos().add(grupo);
						daoUsuario.alterar(usr);
					}
				}
				return responsavelOrcamento;
			}
			
			return null;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}

	public void excluirResponsavel(CentroCustoRM centroCusto, Usuario usuario, Coligada coligada) throws ApplicationException {
		try {
			ResponsavelOrcamento roExiste = daoResponsavelOrcamento.obterResponsavel(centroCusto.getCodigoCentroCusto(), coligada, usuario);
			daoResponsavelOrcamento.excluir(roExiste);
			
			List<ResponsavelOrcamento> responsabilidades = daoResponsavelOrcamento.listarResponsavelPorUsuario(usuario);
			if(CollectionUtils.isEmpty(responsabilidades)) {
				Grupo grupo = daoGrupo.obterGrupo(GRUPO_RESPONSAVEL);
				Usuario usr = daoUsuario.obterUsuarioPorId(usuario.getId());
				if(CollectionUtils.isNotEmpty(usr.getGrupos())) {
					if(Util.isNotNull(grupo)) {
						if(usr.getGrupos().contains(grupo)) {
							usr.getGrupos().remove(grupo);
							daoUsuario.alterar(usr);
						}
					}
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluirResponsavel" }, e);
		}
	}
}
