package br.com.grupojcr.dto;

import java.io.IOException;
import java.io.InputStream;

import org.primefaces.model.UploadedFile;

import br.com.grupojcr.util.TreatString;


public class ArquivoDTO {
	
	private String nome;

	private InputStream data;

	private UploadedFile uploadedFile;

	public ArquivoDTO() {
	}
	
	public ArquivoDTO(String nome, InputStream data, UploadedFile ArquivoDTO) {
		this.nome = nome;
		this.data = data;
		this.uploadedFile = ArquivoDTO;
	}

	public ArquivoDTO(String nome) {
		this.nome = nome;
	}
	
	public ArquivoDTO(String nome, InputStream data) {
		this.nome = nome;
		this.data = data;
	}

	public static ArquivoDTO novo(UploadedFile ArquivoDTO) {
		if (ArquivoDTO == null) {
			return new ArquivoDTO();
		}
		try {
			return new ArquivoDTO(ArquivoDTO.getFileName(), ArquivoDTO.getInputstream(), ArquivoDTO);
		} catch (IOException e) {
			return null;
		}
	}

	public static ArquivoDTO novo(String pathFotoExcluir) {
		return new ArquivoDTO(pathFotoExcluir);
	}

	public boolean eq(ArquivoDTO test) {
		if (uploadedFile != null && test.getUploadedFile() != null) {
			return uploadedFile.equals(test.getUploadedFile());
		}
		if (TreatString.isBlank(nome)) {
			return false;
		}
		return this.nome.equals(test.getNome());
	}

	public boolean isConteudoInformado() {
		return data != null;
	}

	public InputStream getData() {
		return data;
	}

	public void setData(InputStream data) {
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

}
