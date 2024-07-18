package com.tas.applicazionebancaria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

@Repository("ClienteRepository")
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	@Query(value= "Select * from utente where email=?1", nativeQuery = true )
	Optional<Cliente> findByEmail(String email);
}
