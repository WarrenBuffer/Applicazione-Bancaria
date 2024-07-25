package com.tas.applicazionebancaria.aspect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest(classes=ApplicazioneBancariaApplication.class)
class HttpLogTest {
	MockedStatic<Files> mockedStaticfiles;
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
		mockedStaticfiles = mockStatic(Files.class);
		mockedStaticJWT= mockStatic(JWT.class);
	}
	
	@Test
	void pathNonEsisteArgsVuoti() throws Throwable {
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		when(Files.notExists(path)).thenReturn(true);
		when(Files.createDirectories(path)).thenReturn(null);
		when(jp.getArgs()).thenReturn(null);
	    logAspect.httpLog(jp);
	}
	@Test
	void requestInesistente() throws Throwable {
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		when(Files.notExists(path)).thenReturn(false);
		Object[] objects=new Object[1];
		objects[0] =2;
		when(jp.getArgs()).thenReturn(objects);
	    logAspect.httpLog(jp);
	}
	@Test
	void cookiesInesistenti() throws Throwable {
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		when(Files.notExists(path)).thenReturn(false);
		Object[] objects=new Object[1];
		HttpServletRequest request = mock(HttpServletRequest.class);
		objects[0]=request;
		when(jp.getArgs()).thenReturn(objects);
	    logAspect.httpLog(jp);
	}
	
	
	@Test
	void tokenInesistente() throws Throwable {
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		when(Files.notExists(path)).thenReturn(false);
		Object[] objects=new Object[1];
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies=new Cookie[1];
		cookies[0]=new Cookie("nomeCookie", "INVALID");
		when(request.getCookies()).thenReturn(cookies);
		objects[0]=request;
		when(jp.getArgs()).thenReturn(objects);
	    logAspect.httpLog(jp);
	}
	
	@Test
	void tokenPresente() throws Throwable {
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		when(Files.notExists(path)).thenReturn(false);
		Object[] objects=new Object[1];
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies=new Cookie[1];
		cookies[0]=new Cookie("token", token);
		when(request.getCookies()).thenReturn(cookies);
		objects[0]=request;
		when(jp.getArgs()).thenReturn(objects);
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");
	    when(mockClaims.getIssuedAt()).thenReturn(new Date());
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    logAspect.httpLog(jp);
	}
	@AfterEach
	public void teardown() {
		mockedStaticfiles.close();
		mockedStaticJWT.close();;
	}
}