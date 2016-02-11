package com.poorknight.alerting.email;

public class EmailerFactory {

	public static Emailer buildEmailer(final EmailTo to, final EmailFrom from) {
		return new GmailEmailer(to, from);
	}
}
