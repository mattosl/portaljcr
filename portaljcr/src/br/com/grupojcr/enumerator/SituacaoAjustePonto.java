package br.com.grupojcr.enumerator;

public enum SituacaoAjustePonto {

	RASCUNHO(0, "Rascunho"),
	AGUARDANDO_APROVACAO(1, "Aguardando Aprovação"),
	APROVADO(2, "Aprovado");
	
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
