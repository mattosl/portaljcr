package br.com.grupojcr.controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ibm.icu.util.Calendar;

import br.com.grupojcr.business.HoleriteBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.HoleriteDTO;
import br.com.grupojcr.dto.RelatorioHoleriteDTO;
import br.com.grupojcr.util.RelatorioUtil;
import br.com.grupojcr.util.TreatDate;
import br.com.grupojcr.util.TreatNumber;
import br.com.grupojcr.util.Util;
import br.com.grupojcr.util.exception.ApplicationException;
import br.com.grupojcr.util.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class HoleriteController implements Serializable {
	
	private static final long serialVersionUID = -5499017085363361305L;
	protected static Logger LOG = Logger.getLogger(HoleriteController.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	private String chapa;
	private String chave;
	
	private List<HoleriteDTO> listaHolerite;
	
	private StreamedContent stream;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private HoleriteBusiness holeriteBusiness;

	public String autenticarUsuario() throws ApplicationException {
		try {
			if(Util.isBlank(getChapa())) {
				throw new ApplicationException("message.empty", new String[] {"Login ou Senha inválidas."}, FacesMessage.SEVERITY_WARN);
			}
			
			if(Util.isBlank(getChave())) {
				throw new ApplicationException("message.empty", new String[] {"Chapa ou Senha inválidas."}, FacesMessage.SEVERITY_WARN);
			}
			
			try {
				rmBusiness.autenticarUsuario(getChapa(), getChave());
			} catch (ApplicationException e) {
				throw new ApplicationException("message.empty", new String[] {"Chapa ou Senha inválidas."}, FacesMessage.SEVERITY_WARN);
			}
				
			setListaHolerite(holeriteBusiness.listarHolerite(getChapa(), getChave()));
			
			return "/pages/recursosHumanos/holerite/listar_holerite.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "autenticarUsuario" }, e);
		}
	}
	
	public StreamedContent download() {
		RelatorioHoleriteDTO dto = new RelatorioHoleriteDTO();
		dto.setRazaoSocial("JCR ADMINISTRAÇÃO E PARTICIPAÇÕES LTDA");
		dto.setCnpj(Util.formatarCNPJ("99999999000114"));
		dto.setMatricula("12345789");
		dto.setNomeFuncionario("LEONAN YGLECIAS MATTOS");
		dto.setFuncao("ANALISTA DE SISTEMAS");
		dto.setDtAdmissao(TreatDate.format("dd/MM/yyyy", Calendar.getInstance().getTime()));
		dto.setEndereco("RUA JOAO CARLOS RIBEIRO");
		dto.setBairro("ALTO DA XV");
		dto.setCidade("CURITIBA");
		dto.setCep("81050180");
		dto.setUf("PR");
		dto.setPispasep("123456789");
		dto.setCpf(Util.formatarCPF("99999999999"));
		dto.setIdentidade("126510446");
		dto.setCompetencia("AGOSTO/2018");
		dto.setDepSalarioFamilia("0");
		dto.setDepIRRF("0");
		dto.setSalarioCalculo(TreatNumber.formatMoney(20000));
		dto.setDtPagamento(TreatDate.format("dd/MM/yyyy", Calendar.getInstance().getTime()));
		dto.setBanco("BRADESCO S/A");
		dto.setAgencia("AGENCIA X");
		dto.setConta("124564-6");
		dto.setListaItens(new ArrayList<RelatorioHoleriteDTO>());
		
		RelatorioHoleriteDTO item1 = new RelatorioHoleriteDTO();
		item1.setCodigo("0002");
		item1.setDescricao("DIAS TRABALHADOS");
		item1.setReferencia(TreatNumber.formatMoney(30));
		item1.setProvento(TreatNumber.formatMoney(2000));
		item1.setDesconto(null);
		RelatorioHoleriteDTO item2 = new RelatorioHoleriteDTO();
		item2.setCodigo("0003");
		item2.setDescricao("I.N.S.S");
		item2.setReferencia(TreatNumber.formatMoney(9));
		item2.setProvento(null);
		item2.setDesconto(TreatNumber.formatMoney(2000));
		RelatorioHoleriteDTO item3 = new RelatorioHoleriteDTO();
		item3.setCodigo("0019");
		item3.setDescricao("ADIANTAMENTO (SALARIO)");
		item3.setReferencia(TreatNumber.formatMoney(0));
		item3.setProvento(null);
		item3.setDesconto(TreatNumber.formatMoney(2000));
		
		dto.getListaItens().add(item1);
		dto.getListaItens().add(item2);
		dto.getListaItens().add(item3);
		
		
		dto.setBaseFGTS(TreatNumber.formatMoney(1000));
		dto.setBaseIRRF(TreatNumber.formatMoney(1000));
		dto.setSaldoContribuicaoINSS(TreatNumber.formatMoney(1000));
		dto.setFgtsMes(TreatNumber.formatMoney(80));
		dto.setTotalProventos(TreatNumber.formatMoney(20000));
		dto.setTotalDescontos(TreatNumber.formatMoney(1555));
		dto.setLiquidoReceber(TreatNumber.formatMoney(19565));
		HashMap<String, Object> map = new HashMap<String, Object>();
		byte [] relatorio = RelatorioUtil.gerarRelatorio("Holerite", Arrays.asList(dto), map);
		return new DefaultStreamedContent(new ByteArrayInputStream(relatorio), null, "holerite.pdf");
	}


	public String getChapa() {
		return chapa;
	}


	public void setChapa(String chapa) {
		this.chapa = chapa;
	}


	public String getChave() {
		return chave;
	}


	public void setChave(String chave) {
		this.chave = chave;
	}


	public List<HoleriteDTO> getListaHolerite() {
		return listaHolerite;
	}


	public void setListaHolerite(List<HoleriteDTO> listaHolerite) {
		this.listaHolerite = listaHolerite;
	}


	public StreamedContent getStream() {
		return stream;
	}


	public void setStream(StreamedContent stream) {
		this.stream = stream;
	}
	

}
