package br.com.grupojcr.ws.response;

public class SolicitacaoCompraResponse {
	
	private Long idSolicitacao;
	private String solicitante;
	private String dataSolicitacao;
	private String usuarioAprovador;
	private String motivoCompra;
	private String valorEstimado;
	private String listaCotacao;
	
	public String getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	public Long getIdSolicitacao() {
		return idSolicitacao;
	}
	public void setIdSolicitacao(Long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}
	public String getDataSolicitacao() {
		return dataSolicitacao;
	}
	public void setDataSolicitacao(String dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}
	public String getUsuarioAprovador() {
		return usuarioAprovador;
	}
	public void setUsuarioAprovador(String usuarioAprovador) {
		this.usuarioAprovador = usuarioAprovador;
	}
	public String getMotivoCompra() {
		return motivoCompra;
	}
	public void setMotivoCompra(String motivoCompra) {
		this.motivoCompra = motivoCompra;
	}
	public String getValorEstimado() {
		return valorEstimado;
	}
	public void setValorEstimado(String valorEstimado) {
		this.valorEstimado = valorEstimado;
	}
	public String getListaCotacao() {
		return listaCotacao;
	}
	public void setListaCotacao(String listaCotacao) {
		this.listaCotacao = listaCotacao;
	}

}
