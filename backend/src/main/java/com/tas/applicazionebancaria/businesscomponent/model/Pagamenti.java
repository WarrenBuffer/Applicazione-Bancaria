package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.MetodoPagamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pagamenti")
@Data
public class Pagamenti implements Serializable {
	private static final long serialVersionUID = -6740655363264983299L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long codPagamento;

	@Column(nullable = false)
	private double importo;

	@Column(nullable = false)
	private Date dataPagamento;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MetodoPagamento metodoPagamento;

	@JoinColumn(name = "codCliente")
	private long codCliente;
}


