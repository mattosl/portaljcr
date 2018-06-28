package br.com.grupojcr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TB_APROVADOR_CENTRO_CUSTO")
public class AprovadorCentroCusto implements Serializable {
	
	private static final long serialVersionUID = 1088660198054004815L;

	@Id
	@Column(name = "CODCCUSTO", length = 50, nullable = false)
	private String codigoCentroCusto;
	
	@Column(name = "APROVADOR", length = 50, nullable = false)
	private String aprovador;
	
	@Column(name = "USUARIO", length = 50, nullable = false)
	private String usuario;
	
	@Column(name = "NIVEL", nullable = false)
	private Integer nivel;

	@Column(name = "CODCOLIGADA", nullable = false)
	private Integer codigoColigada;

	public String getCodigoCentroCusto() {
		return codigoCentroCusto;
	}

	public void setCodigoCentroCusto(String codigoCentroCusto) {
		this.codigoCentroCusto = codigoCentroCusto;
	}

	public String getAprovador() {
		return aprovador;
	}

	public void setAprovador(String aprovador) {
		this.aprovador = aprovador;
	}

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getCodigoColigada() {
		return codigoColigada;
	}

	public void setCodigoColigada(Integer codigoColigada) {
		this.codigoColigada = codigoColigada;
	}
	
}
