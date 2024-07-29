package com.tas.applicazionebancaria.service;

import java.util.List;

import com.tas.applicazionebancaria.businesscomponent.model.LogAccessiAdmin;

public interface LogAccessiService {
	LogAccessiAdmin saveLogAccesso(LogAccessiAdmin log);
	List<LogAccessiAdmin> findAll();
}
