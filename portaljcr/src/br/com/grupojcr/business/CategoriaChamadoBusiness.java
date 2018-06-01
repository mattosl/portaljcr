package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.dao.CategoriaChamadoDAO;
import br.com.grupojcr.dao.SubCategoriaChamadoDAO;
import br.com.grupojcr.entity.CategoriaChamado;
import br.com.grupojcr.entity.SubCategoriaChamado;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class CategoriaChamadoBusiness {
	
	private static Logger LOG = Logger.getLogger(CategoriaChamadoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private CategoriaChamadoDAO daoCategoriaChamado;
	
	@EJB
	private SubCategoriaChamadoDAO daoSubcategoriaChamado;
	
	/**
	 * Método responsavel por listar categorias
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 05/04/2018
	 * @return List<CategoriaChamado>
	 * @throws ApplicationException
	 */
	public List<CategoriaChamado> listarCategoriaChamado() throws ApplicationException {
		try {
			return daoCategoriaChamado.listarCategorias();
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarCategoriaChamado" }, e);
		}
	}
	
	public void salvar(CategoriaChamado cc) throws ApplicationException {
		try {
			if(TreatString.isBlank(cc.getNome())) {
				throw new ApplicationException("message.empty", new String[] {"Nome da Categoria de Chamado é obrigatório"},  FacesMessage.SEVERITY_ERROR);
			}
			if(CollectionUtils.isEmpty(cc.getSubCategorias())) {
				throw new ApplicationException("categoria.chamado.subcategoria.vazio", FacesMessage.SEVERITY_WARN);
			}
			if(daoCategoriaChamado.verificarCategoriaExiste(cc.getNome(), cc.getId())) {
				throw new ApplicationException("grupo.cotacao.existente", FacesMessage.SEVERITY_WARN);
			}
			
			if(Util.isNotNull(cc.getId())) {
				CategoriaChamado categoriaChamadoBanco = daoCategoriaChamado.obterCategoriaChamado(cc.getId());
				
				List<SubCategoriaChamado> subcategoriasRemover = new ArrayList<SubCategoriaChamado>();
				for(SubCategoriaChamado scc : categoriaChamadoBanco.getSubCategorias()) {
					Boolean existe = Boolean.FALSE;
					for(SubCategoriaChamado sub : cc.getSubCategorias()) {
						if(Util.isNotNull(sub.getId())) {
							if(scc.getId().equals(sub.getId())) {
								existe = Boolean.TRUE;
							}
						} else {
							existe = Boolean.TRUE;
						}
						
					}
					if(!existe) {
						subcategoriasRemover.add(scc);
					}
				}
				
				for(SubCategoriaChamado remover : subcategoriasRemover) {
					daoSubcategoriaChamado.excluir(remover);
				}
				
				for(SubCategoriaChamado scc : cc.getSubCategorias()) {
					if(Util.isNull(scc.getId())) {
						scc.setCategoria(cc);
						daoSubcategoriaChamado.incluir(scc);
					}
				}
				
				daoCategoriaChamado.alterar(cc);
			} else {
				CategoriaChamado novaCategoria = new CategoriaChamado();
				novaCategoria.setNome(cc.getNome());
				
				daoCategoriaChamado.incluir(novaCategoria);
				
				for(SubCategoriaChamado subcategoria : cc.getSubCategorias()) {
					subcategoria.setCategoria(novaCategoria);
					daoSubcategoriaChamado.incluir(subcategoria);
				}
			}
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}

}
