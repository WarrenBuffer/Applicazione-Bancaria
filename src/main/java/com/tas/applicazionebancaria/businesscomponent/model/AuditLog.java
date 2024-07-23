package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoModificaAudit;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(collection = "audit_mongo")
public class AuditLog implements Serializable{
	
	private static final long serialVersionUID = -8110110095719109493L;
	
	@Id
	private String codAdmin;
	
	private Date data;
	
	private TipoModificaAudit tipo;
	
	private String dettagli;
}
