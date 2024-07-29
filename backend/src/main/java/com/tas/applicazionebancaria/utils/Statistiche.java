package com.tas.applicazionebancaria.utils;

import java.util.Date;
import java.util.List;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

import lombok.Data;

@Data
public class Statistiche {
	private List<Cliente> clienti;
	private List<Cliente> saldoPiuAlto;
	private Date ultimaTransazione; 
	private long numTransazioni;
	private double sommaImporti;
	private double saldoMedio;
	private Long totAddebiti;
	private Long totAccrediti;
	private Long transazioniMediePerCliente;
	private List<TransazioniMongo> importoTransazioniPerMese;
	private long clientiSaldoPositivo;
	private long clientiSaldoNegativo;
	private long saldoTotale;
}
