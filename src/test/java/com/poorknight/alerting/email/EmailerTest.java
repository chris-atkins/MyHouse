package com.poorknight.alerting.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailerTest {

	private final EmailTo to = new EmailTo("hi@its.me");
	private final EmailFrom from = new EmailFrom("oh@hello.there");
	private final EmailSubject subject = new EmailSubject(RandomStringUtils.random(10));
	private final EmailBody body = new EmailBody(RandomStringUtils.random(50));

	private Emailer emailer;

	@Captor
	private ArgumentCaptor<Message> messageCaptor;

	@BeforeEach
	public void setup() {
		emailer = Emailer.buildEmailer(to, from);
	}

	@Test
	public void emailIsSetupCorrectly() throws Exception {
		try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {

			emailer.sendEmail(subject, body);
			mockedTransport.verify(() -> Transport.send(messageCaptor.capture()));

			final Message message = messageCaptor.getValue();
			assertSessionIsCorrect(message.getSession());
			assertMessageContentsAreCorrect(message);
		}
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
		assertThat(actualSubject, equalTo(subject.getSubject()));
		assertThat(actualContent, equalTo(body.getBody()));
	}

	@Test
	public void throwsExceptionIfAuthenticationFails() throws Exception {
		try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
			mockedTransport.when(() -> Transport.send(any(Message.class))).thenThrow(AuthenticationFailedException.class);

			emailer.sendEmail(subject, body);
			fail("expecting exception");

		} catch (final RuntimeException e) {
			assertThat(e.getMessage(), equalTo("************************ Did you set the environment variables? ************************"));
		}
	}
}
