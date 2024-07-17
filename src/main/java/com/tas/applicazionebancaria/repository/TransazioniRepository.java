package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;

@Repository("TransazioniRepository")
public interface TransazioniRepository extends JpaRepository<Transazioni, Long>{
	
}
