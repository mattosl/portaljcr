package br.com.grupojcr.entity.datamodel;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.dto.FiltroSolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.Message;

public class SolicitacaoCompraDataModel extends LazyDataModel<SolicitacaoCompra> {

	private static Logger log = Logger.getLogger(SolicitacaoCompraDataModel.class);
	private static final long serialVersionUID = -4141373083662145792L;
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	private FiltroSolicitacaoCompra filtro;
	
	private List<SolicitacaoCompra> dataSource;
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@Override
	public SolicitacaoCompra getRowData(String rowKey) {
		for(SolicitacaoCompra solicitacao : getDataSource()) {
			if(solicitacao.getId().toString().equals(rowKey)) {
				return solicitacao;
			}
		}
		return null;
	}
	
	@Override
	public Object getRowKey(SolicitacaoCompra solicitacao) {
		return solicitacao.getId();
	}
	
	@Override
	public List<SolicitacaoCompra> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		try {
			this.setRowCount(solicitacaoCompraBusiness.obterQtdSolicitacaoCompra(getFiltro()));
			if(getRowCount() > 0){
				this.setDataSource(solicitacaoCompraBusiness.listarSolicitacaoCompraPaginado(first, pageSize, getFiltro()));
			}
		} catch(ApplicationException appEx)	{
			Message.setMessage(appEx);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			Message.setMessage(KEY_MENSAGEM_PADRAO, new String[] { "listar" }, FacesMessage.SEVERITY_ERROR);
		}
		return getDataSource();
	}

	public FiltroSolicitacaoCompra getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroSolicitacaoCompra filtro) {
		this.filtro = filtro;
	}

	public List<SolicitacaoCompra> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<SolicitacaoCompra> dataSource) {
		this.dataSource = dataSource;
	}


}
