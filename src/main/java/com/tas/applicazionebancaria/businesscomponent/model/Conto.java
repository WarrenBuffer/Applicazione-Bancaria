package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table
@Data
public class Conto implements Serializable{
	private static final long serialVersionUID = -338002594390170971L;
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private long codConto;
	@Column(nullable=false, unique = true)
	private String emailCliente;
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Enum tipoConto;
	@Column(nullable=false)
	private double saldo;
	@Column(nullable=false)
	private long codCliente;
	
}
enum Enum{
	CORRENTE, RISPARMIO;
}