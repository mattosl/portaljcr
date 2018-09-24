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
@Table(name = "TB_BATIDA_PONTO")
public class BatidaPonto implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = -4443028572966927113L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_AJUSTE_PONTO", nullable = false)
	private AjustePonto ajuste;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_BATIDA", nullable = false)
	private Date dtBatida;
	
	@Column(name = "JUSTIFICATIVA", length = 1000, nullable = false)
	private String justificativa;
	
	@Column(name = "BATIDA", nullable = false)
	private Integer batida;

	@Column(name = "SEQ", nullable = false)
	private Integer sequencia;

	@Column(name = "TIPO", nullable = false)
	private Integer tipo;

	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		BatidaPonto other = (BatidaPonto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public AjustePonto getAjuste() {
		return ajuste;
	}

	public void setAjuste(AjustePonto ajuste) {
		this.ajuste = ajuste;
	}

	public Date getDtBatida() {
		return dtBatida;
	}

	public void setDtBatida(Date dtBatida) {
		this.dtBatida = dtBatida;
	}

	public Integer getBatida() {
		return batida;
	}

	public void setBatida(Integer batida) {
		this.batida = batida;
	}

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
}
