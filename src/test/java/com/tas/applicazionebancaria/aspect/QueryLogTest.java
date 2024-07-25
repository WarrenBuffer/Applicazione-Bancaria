package com.tas.applicazionebancaria.aspect;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.aspect.LogAspect;

@SpringBootTest(classes=ApplicazioneBancariaApplication.class)
class QueryLogTest {
	
	MockedStatic<Files> mockedStatic;
    @Mock
    private ProceedingJoinPoint joinPoint;
    
    @Mock
    private Logger logger;
    
    @InjectMocks
    private LogAspect logAspect;
    
    @BeforeEach
	public void setUp() {
		mockedStatic = mockStatic(Files.class);
	}

	@Test
	void deltaPiccolo() throws Throwable {
		when(joinPoint.proceed()).thenAnswer(answer->{
				Thread.sleep(2);
				return null;
		});
		logAspect.queryLog(joinPoint);
	}
	@Test
	void pathEsiste() throws Throwable {
		when(joinPoint.proceed()).thenAnswer(answer->{
				Thread.sleep(500);
				return null;
		});
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		when(Files.notExists(path)).thenReturn(false);
		when(Files.createDirectories(path)).thenReturn(null);
		
		logAspect.queryLog(joinPoint);
	}
	@Test
	void pathNonEsiste() throws Throwable {
		when(joinPoint.proceed()).thenAnswer(answer->{
				Thread.sleep(500);
				return null;
		});
		Path path = Paths.get("c:\\log\\ApplicazioneBancariaLog");
		when(Files.notExists(path)).thenReturn(true);
		when(Files.createDirectories(path)).thenReturn(null);
		
		logAspect.queryLog(joinPoint);
	}
	@AfterEach
	public void teardown() {
		mockedStatic.close();
	}
}
