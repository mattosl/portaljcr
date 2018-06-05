package br.com.grupojcr.dto;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.GrupoCotacao;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.enumerator.PrioridadeSolicitacaoCompra;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;

public class SolicitacaoCompraDTO {
	
	private Coligada coligada;
	private Boolean possuiGrupoCotacao;
	private GrupoCotacao grupoCotacao;
	private CentroCustoRM centroCusto;
	private NaturezaOrcamentariaRM naturezaOrcamentaria;
	private Modalidade modalidade;
	private Usuario usuarioCotacao;
	private PrioridadeSolicitacaoCompra prioridade;
	private String motivoCompra;
	private String sugestaoFornecedor;
	private String localEntrega;
	private Double valorAproximado;
	
	public Coligada getColigada() {
		return coligada;
	}
	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}
	public GrupoCotacao getGrupoCotacao() {
		return grupoCotacao;
	}
	public void setGrupoCotacao(GrupoCotacao grupoCotacao) {
		this.grupoCotacao = grupoCotacao;
	}
	public CentroCustoRM getCentroCusto() {
		return centroCusto;
	}
	public void setCentroCusto(CentroCustoRM centroCusto) {
		this.centroCusto = centroCusto;
	}
	public Boolean getPossuiGrupoCotacao() {
		return possuiGrupoCotacao;
	}
	public void setPossuiGrupoCotacao(Boolean possuiGrupoCotacao) {
		this.possuiGrupoCotacao = possuiGrupoCotacao;
	}
	public Usuario getUsuarioCotacao() {
		return usuarioCotacao;
	}
	public void setUsuarioCotacao(Usuario usuarioCotacao) {
		this.usuarioCotacao = usuarioCotacao;
	}
	public NaturezaOrcamentariaRM getNaturezaOrcamentaria() {
		return naturezaOrcamentaria;
	}
	public void setNaturezaOrcamentaria(NaturezaOrcamentariaRM naturezaOrcamentaria) {
		this.naturezaOrcamentaria = naturezaOrcamentaria;
	}
	public Modalidade getModalidade() {
		return modalidade;
	}
	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}
	public PrioridadeSolicitacaoCompra getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(PrioridadeSolicitacaoCompra prioridade) {
		this.prioridade = prioridade;
	}
	public String getMotivoCompra() {
		return motivoCompra;
	}
	public void setMotivoCompra(String motivoCompra) {
		this.motivoCompra = motivoCompra;
	}
	public String getSugestaoFornecedor() {
		return sugestaoFornecedor;
	}
	public void setSugestaoFornecedor(String sugestaoFornecedor) {
		this.sugestaoFornecedor = sugestaoFornecedor;
	}
	public String getLocalEntrega() {
		return localEntrega;
	}
	public void setLocalEntrega(String localEntrega) {
		this.localEntrega = localEntrega;
	}
	public Double getValorAproximado() {
		return valorAproximado;
	}
	public void setValorAproximado(Double valorAproximado) {
		this.valorAproximado = valorAproximado;
	}

}
