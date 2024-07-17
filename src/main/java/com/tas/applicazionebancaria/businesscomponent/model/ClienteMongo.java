package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document
public class ClienteMongo implements Serializable{
	private static final long serialVersionUID = -6090801054487536188L;
	@Id
	private long codCliente;
	private String nomeCliente;
	private String cognomeCliente;
	private String emailCliente;
	private String passwordCliente;
	private int tentativiErrati=0;
	private boolean accountBloccato=false;
}
