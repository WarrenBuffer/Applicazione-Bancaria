package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

public interface ClienteService {
	Cliente saveCliente(Cliente cliente);
	List<Cliente> findAll();
	Optional<Cliente> findById(long id);
	void deleteCliente(Cliente cliente);
}
