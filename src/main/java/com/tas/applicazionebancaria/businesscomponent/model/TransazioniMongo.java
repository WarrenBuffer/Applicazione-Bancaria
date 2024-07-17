package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Document(collection = "transazioni")
@Data
public class TransazioniMongo implements Serializable {
	private static final long serialVersionUID = 6122473539967680294L;
	
	@Id
	private String codTransazione;
	
	private double importo;
	
	private Date dataTransazione;
	
	private tipoTransazione tipoTransazione;
	
	private int codiceConto;
}
