package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;
import com.tas.applicazionebancaria.repository.ClienteMongoRepository;
import com.tas.applicazionebancaria.service.ClienteMongoService;

public class ClienteMongoServiceImpl implements ClienteMongoService{
	@Autowired
	ClienteMongoRepository cmr;
	@Override
	public ClienteMongo saveClienteMongo(ClienteMongo clienteMongo) {
		return cmr.save(clienteMongo);
	}

	@Override
	public List<ClienteMongo> findAll() {
		return cmr.findAll();
	}

	@Override
	public Optional<ClienteMongo> findById(long id) {
		return cmr.findById(id);
	}

	@Override
	public void deleteClienteMongo(ClienteMongo clienteMongo) {
		cmr.delete(clienteMongo);
	}
}
