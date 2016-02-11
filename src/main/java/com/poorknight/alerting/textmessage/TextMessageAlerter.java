package com.poorknight.alerting.textmessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.poorknight.alerting.email.EmailBody;
import com.poorknight.alerting.email.EmailFrom;
import com.poorknight.alerting.email.EmailSubject;
import com.poorknight.alerting.email.EmailTo;
import com.poorknight.alerting.email.Emailer;

public class TextMessageAlerter {

	private static final String TO = "2483909123@vtext.com";
	private static final String FROM = "chrisatkins55@gmail.com";

	public static TextMessageAlerter instance() {
		return new TextMessageAlerter();
	}

	private TextMessageAlerter() {
		super();
	}

	public void sendTextMessage(final String message) {
		final Emailer emailer = Emailer.buildEmailer(new EmailTo(TO), new EmailFrom(FROM));
		emailer.sendEmail(new EmailSubject(buildSubject()), new EmailBody(message));
	}

	private String buildSubject() {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
	}
}
