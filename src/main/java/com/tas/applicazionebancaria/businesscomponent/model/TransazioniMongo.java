package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoTransazione;

import jakarta.persistence.Id;
import lombok.Data;

@Document(collection = "transazioni")
@Data
public class TransazioniMongo implements Serializable {
	private static final long serialVersionUID = 6122473539967680294L;
	
	@Id
	private String id;
	private long codTransazione;
	
	private double importo;
	
	private Date dataTransazione;
	
	private TipoTransazione tipoTransazione;
	
	private long codiceConto;
}
