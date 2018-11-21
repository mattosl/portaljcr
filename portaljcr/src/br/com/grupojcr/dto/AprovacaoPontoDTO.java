package br.com.grupojcr.dto;

public class AprovacaoPontoDTO {
	
	private Integer idFluig;
	private String nomeFuncionario;
	private String periodo;
	private Integer sequenciaMovimento;
	
	public Integer getIdFluig() {
		return idFluig;
	}
	public void setIdFluig(Integer idFluig) {
		this.idFluig = idFluig;
	}
	public Integer getSequenciaMovimento() {
		return sequenciaMovimento;
	}
	public void setSequenciaMovimento(Integer sequenciaMovimento) {
		this.sequenciaMovimento = sequenciaMovimento;
	}
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

}
