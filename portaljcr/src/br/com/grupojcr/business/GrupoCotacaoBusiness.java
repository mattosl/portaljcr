package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.GrupoCotacaoDAO;
import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class GrupoCotacaoBusiness {

	protected static Logger LOG = Logger.getLogger(GrupoCotacaoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private GrupoCotacaoDAO daoGrupoCotacao;

	@EJB
	private UsuarioDAO daoUsuario;

	public List<GrupoCotacao> listar() throws ApplicationException {
		try {
			return daoGrupoCotacao.listarGrupoCotacao();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listar" }, e);
		}
	}
	
	public void salvar(GrupoCotacao gc) throws ApplicationException {
		try {
			if(TreatString.isBlank(gc.getNome())) {
				throw new ApplicationException("message.empty", new String[] {"Nome do grupo de cotação é obrigatório"},  FacesMessage.SEVERITY_ERROR);
			}
			if(CollectionUtils.isEmpty(gc.getUsuarios())) {
				throw new ApplicationException("grupo.cotacao.usuario.vazio", FacesMessage.SEVERITY_WARN);
			}
			if(daoGrupoCotacao.verificarGrupoCotacaoExiste(gc.getNome())) {
				throw new ApplicationException("grupo.cotacao.existente", FacesMessage.SEVERITY_WARN);
			}
			
			if(Util.isNotNull(gc.getId())) {
				daoGrupoCotacao.alterar(gc);
			} else {
				GrupoCotacao grupoCotacao = new GrupoCotacao();
				grupoCotacao.setNome(gc.getNome());
				grupoCotacao.setSituacao(Boolean.TRUE);
				grupoCotacao.setUsuarios(new HashSet<Usuario>());
				List<Usuario> usuarios = new ArrayList<Usuario>();
				for(Usuario usr : gc.getUsuarios()) {
					Usuario u = daoUsuario.obter(usr.getId());
					usuarios.add(u);
				}
				grupoCotacao.getUsuarios().addAll(usuarios);
				daoGrupoCotacao.incluir(grupoCotacao);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
}
