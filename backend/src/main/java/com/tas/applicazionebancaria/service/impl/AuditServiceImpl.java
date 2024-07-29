package com.tas.applicazionebancaria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.AuditLog;
import com.tas.applicazionebancaria.repository.AuditRepository;
import com.tas.applicazionebancaria.service.AuditService;

@Service
public class AuditServiceImpl implements AuditService{

	@Autowired
	private AuditRepository ar;
	
	@Override
	public void saveAudit(AuditLog audit) {
		ar.save(audit);
		
	}

	@Override
	public List<AuditLog> findAll() {
		return ar.findAll();
	}
}
