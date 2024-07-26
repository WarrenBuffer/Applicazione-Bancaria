package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;

public interface TransazioniBancarieService {
	TransazioniBancarie saveTransazioniBancarie(TransazioniBancarie transazioniBancarie);
	List<TransazioniBancarie> findAll();
	Optional<TransazioniBancarie> findById(long id);
	void deleteTransazioniBancarie(TransazioniBancarie transazioniBancarie);
	long findNumTransazioni();
	double findSommaImporti();
	List<TransazioniBancarie> findUltime10(long contoOrigine);
	List<TransazioniBancarie> findUltimi10Accrediti(long conto);
}
