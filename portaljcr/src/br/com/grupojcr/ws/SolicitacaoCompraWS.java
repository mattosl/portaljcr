package br.com.grupojcr.ws;

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
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.serialize.SolicitacaoCompraSerialize;
import br.com.grupojcr.util.Util;

@Path("/solicitacaoCompra")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SolicitacaoCompraWS {
	
	protected static Logger LOG = Logger.getLogger(SolicitacaoCompraWS.class);
	
	@EJB
	private SolicitacaoCompraBusiness solicitacaoCompraBusiness;
	
	@POST
	@Path("/aprovar")
	public Response aprovar(String data) {
		LOG.info("[aprovar] Método iniciado");
		try {
			SolicitacaoCompraSerialize solicitacao = (SolicitacaoCompraSerialize) new Gson().fromJson(data, SolicitacaoCompraSerialize.class);
			if (Util.isNotNull(solicitacao.getIdSolicitacao())) {
				SolicitacaoCompra solicitacaoCompra = solicitacaoCompraBusiness.aprovar(solicitacao.getIdSolicitacao(), solicitacao.getObservacao());
				if(Util.isNotNull(solicitacaoCompra)) {
					solicitacaoCompraBusiness.enviarEmailAprovacao(solicitacaoCompra);
				}
				return Response.status(200).build();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
		return Response.status(400).build();
	}
	
	@POST
	@Path("/recusar")
	public Response recusar(String data) {
		LOG.info("[recusar] Método iniciado");
		try {
			SolicitacaoCompraSerialize solicitacao = (SolicitacaoCompraSerialize) new Gson().fromJson(data, SolicitacaoCompraSerialize.class);
			if (Util.isNotNull(solicitacao.getIdSolicitacao())) {
				SolicitacaoCompra solicitacaoCompra = solicitacaoCompraBusiness.recusar(solicitacao.getIdSolicitacao(), solicitacao.getMotivo());
				if(Util.isNotNull(solicitacaoCompra)) {
					solicitacaoCompraBusiness.enviarEmailRecusar(solicitacaoCompra);
				}
				return Response.status(200).build();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
		return Response.status(400).build();
	}

}
