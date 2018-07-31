package br.com.grupojcr.dto;

import java.util.List;

import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.rm.CentroCustoRM;

public class ResponsavelOrcamentoDTO {
	
	private CentroCustoRM centroCusto;
	private List<Usuario> listaResponsavel;

	public CentroCustoRM getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCustoRM centroCusto) {
		this.centroCusto = centroCusto;
	}

	public List<Usuario> getListaResponsavel() {
		return listaResponsavel;
	}

	public void setListaResponsavel(List<Usuario> listaResponsavel) {
		this.listaResponsavel = listaResponsavel;
	}


}
