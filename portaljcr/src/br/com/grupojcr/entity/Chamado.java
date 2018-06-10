package br.com.grupojcr.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import br.com.grupojcr.enumerator.CausaChamado;
import br.com.grupojcr.enumerator.PrioridadeChamado;
import br.com.grupojcr.enumerator.SituacaoChamado;

@Entity
@Table(name = "TB_CHAMADO")
public class Chamado implements Serializable {

	private static final long serialVersionUID = 1381450786544223095L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CATEGORIA", length = 100, nullable = false)
	private String categoria;
	
	@Column(name = "SUBCATEGORIA", length = 100, nullable = false)
	private String subcategoria;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "PRIORIDADE", nullable = false)
	private PrioridadeChamado prioridade;
	
	@Column(name = "TITULO", length = 100, nullable = false)
	private String titulo;
	
	@Column(name = "DESCRICAO", length = 500, nullable = false)
	private String descricao;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "SITUACAO", nullable = false)
	private SituacaoChamado situacao;
	
	@Column(name = "SOLUCAO", length = 500)
	private String solucao;
	
	@Column(name = "FEEDBACK", length = 200)
	private String feedback;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "CAUSA")
	private CausaChamado causa;
	
	@Column(name = "DT_ABERTURA", nullable = false)
	private Date dtAbertura;
	
	@Column(name = "DT_FECHAMENTO")
	private Date dtFechamento;
	
	@Column(name = "DT_PREVISAO", nullable = false)
	private Date dtPrevisao;
	
	@Column(name = "DT_RESOLUCAO")
	private Date dtResolucao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_SOLICITANTE", nullable = false)
	private Usuario usuarioSolicitante;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_RESPONSAVEL")
	private Usuario usuarioResponsavel;
	
	@OneToMany(mappedBy = "chamado", fetch = FetchType.LAZY)
	private Set<AnexoChamado> anexos;
	
	@OneToMany(mappedBy = "chamado", fetch = FetchType.LAZY)
	@OrderBy("dtAcompanhamento DESC")
	private Set<ChamadoAcompanhamento> mensagens;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getSubcategoria() {
		return subcategoria;
	}

	public void setSubcategoria(String subcategoria) {
		this.subcategoria = subcategoria;
	}

	public PrioridadeChamado getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(PrioridadeChamado prioridade) {
		this.prioridade = prioridade;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public SituacaoChamado getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoChamado situacao) {
		this.situacao = situacao;
	}

	public String getSolucao() {
		return solucao;
	}

	public void setSolucao(String solucao) {
		this.solucao = solucao;
	}

	public CausaChamado getCausa() {
		return causa;
	}

	public void setCausa(CausaChamado causa) {
		this.causa = causa;
	}

	public Date getDtAbertura() {
		return dtAbertura;
	}

	public void setDtAbertura(Date dtAbertura) {
		this.dtAbertura = dtAbertura;
	}

	public Date getDtFechamento() {
		return dtFechamento;
	}

	public void setDtFechamento(Date dtFechamento) {
		this.dtFechamento = dtFechamento;
	}

	public Date getDtPrevisao() {
		return dtPrevisao;
	}

	public void setDtPrevisao(Date dtPrevisao) {
		this.dtPrevisao = dtPrevisao;
	}
	
	public Date getDtResolucao() {
		return dtResolucao;
	}

	public void setDtResolucao(Date dtResolucao) {
		this.dtResolucao = dtResolucao;
	}

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public Usuario getUsuarioResponsavel() {
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}
	
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
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
		Chamado other = (Chamado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Set<AnexoChamado> getAnexos() {
		return anexos;
	}

	public void setAnexos(Set<AnexoChamado> anexos) {
		this.anexos = anexos;
	}

	public Set<ChamadoAcompanhamento> getMensagens() {
		return mensagens;
	}

	public void setMensagens(Set<ChamadoAcompanhamento> mensagens) {
		this.mensagens = mensagens;
	}
	
	@Transient
	public List<ChamadoAcompanhamento> getMensagensList() {
		List<ChamadoAcompanhamento> lista = new ArrayList<ChamadoAcompanhamento>(mensagens);
		lista.sort(new Comparator<ChamadoAcompanhamento>() {

			@Override
			public int compare(ChamadoAcompanhamento o1, ChamadoAcompanhamento o2) {
				if(o1.getDtAcompanhamento().after(o2.getDtAcompanhamento())) {
					return 1;
				} else {
					return -1;
				}
			}
			
		});
		return lista;
	}

}
