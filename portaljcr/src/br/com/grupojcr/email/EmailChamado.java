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

import br.com.grupojcr.entity.Chamado;
import br.com.grupojcr.entity.Usuario;
import br.com.grupojcr.util.BrasilUtils;
import br.com.grupojcr.util.Dominios;
import br.com.grupojcr.util.TreatDate;

@Stateless
public class EmailChamado {
	
	protected static Logger LOG = Logger.getLogger(EmailChamado.class);
	
	@Resource(name = "java:jboss/mail/MailService")
	private Session session;
	
	public EmailChamado() {}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void novoChamado(String assunto, List<String> destinatariosPara, Chamado chamado) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("nfse@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email.novoChamado.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${numeroChamado}", chamado.getId().toString());
			bodyEmail = bodyEmail.replace("${nomeUsuario}", chamado.getUsuarioSolicitante().getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${titulo}", chamado.getTitulo().toUpperCase());
			bodyEmail = bodyEmail.replace("${solicitante}", chamado.getUsuarioSolicitante().getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${dtAbertura}", TreatDate.format("dd/MM/yyyy HH:mm", chamado.getDtAbertura()));
			bodyEmail = bodyEmail.replace("${prioridade}", chamado.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${categoria}", chamado.getCategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${subcategoria}", chamado.getSubcategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${localizacao}", chamado.getLocalizacao().toUpperCase());
			bodyEmail = bodyEmail.replace("${descricao}", chamado.getDescricao());
			String url = Dominios.PATH_URL + "/pages/suporte/editar_chamado.xhtml?idChamado=" + chamado.getId().toString();
			bodyEmail = bodyEmail.replace("${urlChamado}", url);
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void atribuido(String assunto, List<String> destinatariosPara, Chamado chamado) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("nfse@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email.chamadoAtribuido.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${numeroChamado}", chamado.getId().toString());
			bodyEmail = bodyEmail.replace("${nomeResponsavel}", chamado.getUsuarioResponsavel().getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${titulo}", chamado.getTitulo().toUpperCase());
			bodyEmail = bodyEmail.replace("${solicitante}", chamado.getUsuarioSolicitante().getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${dtAbertura}", TreatDate.format("dd/MM/yyyy HH:mm", chamado.getDtAbertura()));
			bodyEmail = bodyEmail.replace("${prioridade}", chamado.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${categoria}", chamado.getCategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${subcategoria}", chamado.getSubcategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${localizacao}", chamado.getLocalizacao().toUpperCase());
			bodyEmail = bodyEmail.replace("${descricao}", chamado.getDescricao());
			String url = Dominios.PATH_URL + "/pages/suporte/editar_chamado.xhtml?idChamado=" + chamado.getId().toString();
			bodyEmail = bodyEmail.replace("${urlChamado}", url);
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void resolvido(String assunto, List<String> destinatariosPara, Chamado chamado) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("nfse@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email.chamadoResolvido.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${numeroChamado}", chamado.getId().toString());
			bodyEmail = bodyEmail.replace("${nomeResponsavel}", chamado.getUsuarioResponsavel().getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${solucao}", chamado.getSolucao().toUpperCase());
			bodyEmail = bodyEmail.replace("${titulo}", chamado.getTitulo().toUpperCase());
			bodyEmail = bodyEmail.replace("${solicitante}", chamado.getUsuarioSolicitante().getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${dtAbertura}", TreatDate.format("dd/MM/yyyy HH:mm", chamado.getDtAbertura()));
			bodyEmail = bodyEmail.replace("${prioridade}", chamado.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${categoria}", chamado.getCategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${subcategoria}", chamado.getSubcategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${localizacao}", chamado.getLocalizacao().toUpperCase());
			bodyEmail = bodyEmail.replace("${descricao}", chamado.getDescricao());
			String url = Dominios.PATH_URL + "/pages/suporte/editar_chamado.xhtml?idChamado=" + chamado.getId().toString();
			bodyEmail = bodyEmail.replace("${urlChamado}", url);
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void novaMensagem(String assunto, List<String> destinatariosPara, Chamado chamado, Usuario usuarioLogado, String mensagem) throws Exception {
		try {
			InitialContext ic = new InitialContext();
			session = ((Session) ic.lookup("java:jboss/mail/MailService"));

			InternetAddress[] destinatario = new InternetAddress[destinatariosPara.size()];

			InternetAddress remetente = null;
			remetente = new InternetAddress("nfse@grupojcr.com.br");

			for (int i = 0; i < destinatariosPara.size(); i++) {
				destinatario[i] = new InternetAddress(destinatariosPara.get(i));
			}

			Message message = new MimeMessage(session);
			message.setFrom(remetente);
			message.setRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(MimeUtility.encodeText(assunto, "UTF-8", null));

			BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/email.novoAcompanhamento.html")));
			String bodyEmail = IOUtils.toString(fis);
			bodyEmail = bodyEmail.replace("${numeroChamado}", chamado.getId().toString());
			bodyEmail = bodyEmail.replace("${nomeUsuario}", usuarioLogado.getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${mensagem}", mensagem.toUpperCase());
			bodyEmail = bodyEmail.replace("${titulo}", chamado.getTitulo().toUpperCase());
			bodyEmail = bodyEmail.replace("${solicitante}", chamado.getUsuarioSolicitante().getNome().toUpperCase());
			bodyEmail = bodyEmail.replace("${dtAbertura}", TreatDate.format("dd/MM/yyyy HH:mm", chamado.getDtAbertura()));
			bodyEmail = bodyEmail.replace("${prioridade}", chamado.getPrioridade().getDescricao().toUpperCase());
			bodyEmail = bodyEmail.replace("${categoria}", chamado.getCategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${subcategoria}", chamado.getSubcategoria().toUpperCase());
			bodyEmail = bodyEmail.replace("${localizacao}", chamado.getLocalizacao().toUpperCase());
			bodyEmail = bodyEmail.replace("${descricao}", chamado.getDescricao());
			String url = Dominios.PATH_URL + "/pages/suporte/editar_chamado.xhtml?idChamado=" + chamado.getId().toString();
			bodyEmail = bodyEmail.replace("${urlChamado}", url);
			
			message.setContent(BrasilUtils.converterCaracteresEspeciaisHTML(bodyEmail), "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error("ERRO NO ENVIO DE E-MAIL: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
