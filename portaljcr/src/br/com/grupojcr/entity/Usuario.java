package br.com.grupojcr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_USUARIO")
public class Usuario implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = 3249712048111102804L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "NOME", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "USUARIO", length = 50, nullable = false)
	private String usuario;
	
	@Column(name = "EMAIL", length = 100, nullable = false)
	private String email;

	@Column(name = "CHAPA", length = 10)
	private String chapa;
	
	@Column(name = "SITUACAO", nullable = false, columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean situacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ULTIMO_LOGIN")
	private Date dtUltimoLogin;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "TB_COLIGADA_USUARIO", joinColumns = {
			@JoinColumn(name = "ID_USUARIO", referencedColumnName = "id") }, inverseJoinColumns = {
			@JoinColumn(name = "ID_COLIGADA", referencedColumnName = "id") })
	private Set<Coligada> coligadas;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TB_GRUPO_USUARIO", joinColumns = {
			@JoinColumn(name = "ID_USUARIO", referencedColumnName = "id") }, inverseJoinColumns = {
			@JoinColumn(name = "ID_GRUPO", referencedColumnName = "id") })
	private Set<Grupo> grupos;

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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getDtUltimoLogin() {
		return dtUltimoLogin;
	}

	public void setDtUltimoLogin(Date dtUltimoLogin) {
		this.dtUltimoLogin = dtUltimoLogin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getSituacao() {
		return situacao;
	}

	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}

	public Set<Grupo> getGrupos() {
		return grupos;
	}
	
	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}
	
	public Set<Coligada> getColigadas() {
		return coligadas;
	}
	
	public void setColigadas(Set<Coligada> coligadas) {
		this.coligadas = coligadas;
	}
	
	public String getChapa() {
		return chapa;
	}

	public void setChapa(String chapa) {
		this.chapa = chapa;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
