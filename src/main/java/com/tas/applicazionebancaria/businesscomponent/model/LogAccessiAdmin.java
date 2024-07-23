package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Document(collection = "log_accessi_admin")
@Data
public class LogAccessiAdmin implements Serializable{

	private static final long serialVersionUID = -9053244362811124361L;
	
	@Id
	private String codAdmin;
	
	private Date data;
	
	private String dettagli;
}
