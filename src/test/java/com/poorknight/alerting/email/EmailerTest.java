package com.poorknight.alerting.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.server.settings.Environment;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Transport.class, Environment.class })
public class EmailerTest {

	private final String smtpPassword = RandomStringUtils.random(10);
	private final String smtpUser = RandomStringUtils.random(10);
	private final EmailTo to = new EmailTo("hi@its.me");
	private final EmailFrom from = new EmailFrom("oh@hello.there");
	private final EmailSubject subject = new EmailSubject(RandomStringUtils.random(10));
	private final EmailBody body = new EmailBody(RandomStringUtils.random(50));

	private Emailer emailer;

	@Captor
	private ArgumentCaptor<Message> messageCaptor;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Transport.class);
		PowerMockito.mockStatic(Environment.class);
		when(Environment.getEnvironmentVariable("SMTP_USER")).thenReturn(smtpUser);
		when(Environment.getEnvironmentVariable("SMTP_PASSWORD")).thenReturn(smtpPassword);
		emailer = Emailer.buildEmailer(to, from);
	}

	@Test
	public void emailIsSetupCorrectly() throws Exception {
		emailer.sendEmail(subject, body);
		PowerMockito.verifyStatic(Transport.class);
		Transport.send(messageCaptor.capture());

		final Message message = messageCaptor.getValue();
		assertSessionIsCorrect(message.getSession());
		assertMessageContentsAreCorrect(message);
	}

	private void assertSessionIsCorrect(final Session session) {
		assertThat(session.getDebug(), is(false));
		assertThat(session.getProperty("mail.smtp.host"), equalTo("smtp.gmail.com"));
		assertThat(session.getProperty("mail.smtp.port"), equalTo("587"));
		assertThat(session.getProperty("mail.smtp.starttls.enable"), equalTo("true"));
		assertThat(session.getProperty("mail.smtp.auth"), equalTo("true"));
	}

	private void assertMessageContentsAreCorrect(final Message message) throws Exception {
		final Address[] actualFrom = message.getFrom();
		final Address[] actualRecipients = message.getAllRecipients();
		final String actualSubject = message.getSubject();
		final String actualContent = message.getContent().toString();

		assertThat(actualFrom.length, equalTo(1));
		assertThat(actualFrom[0].toString(), equalTo(from.getFrom()));
		assertThat(actualRecipients.length, equalTo(1));
		assertThat(actualRecipients[0].toString(), equalTo(to.getTo()));
		assertThat(actualSubject.toString(), equalTo(subject.getSubject()));
		assertThat(actualContent.toString(), equalTo(body.getBody()));
	}

	@Test
	public void throwsExceptionIfAuthenticationFails() throws Exception {
		try {
			PowerMockito.doThrow(new AuthenticationFailedException()).when(Transport.class);
			Transport.send(Mockito.any(Message.class));

			final Emailer failEmailer = Emailer.buildEmailer(to, from);
			failEmailer.sendEmail(subject, body);
			fail("expecting exception");

		} catch (final RuntimeException e) {
			assertThat(e.getMessage(), equalTo("************************ Did you set the environment variables? ************************"));
		}
	}
}
