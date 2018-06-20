package br.com.grupojcr.business;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.CotacaoDAO;
import br.com.grupojcr.dao.CotacaoItemDAO;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class CotacaoBusiness {
	
	private static Logger LOG = Logger.getLogger(ChamadoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";

	@EJB
	private CotacaoDAO daoCotacao;
	
	@EJB
	private CotacaoItemDAO daoCotacaoItem;
	
	public Cotacao salvar(SolicitacaoCompra solicitacao, Cotacao cotacao) throws ApplicationException {
		try {
			Usuario usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
			cotacao.setSolicitacaoCompra(solicitacao);
			cotacao.setUsuarioCotacao(usuarioLogado);
			cotacao.setDtCotacao(Calendar.getInstance().getTime());
			cotacao.setMelhorOpcao(Boolean.FALSE);
			cotacao.setCotacaoPrincipal(Boolean.FALSE);
			
			daoCotacao.incluir(cotacao);
			
			for(CotacaoItem item : cotacao.getItens()) {
				item.setCotacao(cotacao);
				if(item.getNaoPossui()) {
					item.setValor(new Double(0));
					item.setValorTotal(new Double(0));
				}
				daoCotacaoItem.incluir(item);
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
	
}
