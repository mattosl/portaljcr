package br.com.grupojcr.enumerator;

public enum EstiloXML {
	CURITIBA(0);
	
	private Integer id;
	
	private EstiloXML(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
