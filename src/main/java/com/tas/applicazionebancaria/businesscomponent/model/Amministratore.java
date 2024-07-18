package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class Amministratore implements Serializable {
	private static final long serialVersionUID = -1103482806710004896L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long codAdmin;

	@Column(nullable = false)
	private String nomeAdmin;
	@Column(nullable = false)
	private String cognomeAdmin;
	@Column(nullable = false, unique = true)
	private String emailAdmin;
	@Column(nullable = false)
	private String passwordAdmin;
	@Column(nullable = false)
	private int tentativiErrati = 0;
	@Column(nullable = false)
	private boolean accountBloccato = false;

}
