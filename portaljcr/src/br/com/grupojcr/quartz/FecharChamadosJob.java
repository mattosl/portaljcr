package br.com.grupojcr.quartz;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.grupojcr.business.ChamadoBusiness;

public class FecharChamadosJob implements Job {
	
	private static Logger log = Logger.getLogger(FecharChamadosJob.class);
	private final static String KEY_ERRO = "ERRO:";

	public static void main(String[] args) throws Exception {
		
		new FecharChamadosJob().execute(null);
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			log.info("Fechando chamados...");
			
			InitialContext initialContext = new InitialContext();
			ChamadoBusiness business = (ChamadoBusiness) initialContext.lookup("java:global/portaljcr/ChamadoBusiness");
			
			business.fecharChamados();
			
		} catch (Exception e) {
			log.error(KEY_ERRO, e);
			throw new JobExecutionException(e);
		}

		
	}

}
