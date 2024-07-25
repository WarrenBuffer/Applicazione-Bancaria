package com.tas.applicazionebancaria.aspect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.aspect.LogAspect;
import com.tas.applicazionebancaria.exceptions.TokenException;
import com.tas.applicazionebancaria.utils.JWT;

import static org.junit.jupiter.api.Assertions.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest(classes=ApplicazioneBancariaApplication.class)
class ControlloLogUtenteTest {
	MockedStatic<JWT> mockedStaticJWT;

	static String token=JWT.generate("Mario", "Rossi", "mario.rossi@email.it");
    @Mock
    private JoinPoint jp;
    
    @Mock
    private Logger logger;

    @InjectMocks
    private LogAspect logAspect;
    
    @BeforeEach
	public void setUp() {
		mockedStaticJWT= mockStatic(JWT.class);
	}
    
    @Test
	void pathNonEsisteArgsVuoti() throws Throwable {
		when(jp.getArgs()).thenReturn(null);
	    logAspect.controlloLogUtente(jp);
	}
	@Test
	void requestInesistente() throws Throwable {
		Object[] objects=new Object[1];
		objects[0] =2;
		when(jp.getArgs()).thenReturn(objects);
	    logAspect.controlloLogUtente(jp);
	}
	@Test
	void cookiesInesistenti() throws Throwable {
		Object[] objects=new Object[1];
		HttpServletRequest request = mock(HttpServletRequest.class);
		objects[0]=request;
		when(jp.getArgs()).thenReturn(objects);
	    logAspect.controlloLogUtente(jp);
	}
	
	
	@Test
	void tokenInesistente() throws Throwable {
		Object[] objects=new Object[1];
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies=new Cookie[1];
		cookies[0]=new Cookie("nomeCookie", "INVALID");
		when(request.getCookies()).thenReturn(cookies);
		objects[0]=request;
		when(jp.getArgs()).thenReturn(objects);
	    logAspect.controlloLogUtente(jp);
	}
	
	@Test
	void tokenNonValido() throws Throwable {
		Object[] objects=new Object[1];
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies=new Cookie[1];
		cookies[0]=new Cookie("token", token);
		when(request.getCookies()).thenReturn(cookies);
		objects[0]=request;
		when(jp.getArgs()).thenReturn(objects);
		
	    when(JWT.validate(token)).thenThrow(new RuntimeException());
	    assertThrows(TokenException.class, ()->{logAspect.controlloLogUtente(jp);});
	}
	@Test
	void tokenPresente() throws Throwable {
		Object[] objects=new Object[1];
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies=new Cookie[1];
		cookies[0]=new Cookie("token", token);
		when(request.getCookies()).thenReturn(cookies);
		objects[0]=request;
		when(jp.getArgs()).thenReturn(objects);
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    logAspect.controlloLogUtente(jp);
	}

	@AfterEach
	public void teardown() {
		mockedStaticJWT.close();;
	}

}
