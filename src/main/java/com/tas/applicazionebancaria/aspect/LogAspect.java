package com.tas.applicazionebancaria.aspect;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tas.applicazionebancaria.exceptions.TokenException;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
	/*-----------------------------------------POINTCUTS-----------------------------------------*/

	@Pointcut("execution(* com.tas.applicazionebancaria.repository.ContoRepository.*(..))")
	public void contoRepositoryMethods() {
	}
	/*-------------------------------------------------------------------------------------------*/

	@Around("execution(* com.tas.applicazionebancaria.repository.AmministratoreRepository.*(..)) || "
			+ " execution(* com.tas.applicazionebancaria.repository.ClienteMongoRepository.*(..)) || "
			+ " execution(* com.tas.applicazionebancaria.repository.ClienteRepository.*(..)) || "
			+ " execution(* com.tas.applicazionebancaria.repository.ContoRepository.*(..))")
	public Object queryLog(ProceedingJoinPoint pjp) throws Throwable {
		Logger logger = Logger.getLogger("com.tas.applicazionebancaria.logger");
		long inizio = System.currentTimeMillis();
		Object object = pjp.proceed();
		long delta = System.currentTimeMillis() - inizio;
		if (delta >= 0L) { // TODO mettere 50L al posto di 0L
			Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
			if (Files.notExists(path)) {
				Files.createDirectories(path);
			}
			FileHandler handleQueryLog = new FileHandler(path + "\\QueryEtimeLog.log", true);
			logger.setLevel(Level.ALL);
			SimpleFormatter formato = new SimpleFormatter();
			handleQueryLog.setFormatter(formato);
			logger.addHandler(handleQueryLog);
			logger.log(Level.INFO,
					"Signature Metodo [" + pjp.getSignature() + "]\nEseguita query in " + delta + " millis.");
			handleQueryLog.close();
		}
		return object;
	}

	@After("execution(* com.tas.applicazionebancaria.controller.ClientController.* (..)) && !execution(static * *(..))")
	public void httpLog(JoinPoint jp) throws Throwable {
		Logger logger = Logger.getLogger("com.tas.applicazionebancaria.logger");
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}
		FileHandler handleHttpLog = new FileHandler(path + "\\HttpsLog.log", true);
		logger.setLevel(Level.ALL);
		SimpleFormatter formato = new SimpleFormatter();
		handleHttpLog.setFormatter(formato);

		logger.addHandler(handleHttpLog);
		String token = null;
		HttpServletRequest request = null;
		for (Object o : jp.getArgs()) {
			if (o instanceof HttpServletRequest) {
				request = (HttpServletRequest) o;
				if (request != null) {
					for (Cookie c : request.getCookies()) {
						if (c.getName().equals("token")) {
							token = c.getValue();
						}
					}
				}
			}
		}
		try {
			token = JWT.validate(token).getBody().getSubject();
		} catch (Exception e) {
			token = null;
		}
		logger.log(Level.INFO, "Signature Metodo [" + jp.getSignature() + "]");
		if (token != null && request != null) {
			logger.log(Level.INFO, "Utente: " + token + "\n\t" + "URL: " + request.getRequestURI());
		} else {
			logger.log(Level.WARNING, "Manca request oppure token in: \n\t" + jp.getSignature());
		}
		handleHttpLog.close();
	}

	@Before("execution(* com.tas.applicazionebancaria.controller.ClientController.* (..)) && "
			+ "!(execution(* com.tas.applicazionebancaria.controller.ClientController.registrazione(String, HttpServletRequest)) ||"
			+ "  execution(* com.tas.applicazionebancaria.controller.ClientController.registrazione(Cliente, HttpServletRequest)) ||"
			+ "  execution(* com.tas.applicazionebancaria.controller.ClientController.login (..))"
			+ "  execution(* com.tas.applicazionebancaria.controller.ClientController.controlloLogin (..))"
			+ ") && !execution(static * *(..))")
	public void controlloLogUtente(JoinPoint jp) throws Throwable {
		System.out.println("Starting: controlloLogUtente");
		String token = null;
		HttpServletRequest request = null;
		for (Object o : jp.getArgs()) {
			if (o instanceof HttpServletRequest) {
				request = (HttpServletRequest) o;
				if (request != null) {
					for (Cookie c : request.getCookies()) {
						if (c.getName().equals("token")) {
							token = c.getValue();
						}
					}
				}
			}
		}
		try {
			if (request != null) {
				JWT.validate(token);
			} else {
				System.out.println("Mancano request oppure token in: \n\t" + jp.getSignature());
			}
		} catch (Exception e) {
			throw new TokenException(e.getMessage());
		}

	}

}