package br.com.grupojcr.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FopEnvelope")
@XmlAccessorType(XmlAccessType.FIELD)
public class FopEnvelopeXML {
	
	public FopEnvelopeXML() {
	}
	
	@XmlElement(name = "PFPERFF")
	private PFPERFFXML pfperff;

	public PFPERFFXML getPfperff() {
		return pfperff;
	}

	public void setPfperff(PFPERFFXML pfperff) {
		this.pfperff = pfperff;
	}

}
