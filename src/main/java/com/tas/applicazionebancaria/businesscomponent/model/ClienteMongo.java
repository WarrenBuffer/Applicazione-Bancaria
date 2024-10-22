package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Document(collection = "cliente")
@Data
public class ClienteMongo implements Serializable {
	private static final long serialVersionUID = 4581204978132293292L;
	@Id
	private String id;
	private long codCliente;
	private String nomeCliente;
	private String cognomeCliente;
	private String emailCliente;
	private String passwordCliente;
	private double saldo;
}
