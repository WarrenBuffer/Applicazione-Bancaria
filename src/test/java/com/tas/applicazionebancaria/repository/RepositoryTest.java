package com.tas.applicazionebancaria.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.AuditLog;
import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.LogAccessiAdmin;
import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;
import com.tas.applicazionebancaria.businesscomponent.model.MovimentiContoMongo;
import com.tas.applicazionebancaria.businesscomponent.model.Pagamenti;
import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;
import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;
import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.MetodoPagamento;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.StatoPrestito;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoConto;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoModificaAudit;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoMovimento;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoTransazione;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.AuditService;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteMongoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.LogAccessiService;
import com.tas.applicazionebancaria.service.MovimentiContoMongoService;
import com.tas.applicazionebancaria.service.MovimentiContoService;
import com.tas.applicazionebancaria.service.PagamentiService;
import com.tas.applicazionebancaria.service.PrestitiService;
import com.tas.applicazionebancaria.service.RichiestePrestitoService;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;
import com.tas.applicazionebancaria.service.TransazioniMongoService;
import com.tas.applicazionebancaria.service.TransazioniService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
		locations = "classpath:test.properties")
class RepositoryTest {

	@Autowired
	AmministratoreService ams;
	@Autowired
	AuditService aus;
	@Autowired
	ClienteService cls;
	@Autowired
	CarteDiCreditoService ccs;
	@Autowired
	ContoService cs;
	@Autowired
	MovimentiContoService mcs;
	@Autowired
	PagamentiService ps;
	@Autowired
	PrestitiService prs;
	@Autowired
	RichiestePrestitoService rps;
	@Autowired
	TransazioniService ts;
	
	@Autowired
	TransazioniBancarieService tbs;
	@Autowired
	ClienteMongoService clms;
	@Autowired
	LogAccessiService las;
	@Autowired
	MovimentiContoMongoService mcms;
	@Autowired
	TransazioniMongoService tms;
	
	static Cliente cliente=new Cliente();
	static Amministratore amministratore =new Amministratore(); 
	static Conto conto=new Conto();
	static CarteDiCredito carta=new CarteDiCredito();
	static MovimentiConto movimento=new MovimentiConto();
	static Pagamenti pagamento=new Pagamenti();
	static Prestiti prestito=new Prestiti();
	static RichiestePrestito richiesta=new RichiestePrestito();
	static Transazioni transazione=new Transazioni();
	static TransazioniBancarie transazioneBancaria =new TransazioniBancarie();
	
	static ClienteMongo clienteMongo=new ClienteMongo();
	static MovimentiContoMongo movimentoMongo=new MovimentiContoMongo();
	static LogAccessiAdmin logAccessiAdmin =new LogAccessiAdmin();
	static TransazioniMongo transazioneMongo=new TransazioniMongo();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		amministratore.setAccountBloccato(false);
		amministratore.setCodAdmin(1);
		amministratore.setCognomeAdmin("Cognome");
		amministratore.setEmailAdmin("Email");
		amministratore.setNomeAdmin("Nome");
		amministratore.setPasswordAdmin("Password");
		amministratore.setTentativiErrati(0);
		
		Set<CarteDiCredito> carte=new HashSet<CarteDiCredito>();
		carta.setCvv("123");
		carta.setDataScadenza(new Date());
		carta.setNumeroCarta("123");
		carta.setCodCliente(1);
		carte.add(carta);
		
		Set<Conto> conti=new HashSet<Conto>();
		conto.setSaldo(0);
		conto.setTipoConto(TipoConto.CORRENTE);
		conto.setTransazioni(null);
		conto.setTransazioniBancarie(null);
		conto.setCodCliente(1);
		conti.add(conto);
		
		Set<Pagamenti> pagamenti=new HashSet<Pagamenti>();
		pagamento.setDataPagamento(new Date());
		pagamento.setImporto(1);
		pagamento.setMetodoPagamento(MetodoPagamento.BONIFICO);
		pagamento.setCodCliente(1);
		pagamenti.add(pagamento);
		
		Set<Prestiti> prestiti=new HashSet<Prestiti>();
		prestito.setDurataInMesi(1);
		prestito.setImporto(1);
		prestito.setTassoInteresse(1);
		prestito.setCodCliente(1);
		prestiti.add(prestito);
		
		cliente.setAccountBloccato(true);
		cliente.setCarte(carte);
		cliente.setCodCliente(1);
		cliente.setCognomeCliente("Cognome");
		cliente.setConti(conti);
		cliente.setEmailCliente("Email");
		cliente.setNomeCliente("Nome");
		cliente.setPagamenti(pagamenti);
		cliente.setPasswordCliente("Password");
		cliente.setPrestiti(prestiti);
		cliente.setTentativiErrati(1);
		
