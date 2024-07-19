package com.tas.applicazionebancaria.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.repository.ClienteRepository;
import com.tas.applicazionebancaria.repository.StatisticheRepository;
import com.tas.applicazionebancaria.service.StatisticheService;

@Service
public class StatisticheServiceImpl implements StatisticheService {
	@Autowired(required = false)
	StatisticheRepository sr;
	@Autowired
	ClienteRepository cr;
	
	@Override
	public List<Cliente> findClienteSaldoPiuAlto() {
		return sr.findClienteSaldoPiuAlto();
	}

	@Override
	public Date findUltimaTransazione() {
		return sr.findUltimaTransazione();
	}

	@Override
	public long findNumTransazioni() {
		return sr.findNumTransazioni();
	}

	@Override
	public double findSommaImporti() {
		return sr.findSommaImporti();
	}

	@Override
	public double findSaldoMedio() {
		return sr.findSaldoMedio();
	}

	@Override
	public long findNumContiByCodCliente(long id) {
		return sr.findNumContiByCodCliente(id);
	}

	@Override
	public Map<Cliente, Long> findNumContiPerCliente() {
		Map<Cliente, Long> numContiPerCliente = new HashMap<Cliente, Long>();
		for (Cliente c : cr.findAll()) {
			long numConti = sr.findNumContiByCodCliente(c.getCodCliente());
			numContiPerCliente.put(c, numConti);
		}
		return numContiPerCliente;
	}

	@Override
	public long findNumCarteByCodCliente(long id) {
		return sr.findNumCarteByCodCliente(id);
	}

	@Override
	public Map<Cliente, Long> findNumCartePerCliente() {
		Map<Cliente, Long> numCartePerCliente = new HashMap<Cliente, Long>();
		for (Cliente c : cr.findAll()) {
			long numCarte = sr.findNumCarteByCodCliente(c.getCodCliente());
			numCartePerCliente.put(c, numCarte);
		}
		return numCartePerCliente;
	}

	@Override
	public double findTotPrestitiByCodCliente(long id) {
		return sr.findTotPrestitiByCodCliente(id);
	}

	@Override
	public Map<Cliente, Double> findTotPrestitiPerCliente() {
		Map<Cliente, Double> totPrestitiPerCliente = new HashMap<Cliente, Double>();
		for (Cliente c : cr.findAll()) {
			double totPrestiti = sr.findTotPrestitiByCodCliente(c.getCodCliente()); 
			totPrestitiPerCliente.put(c, totPrestiti);
		}
		return totPrestitiPerCliente;
	}

	@Override
	public double findTotPagamentiByCodCliente(long id) {
		return sr.findTotPagamentiByCodCliente(id);
	}

	@Override
	public Map<Cliente, Double> findTotPagamentiPerCliente() {
		Map<Cliente, Double> totPagamentiPerCliente = new HashMap<Cliente, Double>();
		for (Cliente c : cr.findAll()) {
			double totPagamenti = sr.findTotPagamentiByCodCliente(c.getCodCliente()); 
			totPagamentiPerCliente.put(c, totPagamenti);
		}
		return totPagamentiPerCliente;
	}
}
