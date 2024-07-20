package com.tas.applicazionebancaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

@Repository("ClienteRepository")
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	@Query(value = "Select * from cliente where email_cliente = ?1", nativeQuery = true)
	Optional<Cliente> findByEmail(String email);
	
	// @Query(value = "select cl.cod_cliente, cl.nome_cliente, cl.cognome_cliente, cl.email_cliente, cl.account_bloccato, cl.tentativi_errati, co.saldo from cliente cl, conto co where cl.cod_cliente = co.cod_cliente group by cl.cod_cliente, co.saldo having max(saldo)", nativeQuery = true)
	@Query(value = "select cl.cod_cliente, cl.nome_cliente, cl.cognome_cliente, cl.email_cliente, cl.password_cliente, cl.account_bloccato, cl.tentativi_errati, co.saldo from cliente cl, conto co where cl.cod_cliente = co.cod_cliente group by cl.cod_cliente, co.saldo having max(saldo)", nativeQuery = true)
	List<Cliente> findClienteSaldoPiuAlto();
}
