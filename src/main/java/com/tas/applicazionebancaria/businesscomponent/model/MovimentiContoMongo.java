package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoMovimento;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(collection = "operazioni_bancarie")

// --- classe mongo equilvalente alla classe MovimentiConto MySQL

public class MovimentiContoMongo implements Serializable{
	
	private static final long serialVersionUID = 3589150116495049789L;
	
	@Id
	private String id;
	
	private String codOperazione;
	private double importo;
	private Date dataOperazione;
	private TipoMovimento tipoOperazione;
	private int codContoOrigine;
	private int codContoDestinazione;
}
