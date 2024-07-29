package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;
import com.tas.applicazionebancaria.repository.PrestitiRepository;
import com.tas.applicazionebancaria.service.PrestitiService;

@Service
public class PrestitiServiceImpl implements PrestitiService{
	@Autowired
	PrestitiRepository pr;

	@Override
	public Prestiti savePrestiti(Prestiti prestiti) {
		return pr.save(prestiti);
	}

	@Override
	public List<Prestiti> findAll() {
		return pr.findAll();
	}

	@Override
	public Optional<Prestiti> findById(long id) {
		return pr.findById(id);
	}

	@Override
	public void deletePrestiti(Prestiti prestiti) {
		pr.delete(prestiti);
	}

	@Override
	public List<Prestiti> findByCodCliente(long codCliente) {
		return pr.findByCodCliente(codCliente);
	}
}
