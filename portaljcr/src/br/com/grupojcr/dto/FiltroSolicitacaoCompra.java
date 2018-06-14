package br.com.grupojcr.dto;

import java.util.Date;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;

public class FiltroSolicitacaoCompra {
	
	private Coligada coligada;
	private SituacaoSolicitacaoCompra situacao;
	private Date periodoInicial;
	private Date periodoFinal;
	private Usuario usuarioLogado;
	
	public SituacaoSolicitacaoCompra getSituacao() {
		return situacao;
	}
	public void setSituacao(SituacaoSolicitacaoCompra situacao) {
		this.situacao = situacao;
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
	public Coligada getColigada() {
		return coligada;
	}
	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
	

}
