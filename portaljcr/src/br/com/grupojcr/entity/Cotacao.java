package br.com.grupojcr.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;

@Entity
@Table(name = "TB_COTACAO")
public class Cotacao implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = -5743518335831505983L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_COTACAO", nullable = false)
	private Date dtCotacao;
	
	@Column(name = "CODFORNECEDOR")
	private String codigoFornecedor;

	@Column(name = "FORNECEDOR", length = 100, nullable = false)
	private String fornecedor;

	@Column(name = "CONTATO_FORNECEDOR", length = 15)
	private String contatoFornecedor;

	@Column(name = "VLR_TOTAL", precision = 10, scale = 2, nullable = false)
	private Double valorTotal;
	
	@Column(name = "OBSERVACAO", length = 300)
	private String observacao;
	
	@Column(name = "FRETE", precision = 10, scale = 2, nullable = false)
	private Double frete;
	
	@Column(name = "COTACAO_PRINCIPAL", nullable = false, columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean cotacaoPrincipal;
	
	@Column(name = "MELHOR_OPCAO", nullable = false, columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean melhorOpcao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_SOLICITACAO_COMPRA", nullable = false)
	private SolicitacaoCompra solicitacaoCompra;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_COTACAO", nullable = false)
	private Usuario usuarioCotacao;
	
	@OneToMany(mappedBy = "cotacao", fetch = FetchType.LAZY)
	private Set<CotacaoItem> itens;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDtCotacao() {
		return dtCotacao;
	}

	public void setDtCotacao(Date dtCotacao) {
		this.dtCotacao = dtCotacao;
	}

	public String getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(String codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getContatoFornecedor() {
		return contatoFornecedor;
	}

	public void setContatoFornecedor(String contatoFornecedor) {
		this.contatoFornecedor = contatoFornecedor;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Boolean getCotacaoPrincipal() {
		return cotacaoPrincipal;
	}

	public void setCotacaoPrincipal(Boolean cotacaoPrincipal) {
		this.cotacaoPrincipal = cotacaoPrincipal;
	}

	public Boolean getMelhorOpcao() {
		return melhorOpcao;
	}

	public void setMelhorOpcao(Boolean melhorOpcao) {
		this.melhorOpcao = melhorOpcao;
	}

	public SolicitacaoCompra getSolicitacaoCompra() {
		return solicitacaoCompra;
	}

	public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
		this.solicitacaoCompra = solicitacaoCompra;
	}

	public Usuario getUsuarioCotacao() {
		return usuarioCotacao;
	}

	public void setUsuarioCotacao(Usuario usuarioCotacao) {
		this.usuarioCotacao = usuarioCotacao;
	}
	
	public Set<CotacaoItem> getItens() {
		return itens;
	}

	public void setItens(Set<CotacaoItem> itens) {
		this.itens = itens;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public Double getFrete() {
		return frete;
	}

	public void setFrete(Double frete) {
		this.frete = frete;
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
		Cotacao other = (Cotacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public List<CotacaoItem> getItensList() {
		return new ArrayList<CotacaoItem>(itens);
	}
	
	@Transient
	public String getValorTotalFormatado() {
		if(valorTotal != null) {
			return TreatNumber.formatMoneyCurrency(valorTotal);
		}
		return null;
	}
	
	@Transient
	public String getDataCotacaoFormatado() {
		if(dtCotacao != null) {
			return TreatDate.format("dd/MM/yyyy", dtCotacao);
		}
		return null;
	}

}
