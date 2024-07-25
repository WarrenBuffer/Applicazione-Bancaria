package com.tas.applicazionebancaria.utils;

import java.time.temporal.ChronoUnit;

public interface Costanti {
	String TOKEN_SECRET = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
	long TOKEN_EXPIRATION = 24l; // 1 giorgno
	ChronoUnit UNIT = ChronoUnit.HOURS;
	int MAX_TENTATIVI = 5;
	String COOKIE_NAME = "bearer";
	int COOKIE_MAX_AGE = 60 * 60 * 24 * 7; // 7 giorni
}
