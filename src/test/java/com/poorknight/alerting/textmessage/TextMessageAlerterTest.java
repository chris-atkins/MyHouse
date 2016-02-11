package com.poorknight.alerting.textmessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.exparity.hamcrest.date.DateMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.alerting.email.EmailBody;
import com.poorknight.alerting.email.EmailFrom;
import com.poorknight.alerting.email.EmailSubject;
import com.poorknight.alerting.email.EmailTo;
import com.poorknight.alerting.email.Emailer;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Emailer.class)
public class TextMessageAlerterTest {

	private final TextMessageAlerter textMessageAlerter = TextMessageAlerter.instance();

	@Mock
	private Emailer emailer;

	@Captor
	private ArgumentCaptor<EmailSubject> emailSubjectCaptor;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Emailer.class);
		when(Emailer.buildEmailer(new EmailTo("2483909123@vtext.com"), new EmailFrom("chrisatkins55@gmail.com"))).thenReturn(emailer);
	}

	@Test
	public void requestsToSendEmail_FromEmailer_BuiltWithCorrectTOAndFROM() throws Exception {
		when(Emailer.buildEmailer(new EmailTo("2483909123@vtext.com"), new EmailFrom("chrisatkins55@gmail.com"))).thenReturn(emailer);
		textMessageAlerter.sendTextMessage("a message");
		verify(emailer).sendEmail(Mockito.any(EmailSubject.class), Mockito.any(EmailBody.class));
	}

	@Test
	public void sendsSubject_ThatIsTheCurrentDateInTheRightFormat() throws Exception {
		textMessageAlerter.sendTextMessage("a message");
		verify(emailer).sendEmail(emailSubjectCaptor.capture(), Mockito.any(EmailBody.class));
		assertThat(getDateFromCaptor("yyyy.MM.dd HH:mm:ss"), DateMatchers.isToday());
	}

	private Date getDateFromCaptor(final String dateFormatString) throws ParseException {
		return new SimpleDateFormat(dateFormatString).parse(emailSubjectCaptor.getValue().getSubject());
	}

	@Test
	public void sendsBodyFromParametersWithoutAlteringIT() throws Exception {
		final String message = RandomStringUtils.random(500);
		textMessageAlerter.sendTextMessage(message);
		verify(emailer).sendEmail(Mockito.any(EmailSubject.class), Mockito.eq(new EmailBody(message)));
	}
}
