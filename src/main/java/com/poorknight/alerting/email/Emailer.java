package com.poorknight.alerting.email;

public interface Emailer {

	void sendEmail(EmailSubject subject, EmailBody body);
}
