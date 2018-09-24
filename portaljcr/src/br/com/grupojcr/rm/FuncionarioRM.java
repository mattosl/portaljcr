package br.com.grupojcr.rm;

import java.io.Serializable;
import java.util.Date;

public class FuncionarioRM implements Serializable {
	
	private static final long serialVersionUID = 4748704330742155442L;
	
	private Integer codColigada;
	private String empresa;
	private String cnpj;
	private String chapa;
	private String nomeFuncionario;
	private String funcao;
	private Date dtAdmissao;
	private String rua;
	private String numero;
	private String bairro;
	private String cidade;
	private String cep;
	private String estado;
	private String pispasep;
	private String cpf;
	private String identidade;
	private String nomeBanco;
	private String agencia;
	private String numeroAgencia;
	private String numeroConta;
	private String secao;
	private String codSecao;
	
	public String getChapa() {
		return chapa;
	}
	public void setChapa(String chapa) {
		this.chapa = chapa;
	}
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	public Date getDtAdmissao() {
		return dtAdmissao;
	}
	public void setDtAdmissao(Date dtAdmissao) {
		this.dtAdmissao = dtAdmissao;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getPispasep() {
		return pispasep;
	}
	public void setPispasep(String pispasep) {
		this.pispasep = pispasep;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getIdentidade() {
		return identidade;
	}
	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}
	public String getNomeBanco() {
		return nomeBanco;
	}
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getNumeroAgencia() {
		return numeroAgencia;
	}
	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}
	public String getNumeroConta() {
		return numeroConta;
	}
	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}
	public String getFuncao() {
		return funcao;
	}
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public Integer getCodColigada() {
		return codColigada;
	}
	public void setCodColigada(Integer codColigada) {
		this.codColigada = codColigada;
	}
	public String getSecao() {
		return secao;
	}
	public void setSecao(String secao) {
		this.secao = secao;
	}
	public String getCodSecao() {
		return codSecao;
	}
	public void setCodSecao(String codSecao) {
		this.codSecao = codSecao;
	}

}
