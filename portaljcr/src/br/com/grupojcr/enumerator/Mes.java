package br.com.grupojcr.enumerator;

import java.util.ArrayList;
import java.util.List;

public enum Mes {
	JANEIRO(1, "Janeiro"),
	FEVEREIRO(2, "Fevereiro"),
	MARCO(3, "Março"),
	ABRIL(4, "Abril"),
	MAIO(5, "Maio"),
	JUNHO(6, "Junho"),
	JULHO(7, "Julho"),
	AGOSTO(8, "Agosto"),
	SETEMBRO(9, "Setembro"),
	OUTUBRO(10, "Outubro"),
	NOVEMBRO(11, "Novembro"),
	DEZEMBRO(12, "Dezembro");
	
	private Integer id;
	private String descricao;
	
	private Mes(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Mes obterPorCodigo(Integer codigo) {
		if(codigo != null) {
			for(Mes obj : Mes.values()) {
				if(obj.getId().equals(codigo)) {
					return obj;
				}
			}
		}
		return null;
	}
	
	public static List<Mes> listarAPartir(Integer mes) {
		List<Mes> meses = new ArrayList<Mes>();
		for(Mes obj : Mes.values()) {
			if(obj.getId() > mes) {
				meses.add(obj);
			}
		}
		return meses;
	}

}
