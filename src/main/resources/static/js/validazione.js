// Funzione che si avvia quando viene caricata la pagina
$(document).ready(function() {
	// Bisogna fare riferimento all'id del form con il bootstrapValidator
	$('#utenteForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh',
		},
		// Recupero degli id del form per ogni input
		fields: {
			nome: {
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
			cognome: {
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
			indirizzo: {
				container: '#infoIndirizzo',
				validators: {
					notEmpty: {
						message: 'Il campo indirizzo non può essere vuoto'
					},
					regexp: {
						//specifico i numeri che posso mettere
						regexp: /^[a-zA-Z ,.'-]{6,45}[0-9]{1,4}$/,
						message: 'Inserire Via | Piazza | Viale e numero civico'
					}
				}
			},
			cap: {
				container: '#infoCap',
				validators: {
					notEmpty: {
						message: 'Il campo cap non può essere vuoto'
					},
					regexp: {
						regexp: /^[0-9]{5}$/,
						message: 'Inserire 5 cifre per il CAP'
					}
				}
			},
			data: {
				container: '#infoNascita',
				validators: {
					notEmpty: {
						message: 'Il campo nascita non può essere vuoto'
					},
					//utlizzo il validatore del datepicker
					date: {
						format: 'YYYY/MM/DD',
						message: 'Inserire una data valida. Formato YYYY/MM/DD'
					}
				}
			},
			username: {
				container: '#infoUsername',
				validators: {
					notEmpty: {
						message: 'Il campo username nascita non può essere vuoto'
					},
					regexp: {
						regexp: /^[a-zA-Z0-9.-]{4,10}$/,
						message: 'Da 4 a 10 caratteri (Lettere e numeri)'
					}
				}
			},
			password: {
				container: '#infoPassword',
				validators: {
					notEmpty: {
						message: 'Il campo password nascita non può essere vuoto'
					},
					regexp: {
						regexp: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^?!$=])[a-zA-Z0-9@#&%^?!$=]{7,15}$/,
						message: 'Lunghezza da a 7 a 15 con almeno un carattere maiuscolo, uno minuscolo e un carattere speciale(@#&%^?!$=)'
					}
				}
			},
			email: {
				container: '#infoEmail',
				validators: {
					notEmpty: {
						message: 'Il campo email nascita non può essere vuoto'
					},
					regexp: {
						//\w significa qualsiasi carattere, qualsiasi numero e underscore
						regexp: /^[\w-\.\+]+\@[a-zA-Z0-9]+\.[a-zA-Z]{2,5}$/,
						message: 'Inserire un email valida'
					}
				}
			},
		}
	});
});