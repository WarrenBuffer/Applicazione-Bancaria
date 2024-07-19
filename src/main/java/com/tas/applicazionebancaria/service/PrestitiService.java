package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;

public interface PrestitiService {
	Prestiti savePrestiti(Prestiti prestiti);
	List<Prestiti> findAll();
	Optional<Prestiti> findById(long id);
	void deletePrestiti(Prestiti prestiti);
	double findTotPrestitiByCodCliente(long id);
}
