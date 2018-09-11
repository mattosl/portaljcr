package br.com.grupojcr.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;

import br.com.grupojcr.dao.ColigadaDAO;
import br.com.grupojcr.dao.RMDAO;
import br.com.grupojcr.dto.HoleriteDTO;
import br.com.grupojcr.util.TreatString;
import br.com.grupojcr.util.exception.ApplicationException;

@Stateless
public class HoleriteBusiness {
	
	private static Logger LOG = Logger.getLogger(HoleriteBusiness.class);
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
	
	@EJB
	private RMDAO daoRM;
	
	@EJB
	private ColigadaDAO daoColigada;
	
	@EJB
	private RMBusiness rmBusiness;

	public List<HoleriteDTO> listarHolerite(String chapa) throws ApplicationException {
		try {
			String chapaCompleta = TreatString.filterOnlyNumber(chapa);
			try {
				Long.parseLong(chapaCompleta);
			} catch(NumberFormatException e) {
				throw new ApplicationException("holerite.chapa.nao.identificada", FacesMessage.SEVERITY_WARN);
			}
			
			Long idColigada = null;
			String chapaOriginal = null;
			if(chapaCompleta.length() == 7) {
				String coligada = chapaCompleta.substring(0, 1);
				if(TreatString.isNotBlank(coligada)) {
					idColigada = Long.parseLong(coligada);
				}
				chapaOriginal = chapaCompleta.substring(1, chapaCompleta.length());
				
			} else if(chapaCompleta.length() == 8) {
				String coligada = chapaCompleta.substring(0, 2);
				if(TreatString.isNotBlank(coligada)) {
					idColigada = Long.parseLong(coligada);
				}
				chapaOriginal = chapaCompleta.substring(2, chapaCompleta.length());
			} else {
				throw new ApplicationException("holerite.chapa.nao.identificada", FacesMessage.SEVERITY_WARN);
			}
			
			List<HoleriteDTO> listaHolerite = new ArrayList<HoleriteDTO>();
			List<HoleriteDTO> holeritesChapa = daoRM.listarHoleriteFuncionario(idColigada, chapaOriginal);
			Calendar dataAtual = Calendar.getInstance();
			for(HoleriteDTO h : holeritesChapa) {
				if(h.getMes().getId().equals(dataAtual.get(Calendar.MONTH) + 1) && h.getAno().equals(dataAtual.get(Calendar.YEAR)) ) {
					continue;
				} else {
					h.setColigada(daoColigada.obter(h.getColigada().getId()));
					listaHolerite.add(h);
				}
			}
//			List<HoleriteDTO> listaHolerite = new ArrayList<HoleriteDTO>();
//			Integer codPessoa = daoRM.obterCodigoPessoa(idColigada, chapaOriginal);
//			if(Util.isNotNull(codPessoa)) {
//				List<ChapaDTO> listaChapas = daoRM.listarChapaFuncionario(codPessoa);
//				for(ChapaDTO dto : listaChapas) {
//					List<HoleriteDTO> holeritesChapa = daoRM.listarHoleriteFuncionario(dto.getIdColigada(), dto.getChapa());
//					Calendar dataAtual = Calendar.getInstance();
//					for(HoleriteDTO h : holeritesChapa) {
//						if(h.getMes().getId().equals(dataAtual.get(Calendar.MONTH) + 1) && h.getAno().equals(dataAtual.get(Calendar.YEAR)) ) {
//							continue;
//						} else {
//							h.setColigada(daoColigada.obter(h.getColigada().getId()));
//							listaHolerite.add(h);
//						}
//					}
//					
//				}
//			}
			
			return listaHolerite;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "listarHolerite" }, e);
		}
	}
}
