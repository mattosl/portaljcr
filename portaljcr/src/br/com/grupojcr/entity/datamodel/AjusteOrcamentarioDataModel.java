package br.com.grupojcr.entity.datamodel;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.grupojcr.business.OrcamentoBusiness;
import br.com.grupojcr.dto.AjusteOrcamentarioDTO;
import br.com.grupojcr.dto.FiltroOrcamento;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.Message;

public class AjusteOrcamentarioDataModel extends LazyDataModel<AjusteOrcamentarioDTO> {

	private static Logger log = Logger.getLogger(AjusteOrcamentarioDataModel.class);
	private static final long serialVersionUID = -4141373083662145792L;
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	private FiltroOrcamento filtro;
	
	private List<AjusteOrcamentarioDTO> dataSource;
	
	@EJB
	private OrcamentoBusiness orcamentoBusiness;
	
	@Override
	public AjusteOrcamentarioDTO getRowData(String rowKey) {
		for(AjusteOrcamentarioDTO dto : getDataSource()) {
			if(dto.getId().toString().equals(rowKey)) {
				return dto;
			}
		}
		return null;
	}
	
	@Override
	public Object getRowKey(AjusteOrcamentarioDTO dto) {
		return dto.getId();
	}
	
	@Override
	public List<AjusteOrcamentarioDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		try {
			this.setRowCount(orcamentoBusiness.obterQtdAjusteOrcamentario(getFiltro()));
			if(getRowCount() > 0){
				this.setDataSource(orcamentoBusiness.listarAjusteOrcamentarioPaginado(first, pageSize, getFiltro()));
			}
		} catch(ApplicationException appEx)	{
			Message.setMessage(appEx);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			Message.setMessage(KEY_MENSAGEM_PADRAO, new String[] { "listar" }, FacesMessage.SEVERITY_ERROR);
		}
		return getDataSource();
	}

	public List<AjusteOrcamentarioDTO> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<AjusteOrcamentarioDTO> dataSource) {
		this.dataSource = dataSource;
	}

	public FiltroOrcamento getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroOrcamento filtro) {
		this.filtro = filtro;
	}


}
