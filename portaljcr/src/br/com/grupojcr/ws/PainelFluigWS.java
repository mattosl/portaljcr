package br.com.grupojcr.ws;

import java.text.ParseException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import br.com.grupojcr.business.FluigBusiness;
import br.com.grupojcr.dto.SolicitacaoAprovacaoDTO;

@Path("/painelFluig")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PainelFluigWS {
	
	protected static Logger LOG = Logger.getLogger(PainelFluigWS.class);
	
	@EJB
	private FluigBusiness fluigBusiness;
	
	
	@POST
	@Path("/solicitacaoAprovacao")
	public Response listarSolicitacaoAprovacao(String data) throws ParseException {
		LOG.info("[listarSolicitacaoAprovacao] Método iniciado");
		try {
			JsonSerialize json = (JsonSerialize) new Gson().fromJson(data, JsonSerialize.class);
			SolicitacaoAprovacaoDTO dto = fluigBusiness.listarSolicitacoesAprovacao(json.getUsuarioLogado());
			return Response.status(200).entity(dto).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
	}

}
