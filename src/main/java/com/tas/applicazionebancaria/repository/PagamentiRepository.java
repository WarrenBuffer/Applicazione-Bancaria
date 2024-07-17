package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Pagamenti;

@Repository("PagamentiRepository")
public interface PagamentiRepository extends JpaRepository<Pagamenti, Long>{
	
}
