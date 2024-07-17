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
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Transazioni implements Serializable{
	private static final long serialVersionUID = 8816334464026097444L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codTransazione;
	
	@Column(name="importo", nullable=false)
	private double importo; 
	
	@Column(name="dataTransazione", nullable=false)
	private Date dataTransazione;
	
	@Column(name="tipoTransazione", nullable=false)
	@Enumerated(EnumType.STRING)
	private TipoTransazione tipoTransazione;
	
	@Column(name="codConto", nullable=false)
	private long codConto;
}

enum TipoTransazione{
	Accredito, Addebito;
}
