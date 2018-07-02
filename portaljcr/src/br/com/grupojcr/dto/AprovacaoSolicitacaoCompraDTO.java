package br.com.grupojcr.dto;

public class AprovacaoSolicitacaoCompraDTO {
	
	private Integer idFluig;
	private String tipo;
	private Long numeroSolicitacao;
	private String nomeColigada;
	private String codigoCentroCusto;
	private String centroCusto;
	private String requisitante;
	private String valorAproximado;
	private Integer sequenciaMovimento;
	
	public String getNomeColigada() {
		return nomeColigada;
	}
	public void setNomeColigada(String nomeColigada) {
		this.nomeColigada = nomeColigada;
	}
	public String getCodigoCentroCusto() {
		return codigoCentroCusto;
	}
	public void setCodigoCentroCusto(String codigoCentroCusto) {
		this.codigoCentroCusto = codigoCentroCusto;
	}
	public String getCentroCusto() {
		return centroCusto;
	}
	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}
	public String getRequisitante() {
		return requisitante;
	}
	public void setRequisitante(String requisitante) {
		this.requisitante = requisitante;
	}
	public Integer getIdFluig() {
		return idFluig;
	}
	public void setIdFluig(Integer idFluig) {
		this.idFluig = idFluig;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getSequenciaMovimento() {
		return sequenciaMovimento;
	}
	public void setSequenciaMovimento(Integer sequenciaMovimento) {
		this.sequenciaMovimento = sequenciaMovimento;
	}
	public Long getNumeroSolicitacao() {
		return numeroSolicitacao;
	}
	public void setNumeroSolicitacao(Long numeroSolicitacao) {
		this.numeroSolicitacao = numeroSolicitacao;
	}
	public String getValorAproximado() {
		return valorAproximado;
	}
	public void setValorAproximado(String valorAproximado) {
		this.valorAproximado = valorAproximado;
	}
	

}
