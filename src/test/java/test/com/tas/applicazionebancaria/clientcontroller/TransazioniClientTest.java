package test.com.tas.applicazionebancaria.clientcontroller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;
import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;
import com.tas.applicazionebancaria.service.ClienteMongoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.MovimentiContoService;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;
import com.tas.applicazionebancaria.service.TransazioniMongoService;
import com.tas.applicazionebancaria.service.TransazioniService;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class TransazioniClientTest {

	MockedStatic<JWT> mockedStatic;
	static String token = JWT.generate("gabriele", "vazzana", "vazzanagabriele4@gmail.com");

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClienteService clienteService;

	@MockBean
	private ContoService contoService;

	@MockBean
	private ClienteMongoService cmService;

	@MockBean
	private MovimentiContoService mcService;

	@MockBean
	private TransazioniService transazioneService;

	@MockBean
	private TransazioniBancarieService transazioniBancarieService;

	@MockBean
	private TransazioniMongoService transazioneServiceM;

	private Cliente cliente;
	private ClienteMongo clienteM;
	private Conto conto;
	private MovimentiConto mc;
	private Transazioni t;
	private TransazioniMongo transazioneM;

	@BeforeEach
	void setUp() throws Exception {
		mockedStatic = mockStatic(JWT.class);
		/*
		 * cliente = new Cliente(); cliente.setCodCliente(802);
		 * cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		 * cliente.setPasswordCliente(new BCryptPasswordEncoder().encode("Aags1127!"));
		 * cliente.setNomeCliente("gabriele"); cliente.setCognomeCliente("vazzana");
		 * 
		 * clienteM = new ClienteMongo(); clienteM.setCodCliente(802);
		 * clienteM.setCognomeCliente("vazzana");
		 * clienteM.setEmailCliente("vazzanagabriele4@gmail.com");
		 * clienteM.setNomeCliente("gabriele"); clienteM.setPasswordCliente(new
		 * BCryptPasswordEncoder().encode("Aags1127!")); clienteM.setSaldo(2000);
		 * 
		 * conto = new Conto(); conto.setCodCliente(802); conto.setCodConto(52);
		 * conto.setEmailCliente("vazzanagabriele4@gmail.com"); conto.setSaldo(2000);
		 * conto.setTipoConto(TipoConto.CORRENTE);
		 * 
		 * mc = new MovimentiConto(); mc.setCodConto(52); mc.setCodMovimento(1);
		 * mc.setDataMovimento(new Date()); mc.setImporto(2000);
		 * mc.setTipoMovimento(TipoMovimento.ACCREDITO);
		 * 
		 * transazione = new Transazioni(); transazione.setCodConto(52);
		 * transazione.setCodTransazione(1); transazione.setDataTransazione(new Date());
		 * transazione.setImporto(2000);
		 * transazione.setTipoTransazione(TipoTransazione.ACCREDITO);
		 * 
		 * transazioneM = new TransazioniMongo(); transazioneM.setCodiceConto(52);
		 * transazioneM.setCodTransazione(1); transazioneM.setDataTransazione(new
		 * Date()); transazioneM.setImporto(2000);
		 * transazioneM.setTipoTransazione(TipoTransazione.ACCREDITO);
		 */
	}

	@AfterEach
	public void teardown() {
		mockedStatic.close();
	}

	// TODO POST_DEPOSITO_TOKEN_VALIDO_CONTO_ASSENTE
	/************** POST_DEPOSITO_TOKEN_VALIDO_CONTO_ASSENTE **************/
	@Test
	void testDepositoTokenValidoContoAssente() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		// Mock JWT validation
		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(contoService.findById(52)).thenReturn(Optional.empty());
		mockMvc.perform(
				post("/deposito").param("codConto", "2").param("importo", "100.0").cookie(new Cookie("token", token)))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/home"));
	}

	// TODO POST_DEPOSITO_TOKEN_VALIDO_OGGETTI_PRESENTI
	/************** POST_DEPOSITO_TOKEN_VALIDO_OGGETTI_PRESENTI **************/
	@Test
	void testDepositoTokenValidoOggettiPresenti() throws Exception {
		// Initialize your test data objects with real values
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setEmailCliente("vazzanagabriele4@gmail.com");
		clienteM.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		mc = new MovimentiConto();
		mc.setCodConto(52);
		mc.setImporto(100.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(100.0);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		// Mock JWT validation
		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		// Configure the mock service responses
		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(conto);

		when(mcService.saveMovimentiConto(Mockito.any(MovimentiConto.class))).thenReturn(mc);
		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		Set<Conto> conti = new HashSet<Conto>();
		conti.add(conto);
		cliente.setConti(conti);

		List<MovimentiConto> movimentiConto = Arrays.asList(mc, new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<TransazioniBancarie> listaTransazioni = Arrays.asList(new TransazioniBancarie(),
				new TransazioniBancarie());
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		// Simulate the deposit request with the valid token
		mockMvc.perform(
				post("/deposito").param("codConto", "52").param("importo", "100.0").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(view().name("visualizzaconti"))
				.andExpect(model().attributeExists("nome"))
				.andExpect(model().attribute("nome", mockClaimsJws.getBody().get("nome")))
				.andExpect(model().attributeExists("cliente")).andExpect(model().attribute("cliente", cliente))
				.andExpect(model().attributeExists("listaConti"))
				.andExpect(model().attribute("listaConti", cliente.getConti()))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(model().attributeExists("listaMovimenti"))
				.andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeExists("listaTransazioni"))
				.andExpect(model().attribute("listaTransazioni", listaTransazioni))
				.andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Deposito andato a buon fine!"));
	}

	// TODO POST_DEPOSITO_TOKEN_VALIDO_SENZA_LISTACONTI_E_CONTO
	/**************
	 * POST_DEPOSITO_TOKEN_VALIDO_SENZA_LISTACONTI_E_CONTO
	 **************/
	@Test
	void testDepositoTokenValidoSenzaListaContiEConto() throws Exception {
		// Initialize your test data objects with real values
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setEmailCliente("vazzanagabriele4@gmail.com");
		clienteM.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		mc = new MovimentiConto();
		mc.setCodConto(52);
		mc.setImporto(100.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(100.0);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		// Mock JWT validation
		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		// Configure the mock service responses
		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(conto);

		when(mcService.saveMovimentiConto(Mockito.any(MovimentiConto.class))).thenReturn(mc);
		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		cliente.setConti(new HashSet<Conto>());

		List<MovimentiConto> movimentiConto = Arrays.asList(mc, new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<TransazioniBancarie> listaTransazioni = Arrays.asList(new TransazioniBancarie(),
				new TransazioniBancarie());
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		// Simulate the deposit request with the valid token
		mockMvc.perform(
				post("/deposito").param("codConto", "52").param("importo", "100.0").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(view().name("visualizzaconti"))
				.andExpect(model().attributeExists("nome"))
				.andExpect(model().attribute("nome", mockClaimsJws.getBody().get("nome")))
				.andExpect(model().attributeExists("cliente")).andExpect(model().attribute("cliente", cliente))
				.andExpect(model().attributeDoesNotExist("listaConti"))
				.andExpect(model().attributeDoesNotExist("conto"))
				.andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeExists("listaTransazioni"))
				.andExpect(model().attribute("listaTransazioni", listaTransazioni))
				.andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Deposito andato a buon fine!"));
	}

	// TODO POST_DEPOSITO_TOKEN_VALIDO_SENZA_LISTAMOVIMENTI
	/************** POST_DEPOSITO_TOKEN_VALIDO_SENZA_LISTAMOVIMENTI **************/
	@Test
	void testDepositoTokenValidoSenzaListaMovimenti() throws Exception {
		// Initialize your test data objects with real values
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setEmailCliente("vazzanagabriele4@gmail.com");
		clienteM.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		mc = new MovimentiConto();
		mc.setCodConto(52);
		mc.setImporto(100.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(100.0);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		// Mock JWT validation
		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		// Configure the mock service responses
		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(conto);

		when(mcService.saveMovimentiConto(Mockito.any(MovimentiConto.class))).thenReturn(mc);
		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		Set<Conto> conti = new HashSet<Conto>();
		conti.add(conto);
		cliente.setConti(conti);

		List<MovimentiConto> movimentiConto = Collections.emptyList();
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<TransazioniBancarie> listaTransazioni = Arrays.asList(new TransazioniBancarie(),
				new TransazioniBancarie());
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		// Simulate the deposit request with the valid token
		mockMvc.perform(
				post("/deposito").param("codConto", "52").param("importo", "100.0").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(view().name("visualizzaconti"))
				.andExpect(model().attributeExists("nome"))
				.andExpect(model().attribute("nome", mockClaimsJws.getBody().get("nome")))
				.andExpect(model().attributeExists("cliente")).andExpect(model().attribute("cliente", cliente))
				.andExpect(model().attributeExists("listaConti"))
				.andExpect(model().attribute("listaConti", cliente.getConti()))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(model().attributeDoesNotExist("listaMovimenti"))
				.andExpect(model().attributeExists("listaTransazioni"))
				.andExpect(model().attribute("listaTransazioni", listaTransazioni))
				.andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Deposito andato a buon fine!"));
	}

	// TODO POST_DEPOSITO_TOKEN_VALIDO_SENZA_LISTATRANSAZIONI
	/**************
	 * POST_DEPOSITO_TOKEN_VALIDO_SENZA_LISTATRANSAZIONI
	 **************/
	@Test
	void testDepositoTokenValidoSenzaListaTransazioni() throws Exception {
		// Initialize your test data objects with real values
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setEmailCliente("vazzanagabriele4@gmail.com");
		clienteM.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		mc = new MovimentiConto();
		mc.setCodConto(52);
		mc.setImporto(100.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(100.0);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		// Mock JWT validation
		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		// Configure the mock service responses
		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(conto);

		when(mcService.saveMovimentiConto(Mockito.any(MovimentiConto.class))).thenReturn(mc);
		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		Set<Conto> conti = new HashSet<Conto>();
		conti.add(conto);
		cliente.setConti(conti);

		List<MovimentiConto> movimentiConto = Arrays.asList(mc, new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<TransazioniBancarie> listaTransazioni = Collections.emptyList();
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		// Simulate the deposit request with the valid token
		mockMvc.perform(
				post("/deposito").param("codConto", "52").param("importo", "100.0").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(view().name("visualizzaconti"))
				.andExpect(model().attributeExists("nome"))
				.andExpect(model().attribute("nome", mockClaimsJws.getBody().get("nome")))
				.andExpect(model().attributeExists("cliente")).andExpect(model().attribute("cliente", cliente))
				.andExpect(model().attributeExists("listaConti"))
				.andExpect(model().attribute("listaConti", cliente.getConti()))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(model().attributeExists("listaMovimenti"))
				.andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeDoesNotExist("listaTransazioni"))
				.andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Deposito andato a buon fine!"));
	}

	// TODO POST_PRELIEVO_TOKEN_VALIDO_CONTO_ASSENTE
	/************** POST_PRELIEVO_TOKEN_VALIDO_CONTO_ASSENTE **************/
	@Test
	void testPrelievoTokenValidoContoAssente() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		// Mock JWT validation Jws<Claims>
		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(contoService.findById(52)).thenReturn(Optional.empty());

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "100.0").cookie(new Cookie("token", token)))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/home"));
	}

	// TODO POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_OGGETTI_PRESENTI
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_OGGETTI_PRESENTI
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoNonSufficienteOggettiPresenti() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));

		List<MovimentiConto> movimentiConto = Arrays.asList(new MovimentiConto(), new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Arrays.asList(conto, new Conto());
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Impossibile prelevare questa cifra! Saldo insufficiente."))
				.andExpect(model().attributeExists("listaMovimenti"))
				.andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeExists("listaConti")).andExpect(model().attribute("listaConti", listaConti))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(view().name("visualizzaconti"));
	}

	// TODO POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_LISTA_MOVIMENTI_ASSENTI
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_LISTA_MOVIMENTI_ASSENTI
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoNonSufficienteListaMovimentiAssenti() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));

		List<MovimentiConto> movimentiConto = Collections.emptyList();
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Arrays.asList(conto, new Conto());
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Impossibile prelevare questa cifra! Saldo insufficiente."))
				.andExpect(model().attributeDoesNotExist("listaMovimenti"))
				.andExpect(model().attributeExists("listaConti")).andExpect(model().attribute("listaConti", listaConti))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(view().name("visualizzaconti"));
	}

	// TODO POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_LISTA_CONTI_ASSENTI
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_LISTA_CONTI_ASSENTI
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoNonSufficienteListaContiAssenti() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));

		List<MovimentiConto> movimentiConto = Arrays.asList(new MovimentiConto(), new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Collections.emptyList();
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Impossibile prelevare questa cifra! Saldo insufficiente."))
				.andExpect(model().attributeExists("listaMovimenti"))
				.andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeDoesNotExist("listaConti"))
				.andExpect(model().attributeDoesNotExist("conto")).andExpect(view().name("visualizzaconti"));
	}

	// TODO POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_OGGETTI_ASSENTI
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_NON_SUFFICIENTE_OGGETTI_ASSENTI
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoNonSufficienteOggettiAssenti() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));

		List<MovimentiConto> movimentiConto = Collections.emptyList();
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Collections.emptyList();
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Impossibile prelevare questa cifra! Saldo insufficiente."))
				.andExpect(model().attributeDoesNotExist("listaMovimenti"))
				.andExpect(model().attributeDoesNotExist("listaConti"))
				.andExpect(model().attributeDoesNotExist("conto")).andExpect(view().name("visualizzaconti"));
	}

	// TODO POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_PRESENTI
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_PRESENTI
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoSufficienteOggettiPresenti() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setNomeCliente("gabriele");
		clienteM.setEmailCliente(cliente.getEmailCliente());

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(500);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(new Conto());

		List<MovimentiConto> movimentiConto = Arrays.asList(new MovimentiConto(), new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Arrays.asList(conto, new Conto());
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		List<TransazioniBancarie> listaTransazioni = Arrays.asList(new TransazioniBancarie(),
				new TransazioniBancarie());
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(model().attributeExists("listaMovimenti"))
				.andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeExists("listaConti")).andExpect(model().attribute("listaConti", listaConti))
				.andExpect(model().attributeExists("listaTransazioni"))
				.andExpect(model().attribute("listaTransazioni", listaTransazioni))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Prelievo andato a buon fine!"))
				.andExpect(view().name("visualizzaconti"));
	}

	// TODO POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_LISTA_CONTI_ASSENTE
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_PRESENTI
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoSufficienteListaContiAssente() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setNomeCliente("gabriele");
		clienteM.setEmailCliente(cliente.getEmailCliente());

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(500);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(new Conto());

		List<MovimentiConto> movimentiConto = Arrays.asList(new MovimentiConto(), new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Collections.emptyList();
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		List<TransazioniBancarie> listaTransazioni = Arrays.asList(new TransazioniBancarie(),
				new TransazioniBancarie());
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(model().attributeExists("listaMovimenti"))
				.andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeDoesNotExist("listaConti"))
				.andExpect(model().attributeExists("listaTransazioni"))
				.andExpect(model().attribute("listaTransazioni", listaTransazioni))
				.andExpect(model().attributeDoesNotExist("conto")).andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Prelievo andato a buon fine!"))
				.andExpect(view().name("visualizzaconti"));
	}

	// TODO
	// POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_LISTA_MOVIMENTI_ASSENTE
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_PRESENTI_LISTA_MOVIMENTI_ASSENTE
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoSufficienteListaMovimentiAssente() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setNomeCliente("gabriele");
		clienteM.setEmailCliente(cliente.getEmailCliente());

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(500);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(new Conto());

		List<MovimentiConto> movimentiConto = Collections.emptyList();
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Arrays.asList(conto, new Conto());
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		List<TransazioniBancarie> listaTransazioni = Arrays.asList(new TransazioniBancarie(),
				new TransazioniBancarie());
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk()).andExpect(model().attributeDoesNotExist("listaMovimenti"))
				.andExpect(model().attributeExists("listaConti")).andExpect(model().attribute("listaConti", listaConti))
				.andExpect(model().attributeExists("listaTransazioni"))
				.andExpect(model().attribute("listaTransazioni", listaTransazioni))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Prelievo andato a buon fine!"))
				.andExpect(view().name("visualizzaconti"));
	}

	// TODO
	// POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_LISTA_TRANSAZIONI_ASSENTE
	/**************
	 * POST_PRELEVA_TOKEN_VALIDO_SALDO_SUFFICIENTE_OGGETTI_PRESENTI_LISTA_TRANSAZIONE_ASSENTE
	 **************/
	@Test
	void testPrelievoTokenValidoSaldoSufficienteListaTransazioniAssente() throws Exception {
		cliente = new Cliente();
		cliente.setCodCliente(802);
		cliente.setEmailCliente("vazzanagabriele4@gmail.com");
		cliente.setNomeCliente("gabriele");

		clienteM = new ClienteMongo();
		clienteM.setCodCliente(802);
		clienteM.setNomeCliente("gabriele");
		clienteM.setEmailCliente(cliente.getEmailCliente());

		conto = new Conto();
		conto.setCodConto(52);
		conto.setSaldo(2000.0);

		t = new Transazioni();
		t.setCodConto(52);
		t.setImporto(500);

		transazioneM = new TransazioniMongo();
		transazioneM.setCodiceConto(52);
		transazioneM.setImporto(100.0);

		Jws<Claims> mockClaimsJws = mock(Jws.class);
		Claims claims = Mockito.mock(Claims.class);
		when(claims.get("nome")).thenReturn(cliente.getNomeCliente());
		when(claims.getSubject()).thenReturn(cliente.getEmailCliente());
		when(mockClaimsJws.getBody()).thenReturn(claims);
		when(JWT.validate(token)).thenReturn(mockClaimsJws);

		when(clienteService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		when(cmService.findByEmail("vazzanagabriele4@gmail.com")).thenReturn(Optional.of(clienteM));
		when(contoService.findById(52)).thenReturn(Optional.of(conto));
		when(contoService.saveConto(conto)).thenReturn(new Conto());

		List<MovimentiConto> movimentiConto = Arrays.asList(new MovimentiConto(), new MovimentiConto());
		when(mcService.findUltimi10(conto.getCodConto())).thenReturn(movimentiConto);

		List<Conto> listaConti = Arrays.asList(conto, new Conto());
		when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaConti);

		when(transazioneService.saveTransazioni(Mockito.any(Transazioni.class))).thenReturn(t);
		when(transazioneServiceM.saveTransazioniMongo(Mockito.any(TransazioniMongo.class))).thenReturn(transazioneM);

		List<TransazioniBancarie> listaTransazioni = Collections.emptyList();
		when(transazioniBancarieService.findUltime10(conto.getCodConto())).thenReturn(listaTransazioni);

		mockMvc.perform(
				post("/preleva").param("codConto", "52").param("importo", "500").cookie(new Cookie("token", token)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("listaMovimenti")).andExpect(model().attribute("listaMovimenti", movimentiConto))
				.andExpect(model().attributeExists("listaConti")).andExpect(model().attribute("listaConti", listaConti))
				.andExpect(model().attributeDoesNotExist("listaTransazioni"))
				.andExpect(model().attributeExists("conto")).andExpect(model().attribute("conto", conto))
				.andExpect(model().attributeExists("success"))
				.andExpect(model().attribute("success", "Prelievo andato a buon fine!"))
				.andExpect(view().name("visualizzaconti"));
	}

}
