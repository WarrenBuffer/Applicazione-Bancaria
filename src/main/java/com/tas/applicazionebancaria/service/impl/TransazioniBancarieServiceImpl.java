package com.tas.applicazionebancaria.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;
import com.tas.applicazionebancaria.repository.TransazioniBancarieRepository;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;

@Service
public class TransazioniBancarieServiceImpl implements TransazioniBancarieService {
	@Autowired
	TransazioniBancarieRepository tbr;

	@Override
	public TransazioniBancarie saveTransazioniBancarie(TransazioniBancarie transazioniBancarie) {
		return tbr.save(transazioniBancarie);
	}

	@Override
	public List<TransazioniBancarie> findAll() {
		return tbr.findAll();
	}

	@Override
	public Optional<TransazioniBancarie> findById(long id) {
		return tbr.findById(id);
	}

	@Override
	public void deleteTransazioniBancarie(TransazioniBancarie transazioniBancarie) {
		tbr.delete(transazioniBancarie);
	}

	@Override
	public Date findUltimaTransazione() {
		return tbr.findUltimaTransazione();
	}

	@Override
	public long findNumTransazioni() {
		return tbr.findNumTransazioni();
	}

	@Override
	public double findSommaImporti() {
		return tbr.findSommaImporti();
	}
}
