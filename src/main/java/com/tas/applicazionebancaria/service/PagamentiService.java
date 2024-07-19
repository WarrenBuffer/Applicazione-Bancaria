package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.Pagamenti;

public interface PagamentiService {
	Pagamenti savePagamenti(Pagamenti pagamenti);
	List<Pagamenti> findAll();
	Optional<Pagamenti> findById(long id);
	void deletePagamenti(Pagamenti pagamenti);
	double findTotPagamentiByCodCliente(long id);
}
