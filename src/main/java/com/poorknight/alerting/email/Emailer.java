package com.poorknight.alerting.email;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.poorknight.server.settings.Environment;

class SMTPAuthenticator extends Authenticator {
	private static final String SMTP_AUTH_USER = Environment.getEnvironmentVariable("SMTP_USER");
	private static final String SMTP_AUTH_PWD = Environment.getEnvironmentVariable("SMTP_PASSWORD");

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		final String username = SMTP_AUTH_USER;
		final String password = SMTP_AUTH_PWD;
		return new PasswordAuthentication(username, password);
	}
}

public class Emailer {

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "587";

	private final EmailTo to;
	private final EmailFrom from;

	public static Emailer buildEmailer(final EmailTo to, final EmailFrom from) {
		return new Emailer(to, from);
	}

	private Emailer(final EmailTo to, final EmailFrom from) {
		this.to = to;
		this.from = from;
	}

	public void sendEmail(final EmailSubject subject, final EmailBody body) {
		try {
			buildAndSendMessage(subject, body);

		} catch (final AuthenticationFailedException e) {
			throw new RuntimeException("************************ Did you set the environment variables? ************************", e);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void buildAndSendMessage(final EmailSubject subject, final EmailBody body) throws MessagingException, AddressException {
		final Session session = buildEmailSession();
		final Message message = buildMessage(subject, body, session);
		Transport.send(message);
	}

	private Session buildEmailSession() {
		final Properties props = buildProperties();
		final Authenticator auth = new SMTPAuthenticator();
		final Session session = Session.getDefaultInstance(props, auth);
		return session;
	}

	private Properties buildProperties() {
		final Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		return props;
	}

	private Message buildMessage(final EmailSubject subject, final EmailBody body, final Session session) throws MessagingException, AddressException {
		final Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from.getFrom()));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.getTo()));
		message.setSubject(subject.getSubject());
		message.setText(body.getBody());
		return message;
	}
}





