package br.com.grupojcr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.grupojcr.enumerator.SituacaoAjustePonto;

@Entity
@Table(name = "TB_AJUSTE_PONTO")
public class AjustePonto implements BaseEntity, Serializable {

	private static final long serialVersionUID = 3369160868246611245L;
	
	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_AJUSTE", nullable = false)
	private Usuario usuario;
	
	@Column(name = "CHAPA", length = 10, nullable = false)
	private String chapa;

	@Column(name = "CODCCUSTO", length = 50)
	private String codigoCentroCusto;

	@Column(name = "CENTRO_CUSTO", length = 100)
	private String centroCusto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_AJUSTE", nullable = false)
	private Date dtAjuste;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_PERIODO_INICIAL", nullable = false)
	private Date dtPeriodoInicial;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_PERIODO_FINAL", nullable = false)
	private Date dtPeriodoFinal;
	
	@Column(name = "USR_APROVACAO_FLUIG", length = 100)
	private String usrFluig;
	
	@Column(name = "IDENTIFICADOR_FLUIG")
	private Long identificadorFluig;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "SITUACAO", nullable = false)
	private SituacaoAjustePonto situacao;
	
	@Override
	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getChapa() {
		return chapa;
	}

	public void setChapa(String chapa) {
		this.chapa = chapa;
	}

	public String getCodigoCentroCusto() {
		return codigoCentroCusto;
	}

	public void setCodigoCentroCusto(String codigoCentroCusto) {
		this.codigoCentroCusto = codigoCentroCusto;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Date getDtAjuste() {
		return dtAjuste;
	}

	public void setDtAjuste(Date dtAjuste) {
		this.dtAjuste = dtAjuste;
	}

	public Date getDtPeriodoInicial() {
		return dtPeriodoInicial;
	}

	public void setDtPeriodoInicial(Date dtPeriodoInicial) {
		this.dtPeriodoInicial = dtPeriodoInicial;
	}

	public Date getDtPeriodoFinal() {
		return dtPeriodoFinal;
	}

	public void setDtPeriodoFinal(Date dtPeriodoFinal) {
		this.dtPeriodoFinal = dtPeriodoFinal;
	}

	public String getUsrFluig() {
		return usrFluig;
	}

	public void setUsrFluig(String usrFluig) {
		this.usrFluig = usrFluig;
	}

	public Long getIdentificadorFluig() {
		return identificadorFluig;
	}

	public void setIdentificadorFluig(Long identificadorFluig) {
		this.identificadorFluig = identificadorFluig;
	}

	public SituacaoAjustePonto getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoAjustePonto situacao) {
		this.situacao = situacao;
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
		AjustePonto other = (AjustePonto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
