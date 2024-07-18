package com.tas.applicazionebancaria.utils;

import lombok.Data;

@Data
public class ServerResponse {
	private int code;
	private Object message;
	
	public ServerResponse() {}
	
	public ServerResponse(int code, Object message) {
		this.code = code;
		this.message = message;
	}
}
