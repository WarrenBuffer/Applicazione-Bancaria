package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;

@Repository("MovimentiContoRepository")
public interface MovimentiContoRepository extends JpaRepository<MovimentiConto, Long> {
	
}
