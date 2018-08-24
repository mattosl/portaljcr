package br.com.grupojcr.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PFPERFF")
@XmlAccessorType(XmlAccessType.FIELD)
public class PFPERFFXML {
	
	public PFPERFFXML() {
		
	}
	@XmlElement(name = "CODCOLIGADA")
	private String CODCOLIGADA;

	public String getCODCOLIGADA() {
		return CODCOLIGADA;
	}

	public void setCODCOLIGADA(String cODCOLIGADA) {
		CODCOLIGADA = cODCOLIGADA;
	}
}
