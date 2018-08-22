package br.com.grupojcr.ws;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class MyJaxBean {
	
	@XmlElement
	private Integer idColigada;
	
	@XmlElement
	private Integer idMovimento;

	public Integer getIdColigada() {
		return idColigada;
	}

	public void setIdColigada(Integer idColigada) {
		this.idColigada = idColigada;
	}

	public Integer getIdMovimento() {
		return idMovimento;
	}

	public void setIdMovimento(Integer idMovimento) {
		this.idMovimento = idMovimento;
	}

	
}
