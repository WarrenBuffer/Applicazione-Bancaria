package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Prestiti implements Serializable{
	private static final long serialVersionUID = 4327653273740314817L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codPrestito;
	
	@Column(name="importo", nullable=false)
	private double importo;
	
	@Column(name="tassoInteresse", nullable=false)
	private double tassoInteresse;
	
	@Column(name="duratainmesi", nullable=false)
	private int durataInMesi;
	
	@ManyToOne
	@JoinColumn(name = "codCliente")
	private Cliente codCliente;
}
