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

@Data
@Entity
@Table(name = "richieste_prestito")
public class RichiestePrestito implements Serializable {

	private static final long serialVersionUID = -8584632604811064924L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codRichiesta;

	@Column(nullable = false)
	private double importo;
	@Column(nullable = false)
	private Date dataRichiesta;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private statoEnum stato;
	
	@ManyToOne
	@JoinColumn(name = "codCliente", nullable = false)
	private Cliente codCliente;
}

enum statoEnum {
	IN_ATTESA, APPROVATO, RIFIUTATO
}
