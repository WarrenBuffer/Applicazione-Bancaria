package com.tas.applicazionebancaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.LogAccessiAdmin;

@Repository("LogAccessiAdminRepository")
public interface LogAccessiAdminRepository extends MongoRepository<LogAccessiAdmin, String>{	

}
