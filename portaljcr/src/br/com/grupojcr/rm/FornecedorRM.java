package br.com.grupojcr.rm;

import java.io.Serializable;

import br.com.grupojcr.entity.BaseStringEntity;

public class FornecedorRM implements BaseStringEntity, Serializable {
	
	private static final long serialVersionUID = 8107905232953442774L;
	
	private String codigoFornecedor;
	private String fornecedor;
	private String cnpjCpf;
	
	@Override
	public String getId() {
		return codigoFornecedor;
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

	public String getCnpjCpf() {
		return cnpjCpf;
	}

	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}
	
	public String getFornecedorFormatado() {
		return codigoFornecedor + " - " + fornecedor + " (" + cnpjCpf + ")";
	}

}
