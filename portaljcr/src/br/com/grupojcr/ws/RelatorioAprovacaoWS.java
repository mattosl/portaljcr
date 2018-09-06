package br.com.grupojcr.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.CentroCustoDTO;
import br.com.grupojcr.dto.ColigadaDTO;
import br.com.grupojcr.dto.MovimentoDTO;
import br.com.grupojcr.dto.OrcamentoDTO;

@Path("/relatorioAprovacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioAprovacaoWS {
	
	protected static Logger LOG = Logger.getLogger(RelatorioAprovacaoWS.class);
	
	@EJB
	private RMDAO daoRM;
	
	@EJB
	private RMBusiness rmBusiness;
	
	
	@POST
	@Path("/coligadas")
	public Response listarColigadas(String data) throws ParseException {
		LOG.info("[listarColigadas] Método iniciado");
		try {
			LOG.info("[listarColigadas] Listando coligadas...");
			List<ColigadaDTO> listaColigada = daoRM.listarColigadas();
			return Response.status(200).entity(listaColigada).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@POST
	@Path("/centroCusto")
	public Response listarCentroCusto(String data) throws ParseException {
		LOG.info("[listarCentroCusto] Método iniciado");
		try {
			LOG.info("[listarCentroCusto] Listando centro de custos...");
			List<CentroCustoDTO> listaCentroCusto = daoRM.listarCentroCusto();
			return Response.status(200).entity(listaCentroCusto).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@POST
	@Path("/orcamento")
	public Response listarOrcamento(String data) throws ParseException {
		LOG.info("[listarOrcamento] Método iniciado");
		try {
			LOG.info("[listarOrcamento] Listando orçamentos...");
			List<OrcamentoDTO> listaOrcamento = daoRM.listarOrcamento();
			return Response.status(200).entity(listaOrcamento).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@POST
	@Path("/gerarRelatorioAprovacao")
	public Response gerarRelatorioAprovacao(String data) {
		LOG.info("[gerarRelatorioAprovacao] Método iniciado");
		try {
			JsonSerialize jsonRelatorio = (JsonSerialize) new Gson().fromJson(data, JsonSerialize.class);
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Date dtInicio = formato.parse(jsonRelatorio.getPeriodoInicial());
			Date dtFinal = formato.parse(jsonRelatorio.getPeriodoFinal());
			LOG.info("[gerarRelatorioAprovacao] Listando movimentos...");
			List<MovimentoDTO> listaMovimento = rmBusiness.listarMovimentos(jsonRelatorio.getUsuarioLogado(), dtInicio, dtFinal, jsonRelatorio.getIdColigada(), jsonRelatorio.getCentroCusto(), jsonRelatorio.getOrcamento());
			return Response.status(200).entity(listaMovimento).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(500).entity(e).build();
		}
	}

}
