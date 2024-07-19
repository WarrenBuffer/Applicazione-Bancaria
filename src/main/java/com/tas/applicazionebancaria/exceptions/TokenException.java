package com.tas.applicazionebancaria.exceptions;

public class TokenException extends RuntimeException {

	private static final long serialVersionUID = -2519250924810839005L;
	
	private final String message;
	
	public TokenException(String message) {
		this.message=message;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
