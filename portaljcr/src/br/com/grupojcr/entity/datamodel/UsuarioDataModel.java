package br.com.grupojcr.entity.datamodel;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.grupojcr.business.UsuarioBusiness;
import br.com.grupojcr.dto.FiltroUsuario;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.Message;

public class UsuarioDataModel extends LazyDataModel<Usuario> {

	private static Logger log = Logger.getLogger(UsuarioDataModel.class);
	private static final long serialVersionUID = -4141373083662145792L;
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	private FiltroUsuario filtro;
	
	private List<Usuario> dataSource;
	
	@EJB
	private UsuarioBusiness usuarioBusiness;
	
	@Override
	public Usuario getRowData(String rowKey) {
		for(Usuario usuario : getDataSource()) {
			if(usuario.getId().toString().equals(rowKey)) {
				return usuario;
			}
		}
		return null;
	}
	
	@Override
	public Object getRowKey(Usuario usuario) {
		return usuario.getId();
	}
	
	@Override
	public List<Usuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		try {
			this.setRowCount(usuarioBusiness.obterQtdUsuario(getFiltro()));
			if(getRowCount() > 0){
				this.setDataSource(usuarioBusiness.listarUsuarioPaginado(first, pageSize, getFiltro()));
			}
		} catch(ApplicationException appEx)	{
			Message.setMessage(appEx);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			Message.setMessage(KEY_MENSAGEM_PADRAO, new String[] { "listar" }, FacesMessage.SEVERITY_ERROR);
		}
		return getDataSource();
	}

	public FiltroUsuario getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroUsuario filtro) {
		this.filtro = filtro;
	}

	public List<Usuario> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<Usuario> dataSource) {
		this.dataSource = dataSource;
	}

}
