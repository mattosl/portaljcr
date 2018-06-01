package br.com.grupojcr.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "TB_CATEGORIA_CHAMADO")
public class CategoriaChamado implements Serializable {

	private static final long serialVersionUID = -6132325561192536178L;
	
	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "NOME", length = 100, nullable = false)
	private String nome;
	
	@OneToMany(mappedBy = "categoria")
	@OrderBy("id DESC")
	private Set<SubCategoriaChamado> subCategorias;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<SubCategoriaChamado> getSubCategorias() {
		return subCategorias;
	}

	public void setSubCategorias(Set<SubCategoriaChamado> subCategorias) {
		this.subCategorias = subCategorias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoriaChamado other = (CategoriaChamado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
