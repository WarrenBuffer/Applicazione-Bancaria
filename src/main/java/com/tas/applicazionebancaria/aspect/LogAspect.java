package com.tas.applicazionebancaria.aspect;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Component;

import com.tas.applicazionebancaria.exceptions.TokenException;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
	/*----------------------------------FILE DI LOG------------------------------------------*/
	@Around("execution(* com.tas.applicazionebancaria.repository.AmministratoreRepository.*(..)) || "
			+ " execution(* com.tas.applicazionebancaria.repository.ClienteMongoRepository.*(..)) || "
			+ " execution(* com.tas.applicazionebancaria.repository.ClienteRepository.*(..)) || "
			+ " execution(* com.tas.applicazionebancaria.repository.ContoRepository.*(..))")
	public Object queryLog(ProceedingJoinPoint pjp) throws Throwable {
		Logger logger = Logger.getLogger("com.tas.applicazionebancaria.logger");
		long inizio = System.currentTimeMillis();
		Object object = pjp.proceed();
		long delta = System.currentTimeMillis() - inizio;
		if (delta >= 50L) {
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
	
	@After("execution(* com.tas.applicazionebancaria.controller.ClientController.* (..)) && !execution(static * com.tas.applicazionebancaria.controller.ClientController.*(..))")
	public void httpLog(JoinPoint jp) throws Throwable{
		Logger logger=Logger.getLogger("com.tas.applicazionebancaria.logger");
		Path path=Paths.get("c:\\log\\ApplicazioneBancariaLog");
		if(Files.notExists(path)) {
			Files.createDirectories(path);
		}
		FileHandler handleHttpLog = new FileHandler(path + "\\HttpsLog.log", true);
		try {
		logger.setLevel(Level.ALL);
		SimpleFormatter formato = new SimpleFormatter();
		handleHttpLog.setFormatter(formato);

		logger.addHandler(handleHttpLog);
		String token=null;
		HttpServletRequest request=null;
		if(jp.getArgs()!=null) {
			for(Object o: jp.getArgs()) {
				if(o instanceof HttpServletRequest) {
					request=(HttpServletRequest) o;
					if(request.getCookies()!=null) {
						for(Cookie c: request.getCookies()) {
							if(c.getName().equals("token")) {
								token=c.getValue();
							}
						}
					}
				}
			}
		}
		String data;
		String subject;
		try {
			subject=JWT.validate(token).getBody().getSubject();
			data=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(JWT.validate(token).getBody().getIssuedAt());
		} catch (Exception e) {
			subject=null;
			data=null;
		}
		logger.log(Level.INFO,"Signature Metodo ["+jp.getSignature()+"]");
		if(request!=null) {
			if(subject!=null){
				logger.log(Level.INFO,"Utente: "+ subject +"\n\t"
									+ "URL: "+ request.getRequestURI()+"\n\t"
									+ "Autenticato il: "+ data);
			}
		}else {
			logger.log(Level.WARNING,"Manca request oppure token in: \n\t"+jp.getSignature());
		}
		}finally {
			handleHttpLog.close();
		}
			
	}			
	
	@After("execution(* com.tas.applicazionebancaria.controller.AdminController.* (..)) && !execution(static * com.tas.applicazionebancaria.controller.AdminController.*(..))")
	public void accessiLog(JoinPoint jp) throws Throwable{
		Logger logger=Logger.getLogger("com.tas.applicazionebancaria.logger");
		Path path=Paths.get("c:\\log\\ApplicazioneBancariaLog");
		if(Files.notExists(path)) {
			Files.createDirectories(path);
		}
		FileHandler handleHttpLog=new FileHandler(path+"\\HttpsLog.log", true);
		try {
		logger.setLevel(Level.ALL);
		SimpleFormatter formato=new SimpleFormatter();
		handleHttpLog.setFormatter(formato);

		logger.addHandler(handleHttpLog);
		String token=null;
		HttpServletRequest request=null;
		if(jp.getArgs()!=null) {
			for(Object o: jp.getArgs()) {
				if(o instanceof HttpServletRequest) {
					request=(HttpServletRequest) o;
					if(request.getCookies()!=null) {
						for(Cookie c: request.getCookies()) {
							if(c.getName().equals("token")) {
								token=c.getValue();
								break;
							}
						}
					}
				}
			}
		}
		String data;
		String subject;
		try {
			subject=JWT.validate(token).getBody().getSubject();
			data=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(JWT.validate(token).getBody().getIssuedAt());
		} catch (Exception e) {
			subject=null;
			data=null;
		}
		logger.log(Level.INFO,"Signature Metodo ["+jp.getSignature()+"]");
		if(request!=null) {
			if(subject!=null){
				logger.log(Level.INFO,"Admin: "+ subject +"\n\t"
									+ "URL: "+ request.getRequestURI()+"\n\t"
									+ "Autenticato il: "+ data);
			}
		}else {
			logger.log(Level.WARNING,"Manca request oppure token in: \n\t"+jp.getSignature());
		}
		}finally {
		handleHttpLog.close();
		}
	}

	/*----------------------------------CONTROLLO DEGLI ACCESSI------------------------------------------*/
	@Before("execution(* com.tas.applicazionebancaria.controller.ClientController.* (..)) && "
			+ "!(execution(* com.tas.applicazionebancaria.controller.ClientController.registrazione(..)) ||"
			+ "  execution(* com.tas.applicazionebancaria.controller.ClientController.login (..)) ||"
			+ "  execution(* com.tas.applicazionebancaria.controller.ClientController.controlloLogin (..)) ||"
			+ "  execution(static * com.tas.applicazionebancaria.controller.ClientController.* (..)))")
	public void controlloLogUtente(JoinPoint jp) throws Throwable {
		String token = null;
		HttpServletRequest request = null;
		if(jp.getArgs()!=null) {
			for (Object o : jp.getArgs()) {
				if (o instanceof HttpServletRequest) {
					request = (HttpServletRequest) o;
					if (request.getCookies()!=null) {
						for (Cookie c : request.getCookies()) {
							if (c.getName().equals("token")) {
								token = c.getValue();
								break;
							}
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
	
//	@Before("execution(* com.tas.applicazionebancaria.config.JwtAuthFilter.doFilterInternal (..)))")
//	public void controlloLogAdmin(JoinPoint jp) throws Throwable{
//		String token=null;
//		if(jp.getArgs()!=null) {
//			for (Object o : jp.getArgs()) {
//				if (o instanceof HttpServletRequest) {
//					HttpServletRequest request = (HttpServletRequest) o;
//					if (request.getCookies()!=null) {
//						for (Cookie c : request.getCookies()) {
//							if (c.getName().equals("bearer")) {
//								token=c.getValue();
//								break;
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		try {
//			if(token!=null)
//				JWT.validate(token);
//		} catch (Exception e) {
//			throw new AdminTokenException(e.getMessage());
//		}
//	}
	
}