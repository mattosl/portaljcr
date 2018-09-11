package br.com.grupojcr.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import br.com.grupojcr.business.SolicitacaoCompraBusiness;
import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.CotacaoItem;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.ws.response.CotacaoResponse;
import br.com.grupojcr.ws.response.SolicitacaoCompraResponse;

@Path("/cotacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CotacaoWS {
	
	protected static Logger LOG = Logger.getLogger(CotacaoWS.class);
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@POST
	@Path("/obter")
	public Response obterCotacao(final MyJaxBean parametros) {
		LOG.info("[obter] Método iniciado");
		try {
			SolicitacaoCompra solicitacao = solicitacaoCompraBusiness.obterSolicitacaoPorColigadaMovimento(parametros.getIdColigada().longValue(), parametros.getIdMovimento().longValue());
			
			if(Util.isNotNull(solicitacao)) {
				
				SolicitacaoCompraResponse resposta = new SolicitacaoCompraResponse();
				resposta.setIdSolicitacao(solicitacao.getId());
				resposta.setSolicitante(solicitacao.getUsuarioSolicitante().getNome());
				resposta.setDataSolicitacao(TreatDate.format("dd/MM/yyyy", solicitacao.getDtAprovacao()));
				resposta.setUsuarioAprovador(solicitacao.getUsuarioAprovacaoFluig());
				resposta.setMotivoCompra(solicitacao.getMotivoCompra());
				resposta.setValorEstimado(TreatNumber.formatMoney(solicitacao.getValorTotalAproximado()));
				
				List<Cotacao> listaCotacao = solicitacaoCompraBusiness.listarCotacoesPorSolicitacao(solicitacao.getId());
				List<CotacaoResponse> listaCotacaoResponse = new ArrayList<CotacaoResponse>();
				if(Util.isNotNull(listaCotacao))  {
					for(Cotacao cot : listaCotacao) {
						CotacaoResponse cotacao = new CotacaoResponse();
						cotacao.setIdCotacao(cot.getId());
						cotacao.setFornecedor(cot.getFornecedor().toUpperCase());
						cotacao.setValorTotalCotacao(TreatNumber.formatMoneyCurrency(cot.getValorTotal()));
						cotacao.setMelhorOpcao(cot.getCotacaoPrincipal());
						cotacao.setListaItens(new ArrayList<CotacaoResponse>());
						if(TreatNumber.isNotNullOrZero(cot.getFrete())) {
							cotacao.setValorFrete(TreatNumber.formatMoneyCurrency(cot.getFrete()));
						} else {
							cotacao.setValorFrete(TreatNumber.formatMoneyCurrency(0));
						}
						
						for(CotacaoItem it : cot.getItens()) {
							CotacaoResponse item = new CotacaoResponse();
							item.setProdutoServico(it.getSolicitacaoCompraItem().getDescricaoProduto());
							item.setUnidade(it.getCodigoUnidade());
							item.setQuantidade(TreatNumber.formatMoney(it.getQuantidade()));
							item.setValorUnitario(TreatNumber.formatMoneyCurrency(it.getValor()));
							item.setValorTotalItem(TreatNumber.formatMoneyCurrency(it.getValorTotal()));
							
							cotacao.getListaItens().add(item);
						}
						
						listaCotacaoResponse.add(cotacao);
					}
				}
				String jsonLista = new Gson().toJson(listaCotacaoResponse);
				resposta.setListaCotacao(jsonLista);
				return Response.status(200).entity(resposta).build();
			}
			return Response.status(200).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
	}

}
