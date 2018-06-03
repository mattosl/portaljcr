package br.com.grupojcr.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.grupojcr.entity.SubCategoriaChamado;
import br.com.grupojcr.enumerator.PrioridadeChamado;

public class ChamadoDTO {
	
	private SubCategoriaChamado subcategoria;
	private PrioridadeChamado prioridade;
	private String titulo;
	private String descricao;
	private List<ArquivoDTO> anexos;
	
	public SubCategoriaChamado getSubcategoria() {
		return subcategoria;
	}
	public void setSubcategoria(SubCategoriaChamado subcategoria) {
		this.subcategoria = subcategoria;
	}
	public PrioridadeChamado getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(PrioridadeChamado prioridade) {
		this.prioridade = prioridade;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public List<ArquivoDTO> getAnexos() {
		if(anexos == null) {
			anexos = new ArrayList<ArquivoDTO>();
		}
		return anexos;
	}
	public void setAnexos(List<ArquivoDTO> anexos) {
		this.anexos = anexos;
	}

}
