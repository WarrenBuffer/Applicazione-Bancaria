package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.Pagamenti;
import com.tas.applicazionebancaria.repository.PagamentiRepository;
import com.tas.applicazionebancaria.service.PagamentiService;

@Service
public class PagamentiServiceImpl implements PagamentiService {
	@Autowired
	PagamentiRepository pr;
	
	@Override
	public Pagamenti savePagamenti(Pagamenti pagamenti) {
		return pr.save(pagamenti);
	}

	@Override
	public List<Pagamenti> findAll() {
		return pr.findAll();
	}

	@Override
	public Optional<Pagamenti> findById(long id) {
		return pr.findById(id);
	}

	@Override
	public void deletePagamenti(Pagamenti pagamenti) {
		pr.delete(pagamenti);
	}

	@Override
	public double findTotPagamentiByCodCliente(long id) {
		return pr.findTotPagamentiByCodCliente(id);
	}

	@Override
	public List<Pagamenti> findUltimi10Pagamenti(long codCliente) {
		return pr.findUltimi10Pagamenti(codCliente);
	}
}
