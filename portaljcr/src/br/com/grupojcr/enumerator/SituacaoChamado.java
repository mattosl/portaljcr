package br.com.grupojcr.enumerator;

public enum SituacaoChamado {
	ABERTO(0, "Aberto"),
	EM_ANDAMENTO(1, "Em Andamento"),
	RESOLVIDO(2, "Resolvido"),
	FECHADO(3, "Fechado");
	
	private Integer id;
	private String descricao;
	
	private SituacaoChamado(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static SituacaoChamado obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(SituacaoChamado status : SituacaoChamado.values()) {
				if(status.getId().equals(codigo)) {
					return status;
				}
			}
		}
		return null;
	}

}
