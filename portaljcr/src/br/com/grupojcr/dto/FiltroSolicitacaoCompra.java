package br.com.grupojcr.dto;

import java.util.Date;
import java.util.List;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;

public class FiltroSolicitacaoCompra {
	
	private Coligada coligada;
	private SituacaoSolicitacaoCompra situacao;
	private SituacaoSolicitacaoCompra[] situacaoIgnorar;
	private Date periodoInicial;
	private Date periodoFinal;
	private Usuario usuarioLogado;
	private Usuario usuarioCotacao;
	private Long numeroSolicitacao;
	private List<Coligada> coligadasUsuario;
	
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
	public Long getNumeroSolicitacao() {
		return numeroSolicitacao;
	}
	public void setNumeroSolicitacao(Long numeroSolicitacao) {
		this.numeroSolicitacao = numeroSolicitacao;
	}
	public Usuario getUsuarioCotacao() {
		return usuarioCotacao;
	}
	public void setUsuarioCotacao(Usuario usuarioCotacao) {
		this.usuarioCotacao = usuarioCotacao;
	}
	public SituacaoSolicitacaoCompra[] getSituacaoIgnorar() {
		return situacaoIgnorar;
	}
	public void setSituacaoIgnorar(SituacaoSolicitacaoCompra[] situacaoIgnorar) {
		this.situacaoIgnorar = situacaoIgnorar;
	}
	public List<Coligada> getColigadasUsuario() {
		return coligadasUsuario;
	}
	public void setColigadasUsuario(List<Coligada> coligadasUsuario) {
		this.coligadasUsuario = coligadasUsuario;
	}

}
