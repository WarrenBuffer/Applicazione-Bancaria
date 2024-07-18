package com.tas.applicazionebancaria.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

public interface StatisticheService {
	List<Cliente> findClienteSaldoPiuAlto();
	Date findUltimaTransazione();
	long findNumTransazioni();
	double findSommaImporti();
	double findSaldoMedio();
	long findNumContiByCodCliente(long id);
	Map<Cliente, Long> findNumContiPerCliente();
	long findNumCarteByCodCliente(long id);
	Map<Cliente, Long> findNumCartePerCliente();
	double findTotPrestitiByCodCliente(long id);
	Map<Cliente, Double> findTotPrestitiPerCliente();
	double findTotPagamentiByCodCliente(long id);
	Map<Cliente, Double> findTotPagamentiPerCliente();
}