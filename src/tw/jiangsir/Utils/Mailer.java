package tw.jiangsir.Utils;

import java.security.Security;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class Mailer implements Runnable {
	private String mailto = "";
	private String subject = "";
	private String content = "";

	public Mailer() {
	}

	public Mailer(String mailto, String subject, String content) {
		this.mailto = mailto;
		this.subject = subject;
		this.content = content;
	}

	public void run() {
		try {
			this.GmailSender();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void SMTPSender() {
		boolean sessionDebug = false;
		Properties properties = System.getProperties();
		properties
				.put("mail.host", ENV.context.getInitParameter("SYSTEM_MAIL"));
		properties.put("mail.transport.protocol", "smtp");
		Session mailsession = Session.getDefaultInstance(properties, null);
		mailsession.setDebug(sessionDebug);
		Message message = new MimeMessage(mailsession);
		InternetAddress[] address = null;
		try {
			message.setFrom(new InternetAddress(ENV.context
					.getInitParameter("SYSTEM_MAIL")));
			address = InternetAddress.parse(this.mailto, false);
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject("[ZeroBB 系統通知信] " + this.subject);
			message.setSentDate(new Date());
			message.setText(this.content);
			Transport.send(message);
		} catch (AddressException e) {
			System.out.println("[Mailer ERROR]: " + e.toString());
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.println("[Mailer ERROR]: " + e.toString());
			e.printStackTrace();
		}
		System.out.println("[Mailer]郵件順利發送到 " + this.mailto);
	}

	public void GmailSender() throws AddressException, MessagingException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");

		final String serveremail = "jiangsir@tea.nknush.kh.edu.tw";
		final String password = "j04xu3";
		Session session = Session.getDefaultInstance(props,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(serveremail, password);
					}
				});

		// -- Create a new message --
		Message message = new MimeMessage(session);
		InternetAddress[] address = null;
		// -- Set the FROM and TO fields --
		message.setFrom(new InternetAddress(serveremail));
		address = InternetAddress.parse(this.mailto, false);
		message.setRecipients(Message.RecipientType.TO, address);
		message.setSubject("[ZeroBB系統通知信] " + this.subject);
		message.setSentDate(new Date());
		message.setText(this.content);
		Transport.send(message);

		System.out.println("Message sent.(" + message.getSubject() + ")");

	}

	/**
	 * 檢查 gmail(app) 密碼是否正確
	 * 
	 * @return
	 */
	public boolean GmailPOP3Checker(String email, String passwd) {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", "995");
		props.setProperty("mail.pop3.socketFactory.port", "995");

		Session session = Session.getDefaultInstance(props, null);

		// 請將紅色部分對應替換成你的郵箱帳號和密碼
		URLName urln = new URLName("pop3", "pop.gmail.com", 995, null, email,
				passwd);
		try {
			Store store = session.getStore(urln);
			store.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
