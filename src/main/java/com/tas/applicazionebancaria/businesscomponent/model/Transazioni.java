package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/** Transazioni per un solo conto (deposito, prelievo)
 * 
 */
@Data
@Entity
@Table

//Transazioni per un solo conto
public class Transazioni implements Serializable{ 
	private static final long serialVersionUID = 8816334464026097444L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codTransazione;
	
	@Column(nullable=false)
	private double importo; 
	
	@Column(nullable=false)
	private Date dataTransazione;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private TipoTransazione tipoTransazione;

	@Column(nullable = false)
	@ManyToOne
	@JoinColumn(name="codConto")
	private long codConto;
}

enum TipoTransazione{
	ACCREDITO, ADDEBITO;
}

