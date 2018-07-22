package br.com.grupojcr.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TB_AJUSTE_ORCAMENTARIO_ITEM")
public class AjusteOrcamentarioItem implements BaseEntity, Serializable {

	private static final long serialVersionUID = -7251467205075314933L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_AJUSTE_ORCAMENTARIO", nullable = false)
	private AjusteOrcamentario ajusteOrcamentario;

	@Override
	public Long getId() {
		return id;
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

	public AjusteOrcamentario getAjusteOrcamentario() {
		return ajusteOrcamentario;
	}

	public void setAjusteOrcamentario(AjusteOrcamentario ajusteOrcamentario) {
		this.ajusteOrcamentario = ajusteOrcamentario;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
