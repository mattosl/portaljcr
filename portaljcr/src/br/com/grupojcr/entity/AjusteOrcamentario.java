package br.com.grupojcr.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "TB_AJUSTE_ORCAMENTARIO")
public class AjusteOrcamentario implements BaseEntity, Serializable {

	private static final long serialVersionUID = 6840332977904004038L;
	
	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CODCCUSTO", length = 50, nullable = false)
	private String codigoCentroCusto;
	
	@Column(name = "CENTRO_CUSTO", length = 100, nullable = false)
	private String centroCusto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_AJUSTE")
	private Date dtAjuste;
	
	@Column(name = "IDPERIODO", nullable = false)
	private Integer idPeriodo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_AJUSTE", nullable = false)
	private Usuario usuarioAjuste;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_COLIGADA", nullable = false)
	private Coligada coligada;
	
	@Column(name = "CODTBORCAMENTO_ORIGEM", length = 40, nullable = false)
	private String idNaturezaOrigem;
	
	@Column(name = "IDORCAMENTO_ORIGEM", nullable = false)
	private Integer idOrcamentoOrigem;
	
	@Column(name = "IDITMPERIODO_ORIGEM", nullable = false)
	private Integer mesOrigem;
	
	@Column(name = "CODTBORCAMENTO_DESTINO", length = 40, nullable = false)
	private String idNaturezaDestino;
	
	@Column(name = "IDORCAMENTO_DESTINO", nullable = false)
	private Integer idOrcamentoDestino;
	
	@Column(name = "IDITMPERIODO_DESTINO", nullable = false)
	private Integer mesDestino;
	
	@Column(name = "VLR", precision = 38, scale = 2, nullable = false)
	private BigDecimal valor;
	
	@Override
	public Long getId() {
		return id;
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

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Usuario getUsuarioAjuste() {
		return usuarioAjuste;
	}

	public void setUsuarioAjuste(Usuario usuarioAjuste) {
		this.usuarioAjuste = usuarioAjuste;
	}

	public Coligada getColigada() {
		return coligada;
	}

	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}

	public String getIdNaturezaOrigem() {
		return idNaturezaOrigem;
	}

	public void setIdNaturezaOrigem(String idNaturezaOrigem) {
		this.idNaturezaOrigem = idNaturezaOrigem;
	}

	public Integer getIdOrcamentoOrigem() {
		return idOrcamentoOrigem;
	}

	public void setIdOrcamentoOrigem(Integer idOrcamentoOrigem) {
		this.idOrcamentoOrigem = idOrcamentoOrigem;
	}

	public Integer getMesOrigem() {
		return mesOrigem;
	}

	public void setMesOrigem(Integer mesOrigem) {
		this.mesOrigem = mesOrigem;
	}

	public String getIdNaturezaDestino() {
		return idNaturezaDestino;
	}

	public void setIdNaturezaDestino(String idNaturezaDestino) {
		this.idNaturezaDestino = idNaturezaDestino;
	}

	public Integer getIdOrcamentoDestino() {
		return idOrcamentoDestino;
	}

	public void setIdOrcamentoDestino(Integer idOrcamentoDestino) {
		this.idOrcamentoDestino = idOrcamentoDestino;
	}

	public Integer getMesDestino() {
		return mesDestino;
	}

	public void setMesDestino(Integer mesDestino) {
		this.mesDestino = mesDestino;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
		AjusteOrcamentario other = (AjusteOrcamentario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
