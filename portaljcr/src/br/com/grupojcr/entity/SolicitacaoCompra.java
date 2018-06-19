package br.com.grupojcr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.enumerator.PrioridadeSolicitacaoCompra;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;

@Entity
@Table(name = "TB_SOLICITACAO_COMPRA")
public class SolicitacaoCompra implements BaseEntity, Serializable {
	
	private static final long serialVersionUID = 8110893893788100221L;
	
	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "SITUACAO", nullable = false)
	private SituacaoSolicitacaoCompra situacao;
	
	@Column(name = "CODCCUSTO", length = 50, nullable = false)
	private String codigoCentroCusto;
	
	@Column(name = "CENTRO_CUSTO", length = 100, nullable = false)
	private String centroCusto;
	
	@Column(name = "CODNATORCAMENTO", length = 50, nullable = false)
	private String codigoNaturezaOrcamentaria;
	
	@Column(name = "NATUREZA_ORC", length = 100 , nullable = false)
	private String naturezaOrcamentaria;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "MODALIDADE", nullable = false)
	private Modalidade modalidade;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "PRIORIDADE", nullable = false)
	private PrioridadeSolicitacaoCompra prioridade;
	
	@Column(name = "MOTIVO_COMPRA", length = 500, nullable = false)
	private String motivoCompra;
	
	@Column(name = "MOTIVO_CANCELAMENTO", length = 300)
	private String motivoCancelamento;
	
	@Column(name = "SUG_FORNECEDOR", length = 100)
	private String sugestaoFornecedor;
	
	@Column(name = "LOCAL_ENTREGA", length = 100)
	private String localEntrega;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_CANCELAMENTO")
	private Date dtCancelamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_SOLICITACAO", nullable = false)
	private Date dtSolicitacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_PRAZO", nullable = false)
	private Date dtPrazo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_APROVACAO")
	private Date dtAprovacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INICIO_COTACAO")
	private Date dtInicioCotacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_FECHAMENTO")
	private Date dtFechamento;
	
	@Column(name = "IDENTIFICADOR_FLUIG")
	private Long identificadorFluig;
	
	@Column(name = "USR_APROVACAO_FLUIG", length = 100)
	private String usuarioAprovacaoFluig;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_SOLICITANTE", nullable = false)
	private Usuario usuarioSolicitante;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_COTACAO")
	private Usuario usuarioCotacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_FINALIZACAO")
	private Usuario usuarioFinalizacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_CANCELAMENTO")
	private Usuario usuarioCancelamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_COLIGADA", nullable = false)
	private Coligada coligada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_GRUPO_COTACAO")
	private GrupoCotacao grupoCotacao;
	
	@OneToMany(mappedBy = "solicitacaoCompra", fetch = FetchType.LAZY)
	private Set<SolicitacaoCompraItem> itens;
	
	@OneToMany(mappedBy = "solicitacaoCompra", fetch = FetchType.LAZY)
	private Set<Cotacao> cotacoes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SituacaoSolicitacaoCompra getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoSolicitacaoCompra situacao) {
		this.situacao = situacao;
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

	public String getCodigoNaturezaOrcamentaria() {
		return codigoNaturezaOrcamentaria;
	}

	public void setCodigoNaturezaOrcamentaria(String codigoNaturezaOrcamentaria) {
		this.codigoNaturezaOrcamentaria = codigoNaturezaOrcamentaria;
	}

	public String getNaturezaOrcamentaria() {
		return naturezaOrcamentaria;
	}

	public void setNaturezaOrcamentaria(String naturezaOrcamentaria) {
		this.naturezaOrcamentaria = naturezaOrcamentaria;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public PrioridadeSolicitacaoCompra getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(PrioridadeSolicitacaoCompra prioridade) {
		this.prioridade = prioridade;
	}

	public String getMotivoCompra() {
		return motivoCompra;
	}

	public void setMotivoCompra(String motivoCompra) {
		this.motivoCompra = motivoCompra;
	}

	public String getSugestaoFornecedor() {
		return sugestaoFornecedor;
	}

	public void setSugestaoFornecedor(String sugestaoFornecedor) {
		this.sugestaoFornecedor = sugestaoFornecedor;
	}

	public String getLocalEntrega() {
		return localEntrega;
	}

	public void setLocalEntrega(String localEntrega) {
		this.localEntrega = localEntrega;
	}


	public Date getDtSolicitacao() {
		return dtSolicitacao;
	}

	public void setDtSolicitacao(Date dtSolicitacao) {
		this.dtSolicitacao = dtSolicitacao;
	}

	public Date getDtPrazo() {
		return dtPrazo;
	}

	public void setDtPrazo(Date dtPrazo) {
		this.dtPrazo = dtPrazo;
	}

	public Date getDtAprovacao() {
		return dtAprovacao;
	}

	public void setDtAprovacao(Date dtAprovacao) {
		this.dtAprovacao = dtAprovacao;
	}

	public Date getDtInicioCotacao() {
		return dtInicioCotacao;
	}

	public void setDtInicioCotacao(Date dtInicioCotacao) {
		this.dtInicioCotacao = dtInicioCotacao;
	}

	public Date getDtFechamento() {
		return dtFechamento;
	}

	public void setDtFechamento(Date dtFechamento) {
		this.dtFechamento = dtFechamento;
	}

	public Long getIdentificadorFluig() {
		return identificadorFluig;
	}

	public void setIdentificadorFluig(Long identificadorFluig) {
		this.identificadorFluig = identificadorFluig;
	}

	public String getUsuarioAprovacaoFluig() {
		return usuarioAprovacaoFluig;
	}

	public void setUsuarioAprovacaoFluig(String usuarioAprovacaoFluig) {
		this.usuarioAprovacaoFluig = usuarioAprovacaoFluig;
	}

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public Usuario getUsuarioCotacao() {
		return usuarioCotacao;
	}

	public void setUsuarioCotacao(Usuario usuarioCotacao) {
		this.usuarioCotacao = usuarioCotacao;
	}

	public Usuario getUsuarioFinalizacao() {
		return usuarioFinalizacao;
	}

	public void setUsuarioFinalizacao(Usuario usuarioFinalizacao) {
		this.usuarioFinalizacao = usuarioFinalizacao;
	}

	public Coligada getColigada() {
		return coligada;
	}

	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}

	public GrupoCotacao getGrupoCotacao() {
		return grupoCotacao;
	}

	public void setGrupoCotacao(GrupoCotacao grupoCotacao) {
		this.grupoCotacao = grupoCotacao;
	}
	
	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}
	
	public Date getDtCancelamento() {
		return dtCancelamento;
	}

	public void setDtCancelamento(Date dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}

	public Set<SolicitacaoCompraItem> getItens() {
		return itens;
	}

	public void setItens(Set<SolicitacaoCompraItem> itens) {
		this.itens = itens;
	}

	public Usuario getUsuarioCancelamento() {
		return usuarioCancelamento;
	}

	public void setUsuarioCancelamento(Usuario usuarioCancelamento) {
		this.usuarioCancelamento = usuarioCancelamento;
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
		SolicitacaoCompra other = (SolicitacaoCompra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Set<Cotacao> getCotacoes() {
		return cotacoes;
	}

	public void setCotacoes(Set<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}

}
