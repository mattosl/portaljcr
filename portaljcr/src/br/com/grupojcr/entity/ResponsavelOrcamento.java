package br.com.grupojcr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_RESPONSAVEL_ORCAMENTO")
public class ResponsavelOrcamento implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = -3441389530184828343L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CODCCUSTO", length = 50, nullable = false)
	private String codigoCentroCusto;
	
	@Column(name = "CENTRO_CUSTO", length = 100, nullable = false)
	private String centroCusto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INCLUSAO")
	private Date dtInclusao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_COLIGADA", nullable = false)
	private Coligada coligada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_RESP", nullable = false)
	private Usuario usuarioResponsavel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoCentroCusto() {
		return codigoCentroCusto;
	}

	public void setCodigoCentroCusto(String codigoCentroCusto) {
		this.codigoCentroCusto = codigoCentroCusto;
	}

	public Coligada getColigada() {
		return coligada;
	}

	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}

	
}
