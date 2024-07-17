package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
//CREATE TABLE clienti (
//codCliente INT AUTO_INCREMENT PRIMARY KEY,
//nomeCliente VARCHAR(50) NOT NULL,
//cognomeCliente VARCHAR(50) NOT NULL,
//emailCliente VARCHAR(100) NOT NULL UNIQUE,
//passwordCliente VARCHAR(100) NOT NULL,
//saldoConto DECIMAL(15, 2) NOT NULL DEFAULT 0.00
//);

@Entity
@Table
@Data
public class Cliente implements Serializable {
	private static final long serialVersionUID = 4581204978132293292L;

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private long codCliente;
	@Column(nullable=false)
	private String nomeCliente;
	@Column(nullable=false)
	private String cognomeCliente;
	@Column(nullable=false, unique = true)
	private String emailCliente;
	@Column(nullable=false)
	private String passwordCliente;
	@Column(nullable=false)
	private int tentativiErrati=0;
	@Column(nullable=false)
	private boolean accountBloccato=false;
}
