package com.tas.applicazionebancaria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.LogAccessiAdmin;
import com.tas.applicazionebancaria.repository.LogAccessiAdminRepository;
import com.tas.applicazionebancaria.service.LogAccessiService;

@Service
public class LogAccessiServiceImpl implements LogAccessiService{

	@Autowired
	private LogAccessiAdminRepository laRepo;
	
	@Override
	public void saveLogAccesso(LogAccessiAdmin log) {
		laRepo.save(log);
	}

	@Override
	public List<LogAccessiAdmin> findAll() {
		return laRepo.findAll();
	}

}
