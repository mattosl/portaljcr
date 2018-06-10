package br.com.grupojcr.entity.datamodel;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.grupojcr.business.ChamadoBusiness;
import br.com.grupojcr.dto.FiltroRelatorioChamado;
import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.Message;

public class RelatorioChamadoDataModel extends LazyDataModel<Chamado> {

	private static Logger log = Logger.getLogger(RelatorioChamadoDataModel.class);
	private static final long serialVersionUID = -4141373083662145792L;
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	private FiltroRelatorioChamado filtro;
	
	private List<Chamado> dataSource;
	
	@EJB
	private ChamadoBusiness chamadoBusiness;
	
	@Override
	public Chamado getRowData(String rowKey) {
		for(Chamado chamado : getDataSource()) {
			if(chamado.getId().toString().equals(rowKey)) {
				return chamado;
			}
		}
		return null;
	}
	
	@Override
	public Object getRowKey(Chamado chamado) {
		return chamado.getId();
	}
	
	@Override
	public List<Chamado> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		try {
			this.setRowCount(chamadoBusiness.obterQtdChamadoRelatorio(getFiltro()));
			if(getRowCount() > 0){
				this.setDataSource(chamadoBusiness.listarChamadoPaginadoRelatorio(first, pageSize, getFiltro()));
			}
		} catch(ApplicationException appEx)	{
			Message.setMessage(appEx);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			Message.setMessage(KEY_MENSAGEM_PADRAO, new String[] { "listar" }, FacesMessage.SEVERITY_ERROR);
		}
		return getDataSource();
	}

	public FiltroRelatorioChamado getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroRelatorioChamado filtro) {
		this.filtro = filtro;
	}

	public List<Chamado> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<Chamado> dataSource) {
		this.dataSource = dataSource;
	}


}