		movimento.setCodConto(1);
		movimento.setDataMovimento(new Date());
		movimento.setImporto(100);
		movimento.setTipoMovimento(TipoMovimento.ACCREDITO);
		
		richiesta.setCodCliente(1);
		richiesta.setDataRichiesta(new Date());
		richiesta.setImporto(100);
		richiesta.setStato(StatoPrestito.APPROVATO);
		
		transazione.setCodConto(1);
		transazione.setDataTransazione(new Date());
		transazione.setImporto(100D);
		transazione.setTipoTransazione(TipoTransazione.ACCREDITO);
		
		transazioneBancaria.setContoDestinazione(1);
		transazioneBancaria.setContoOrigine(1);
		transazioneBancaria.setDataTransazione(new Date(1234567890L));
		transazioneBancaria.setImporto(100D);
		transazioneBancaria.setTipoTransazione(TipoTransazione.ADDEBITO);
		
		
		
		clienteMongo.setCognomeCliente("Cognome");
		clienteMongo.setEmailCliente("Email");
		clienteMongo.setNomeCliente("Nome");
		clienteMongo.setPasswordCliente("Password");
		clienteMongo.setSaldo(100);
		
		logAccessiAdmin.setCodAdmin("1");
		logAccessiAdmin.setData(new Date());
		logAccessiAdmin.setDettagli("Dettagli");
		
		movimentoMongo.setCodOperazione("1");
		movimentoMongo.setCodContoDestinazione(1);
		movimentoMongo.setCodContoOrigine(1);
		movimentoMongo.setDataOperazione(new Date());
		movimentoMongo.setImporto(100);
		movimentoMongo.setTipoOperazione(TipoMovimento.ACCREDITO);
		
