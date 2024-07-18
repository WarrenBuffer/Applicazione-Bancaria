package com.tas.applicazionebancaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

@Repository("StatisticheRepository")
public interface StatisticheRepository {
	@Query(value = "select cl.cod_cliente, cl.nome_cliente, cl.cognome_cliente, cl.email_cliente, cl.account_bloccato, cl.tentativi_errati, co.saldo from cliente cl, conto co where cl.cod_cliente = co.cod_cliente group by cl.cod_cliente, co.saldo having max(saldo)")
	List<Cliente> findClienteSaldoPiuAlto();

	@Query(value = "select data_transazione from transazioni_bancarie where data_transazione >= all (select data_transazione from transazioni_bancarie)")
	Date findUltimaTransazione(); 
	
	@Query(value = "select count(*) from transazioni_bancarie")
	long findNumTransazioni();
	
	@Query(value = "select sum(importo) from transazioni_bancarie")
	double findSommaImporti();
	
	@Query(value = "select avg(saldo) from conto")
	double findSaldoMedio();
	
	@Query(value = "select count(*) from conto where cod_cliente = ?1")
	long findNumContiByCodCliente(long id);
	
	@Query(value = "select count(*) from carte_di_credito where cod_cliente = ?1")
	long findNumCarteByCodCliente(long id);
	
	@Query(value = "select sum(importo) from prestiti where cod_cliente = ?1")
	double findTotPrestitiByCodCliente(long id);
	
	@Query(value = "select sum(importo) from pagamenti where cod_cliente = ?1")
	double findTotPagamentiByCodCliente(long id);
	
	// porco dio le avevo fatte per mysql
//	@Query(value = "select count(*) from transazioni where tipo_transazione = 'ACCREDITO'")
//	long findTransazioniAccredito();
//
//	@Query(value = "select count(*) from transazioni where tipo_transazione = 'ADDEBITO'")
//	long findTransazioniAddebito();
//	
//	@Query(value = "select sum(importo) from transazioni group by month(data_transazione)")
//	List<Double> findTotaleByMese();
}
