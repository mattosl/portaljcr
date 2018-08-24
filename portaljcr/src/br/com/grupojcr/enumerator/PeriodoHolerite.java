package br.com.grupojcr.enumerator;

public enum PeriodoHolerite {
	NENHUM(0, "NENHUM"),
	ADIANTAMENTO(1, "ADIANTAMENTO"),
	DECIMO_TERCEIRO(2, "13º SALÁRIO"),
	FOLHA_MENSAL(3, "FOLHA MENSAL"),
	PREMIO(4, "PRÊMIO");
	
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
