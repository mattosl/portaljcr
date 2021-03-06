package br.com.grupojcr.entity;

import java.beans.Transient;
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

import org.hibernate.annotations.Type;

import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;

@Entity
@Table(name = "TB_SOLICITACAO_COMPRA_ITEM")
public class SolicitacaoCompraItem implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = 8110893893788100221L;
	
	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ID_PRD")
	private Integer idProduto;
	
	@Column(name = "CODPRD", length = 50)
	private String codigoProduto;
	
	@Column(name = "DESCRICAO_PRD", length = 100, nullable = false)
	private String descricaoProduto;
	
	@Column(name = "NE_PRD", nullable = false, columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean produtoNaoEncontrado;
	
	@Column(name = "QTD", precision = 10, scale = 2, nullable = false)
	private BigDecimal quantidade;
	
	@Column(name = "CODUNIDADE", length = 5)
	private String codigoUnidade;
	
	@Column(name = "UNIDADE", length = 50)
	private String unidade;
	
	@Column(name = "OBSERVACAO", length = 300)
	private String observacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INCLUSAO", nullable = false)
	private Date dtInclusao;
	
	@Column(name = "VLR_APROXIMADO", precision = 38, scale = 2)
	private BigDecimal valorAproximado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_SOLICITACAO_COMPRA", nullable = false)
	private SolicitacaoCompra solicitacaoCompra;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_INCLUSAO")
	private Usuario usuarioInclusao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public Boolean getProdutoNaoEncontrado() {
		return produtoNaoEncontrado;
	}

	public void setProdutoNaoEncontrado(Boolean produtoNaoEncontrado) {
		this.produtoNaoEncontrado = produtoNaoEncontrado;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public String getCodigoUnidade() {
		return codigoUnidade;
	}

	public void setCodigoUnidade(String codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public SolicitacaoCompra getSolicitacaoCompra() {
		return solicitacaoCompra;
	}

	public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
		this.solicitacaoCompra = solicitacaoCompra;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public BigDecimal getValorAproximado() {
		return valorAproximado;
	}

	public void setValorAproximado(BigDecimal valorAproximado) {
		this.valorAproximado = valorAproximado;
	}
	
	@Transient
	public String getProdutoFormatado() {
		if(TreatString.isNotBlank(codigoProduto)) {
			return codigoProduto + " - " + descricaoProduto;  
		} else {
			return descricaoProduto;
		}
	}
	
	@Transient
	public BigDecimal getValorTotalAproxFormatado() {
		if(Util.isNotNull(valorAproximado) && Util.isNotNull(quantidade)) {
			return getValorAproximado().multiply(getQuantidade());
		}
		return new BigDecimal(0);
	}
}
