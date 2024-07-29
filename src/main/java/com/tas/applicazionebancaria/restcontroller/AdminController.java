package com.tas.applicazionebancaria.restcontroller;

import java.io.ByteArrayOutputStream;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.AuditLog;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.StatoPrestito;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoModificaAudit;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.AuditService;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.PagamentiService;
import com.tas.applicazionebancaria.service.PrestitiService;
import com.tas.applicazionebancaria.service.RichiestePrestitoService;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;
import com.tas.applicazionebancaria.service.TransazioniMongoService;
import com.tas.applicazionebancaria.service.TransazioniService;
import com.tas.applicazionebancaria.utils.JWT;
import com.tas.applicazionebancaria.utils.LoginRequest;
import com.tas.applicazionebancaria.utils.ServerResponse;
import com.tas.applicazionebancaria.utils.Statistiche;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RequestMapping(value = "/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
public class AdminController {
	@Autowired
	AmministratoreService adminService;
	@Autowired
	ClienteService clienteService;
	@Autowired
	ContoService contoService;
	@Autowired
	TransazioniService transazioniService;
	@Autowired
	TransazioniBancarieService transazioniBancarieService;
	@Autowired
	CarteDiCreditoService ccService;
	@Autowired
	PrestitiService prestitiService;
	@Autowired
	RichiestePrestitoService richiestePrestitoService;
	@Autowired
	PagamentiService pagamentiService;
	@Autowired
	TransazioniMongoService tmService;
	@Autowired
	RichiestePrestitoService rpService;
	@Autowired
	AmministratoreService asService;
	@Autowired
	AuditService as;

	private static boolean validateInputs(String nome, String cognome, String email, String password) {
		if (nome == null || cognome == null || email == null || password == null)
			return false;
		if (!nome.matches("^[a-zA-Z ,.'-]{2,30}$"))
			return false;
		if (!cognome.matches("^[a-zA-Z ,.'-]{2,30}$"))
			return false;
		if (!email.matches("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
			return false;
		if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?=])[a-zA-Z0-9@#&%^$?=]{8,32}$"))
			return false;
		return true;
	}

	@GetMapping("/clienti")
	public ServerResponse getClienti() {
		List<Cliente> clienti = clienteService.findAll();
		return new ServerResponse(0, clienti);
	}

	@GetMapping("/conti")
	public ServerResponse getConti() {
		List<Conto> conti = contoService.findConti0();
		System.out.println("sas");
		return new ServerResponse(0, conti);
	}

	@PostMapping("/clienti")
	public ServerResponse addCliente(@RequestBody Cliente cliente) {
		if (!validateInputs(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente(),
				cliente.getPasswordCliente()))
			return new ServerResponse(1, "Validazione fallita, riprova a inserire i dati.");

		if (clienteService.findByEmail(cliente.getEmailCliente()).isPresent())
			return new ServerResponse(1, "Cliente con email " + cliente.getEmailCliente() + " esiste.");

		cliente.setPasswordCliente(BCryptEncoder.encode(cliente.getPasswordCliente()));
		clienteService.saveCliente(cliente);
		return new ServerResponse(0, "Cliente aggiunto con successo.");
	}

	@GetMapping("/clienti/{id}")
	public ServerResponse getClienteById(@PathVariable long id) {
		Optional<Cliente> cliente = clienteService.findById(id);
		if (cliente.isEmpty())
			return new ServerResponse(1, "Il cliente con id " + id + " non esiste.");

		return new ServerResponse(0, cliente.get());
	}

	@GetMapping("/clienti/email/{email}")
	public ServerResponse getClienteByEmail(@PathVariable String email) {
		Optional<Cliente> cliente = clienteService.findByEmail(email);
		if (cliente.isEmpty()) {
			return new ServerResponse(1, "Il cliente con email " + email + " non esiste.");
		}
		return new ServerResponse(0, cliente.get());
	}

	@PostMapping("/clienti/lock")
	public ServerResponse lockUnlock(@RequestBody LoginRequest request,
			@CookieValue(name = "bearer", required = false) String token) {
		Optional<Cliente> cliente = clienteService.findByEmail(request.getEmail());

		Jws<Claims> claims = JWT.validate(token);
		String adminEmail = claims.getBody().getSubject();

		AuditLog audit = new AuditLog();
		String stringIdAdmin = String.valueOf(asService.findByEmail(adminEmail).get().getCodAdmin());
		audit.setCodAdmin(stringIdAdmin);
		audit.setData(new Date());
		audit.setTipo(TipoModificaAudit.DETTAGLI_CLIENTI);

		if (cliente.isEmpty()) {
			audit.setDettagli("Tentativo di blocco/sblocco cliente con email " + request.getEmail() + " non riuscito");
			as.saveAudit(audit);
			return new ServerResponse(1, "Il cliente con email " + request.getEmail() + " non esiste.");
		}

		Cliente c = cliente.get();
		if (c.isAccountBloccato()) {
			c.setAccountBloccato(false);
			audit.setDettagli("Sblocco account id n. " + cliente.get().getCodCliente());
		} else {
			audit.setDettagli("Blocco account id n. " + cliente.get().getCodCliente());
			c.setAccountBloccato(true);
		}

		clienteService.saveCliente(c);
		as.saveAudit(audit);
		return new ServerResponse(0, "Cliente" + request.getEmail() + " modificato con successo.");
	}

	@DeleteMapping("/conti/{id}")
	public ServerResponse deleteContoById(@PathVariable long id) {
		Optional<Conto> conto = contoService.findById(id);
		if (conto.isEmpty())
			return new ServerResponse(1, "Il conto con id " + id + " non esiste.");

		contoService.deleteConto(conto.get());
		return new ServerResponse(0, "Conto " + id + " eliminato con successo.");
	}

	@GetMapping("/statistiche")
	public ServerResponse getStatistiche() {
		Statistiche stat = new Statistiche();
		stat.setClienti(clienteService.findAll());
		stat.setTotAddebiti(tmService.findTotAddebiti().isEmpty() ? 0 : tmService.findTotAddebiti().get());
		stat.setTotAccrediti(tmService.findTotAccrediti().isEmpty() ? 0 : tmService.findTotAccrediti().get());
		stat.setTransazioniMediePerCliente(
				tmService.transazioniMediePerCliente().isEmpty() ? 0 : tmService.transazioniMediePerCliente().get());
		stat.setImportoTransazioniPerMese(
				tmService.importoTransazioniPerMese().isEmpty() ? null : tmService.importoTransazioniPerMese().get());
		return new ServerResponse(0, stat);
	}

	@GetMapping(value="/download-statistiche")
	public ResponseEntity<byte[]> downloadStatistiche() {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			// Creazione istanza pdf
			PdfWriter.getInstance(document, out);
			document.open();

			// Creazione contenuto PDF
			// Posso includere testo, tabelle, etc.
			document.addTitle("Statistiche Report");
			document.addSubject("Statistiche PDF");
			document.addKeywords("Statistiche, PDF, Java");
			document.addAuthor("Applicazione Bancaria");
			document.addCreator("Applicazione Bancaria");

			document.add(new Paragraph("STATISTICHE REPORT", new Font(FontFamily.HELVETICA, 24, Font.BOLD)));
			document.add(new Paragraph("Clienti totali: " + clienteService.findAll().size(),
					new Font(FontFamily.HELVETICA, 14)));

			// verifico che alcuni valori non siano null per non farmi lanciare un'eccezione
			// e un errore nell'aprire il file pdf
			if (tmService.findTotAddebiti().isEmpty()) {
				document.add(new Paragraph("Totali addebiti: 0", new Font(FontFamily.HELVETICA, 14)));
			} else {
				document.add(new Paragraph("Totali addebiti: " + tmService.findTotAddebiti().get(),
						new Font(FontFamily.HELVETICA, 14)));
			}

			if (tmService.findTotAccrediti().isEmpty()) {
				document.add(new Paragraph("Totali accrediti: 0", new Font(FontFamily.HELVETICA, 14)));
			} else {
				document.add(new Paragraph("Totali accrediti: " + tmService.findTotAccrediti().get(),
						new Font(FontFamily.HELVETICA, 14)));
			}

			if (tmService.transazioniMediePerCliente().isEmpty()) {
				document.add(new Paragraph("Transazioni medie per cliente: 0", new Font(FontFamily.HELVETICA, 14)));
			} else {
				document.add(
						new Paragraph("Transazioni medie per cliente: " + tmService.transazioniMediePerCliente().get(),
								new Font(FontFamily.HELVETICA, 14)));
			}

			if (tmService.importoTransazioniPerMese().isEmpty()) {
				document.add(new Paragraph("Importo transazioni per mese: non ci sono stati importi",
						new Font(FontFamily.HELVETICA, 14)));
			} else {

				// creo un tabella per gli importi transazioni per mese
				Optional<List<TransazioniMongo>> importiPerMese = tmService.importoTransazioniPerMese();
				System.err.println("Importi per mese -> " + importiPerMese);
				document.add(new Paragraph("Statistiche Transazioni per Mese:", new Font(FontFamily.HELVETICA, 14)));
				document.add(new Paragraph(" ")); // Aggiunge un'interlinea vuota

				PdfPTable table = new PdfPTable(2); // 2 colonne
				table.setWidthPercentage(100);
				table.setWidths(new int[] { 2, 3 }); // width per ogni colonna

				// headers tabella
				Font headerFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
				PdfPCell h1 = new PdfPCell(new Phrase("MESE", headerFont));
				PdfPCell h3 = new PdfPCell(new Phrase("Importo", headerFont));

				// tipo di align
				h1.setHorizontalAlignment(Element.ALIGN_CENTER);
				h3.setHorizontalAlignment(Element.ALIGN_CENTER);

				// accodo le celle alla tabella
				table.addCell(h1);
				table.addCell(h3);

				// itero la lista e stampo i valori
				for (TransazioniMongo transazione : importiPerMese.get()) {
					table.addCell(new PdfPCell(new Phrase(Month.of(Integer.valueOf(transazione.getId())).name(),
							new Font(FontFamily.HELVETICA, 14))));

					table.addCell(new PdfPCell(new Phrase(String.format("%.2f", transazione.getImporto()),
							new Font(FontFamily.HELVETICA, 14))));
				}
				document.add(table);
			}

			document.close();
		} 
		catch (DocumentException ex) {
			ex.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Set the headers and content type for the response
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistiche.pdf");

		return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
	}

	@GetMapping("/richiestePrestito")
	public ServerResponse richiestePrestito() {
		List<RichiestePrestito> richiestaPrestiti = rpService.findAll();
		return new ServerResponse(0, richiestaPrestiti);
	}

	@GetMapping("/approvaPrestito/{id}")
	public ServerResponse approvaPrestito(@PathVariable long id,
			@CookieValue(name = "bearer", required = false) String token) {

		Jws<Claims> claims = JWT.validate(token);
		String adminEmail = claims.getBody().getSubject();
		// audit logs
		AuditLog audit = new AuditLog();
		String stringIdAdmin = String.valueOf(asService.findByEmail(adminEmail).get().getCodAdmin());
		audit.setCodAdmin(stringIdAdmin);
		audit.setData(new Date());
		audit.setTipo(TipoModificaAudit.APPROVAZIONE_PRESTITI);

		Optional<RichiestePrestito> rp = rpService.findById(id);
		if (rp.isEmpty()) {
			audit.setDettagli("Tentativo di approvazione prestito n. " + id + " non riuscita");
			as.saveAudit(audit);
			return new ServerResponse(1, "Richiesta Prestito non valida");
		}
		rp.get().setStato(StatoPrestito.APPROVATO);
		// TODO salvare il prestito confermato nella tabella con approvato
		// Prestiti prestito = new Prestiti();
		// prestitiService.savePrestiti()
		rpService.saveRichiestePrestito(rp.get());

		audit.setDettagli("Approvato prestito n. " + rp.get().getCodRichiesta());
		as.saveAudit(audit);
		return new ServerResponse(0, "Approvata richiesta n. " + rp.get().getCodRichiesta());
	}

	@GetMapping("/declinaPrestito/{id}")
	public ServerResponse declinaPrestito(@PathVariable long id,
			@CookieValue(name = "bearer", required = false) String token) {
		Optional<RichiestePrestito> rp = rpService.findById(id);
		Jws<Claims> claims = JWT.validate(token);
		String adminEmail = claims.getBody().getSubject();
		// audit logs
		AuditLog audit = new AuditLog();
		String stringIdAdmin = String.valueOf(asService.findByEmail(adminEmail).get().getCodAdmin());
		audit.setCodAdmin(stringIdAdmin);
		audit.setData(new Date());
		audit.setTipo(TipoModificaAudit.APPROVAZIONE_PRESTITI);

		if (rp.isEmpty()) {
			audit.setDettagli("Tentativo di rifiuto prestito n. " + id + " non riuscito");
			as.saveAudit(audit);
			return new ServerResponse(1, "Richiesta Prestito non valida");
		}
		rp.get().setStato(StatoPrestito.RIFIUTATO);
		rpService.saveRichiestePrestito(rp.get());

		audit.setDettagli("Rifiutato prestito n. " + rp.get().getCodRichiesta());
		as.saveAudit(audit);
		return new ServerResponse(0, "Rifiutata richiesta n. " + rp.get().getCodRichiesta());
	}

	@PostMapping("/confermaNuovaPassword")
	public ServerResponse confermaNuovaPassword(@CookieValue(name = "bearer") String token,
			@RequestBody LoginRequest request) {
		Jws<Claims> claims = JWT.validate(token);
		String email = claims.getBody().getSubject();

		Amministratore a = asService.findByEmail(email).get();
		a.setPasswordAdmin(BCryptEncoder.encode(request.getPassword()));
		asService.saveAmministratore(a);
		return new ServerResponse(0, "Password salvata correttamente");
	}

	@GetMapping("/transazioni")
	public ServerResponse getTransazioni() {
		return new ServerResponse(0, transazioniService.findAll());
	}

	@GetMapping("/prestiti")
	public ServerResponse getPrestiti() {
		return new ServerResponse(0, richiestePrestitoService.findAll());
	}
}
