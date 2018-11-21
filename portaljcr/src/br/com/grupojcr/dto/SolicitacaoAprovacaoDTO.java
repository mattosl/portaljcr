package br.com.grupojcr.dto;

import java.util.List;

public class SolicitacaoAprovacaoDTO {
	
	private List<AprovacaoContratoDTO> contratos;
	private List<AprovacaoOrdemCompraDTO> ordemCompras;
	private List<AprovacaoSolicitacaoCompraDTO> solicitacoes;
	private List<AprovacaoPontoDTO> pontos;
	
	private String classeCSSOrdemCompra;
	private String classeCSSContratos;
	private String classeCSSSolicitacaoCompra;
	private String classeCSSPonto;
	
	private Integer qtdOrdemCompra;
	private Integer qtdContratos;
	private Integer qtdSolicitacaoCompra;
	private Integer qtdPonto;
	
	public Integer getQtdOrdemCompra() {
		return qtdOrdemCompra;
	}
	public void setQtdOrdemCompra(Integer qtdOrdemCompra) {
		this.qtdOrdemCompra = qtdOrdemCompra;
	}
	public Integer getQtdContratos() {
		return qtdContratos;
	}
	public void setQtdContratos(Integer qtdContratos) {
		this.qtdContratos = qtdContratos;
	}
	public String getClasseCSSOrdemCompra() {
		return classeCSSOrdemCompra;
	}
	public void setClasseCSSOrdemCompra(String classeCSSOrdemCompra) {
		this.classeCSSOrdemCompra = classeCSSOrdemCompra;
	}
	public String getClasseCSSContratos() {
		return classeCSSContratos;
	}
	public void setClasseCSSContratos(String classeCSSContratos) {
		this.classeCSSContratos = classeCSSContratos;
	}
	public List<AprovacaoContratoDTO> getContratos() {
		return contratos;
	}
	public void setContratos(List<AprovacaoContratoDTO> contratos) {
		this.contratos = contratos;
	}
	public List<AprovacaoOrdemCompraDTO> getOrdemCompras() {
		return ordemCompras;
	}
	public void setOrdemCompras(List<AprovacaoOrdemCompraDTO> ordemCompras) {
		this.ordemCompras = ordemCompras;
	}
	public List<AprovacaoSolicitacaoCompraDTO> getSolicitacoes() {
		return solicitacoes;
	}
	public void setSolicitacoes(List<AprovacaoSolicitacaoCompraDTO> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}
	public String getClasseCSSSolicitacaoCompra() {
		return classeCSSSolicitacaoCompra;
	}
	public void setClasseCSSSolicitacaoCompra(String classeCSSSolicitacaoCompra) {
		this.classeCSSSolicitacaoCompra = classeCSSSolicitacaoCompra;
	}
	public Integer getQtdSolicitacaoCompra() {
		return qtdSolicitacaoCompra;
	}
	public void setQtdSolicitacaoCompra(Integer qtdSolicitacaoCompra) {
		this.qtdSolicitacaoCompra = qtdSolicitacaoCompra;
	}
	public List<AprovacaoPontoDTO> getPontos() {
		return pontos;
	}
	public void setPontos(List<AprovacaoPontoDTO> pontos) {
		this.pontos = pontos;
	}
	public String getClasseCSSPonto() {
		return classeCSSPonto;
	}
	public void setClasseCSSPonto(String classeCSSPonto) {
		this.classeCSSPonto = classeCSSPonto;
	}
	public Integer getQtdPonto() {
		return qtdPonto;
	}
	public void setQtdPonto(Integer qtdPonto) {
		this.qtdPonto = qtdPonto;
	}
	

}
