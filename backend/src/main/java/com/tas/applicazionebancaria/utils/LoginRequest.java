package com.tas.applicazionebancaria.utils;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Data
@Hidden
public class LoginRequest {
	private String email;
	private String password;
}
