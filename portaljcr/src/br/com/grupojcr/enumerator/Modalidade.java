package br.com.grupojcr.enumerator;

public enum Modalidade {
	MATERIAL(0, "Material"),
	SERVICO(1, "Serviço"),
	MATERIAL_SERVICO(2, "Material/Serviço");
	
	private Integer id;
	private String descricao;
	
	private Modalidade(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Modalidade obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(Modalidade obj : Modalidade.values()) {
				if(obj.getId().equals(codigo)) {
					return obj;
				}
			}
		}
		return null;
	}

}
