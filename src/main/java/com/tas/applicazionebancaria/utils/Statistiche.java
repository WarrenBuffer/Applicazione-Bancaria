package com.tas.applicazionebancaria.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

import lombok.Data;

@Data
public class Statistiche {
	private long numeroClienti;
	private List<Cliente> saldoPiuAlto;
	private Date ultimaTransazione; 
	private long numTransazioni;
	private double sommaImporti;
	private double saldoMedio;
	private Map<Cliente, Long> contiPerCliente;
	private Map<Cliente, Long> cartePerCliente;
	private Map<Cliente, Double> prestitiPerCliente;
	private Map<Cliente, Double> pagamentiPerCliente;
	private List<TransazioniMongo> transazioniPerTipo;
	private List<TransazioniMongo> transazioniMediePerCliente;
	private List<TransazioniMongo> importoTransazioniPerMese;
	private List<Conto> contiSaldo0;
}
