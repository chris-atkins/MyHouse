package com.poorknight.alerting.textmessage;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class TextMessageAlerter {

	private static final String SMTP_AUTH_USER = System.getenv("SMTP_USER");
	private static final String SMTP_AUTH_PWD = System.getenv("SMTP_PASSWORD");

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "587";

	private static final String FROM = "chrisatkins55@gmail.com";
	private static final String TO = "2483909123@vtext.com";

	public static TextMessageAlerter instance() {
		return new TextMessageAlerter();
	}

	private TextMessageAlerter() {
		super();
	}

	public void sendTextMessage(final String message) {
		notifyWithMessage(buildSubject(), message);
	}

	private String buildSubject() {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
	}

	private void notifyWithMessage(final String messageSubject, final String messageBody) {
		try {
			buildAndSendMessage(null, messageBody);

		} catch (final javax.mail.AuthenticationFailedException e) {
			throw new RuntimeException("************************ Did you set the environment variables? ************************", e);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void buildAndSendMessage(final String messageSubject, final String messageBody) throws MessagingException, AddressException {
		final Properties props = buildProperties();
		final Authenticator auth = new SMTPAuthenticator();
		final Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(false);

		final Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(FROM));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
		message.setSubject(buildSubject());
		message.setText(messageBody);

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
