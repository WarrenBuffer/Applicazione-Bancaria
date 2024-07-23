package com.tas.applicazionebancaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.AuditLog;

@Repository("AuditRepository")
public interface AuditRepository extends MongoRepository<AuditLog, String>{

}
