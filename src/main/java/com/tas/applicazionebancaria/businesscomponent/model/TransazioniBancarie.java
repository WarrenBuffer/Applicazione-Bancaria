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
/** Transazioni tra piu conti 
 * 
 */
@Entity
@Table(name = "transazioni_bancarie")
@Data
public class TransazioniBancarie implements Serializable {
	private static final long serialVersionUID = -5940808106740894326L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long codTransazioneBancaria;

	@Column(nullable = false)
	private double importo;

	@Column(nullable = false)
	private Date dataTransazione;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private tipoTransazioneBancaria tipoTransazione;

	@ManyToOne
	@JoinColumn(name="codContoOrigine")
	private Conto contoOrigine;

	@ManyToOne
	@JoinColumn(name="codContoDestinazione")
	private Conto contoDestinazione;
}

enum tipoTransazioneBancaria {
	ACCREDITO, ADDEBITO;
}
