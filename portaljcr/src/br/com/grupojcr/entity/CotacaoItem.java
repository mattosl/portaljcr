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

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_COTACAO_ITEM")
public class CotacaoItem implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = -5743518335831505983L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "VLR_UNITARIO", precision = 10, scale = 2, nullable = false)
	private BigDecimal valor;
	
	@Column(name = "VLR_TOTAL", precision = 38, scale = 2, nullable = false)
	private BigDecimal valorTotal;
	
	@Column(name = "OBSERVACAO", length = 300)
	private String observacao;
	
	@Column(name = "QTD")
	private Integer quantidade;

	@Column(name = "CODUNIDADE", length = 5)
	private String codigoUnidade;
	
	
	@Column(name = "NAO_POSSUI", nullable = false, columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean naoPossui;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_COTACAO", nullable = false)
	private Cotacao cotacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_SOLICITACAO_COMPRA_ITEM", nullable = false)
	private SolicitacaoCompraItem solicitacaoCompraItem;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getNaoPossui() {
		return naoPossui;
	}

	public void setNaoPossui(Boolean naoPossui) {
		this.naoPossui = naoPossui;
	}

	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public SolicitacaoCompraItem getSolicitacaoCompraItem() {
		return solicitacaoCompraItem;
	}

	public void setSolicitacaoCompraItem(SolicitacaoCompraItem solicitacaoCompraItem) {
		this.solicitacaoCompraItem = solicitacaoCompraItem;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getCodigoUnidade() {
		return codigoUnidade;
	}

	public void setCodigoUnidade(String codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

}
