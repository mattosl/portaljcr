package br.com.grupojcr.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.dto.FiltroUsuario;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class UsuarioBusiness {
	
	private static Logger LOG = Logger.getLogger(UsuarioBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private UsuarioDAO daoUsuario;
	
	/**
	 * Método responsavel por consultar usuários cadastrados
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 21/05/2018
	 * @param filtro : FiltroUsuario
	 * @throws ApplicationException
	 */
	public Integer obterQtdUsuario(FiltroUsuario filtro) throws ApplicationException {
		try {
			return daoUsuario.obterQtdUsuario(filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterQtdUsuario" }, e);
		}
	}
	
	/**
	 * Método responsavel por listar usuários paginados
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 21/05/2018
	 * @param first : int
	 * @param pageSize : int
	 * @param filtro : FiltroUsuario
	 * @return List<Usuario>
	 * @throws ApplicationException
	 */
	public List<Usuario> listarUsuarioPaginado(int first, int pageSize, FiltroUsuario filtro) throws ApplicationException {
		try {
			return daoUsuario.listarUsuarioPaginado(first, pageSize, filtro);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarUsuarioPaginado" }, e);
		}
	}
	
	/**
	 * Método responsavel por listar usuários por nome
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 21/05/2018
	 * @param nome : String
	 * @return List<Usuario>
	 * @throws ApplicationException
	 */
	public List<Usuario> listarUsuarioPorNome(String nome) throws ApplicationException {
		try {
			if(TreatString.isNotBlank(nome)) {
				return daoUsuario.listarUsuarioPorNome(nome);
			} else {
				throw new ApplicationException("message.empty", new String [] {"Nome é obrigatório"}, FacesMessage.SEVERITY_WARN);
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarUsuarioPorNome" }, e);
		}
	}


	/**
	 * Método responsavel por alterar usuário
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 21/05/2018
	 * @param usuario : Usuario
	 * @throws ApplicationException
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void alterar(Usuario usuario) throws ApplicationException {
		try {
			daoUsuario.alterar(usuario);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "alterar" }, e);
		}
	}

}
