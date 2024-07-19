package com.tas.applicazionebancaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

import jakarta.transaction.Transactional;

@Repository("ClienteRepository")
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	@Query(value = "Select * from cliente where email_cliente = ?1", nativeQuery = true)
	Optional<Cliente> findByEmail(String email);
	

	@Query(value = "update cliente set tentativi_errati = tentativi_errati + 1 where email_cliente = ?1", nativeQuery = true)
	@Modifying
	@Transactional
	void incrementaTentativoErratoByEmail(String email);
	
	@Query(value = "Select tentativi_errati from cliente where email_cliente = ?1", nativeQuery = true)
	int getTentativiErratiByEmail(String email);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE\r\n"
			+ " cliente \r\n"
			+ "SET\r\n"
			+ " account_bloccato = false \r\n"
			+ "WHERE\r\n"
			+ " email_cliente = ?1;", nativeQuery = true)
	void setBloccatoFalseByEmail(String email);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE\r\n"
			+ " cliente \r\n"
			+ "SET\r\n"
			+ " account_bloccato = true \r\n"
			+ "WHERE\r\n"
			+ " email_cliente = ?1;", nativeQuery = true)
	void setBloccatoTrueByEmail(String email);
	
	@Query(value = "Select account_bloccato from cliente where email_cliente = ?1", nativeQuery = true)
	boolean getStatoBloccatoByEmail(String email);
	
	@Query(value = "select cl.cod_cliente, cl.nome_cliente, cl.cognome_cliente, cl.email_cliente, cl.account_bloccato, cl.tentativi_errati, co.saldo from cliente cl, conto co where cl.cod_cliente = co.cod_cliente group by cl.cod_cliente, co.saldo having max(saldo)", nativeQuery = true)
	List<Cliente> findClienteSaldoPiuAlto();
}