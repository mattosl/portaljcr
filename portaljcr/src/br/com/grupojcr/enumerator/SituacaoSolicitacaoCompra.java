package br.com.grupojcr.enumerator;

public enum SituacaoSolicitacaoCompra {
	AGUARDANDO_APRV(0, "Aguardando Aprovação"),
	APROVADA_COTACAO(1, "Aprovada para Cotação"),
	EM_COTACAO(2, "Em Cotação"),
	AGUARDANDO_APRV_COTACAO(3, "Aguardando Aprovação da Cotação"),
	COTACAO_APROVADA(4, "Cotação Aprovada"),
	COTACAO_RECUSADA(5, "Cotação Recusada"),
	FINALIZADA(6, "Finalizada"),
	CANCELADA(7, "Cancelada");
	
	private Integer id;
	private String descricao;
	
	private SituacaoSolicitacaoCompra(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static SituacaoSolicitacaoCompra obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(SituacaoSolicitacaoCompra obj : SituacaoSolicitacaoCompra.values()) {
				if(obj.getId().equals(codigo)) {
					return obj;
				}
			}
		}
		return null;
	}

}
