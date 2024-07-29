package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;

public interface RichiestePrestitoService {
	RichiestePrestito saveRichiestePrestito(RichiestePrestito richiestePrestito);
	List<RichiestePrestito> findAll();
	Optional<RichiestePrestito> findById(long id);
	void deleteRichiestePrestito(RichiestePrestito richiestePrestito);
	List<RichiestePrestito> findByCodCliente(long codCliente);
}
