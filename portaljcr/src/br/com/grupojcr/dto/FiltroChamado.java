package br.com.grupojcr.dto;

import java.util.Date;

import br.com.grupojcr.entity.Usuario;

public class FiltroChamado {
	
	private Integer situacao;
	private Date periodoInicial;
	private Date periodoFinal;
	private Usuario usuarioLogado;
	
	public Integer getSituacao() {
		return situacao;
	}
	public void setSituacao(Integer situacao) {
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
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

}
