package br.com.grupojcr.business;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.UsuarioDAO;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class LoginBusiness {

	protected static Logger LOG = Logger.getLogger(LoginBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private UsuarioDAO daoUsuario;

	public Usuario obterUsuarioPorLoginSenha(String usuario, String senha) throws ApplicationException {
		try {
			return daoUsuario.obterUsuarioPorLoginSenha(usuario, senha);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "obterUsuarioPorLoginSenha" }, e);
		}
	}

	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void alterarUsuario(Usuario usuario) throws ApplicationException {
		try {
			daoUsuario.alterar(usuario);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
