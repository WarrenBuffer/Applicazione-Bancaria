package com.tas.applicazionebancaria.exceptions;

public class AdminTokenException extends Exception {
	private static final long serialVersionUID = -5412199177213866245L;

	private final String message;
	
	public AdminTokenException(String message) {
		this.message=message;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
