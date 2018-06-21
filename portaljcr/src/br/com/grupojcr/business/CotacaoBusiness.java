package br.com.grupojcr.business;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.CotacaoDAO;
import br.com.grupojcr.dao.CotacaoItemDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class CotacaoBusiness {
	
	private static Logger LOG = Logger.getLogger(ChamadoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private CotacaoDAO daoCotacao;
	
	@EJB
	private CotacaoItemDAO daoCotacaoItem;
	
	@EJB
	private RMDAO daoRM;
	
	public Cotacao salvar(SolicitacaoCompra solicitacao, Cotacao cotacao) throws ApplicationException {
		try {
			Usuario usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			cotacao.setSolicitacaoCompra(solicitacao);
			cotacao.setUsuarioCotacao(usuarioLogado);
			cotacao.setDtCotacao(Calendar.getInstance().getTime());
			cotacao.setMelhorOpcao(Boolean.FALSE);
			cotacao.setCotacaoPrincipal(Boolean.FALSE);
			
			if(Util.isNotNull(cotacao.getId())) {
				daoCotacao.alterar(cotacao);
			} else {
				daoCotacao.incluir(cotacao);
			}
			
			for(CotacaoItem item : cotacao.getItens()) {
				if(item.getNaoPossui()) {
					item.setQuantidade(0);
					item.setCodigoUnidade(null);
					item.setValor(new BigDecimal(0));
					item.setValorTotal(new BigDecimal(0));
				} else {
					if(TreatString.isNotBlank(item.getCodigoUnidade())) {
						item.setCodigoUnidade(item.getCodigoUnidade());
					}
				}
				item.setCotacao(cotacao);
				if(Util.isNotNull(item.getId())) {
					daoCotacaoItem.alterar(item);
				} else {
					daoCotacaoItem.incluir(item);
				}
			}
			
			return cotacao;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
	public void excluir(Cotacao cotacao) throws ApplicationException {
		try {
			for(CotacaoItem item : cotacao.getItens()) {
				daoCotacaoItem.excluir(item);
			}
			
			daoCotacao.excluir(cotacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "excluir" }, e);
		}
	}
	
	public List<CotacaoItem> listarItensCotacao(Long idCotacao) throws ApplicationException {
		try {
			return daoCotacaoItem.listarItensCotacao(idCotacao);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarItensPorSolicitacao" }, e);
		}
	}
	
}
