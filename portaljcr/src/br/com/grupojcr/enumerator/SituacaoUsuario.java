package br.com.grupojcr.enumerator;

public enum SituacaoUsuario {
	INATIVO(0, "Inativo"),
	ATIVO(1, "Ativo");
	
	private Integer id;
	private String descricao;
	
	private SituacaoUsuario(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static SituacaoUsuario obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(SituacaoUsuario status : SituacaoUsuario.values()) {
				if(status.getId().equals(codigo)) {
					return status;
				}
			}
		}
		return null;
	}

}
