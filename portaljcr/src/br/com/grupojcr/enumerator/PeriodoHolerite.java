package br.com.grupojcr.enumerator;

public enum PeriodoHolerite {
	NENHUM(0, "NENHUM"),
	ADIANTAMENTO(1, "ADIANTAMENTO"),
	FOLHA_MENSAL(2, "FOLHA MENSAL"),
	BENEFICIOS(3, "BENEFICIOS"),
	DIRETORES(4, "DIRETORES - FOLHA MENSAL"),
	FOLHA_MENSAL_HORISTA(5, "FOLHA MENSAL HORISTA"),
	PREMIO_BONUS(6, "PREMIO/BONUS");
	
	private Integer id;
	private String descricao;
	
	private PeriodoHolerite(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static PeriodoHolerite obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(PeriodoHolerite status : PeriodoHolerite.values()) {
				if(status.getId().equals(codigo)) {
					return status;
				}
			}
		}
		return null;
	}

}
