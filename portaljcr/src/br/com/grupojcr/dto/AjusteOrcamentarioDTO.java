package br.com.grupojcr.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.rm.CentroCustoRM;

public class AjusteOrcamentarioDTO implements Serializable {
	
	private static final long serialVersionUID = 589215434467451886L;
	
	private Long id;
	private Coligada coligada;
	private CentroCustoRM centroCusto;
	private Date dtAjuste;
	private String ano;
	private String valorTotal;
	
	private List<AjusteOrcamentarioDTO> itens;
	
	private Integer idNaturezaOrigem;
	private String naturezaOrigem;
	private Integer idOrcamentoOrigem;
	private Integer mesOrigem;
	private Integer idNaturezaDestino;
	private String naturezaDestino;
	private Integer idOrcamentoDestino;
	private Integer mesDestino;
	private BigDecimal valor;
	
	public Coligada getColigada() {
		return coligada;
	}
	public void setColigada(Coligada coligada) {
		this.coligada = coligada;
	}
	public CentroCustoRM getCentroCusto() {
		return centroCusto;
	}
	public void setCentroCusto(CentroCustoRM centroCusto) {
		this.centroCusto = centroCusto;
	}
	public Date getDtAjuste() {
		return dtAjuste;
	}
	public void setDtAjuste(Date dtAjuste) {
		this.dtAjuste = dtAjuste;
	}
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<AjusteOrcamentarioDTO> getItens() {
		return itens;
	}
	public void setItens(List<AjusteOrcamentarioDTO> itens) {
		this.itens = itens;
	}
	public Integer getIdNaturezaOrigem() {
		return idNaturezaOrigem;
	}
	public void setIdNaturezaOrigem(Integer idNaturezaOrigem) {
		this.idNaturezaOrigem = idNaturezaOrigem;
	}
	public String getNaturezaOrigem() {
		return naturezaOrigem;
	}
	public void setNaturezaOrigem(String naturezaOrigem) {
		this.naturezaOrigem = naturezaOrigem;
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
	public Integer getIdNaturezaDestino() {
		return idNaturezaDestino;
	}
	public void setIdNaturezaDestino(Integer idNaturezaDestino) {
		this.idNaturezaDestino = idNaturezaDestino;
	}
	public String getNaturezaDestino() {
		return naturezaDestino;
	}
	public void setNaturezaDestino(String naturezaDestino) {
		this.naturezaDestino = naturezaDestino;
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
	
}
