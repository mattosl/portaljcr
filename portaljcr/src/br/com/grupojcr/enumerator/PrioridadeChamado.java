package br.com.grupojcr.enumerator;

public enum PrioridadeChamado {
	ALTA(0, "Alta"),
	MEDIA(1, "Média"),
	BAIXA(2, "Baixa");
	
	private Integer id;
	private String descricao;
	
	private PrioridadeChamado(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static PrioridadeChamado obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(PrioridadeChamado status : PrioridadeChamado.values()) {
				if(status.getId().equals(codigo)) {
					return status;
				}
			}
		}
		return null;
	}

}
