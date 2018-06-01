package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;

import br.com.grupojcr.business.CategoriaChamadoBusiness;
import br.com.grupojcr.entity.CategoriaChamado;
import br.com.grupojcr.entity.SubCategoriaChamado;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class ChamadoController implements Serializable {
	
	private static final long serialVersionUID = 934937300949501891L;
	protected static Logger LOG = Logger.getLogger(ChamadoController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private List<SelectItem> listaCategoria;
	
	@EJB
	private CategoriaChamadoBusiness categoriaChamadoBusiness;

	/**
	 * Método responsavel por iniciar processo
	 * @author Leonan Mattos <leonan.mattos@grupojcr.com.br>
	 * @since 28/05/2018
	 * @throws ApplicationException
	 */
	public void iniciarProcesso() throws ApplicationException {
		try {
			setListaCategoria(new ArrayList<SelectItem>());
			List<CategoriaChamado> categorias = categoriaChamadoBusiness.listarCategoriaChamado();
			
			if(Util.isNotNull(categorias)) {
				for(CategoriaChamado cc : categorias) {
					SelectItemGroup grupo = new SelectItemGroup(cc.getNome());
					
					List<SelectItem> itens = new ArrayList<SelectItem>();
					for(SubCategoriaChamado sc : cc.getSubCategorias()) {
						itens.add(new SelectItem(sc, sc.getNome()));
					}
					
					SelectItem [] selectItem = new SelectItem[itens.size()];
					itens.toArray(selectItem);
					grupo.setSelectItems(selectItem);
					
					getListaCategoria().add(grupo);
					
				}
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "iniciarProcesso" }, e);
		}
	}

	public List<SelectItem> getListaCategoria() {
		return listaCategoria;
	}

	public void setListaCategoria(List<SelectItem> listaCategoria) {
		this.listaCategoria = listaCategoria;
	}

}
