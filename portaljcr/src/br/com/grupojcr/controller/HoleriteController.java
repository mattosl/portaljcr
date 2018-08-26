package br.com.grupojcr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.StreamedContent;

import com.ibm.icu.util.Calendar;

import br.com.grupojcr.business.HoleriteBusiness;
import br.com.grupojcr.business.RMBusiness;
import br.com.grupojcr.dto.HoleriteDTO;
import br.com.grupojcr.dto.RelatorioHoleriteDTO;
import br.com.grupojcr.entity.xml.FopEnvelopeXML;
import br.com.grupojcr.rm.FuncionarioRM;
import br.com.grupojcr.rm.HoleriteItensRM;
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
	private HoleriteDTO holerite;
	
	@EJB
	private RMBusiness rmBusiness;
	
	@EJB
	private HoleriteBusiness holeriteBusiness;

	public String autenticarUsuario() throws ApplicationException {
		try {
			if(Util.isBlank(getChapa())) {
				throw new ApplicationException("message.empty", new String[] {"Chapa ou Senha inválidas."}, FacesMessage.SEVERITY_WARN);
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
	
	public StreamedContent download() throws ApplicationException {
		try {
			FuncionarioRM dadosFuncionario = rmBusiness.obterDadosFuncionario(getChapa());
			FopEnvelopeXML dadosHolerite = rmBusiness.obterDadosHolerite(dadosFuncionario.getCodColigada(), dadosFuncionario.getChapa(), getHolerite().getMes().getId(), getHolerite().getAno(), getHolerite().getPeriodo().getId());
			List<HoleriteItensRM> dadosItensHolerite = rmBusiness.listarItensHolerite(dadosFuncionario.getCodColigada(), dadosFuncionario.getChapa(), getHolerite().getMes().getId(), getHolerite().getAno(), getHolerite().getPeriodo().getId());
			
			RelatorioHoleriteDTO dto = new RelatorioHoleriteDTO();
			dto.setRazaoSocial(dadosFuncionario.getEmpresa().toUpperCase());
			dto.setCnpj(dadosFuncionario.getCnpj());
			dto.setMatricula(dadosFuncionario.getChapa());
			dto.setNomeFuncionario(dadosFuncionario.getNomeFuncionario().toUpperCase());
			dto.setFuncao(dadosFuncionario.getFuncao().toUpperCase());
			dto.setDtAdmissao(TreatDate.format("dd/MM/yyyy", dadosFuncionario.getDtAdmissao()));
			dto.setEndereco(dadosFuncionario.getRua() + ", " + dadosFuncionario.getNumero());
			dto.setBairro(dadosFuncionario.getBairro().toUpperCase());
			dto.setCidade(dadosFuncionario.getCidade().toUpperCase());
			dto.setCep(dadosFuncionario.getCep());
			dto.setUf(dadosFuncionario.getEstado());
			dto.setPispasep(dadosFuncionario.getPispasep());
			dto.setCpf(Util.formatarCPF(dadosFuncionario.getCpf()));
			dto.setIdentidade(dadosFuncionario.getIdentidade());
			dto.setCompetencia(getHolerite().getMes().getDescricao().toUpperCase() + "/" + getHolerite().getAno());
			dto.setDepIRRF(dadosHolerite.getPfperff().getNroDepIrrf().toString());
			dto.setDepSalarioFamilia(dadosHolerite.getPfperff().getNroDepSal().toString());
			dto.setSalarioCalculo(TreatNumber.formatMoney(dadosHolerite.getPfperff().getSalarioCalculo()));
			dto.setDtPagamento(TreatDate.format("dd/MM/yyyy", Calendar.getInstance().getTime()));
			dto.setBanco(dadosFuncionario.getNomeBanco());
			dto.setAgencia(dadosFuncionario.getAgencia() + " - " + dadosFuncionario.getNumeroAgencia());
			dto.setConta(dadosFuncionario.getNumeroConta());
			dto.setListaItens(new ArrayList<RelatorioHoleriteDTO>());
	
			if(CollectionUtils.isNotEmpty(dadosItensHolerite)) {
				dto.setDtPagamento(TreatDate.format("dd/MM/yyyy", dadosItensHolerite.get(0).getDtPagamento()));
				for(HoleriteItensRM item : dadosItensHolerite) {
					RelatorioHoleriteDTO dtoItem = new RelatorioHoleriteDTO();
					dtoItem.setCodigo(item.getCodigo());
					dtoItem.setDescricao(item.getDescricao());
					dtoItem.setReferencia(TreatNumber.formatMoney(item.getReferencia()));
					if(TreatNumber.isNotNullOrZero(item.getProvento())) {
						dtoItem.setProvento(TreatNumber.formatMoney(item.getProvento()));
					}
					if(TreatNumber.isNotNullOrZero(item.getDesconto())) {
						dtoItem.setDesconto(TreatNumber.formatMoney(item.getDesconto()));
					}
					
					dto.getListaItens().add(dtoItem);
				}
			}
			
			if(getHolerite().getPeriodo().getId().equals(2)) {
				dto.setBaseFGTS(TreatNumber.formatMoney(dadosHolerite.getPfperff().getBaseFgts13()));
				dto.setBaseIRRF(TreatNumber.formatMoney(dadosHolerite.getPfperff().getIncRais()));
				dto.setSaldoContribuicaoINSS(TreatNumber.formatMoney(dadosHolerite.getPfperff().getBaseInss13()));
				dto.setFgtsMes(TreatNumber.formatMoney(dadosHolerite.getPfperff().getFgts13()));
			} else {
				dto.setBaseFGTS(TreatNumber.formatMoney(dadosHolerite.getPfperff().getBaseFgts()));
				dto.setBaseIRRF(TreatNumber.formatMoney(dadosHolerite.getPfperff().getIncRais()));
				dto.setSaldoContribuicaoINSS(TreatNumber.formatMoney(dadosHolerite.getPfperff().getBaseInss()));
				dto.setFgtsMes(TreatNumber.formatMoney(dadosHolerite.getPfperff().getFgts()));
			}
			dto.setTotalProventos(TreatNumber.formatMoney(dadosHolerite.getPfperff().getTotalProventos()));
			dto.setTotalDescontos(TreatNumber.formatMoney(dadosHolerite.getPfperff().getTotalDescontos()));
			
			dto.setLiquidoReceber(TreatNumber.formatMoney(dadosHolerite.getPfperff().getLiquido()));
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			byte [] relatorio = RelatorioUtil.gerarRelatorio("Holerite", Arrays.asList(dto), map);
//			return new DefaultStreamedContent(new ByteArrayInputStream(relatorio), null, "holerite.pdf");
			
			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            //Código abaixo gerar o relatório e disponibiliza diretamente na página 
            res.setHeader("Content-disposition", "inline;filename=relatorio.pdf");
            //Código abaixo gerar o relatório e disponibiliza para o cliente baixar ou salvar 
            //res.setHeader("Content-disposition", "attachment;filename=arquivo.pdf");
            res.getOutputStream().write(relatorio);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();
            return null;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "download" }, e);
		}
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

	public HoleriteDTO getHolerite() {
		return holerite;
	}

	public void setHolerite(HoleriteDTO holerite) {
		this.holerite = holerite;
	}
	

}