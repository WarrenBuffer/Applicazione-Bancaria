package com.tas.applicazionebancaria.service;

import java.util.List;

import com.tas.applicazionebancaria.businesscomponent.model.AuditLog;

public interface AuditService {
	void saveAudit (AuditLog audit);
	List<AuditLog> findAll();
}
