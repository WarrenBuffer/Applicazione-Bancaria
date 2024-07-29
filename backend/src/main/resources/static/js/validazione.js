// Funzione che si avvia quando viene caricata la pagina
$(document).ready(function() {
	// Bisogna fare riferimento all'id del form con il bootstrapValidator
	$('#clienteForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh',
		},
		// Recupero degli id del form per ogni input
		fields: {
			nomeCliente: {
				container: '#infoNome',
				validators: {
					notEmpty: {
						message: 'Il campo nome non può essere vuoto'
					},
					regexp: {
						// Espressione regolare
						// Dalla a alla Z inclusi .'- e lunghezza da 2 a 30
						regexp: /^[a-zA-Z ,.'-]{2,30}$/,
						message: 'Da 2 a 30 caratteri (Solo lettere)'
					}
				}
			},
			cognomeCliente: {
				container: '#infoCognome',
				validators: {
					notEmpty: {
						message: 'Il campo cognome non può essere vuoto'
					},
					regexp: {
						regexp: /^[a-zA-Z ,.'-]{2,30}$/,
						message: 'Da 2 a 30 caratteri (Solo lettere)'
					}
				}
			},
			passwordCliente: {
				container: '#infoPassword',
				validators: {
					notEmpty: {
						message: 'Il campo password non può essere vuoto'
					},
					regexp: {
						regexp: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?!=])[a-zA-Z0-9@#&%^$?!=]{7,15}$/,
						message: 'Lunghezza da 7 a 15 con almeno un carattere maiuscolo, uno minuscolo e un carattere speciale(@#&%^$?!=)'
					}
				}
			},
			emailCliente: {
				container: '#infoEmail',
				validators: {
					notEmpty: {
						message: 'Il campo email non può essere vuoto'
					},
					regexp: {
						//\w significa qualsiasi carattere, qualsiasi numero e underscore
						regexp: /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/,
						message: 'Inserire un email valida'
					}
				}
			},
		}
	});
});