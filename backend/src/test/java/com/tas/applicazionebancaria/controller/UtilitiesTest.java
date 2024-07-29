package com.tas.applicazionebancaria.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;

public class UtilitiesTest {

    @Test
    void validateInputsTestNomeNull() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente(null); 
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo nome non può essere vuoto", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestNomeEmpty() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente(""); 
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo nome non può essere vuoto", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestNomeInvalid() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("M"); 
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo nome deve contenere solo lettere (da 2 a 30 caratteri)", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestCognomeNull() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente(null); 
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo cognome non può essere vuoto", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestCognomeEmpty() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente(""); 
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo cognome non può essere vuoto", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestCognomeInvalid() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente("R"); 
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo cognome deve contenere solo lettere (da 2 a 30 caratteri)", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestEmailNull() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente(null); 
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo email non può essere vuoto", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestEmailEmpty() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente(""); 
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo email non può essere vuoto", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestEmailInvalid() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente("mario.rossi"); 
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("Il campo email deve essere un indirizzo email valido", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestPasswordNull() {
        Cliente clientePasswordNull = new Cliente();
        clientePasswordNull.setNomeCliente("Mario");
        clientePasswordNull.setCognomeCliente("Rossi");
        clientePasswordNull.setEmailCliente("mario.rossi@email.com");
        clientePasswordNull.setPasswordCliente(null); 
        
        assertEquals("Il campo password non può essere vuoto", ClientController.validateInputs(clientePasswordNull));
    }

    @Test
    void validateInputsTestPasswordEmpty() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente(""); 
        
        assertEquals("Il campo password non può essere vuoto", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestPasswordInvalid() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("password"); 
        
        assertEquals("Il campo password deve rispettare i criteri di complessità", ClientController.validateInputs(cliente));
    }

    @Test
    void validateInputsTestValid() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Mario");
        cliente.setCognomeCliente("Rossi");
        cliente.setEmailCliente("mario.rossi@email.com");
        cliente.setPasswordCliente("Pass01?");
        
        assertEquals("ok", ClientController.validateInputs(cliente));
    }
	
	/*-----------------------------------------CHECK ESCAPE TEST-----------------------------------------*/
	
	@Test
	void checkEscapeHTML() {
		assertEquals("&lt;abc", ClientController.checkEscapeHTML("<abc"));
		assertEquals("&gt;abc", ClientController.checkEscapeHTML(">abc"));
		assertEquals("&amp;abc", ClientController.checkEscapeHTML("&abc"));
		assertEquals("&quot;abc", ClientController.checkEscapeHTML("\"abc"));
		assertEquals("&#x27;abc", ClientController.checkEscapeHTML("\'abc"));
		assertEquals("&#x2F;abc", ClientController.checkEscapeHTML("/abc"));
	}
	
	

}
