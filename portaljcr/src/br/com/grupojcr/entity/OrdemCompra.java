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
@Table(name = "TB_ORDEM_COMPRA")
public class OrdemCompra implements Serializable {
	
	private static final long serialVersionUID = -6705717750478538718L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "IDENTIFICADOR_RM", length = 50, nullable = false)
	private String identificadorRM;
	
	@Column(name = "CODCPG", length = 5)
	private String codigoCondicaoPagamento;

	@Column(name = "CONDICAO_PAGAMENTO", length = 100)
	private String condicaoPagamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ORDEM_COMPRA", nullable = false)
	private Date dtOrdemCompra;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_SOLICITACAO_COMPRA", nullable = false)
	private SolicitacaoCompra solicitacaoCompra;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_COTACAO", nullable = false)
	private Cotacao cotacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_GERACAO", nullable = false)
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificadorRM() {
		return identificadorRM;
	}

	public void setIdentificadorRM(String identificadorRM) {
		this.identificadorRM = identificadorRM;
	}

	public Date getDtOrdemCompra() {
		return dtOrdemCompra;
	}

	public void setDtOrdemCompra(Date dtOrdemCompra) {
		this.dtOrdemCompra = dtOrdemCompra;
	}

	public SolicitacaoCompra getSolicitacaoCompra() {
		return solicitacaoCompra;
	}

	public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
		this.solicitacaoCompra = solicitacaoCompra;
	}

	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getCodigoCondicaoPagamento() {
		return codigoCondicaoPagamento;
	}

	public void setCodigoCondicaoPagamento(String codigoCondicaoPagamento) {
		this.codigoCondicaoPagamento = codigoCondicaoPagamento;
	}

	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
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
		OrdemCompra other = (OrdemCompra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
