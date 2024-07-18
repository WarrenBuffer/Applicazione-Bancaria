package com.tas.applicazionebancaria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;

@Repository("AmministratoreRepository")
public interface AmministratoreRepository  extends JpaRepository<Amministratore, Long> {
	@Query(value = "Select * from amministratore where emailAdmin = ?1", nativeQuery = true)
	Optional<Amministratore> findByEmail(String email);
}
