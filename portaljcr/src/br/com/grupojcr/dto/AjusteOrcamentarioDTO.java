package br.com.grupojcr.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.grupojcr.entity.Coligada;
import br.com.grupojcr.enumerator.Mes;
import br.com.grupojcr.rm.CentroCustoRM;
import br.com.grupojcr.rm.NaturezaOrcamentariaRM;

public class AjusteOrcamentarioDTO implements Serializable {
	
	private static final long serialVersionUID = 589215434467451886L;
	
	private Long id;
	private Coligada coligada;
	private CentroCustoRM centroCusto;
	private Date dtAjuste;
	private String ano;
	private String valorTotal;
	
	private NaturezaOrcamentariaRM naturezaOrigem;
	private NaturezaOrcamentariaRM naturezaDestino;
	private Mes mesOrigem;
	private Mes mesDestino;
	private Integer idOrcamentoOrigem;
	private Integer idOrcamentoDestino;
	private BigDecimal valor;
	private BigDecimal valorOrcadoOrigem;
	private BigDecimal valorSaldoOrigem;
	private BigDecimal valorOrcadoDestino;
	private BigDecimal valorSaldoDestino;
	private BigDecimal valorNovoSaldo;
	private Integer periodo;
	
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
	public Integer getIdOrcamentoOrigem() {
		return idOrcamentoOrigem;
	}
	public void setIdOrcamentoOrigem(Integer idOrcamentoOrigem) {
		this.idOrcamentoOrigem = idOrcamentoOrigem;
	}
	public Integer getIdOrcamentoDestino() {
		return idOrcamentoDestino;
	}
	public void setIdOrcamentoDestino(Integer idOrcamentoDestino) {
		this.idOrcamentoDestino = idOrcamentoDestino;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public NaturezaOrcamentariaRM getNaturezaOrigem() {
		return naturezaOrigem;
	}
	public void setNaturezaOrigem(NaturezaOrcamentariaRM naturezaOrigem) {
		this.naturezaOrigem = naturezaOrigem;
	}
	public NaturezaOrcamentariaRM getNaturezaDestino() {
		return naturezaDestino;
	}
	public void setNaturezaDestino(NaturezaOrcamentariaRM naturezaDestino) {
		this.naturezaDestino = naturezaDestino;
	}
	public Mes getMesOrigem() {
		return mesOrigem;
	}
	public void setMesOrigem(Mes mesOrigem) {
		this.mesOrigem = mesOrigem;
	}
	public Mes getMesDestino() {
		return mesDestino;
	}
	public void setMesDestino(Mes mesDestino) {
		this.mesDestino = mesDestino;
	}
	public BigDecimal getValorOrcadoOrigem() {
		return valorOrcadoOrigem;
	}
	public void setValorOrcadoOrigem(BigDecimal valorOrcadoOrigem) {
		this.valorOrcadoOrigem = valorOrcadoOrigem;
	}
	public BigDecimal getValorSaldoOrigem() {
		return valorSaldoOrigem;
	}
	public void setValorSaldoOrigem(BigDecimal valorSaldoOrigem) {
		this.valorSaldoOrigem = valorSaldoOrigem;
	}
	public BigDecimal getValorOrcadoDestino() {
		return valorOrcadoDestino;
	}
	public void setValorOrcadoDestino(BigDecimal valorOrcadoDestino) {
		this.valorOrcadoDestino = valorOrcadoDestino;
	}
	public BigDecimal getValorSaldoDestino() {
		return valorSaldoDestino;
	}
	public void setValorSaldoDestino(BigDecimal valorSaldoDestino) {
		this.valorSaldoDestino = valorSaldoDestino;
	}
	public BigDecimal getValorNovoSaldo() {
		return valorNovoSaldo;
	}
	public void setValorNovoSaldo(BigDecimal valorNovoSaldo) {
		this.valorNovoSaldo = valorNovoSaldo;
	}
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	
}
