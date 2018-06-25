package br.com.grupojcr.enumerator;

import java.util.ArrayList;
import java.util.List;

public enum SituacaoSolicitacaoCompra {
	AGUARDANDO_APRV(0, "Aguardando Aprovação"),
	APROVADA_COTACAO(1, "Aprovada para Cotação"),
	EM_COTACAO(2, "Em Cotação"),
	AGUARDANDO_APRV_COTACAO(3, "Cotação para Aprovação"),
	LIBERADO_ORDEM_COMPRA(4, "Liberado Ordem de Compra"),
	FINALIZADA(5, "Finalizada"),
	CANCELADA(6, "Cancelada");
	
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
	
	public static List<SituacaoSolicitacaoCompra> listarParaCotacao() {
		List<SituacaoSolicitacaoCompra> solicitacoes = new ArrayList<SituacaoSolicitacaoCompra>();
		for(SituacaoSolicitacaoCompra obj : SituacaoSolicitacaoCompra.values()) {
			if(!obj.getId().equals(AGUARDANDO_APRV.getId()) && !obj.getId().equals(APROVADA_COTACAO.getId())) {
				solicitacoes.add(obj);
			}
		}
		return solicitacoes;
	}

}
