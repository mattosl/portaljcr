package br.com.grupojcr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TB_ANEXO_CHAMADO")
public class AnexoChamado implements Serializable {

	private static final long serialVersionUID = 1064308060008295335L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "NOME", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "CAMINHO", length = 200, nullable = false)
	private String caminho;
	
	@Column(name = "DT_INCLUSAO", nullable = false)
	private Date dtInclusao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_USR_INCLUSAO", nullable = false)
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_CHAMADO", nullable = false)
	private Chamado chamado;


}
