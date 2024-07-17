package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;

@Repository("PrestitiRepository")
public interface PrestitiRepository extends JpaRepository<Prestiti, Long>{
	
}
