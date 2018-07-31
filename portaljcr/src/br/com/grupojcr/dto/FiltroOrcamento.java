package br.com.grupojcr.dto;

import java.util.Date;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.rm.CentroCustoRM;

public class FiltroOrcamento {
	
	private Coligada coligada;
	private CentroCustoRM centroCusto;
	private Date dtAjuste;
	private Date periodoInicial;
	private Date periodoFinal;
	private Usuario usuarioLogado;
	
	public Coligada getColigada() {
		return coligada;
	}
	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}
	public CentroCustoRM getCentroCusto() {
		return centroCusto;
	}
	public void setCentroCusto(CentroCustoRM centroCusto) {
		this.centroCusto = centroCusto;
	}
	public Date getDtAjuste() {
		return dtAjuste;
	}
	public void setDtAjuste(Date dtAjuste) {
		this.dtAjuste = dtAjuste;
	}
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
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
