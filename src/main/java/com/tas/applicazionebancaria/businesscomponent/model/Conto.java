package com.tas.applicazionebancaria.businesscomponent.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoConto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class Conto implements Serializable{
	private static final long serialVersionUID = -338002594390170971L;
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private long codConto;
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private TipoConto tipoConto;
	@Column(nullable=false)
	private double saldo;

	@JoinColumn(name = "codCliente")
	private long codCliente;

	@OneToMany(cascade =CascadeType.ALL, mappedBy = "contoOrigine")
	private Set<TransazioniBancarie> transazioniBancarie=new HashSet<TransazioniBancarie>();
	@OneToMany(cascade =CascadeType.ALL, mappedBy = "codConto")
	private Set<Transazioni> transazioni=new HashSet<Transazioni>();
}
