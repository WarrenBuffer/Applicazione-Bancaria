package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.repository.ClienteRepository;
import com.tas.applicazionebancaria.service.ClienteService;

public class ClienteServiceImpl implements ClienteService{
	@Autowired
	ClienteRepository cr;
	
	@Override
	public Cliente saveCliente(Cliente cliente) {
		return cr.save(cliente);
	}

	@Override
	public List<Cliente> findAll() {
		return cr.findAll();
	}

	@Override
	public Optional<Cliente> findById(long id) {
		return cr.findById(id);
	}

	@Override
	public void deleteCliente(Cliente cliente) {
		cr.delete(cliente);
	}
	
}
