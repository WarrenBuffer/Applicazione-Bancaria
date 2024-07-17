package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class CarteDiCredito implements Serializable{
	private static final long serialVersionUID = -9110582539270973702L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codCarta;
	
	@Column(name="numeroCarta", nullable=false, unique = true)
	private String numeroCarta;
	
	@Column(name="dataScadenza", nullable=false)
	private Date dataScadenza;
	
	@Column(name="cvv", nullable=false)
	private String cvv;
	
	@Column(name="codCliente", nullable=false)
	private String codCliente;
}
