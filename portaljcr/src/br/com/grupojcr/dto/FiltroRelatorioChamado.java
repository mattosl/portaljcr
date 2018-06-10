package br.com.grupojcr.dto;

import java.util.Date;

import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.CausaChamado;
import br.com.grupojcr.enumerator.PrioridadeChamado;
import br.com.grupojcr.enumerator.SituacaoChamado;

public class FiltroRelatorioChamado {
	
	private SituacaoChamado[] situacao;
	private String categoria;
	private String subCategoria;
	private Usuario usuarioSolicitante;
	private Usuario usuarioResponsavel;
	private PrioridadeChamado prioridadeChamado;
	private CausaChamado causaChamado;
	private Date periodoInicial;
	private Date periodoFinal;
	
	public SituacaoChamado[] getSituacao() {
		return situacao;
	}
	public void setSituacao(SituacaoChamado[] situacao) {
		this.situacao = situacao;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getSubCategoria() {
		return subCategoria;
	}
	public void setSubCategoria(String subCategoria) {
		this.subCategoria = subCategoria;
	}
	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}
	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}
	public Usuario getUsuarioResponsavel() {
		return usuarioResponsavel;
	}
	public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}
	public PrioridadeChamado getPrioridadeChamado() {
		return prioridadeChamado;
	}
	public void setPrioridadeChamado(PrioridadeChamado prioridadeChamado) {
		this.prioridadeChamado = prioridadeChamado;
	}
	public CausaChamado getCausaChamado() {
		return causaChamado;
	}
	public void setCausaChamado(CausaChamado causaChamado) {
		this.causaChamado = causaChamado;
	}
	public Date getPeriodoInicial() {
		return periodoInicial;
	}
	public void setPeriodoInicial(Date periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	public Date getPeriodoFinal() {
		return periodoFinal;
	}
	public void setPeriodoFinal(Date periodoFinal) {
		this.periodoFinal = periodoFinal;
	}
	
	

}
