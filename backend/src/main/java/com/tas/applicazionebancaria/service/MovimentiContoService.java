package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;

public interface MovimentiContoService {
	MovimentiConto saveMovimentiConto(MovimentiConto movimentiConto);
	List<MovimentiConto> findAll();
	Optional<MovimentiConto> findById(long id);
	void deleteMovimentiConto(MovimentiConto movimentiConto);
	List<MovimentiConto> findUltimi10(long codConto);
}
