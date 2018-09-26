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

import br.com.grupojcr.business.PontoBusiness;
import br.com.grupojcr.serialize.PontoSerialize;
import br.com.grupojcr.util.Util;

@Path("/ponto")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PontoWS {
	
	protected static Logger LOG = Logger.getLogger(PontoWS.class);
	
	@EJB
	private PontoBusiness pontoBusiness;
	
	@POST
	@Path("/aprovar")
	public Response aprovar(String data) {
		LOG.info("[aprovar] Método iniciado");
		try {
			PontoSerialize ponto = (PontoSerialize) new Gson().fromJson(data, PontoSerialize.class);
			if (Util.isNotNull(ponto.getIdAjustePonto())) {
				pontoBusiness.aprovar(ponto.getIdAjustePonto());
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
			PontoSerialize ponto = (PontoSerialize) new Gson().fromJson(data, PontoSerialize.class);
			if (Util.isNotNull(ponto.getIdAjustePonto())) {
				pontoBusiness.recusar(ponto.getIdAjustePonto(), ponto.getMotivo());
				return Response.status(200).build();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
		return Response.status(400).build();
	}

}
