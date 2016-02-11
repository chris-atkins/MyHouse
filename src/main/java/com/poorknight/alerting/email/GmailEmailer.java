package com.poorknight.alerting.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailEmailer implements Emailer {

	private static final String SMTP_AUTH_USER = System.getenv("SMTP_USER");
	private static final String SMTP_AUTH_PWD = System.getenv("SMTP_PASSWORD");

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "587";

	private final EmailTo to;
	private final EmailFrom from;

	public GmailEmailer(final EmailTo to, final EmailFrom from) {
		this.to = to;
		this.from = from;
	}

	@Override
	public void sendEmail(final EmailSubject subject, final EmailBody body) {
		notifyWithMessage(subject, body);
	}

	private void notifyWithMessage(final EmailSubject subject, final EmailBody body) {
		try {
			buildAndSendMessage(subject, body);

		} catch (final javax.mail.AuthenticationFailedException e) {
			throw new RuntimeException("************************ Did you set the environment variables? ************************", e);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void buildAndSendMessage(final EmailSubject subject, final EmailBody body) throws MessagingException, AddressException {
		final Properties props = buildProperties();
		final Authenticator auth = new SMTPAuthenticator();
		final Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(false);

		final Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from.getFrom()));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.getTo()));
		message.setSubject(subject.getSubject());
		message.setText(body.getBody());

		Transport.send(message);
	}

	private Properties buildProperties() {
		final Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		return props;
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			final String username = SMTP_AUTH_USER;
			final String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
}
