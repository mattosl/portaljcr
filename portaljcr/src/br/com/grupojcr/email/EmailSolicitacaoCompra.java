package br.com.grupojcr.email;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.naming.InitialContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import br.com.grupojcr.entity.Cotacao;
import br.com.grupojcr.entity.OrdemCompra;
import br.com.grupojcr.entity.SolicitacaoCompra;
import br.com.grupojcr.enumerator.Modalidade;
import br.com.grupojcr.util.BrasilUtils;
import br.com.grupojcr.util.TreatDate;

@Stateless
public class EmailSolicitacaoCompra {
	
	protected static Logger LOG = Logger.getLogger(EmailSolicitacaoCompra.class);
	
	@Resource(name = "java:jboss/mail/MailChamadoService")
	private Session session;
	
	public EmailSolicitacaoCompra() {}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void novaSolicitacaoCompra(String assunto, List<String> destinatariosPara, SolicitacaoCompra solicitacao) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailChamadoService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("helpdesk@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email/solicitacaoCompra/email.novaSolicitacaoCompra.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${nomeUsuario}", solicitacao.getUsuarioSolicitante().getNome());
			bodyEmail = bodyEmail.replace("${numeroSolicitacao}", solicitacao.getId().toString());
			bodyEmail = bodyEmail.replace("${dtSolicitacao}", TreatDate.format("dd/MM/yyyy", solicitacao.getDtSolicitacao()));
			bodyEmail = bodyEmail.replace("${prioridade}", solicitacao.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${modalidade}", solicitacao.getModalidade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${centroCusto}", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto());
			bodyEmail = bodyEmail.replace("${motivoCompra}", solicitacao.getMotivoCompra());
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void solicitacaoAprovada(String assunto, List<String> destinatariosPara, SolicitacaoCompra solicitacao) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailChamadoService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("helpdesk@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email/solicitacaoCompra/email.solicitacaoCompraAprovada.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${numeroSolicitacao}", solicitacao.getId().toString());
			bodyEmail = bodyEmail.replace("${dtSolicitacao}", TreatDate.format("dd/MM/yyyy", solicitacao.getDtSolicitacao()));
			bodyEmail = bodyEmail.replace("${prioridade}", solicitacao.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${modalidade}", solicitacao.getModalidade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${centroCusto}", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto());
			bodyEmail = bodyEmail.replace("${motivoCompra}", solicitacao.getMotivoCompra());
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void solicitacaoRecusada(String assunto, List<String> destinatariosPara, SolicitacaoCompra solicitacao) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailChamadoService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("helpdesk@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email/solicitacaoCompra/email.solicitacaoCompraRecusada.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${numeroSolicitacao}", solicitacao.getId().toString());
			bodyEmail = bodyEmail.replace("${motivoRecusa}", solicitacao.getMotivoCancelamento());
			bodyEmail = bodyEmail.replace("${dtSolicitacao}", TreatDate.format("dd/MM/yyyy", solicitacao.getDtSolicitacao()));
			bodyEmail = bodyEmail.replace("${prioridade}", solicitacao.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${modalidade}", solicitacao.getModalidade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${centroCusto}", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto());
			bodyEmail = bodyEmail.replace("${motivoCompra}", solicitacao.getMotivoCompra());
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void cotacaoConcluida(String assunto, List<String> destinatariosPara, SolicitacaoCompra solicitacao) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailChamadoService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("helpdesk@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email/solicitacaoCompra/email.cotacaoFinalizada.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${nomeUsuario}", solicitacao.getUsuarioCotacao().getNome());
			bodyEmail = bodyEmail.replace("${numeroSolicitacao}", solicitacao.getId().toString());
			bodyEmail = bodyEmail.replace("${dtSolicitacao}", TreatDate.format("dd/MM/yyyy", solicitacao.getDtSolicitacao()));
			bodyEmail = bodyEmail.replace("${prioridade}", solicitacao.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${modalidade}", solicitacao.getModalidade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${centroCusto}", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto());
			bodyEmail = bodyEmail.replace("${motivoCompra}", solicitacao.getMotivoCompra());
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void ordemCompraDisponivel(String assunto, List<String> destinatariosPara, SolicitacaoCompra solicitacao, Cotacao cotacao) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailChamadoService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("helpdesk@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email/solicitacaoCompra/email.ordemCompraDisponivel.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${nomeUsuario}", solicitacao.getUsuarioSolicitante().getNome());
			bodyEmail = bodyEmail.replace("${fornecedor}", cotacao.getFornecedor().toUpperCase());
			bodyEmail = bodyEmail.replace("${valorTotal}", cotacao.getValorTotalFormatado());
			bodyEmail = bodyEmail.replace("${numeroSolicitacao}", solicitacao.getId().toString());
			bodyEmail = bodyEmail.replace("${dtSolicitacao}", TreatDate.format("dd/MM/yyyy", solicitacao.getDtSolicitacao()));
			bodyEmail = bodyEmail.replace("${prioridade}", solicitacao.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${modalidade}", solicitacao.getModalidade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${centroCusto}", solicitacao.getCodigoCentroCusto() + " - " + solicitacao.getCentroCusto());
			bodyEmail = bodyEmail.replace("${motivoCompra}", solicitacao.getMotivoCompra());
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void ordemCompraGerada(String assunto, List<String> destinatariosPara, OrdemCompra oc) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailChamadoService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("helpdesk@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email/solicitacaoCompra/email.ordemCompraGerada.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${identificadorRM}", oc.getIdentificadorRM());
			bodyEmail = bodyEmail.replace("${tipoMovimento}", oc.getSolicitacaoCompra().getModalidade().equals(Modalidade.MATERIAL) ? "1.1.04" : "1.1.17");
			bodyEmail = bodyEmail.replace("${fornecedor}", oc.getCotacao().getFornecedor().toUpperCase());
			bodyEmail = bodyEmail.replace("${valorTotal}", oc.getCotacao().getValorTotalFormatado());
			bodyEmail = bodyEmail.replace("${nomeUsuario}", oc.getUsuario().getNome());
			bodyEmail = bodyEmail.replace("${numeroSolicitacao}", oc.getSolicitacaoCompra().getId().toString());
			bodyEmail = bodyEmail.replace("${dtSolicitacao}", TreatDate.format("dd/MM/yyyy", oc.getSolicitacaoCompra().getDtSolicitacao()));
			bodyEmail = bodyEmail.replace("${prioridade}", oc.getSolicitacaoCompra().getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${modalidade}", oc.getSolicitacaoCompra().getModalidade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${centroCusto}", oc.getSolicitacaoCompra().getCodigoCentroCusto() + " - " + oc.getSolicitacaoCompra().getCentroCusto());
			bodyEmail = bodyEmail.replace("${motivoCompra}", oc.getSolicitacaoCompra().getMotivoCompra());
			bodyEmail = bodyEmail.replace("${solicitante}", oc.getSolicitacaoCompra().getUsuarioSolicitante().getNome());
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
