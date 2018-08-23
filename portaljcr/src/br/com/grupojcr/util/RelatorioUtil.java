package br.com.grupojcr.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RelatorioUtil {
	
	public static <T> byte[] gerarRelatorio(String nomeRelatorio, List<T> lista, Map<String, Object> parametros) {
		if(parametros == null) {
			parametros = new HashMap<String, Object>();
		}
		
		InputStream logoJCR = null;
		InputStream jasper = null;
		
		try {
			JRBeanCollectionDataSource beanDS = new JRBeanCollectionDataSource(lista);
			
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			File logo = new File(servletContext.getRealPath("resources/images/jcr-logo.png"));
			logoJCR = new ByteArrayInputStream(TreatFile.fileToByte(logo));
			
			parametros.put("SUBREPORT_DIR", "/relatorios/jasper/");
			parametros.put("logoJCR", logoJCR);
			
			jasper = RelatorioUtil.class.getResourceAsStream("/relatorios/jasper/" + nomeRelatorio + ".jasper");
			
			JasperPrint print = JasperFillManager.fillReport(jasper, parametros, beanDS);
			
			return JasperExportManager.exportReportToPdf(print);
		} catch (Exception e) {
			throw new RuntimeException("Falha ao gerar relatório", e);
		} finally {
			fechaInputStream(jasper, "Falha ao fechar Jasper");
		}
	}
	
	public static void fechaInputStream(InputStream i, String mensagemErro) {
		if(mensagemErro == null) {
			mensagemErro = "Falha ao fechar InputStream";
		}
		if(i != null) {
			try {
				i.close();
			} catch (IOException e) {
			}
		}
	}

}
