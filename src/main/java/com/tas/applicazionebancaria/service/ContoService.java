package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.Conto;

public interface ContoService {
	Conto saveConto(Conto conto);
	List<Conto> findAll();
	Optional<Conto> findById(long id);
	void deleteConto(Conto conto);
}
