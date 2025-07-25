package com.poorknight.alerting.textmessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.exparity.hamcrest.date.DateMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.poorknight.alerting.email.EmailBody;
import com.poorknight.alerting.email.EmailFrom;
import com.poorknight.alerting.email.EmailSubject;
import com.poorknight.alerting.email.EmailTo;
import com.poorknight.alerting.email.Emailer;

@ExtendWith(MockitoExtension.class)
public class TextMessageAlerterTest {

	private final TextMessageAlerter textMessageAlerter = TextMessageAlerter.instance();

	@Mock
	private Emailer emailer;

	@Captor
	private ArgumentCaptor<EmailSubject> emailSubjectCaptor;

	@Test
	public void requestsToSendEmail_FromEmailer_BuiltWithCorrectTOAndFROM() {
		try (MockedStatic<Emailer> mockedEmailer = mockStatic(Emailer.class)) {
			mockedEmailer.when(() -> Emailer.buildEmailer(new EmailTo("2483909123@msg.fi.google.com"), new EmailFrom("chrisatkins55@gmail.com"))).thenReturn(emailer);
			textMessageAlerter.sendTextMessage("a message");
			verify(emailer).sendEmail(Mockito.any(EmailSubject.class), Mockito.any(EmailBody.class));
		}
	}

	@Test
	public void sendsSubject_ThatIsTheCurrentDateInTheRightFormat() throws Exception {
		try(MockedStatic<Emailer> mockedEmailer = mockStatic(Emailer.class)) {
			mockedEmailer.when(() -> Emailer.buildEmailer(new EmailTo("2483909123@msg.fi.google.com"), new EmailFrom("chrisatkins55@gmail.com"))).thenReturn(emailer);
			textMessageAlerter.sendTextMessage("a message");
			verify(emailer).sendEmail(emailSubjectCaptor.capture(), Mockito.any(EmailBody.class));
			assertThat(getDateFromCaptor(), DateMatchers.isToday());
		}
	}

	private Date getDateFromCaptor() throws ParseException {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(emailSubjectCaptor.getValue().getSubject());
	}

	@Test
	public void sendsBodyFromParametersWithoutAlteringIT() {
		try(MockedStatic<Emailer> mockedEmailer = mockStatic(Emailer.class)) {
			mockedEmailer.when(() -> Emailer.buildEmailer(new EmailTo("2483909123@msg.fi.google.com"), new EmailFrom("chrisatkins55@gmail.com"))).thenReturn(emailer);
			final String message = RandomStringUtils.random(500);
			textMessageAlerter.sendTextMessage(message);
			verify(emailer).sendEmail(Mockito.any(EmailSubject.class), Mockito.eq(new EmailBody(message)));
		}
	}
}
