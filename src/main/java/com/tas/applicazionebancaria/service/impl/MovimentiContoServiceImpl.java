package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;
import com.tas.applicazionebancaria.repository.MovimentiContoRepository;
import com.tas.applicazionebancaria.service.MovimentiContoService;

@Service
public class MovimentiContoServiceImpl implements MovimentiContoService {
	@Autowired
	MovimentiContoRepository mcr;
	@Override
	public MovimentiConto saveMovimentiConto(MovimentiConto movimentiConto) {
		return mcr.save(movimentiConto);
	}

	@Override
	public List<MovimentiConto> findAll() {
		return mcr.findAll();
	}

	@Override
	public Optional<MovimentiConto> findById(long id) {
		return mcr.findById(id);
	}

	@Override
	public void deleteMovimentiConto(MovimentiConto movimentiConto) {
		mcr.delete(movimentiConto);
	}
}
