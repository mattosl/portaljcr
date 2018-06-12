package br.com.grupojcr.business;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.SolicitacaoCompraDAO;
import br.com.grupojcr.dao.SolicitacaoCompraItemDAO;
import br.com.grupojcr.dto.SolicitacaoCompraDTO;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.entity.SolicitacaoCompraItem;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.enumerator.PrioridadeSolicitacaoCompra;
import br.com.grupojcr.enumerator.SituacaoSolicitacaoCompra;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class SolicitacaoCompraBusiness {
	
	private static Logger LOG = Logger.getLogger(ChamadoBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private SolicitacaoCompraDAO daoSolicitacaoCompra;
	
	@EJB
	private SolicitacaoCompraItemDAO daoSolicitacaoCompraItem;
	
	public void salvar(SolicitacaoCompraDTO dto, Usuario usuario) throws ApplicationException {
		try {
			SolicitacaoCompra solicitacao = new SolicitacaoCompra();
			solicitacao.setSituacao(SituacaoSolicitacaoCompra.AGUARDANDO_APRV);
			solicitacao.setCodigoCentroCusto(dto.getCentroCusto().getCodigoCentroCusto());
			solicitacao.setCentroCusto(dto.getCentroCusto().getCentroCusto());
			solicitacao.setCodigoNaturezaOrcamentaria(dto.getNaturezaOrcamentaria().getCodigoNaturezaOrcamentaria());
			solicitacao.setNaturezaOrcamentaria(dto.getNaturezaOrcamentaria().getNaturezaOrcamentaria());
			solicitacao.setModalidade(dto.getModalidade());
			solicitacao.setPrioridade(dto.getPrioridade());
			solicitacao.setMotivoCompra(dto.getMotivoCompra());
			solicitacao.setSugestaoFornecedor(dto.getSugestaoFornecedor());
			solicitacao.setLocalEntrega(dto.getLocalEntrega());
			solicitacao.setValorAproximado(dto.getValorAproximado());
			solicitacao.setDtSolicitacao(Calendar.getInstance().getTime());
			solicitacao.setUsuarioSolicitante(usuario);
			solicitacao.setColigada(dto.getColigada());
			if(dto.getPossuiGrupoCotacao()) {
				solicitacao.setGrupoCotacao(dto.getGrupoCotacao());
			}
			
			Calendar prazo = Calendar.getInstance();
			
			if(dto.getPrioridade().equals(PrioridadeSolicitacaoCompra.ALTA)) {
				prazo.add(Calendar.DAY_OF_MONTH, 10);
			} else if(dto.getPrioridade().equals(PrioridadeSolicitacaoCompra.MEDIA)) {
				prazo.add(Calendar.DAY_OF_MONTH, 7);
			} else {
				prazo.add(Calendar.DAY_OF_MONTH, 3);
			}
			
			while(prazo.get(Calendar.DAY_OF_WEEK) == 1 || prazo.get(Calendar.DAY_OF_WEEK) == 7) {
				prazo.add(Calendar.DAY_OF_MONTH, 1);
			}
			solicitacao.setDtPrazo(prazo.getTime());
			solicitacao.setUsuarioAprovacaoFluig("leonan");
			
			// TODO INICIAR FLUIG
			
			daoSolicitacaoCompra.incluir(solicitacao);
			
			for(SolicitacaoCompraItem sci : dto.getItens()) {
				sci.setSolicitacaoCompra(solicitacao);
				daoSolicitacaoCompraItem.incluir(sci);
			}
			
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "salvar" }, e);
		}
	}
	
}
