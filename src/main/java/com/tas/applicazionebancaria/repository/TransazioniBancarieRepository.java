package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;

@Repository("TransazioniBancarieRepository")
public interface TransazioniBancarieRepository extends JpaRepository<TransazioniBancarie, Long>{
	
}
