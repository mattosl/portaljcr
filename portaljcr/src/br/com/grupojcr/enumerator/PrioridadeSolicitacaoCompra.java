package br.com.grupojcr.enumerator;

public enum PrioridadeSolicitacaoCompra {
	ALTA(0, "Alta"),
	MEDIA(1, "Média"),
	BAIXA(2, "Baixa");
	
	private Integer id;
	private String descricao;
	
	private PrioridadeSolicitacaoCompra(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static PrioridadeSolicitacaoCompra obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(PrioridadeSolicitacaoCompra obj : PrioridadeSolicitacaoCompra.values()) {
				if(obj.getId().equals(codigo)) {
					return obj;
				}
			}
		}
		return null;
	}

}
