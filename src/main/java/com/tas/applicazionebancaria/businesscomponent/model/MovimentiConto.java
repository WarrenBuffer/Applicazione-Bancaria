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

@Entity
@Table(name = "movimenti_conto")
@Data
public class MovimentiConto implements Serializable {
	private static final long serialVersionUID = 3295649427486382058L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long codMovimento;

	@Column(nullable = false)
	private double importo;

	@Column(nullable = false)
	private Date dataMovimento;

	@Column(nullable = false)
	private TipoMovimento tipoMovimento;

	@Column(nullable = false)
	private long codConto;
}

enum TipoMovimento {
	ACCREDITO, ADDEBITO;
}