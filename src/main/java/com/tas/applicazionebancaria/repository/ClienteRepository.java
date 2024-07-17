package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

@Repository("ClienteRepository")
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
}
