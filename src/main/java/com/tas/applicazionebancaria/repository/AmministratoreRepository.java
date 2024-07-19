package com.tas.applicazionebancaria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;

import jakarta.transaction.Transactional;

@Repository("AmministratoreRepository")
public interface AmministratoreRepository  extends JpaRepository<Amministratore, Long> {
	@Query(value = "Select * from amministratore where email_admin = ?1", nativeQuery = true)
	Optional<Amministratore> findByEmail(String email);
	@Query(value = "UPDATE\r\n"
			+ " amministratore \r\n"
			+ "SET\r\n"
			+ " tentativi_errati = tentativi_errati + 1 \r\n"
			+ "WHERE\r\n"
			+ " email_admin = ?1;", nativeQuery = true)
	void incrementaTentativoErratoByEmail(String email);
	
	@Query(value = "Select tentativi_errati from amministratore where email_admin = ?1", nativeQuery = true)
	int getTentativiErratiByEmail(String email);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE\r\n"
			+ " amministratore \r\n"
			+ "SET\r\n"
			+ " account_bloccato = false \r\n"
			+ "WHERE\r\n"
			+ " email_admin = ?1;", nativeQuery = true)
	void setBloccatoFalseByEmail(String email);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE\r\n"
			+ " amministratore \r\n"
			+ "SET\r\n"
			+ " account_bloccato = true \r\n"
			+ "WHERE\r\n"
			+ " email_admin = ?1;", nativeQuery = true)
	void setBloccatoTrueByEmail(String email);
	
	@Query(value = "Select account_bloccato from amministratore where email_admin = ?1", nativeQuery = true)
	boolean getStatoBloccatoByEmail(String email);
}
