package br.com.grupojcr.enumerator;

public enum CausaChamado {
	NORMAL(0, "Normal"),
	USUARIO_SEM_INSTRUCAO(1, "Usuário sem Instrução");
	
	private Integer id;
	private String descricao;
	
	private CausaChamado(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static CausaChamado obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(CausaChamado status : CausaChamado.values()) {
				if(status.getId().equals(codigo)) {
					return status;
				}
			}
		}
		return null;
	}

}
