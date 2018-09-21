package br.com.grupojcr.enumerator;

public enum SituacaoAjustePonto {

	AGUARDANDO_APROVACAO(0, "Aguardando Aprovação"),
	APROVADO(1, "Aprovado");
	
	private Integer id;
	private String descricao;
	
	private SituacaoAjustePonto(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static SituacaoAjustePonto obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(SituacaoAjustePonto obj : SituacaoAjustePonto.values()) {
				if(obj.getId().equals(codigo)) {
					return obj;
				}
			}
		}
		return null;
	}
}
