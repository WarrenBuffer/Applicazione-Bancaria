package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;

public interface ClienteMongoService {
	ClienteMongo saveClienteMongo(ClienteMongo clienteMongo);
	List<ClienteMongo> findAll();
	Optional<ClienteMongo> findById(long id);
	void deleteClienteMongo(ClienteMongo clienteMongo);
}
