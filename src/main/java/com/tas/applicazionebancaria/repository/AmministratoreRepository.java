package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;

@Repository("AmministratoreRepository")
public interface AmministratoreRepository  extends JpaRepository<Amministratore, Long> {
	
}
