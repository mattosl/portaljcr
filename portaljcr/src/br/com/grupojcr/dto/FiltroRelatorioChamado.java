package br.com.grupojcr.dto;

import java.util.Date;

import br.com.grupojcr.entity.CategoriaChamado;
import br.com.grupojcr.entity.SubCategoriaChamado;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.CausaChamado;
import br.com.grupojcr.enumerator.PrioridadeChamado;
import br.com.grupojcr.enumerator.SituacaoChamado;

public class FiltroRelatorioChamado {
	
	private Long numeroChamado;
	private SituacaoChamado[] situacao;
	private CategoriaChamado categoria;
	private SubCategoriaChamado subCategoria;
	private String localizacao;
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
	public String getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	public CategoriaChamado getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaChamado categoria) {
		this.categoria = categoria;
	}
	public SubCategoriaChamado getSubCategoria() {
		return subCategoria;
	}
	public void setSubCategoria(SubCategoriaChamado subCategoria) {
		this.subCategoria = subCategoria;
	}
	public Long getNumeroChamado() {
		return numeroChamado;
	}
	public void setNumeroChamado(Long numeroChamado) {
		this.numeroChamado = numeroChamado;
	}
	
	

}