		transazioneMongo.setCodiceConto(0);
		transazioneMongo.setCodTransazione(0);
		transazioneMongo.setDataTransazione(new Date());
		transazioneMongo.setImporto(100000000);
		transazioneMongo.setTipoTransazione(TipoTransazione.ACCREDITO);
	}

	@Test
	@Transactional
	void AmministratoreServiceTest() throws SQLException {
		amministratore=ams.saveAmministratore(amministratore);
		assertTrue(ams.findById(amministratore.getCodAdmin()).isPresent());
		assertTrue(ams.findByEmail(amministratore.getEmailAdmin()).isPresent());
		assertFalse(ams.findAll().isEmpty());
		ams.deleteAmministratore(amministratore);
		assertFalse(ams.findById(amministratore.getCodAdmin()).isPresent());
		
		AuditLog auditLog=new AuditLog();
		auditLog.setCodAdmin(String.valueOf(amministratore.getCodAdmin()));
		auditLog.setData(new Date());
		auditLog.setTipo(TipoModificaAudit.APPROVAZIONE_PRESTITI);
		auditLog.setDettagli("Dettagli");
		
		aus.saveAudit(auditLog);
		assertFalse(aus.findAll().isEmpty());
	}
	
	@Test
	@Transactional
	void ClienteServiceTest() throws SQLException {
		/*-----------------------------------Cliente-----------------------------------*/
		cliente=cls.saveCliente(cliente);
		assertFalse(cls.findAll().isEmpty());
		assertTrue(cls.findById(cliente.getCodCliente()).isPresent());
		assertTrue(cls.findByEmail(cliente.getEmailCliente()).isPresent());
		cls.findAll().forEach(System.out::println);
		assertEquals(1, cls.count());
		cls.deleteCliente(cliente);
		assertTrue(cls.findAll().isEmpty());
		/*-----------------------------------Carte-----------------------------------*/
		carta=ccs.saveCarteDiCredito(carta);
		assertFalse(ccs.findAll().isEmpty());
		assertTrue(ccs.findById(carta.getCodCarta()).isPresent());
		assertFalse(ccs.findByCodCliente(1).isEmpty());
		assertEquals(ccs.findNumCarteByCodCliente(1), 1);
		ccs.deleteCarteDiCredito(carta);
		assertTrue(ccs.findAll().isEmpty());
		/*-----------------------------------Conto-----------------------------------*/
		conto=cs.saveConto(conto);
		assertFalse(cs.findAll().isEmpty());
		assertTrue(cs.findById(conto.getCodConto()).isPresent());
		assertNotEquals(10, cs.findSaldoMedio());
		assertEquals(1, cs.findNumContiByCodCliente(1));
		assertFalse(cs.findConti0().isEmpty());
		assertFalse(cs.findByIdCliente(1).isEmpty());
		cs.deleteConto(conto);
		assertTrue(cs.findAll().isEmpty());
		/*-----------------------------------Pagamenti-----------------------------------*/
		pagamento=ps.savePagamenti(pagamento);
		assertFalse(ps.findAll().isEmpty());
		assertTrue(ps.findById(pagamento.getCodPagamento()).isPresent());
		assertEquals(1, ps.findTotPagamentiByCodCliente(1));
		assertFalse(ps.findUltimi10Pagamenti(1).isEmpty());
		ps.deletePagamenti(pagamento);
		assertTrue(ps.findAll().isEmpty());
		/*-----------------------------------Prestiti-----------------------------------*/
		prestito=prs.savePrestiti(prestito);
		assertFalse(prs.findAll().isEmpty());
		assertTrue(prs.findById(prestito.getCodPrestito()).isPresent());
		assertFalse(prs.findByCodCliente(1).isEmpty());
		prs.deletePrestiti(prestito);
		assertTrue(prs.findAll().isEmpty());
	}

	@Test
	@Transactional
	void RichiesteServiceTest() throws SQLException{
		richiesta=rps.saveRichiestePrestito(richiesta);
		assertFalse(rps.findAll().isEmpty());
		assertTrue(rps.findById(richiesta.getCodRichiesta()).isPresent());
		assertFalse(rps.findByCodCliente(1).isEmpty());
		rps.deleteRichiestePrestito(richiesta);
		assertTrue(rps.findAll().isEmpty());
	}
	
	@Test
	@Transactional
	void MovimentoServiceTest() throws SQLException{
		movimento=mcs.saveMovimentiConto(movimento);
		assertFalse(mcs.findAll().isEmpty());
		assertTrue(mcs.findById(movimento.getCodMovimento()).isPresent());
		assertFalse(mcs.findUltimi10(1).isEmpty());
		mcs.deleteMovimentiConto(movimento);
		assertTrue(mcs.findAll().isEmpty());
	}
	
	@Test
	@Transactional
	void TransazioniServiceTest() throws SQLException{
		transazione=ts.saveTransazioni(transazione);
		assertFalse(ts.findAll().isEmpty());
		assertTrue(ts.findById(transazione.getCodTransazione()).isPresent());
		ts.deleteTransazioni(transazione);
		assertTrue(ts.findAll().isEmpty());
	}
	
	@Test
	@Transactional
	void TransazioniBancarieServiceTest() throws SQLException{
		transazioneBancaria=tbs.saveTransazioniBancarie(transazioneBancaria);
		assertFalse(tbs.findAll().isEmpty());
		assertTrue(tbs.findById(transazioneBancaria.getCodTransazioneBancaria()).isPresent());
		assertEquals(tbs.findNumTransazioni(), 1);
		assertEquals(tbs.findSommaImporti(), 100D);
		assertFalse(tbs.findUltime10(1).isEmpty());
		tbs.deleteTransazioniBancarie(transazioneBancaria);
		assertTrue(tbs.findAll().isEmpty());
	}
	
	@Test
	void ClienteMongoServiceTest() throws SQLException{
		clienteMongo=clms.saveClienteMongo(clienteMongo);
		assertFalse(clms.findAll().isEmpty());
		assertTrue(clms.findById(clienteMongo.getId()).isPresent());
		assertTrue(clms.findByEmail(clienteMongo.getEmailCliente()).isPresent());
		clms.deleteClienteMongo(clienteMongo);
		assertTrue(clms.findById(clienteMongo.getId()).isEmpty());
	}
	
	@Test
	void LogAccessiServiceTest() throws SQLException{
		logAccessiAdmin=las.saveLogAccesso(logAccessiAdmin);
		assertFalse(las.findAll().isEmpty());
	}
	
	
	@Test
	void MovimentiMongoServiceTest() throws SQLException{
		movimentoMongo=mcms.saveMovimentiContoMongo(movimentoMongo);
		assertFalse(mcms.findAll().isEmpty());
		assertTrue(mcms.findById(movimentoMongo.getId()).isPresent());
		mcms.deleteMovimentiContoMongo(movimentoMongo);
		assertTrue(mcms.findById(movimentoMongo.getId()).isEmpty());
	}
	
	@Test
	void TransazioniMongoServiceTest() throws SQLException{
		transazioneMongo=tms.saveTransazioniMongo(transazioneMongo);
		assertFalse(tms.findAll().isEmpty());
		assertTrue(tms.findById(transazioneMongo.getId()).isPresent());
		assertNotEquals(0, tms.transazioniMediePerCliente());
		assertFalse(tms.importoTransazioniPerMese().isEmpty());
		assertNotEquals(0, tms.findTotAccrediti());
		assertNotEquals(0, tms.findTotAddebiti());
		tms.deleteTransazioniMongo(transazioneMongo);
		assertTrue(tms.findById(transazioneMongo.getId()).isEmpty());
	}
	
	
}
