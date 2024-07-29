-- DA ADMIN
create user 'project_admin'@'localhost' identified by 'pass';
create database applicazione_bancaria;
GRANT ALL PRIVILEGES
ON applicazione_bancaria.*
TO 'project_admin'@'localhost';

-- DROP TABLE
drop table amministratore;
drop table amministratore_seq;
drop table carte_di_credito;
drop table carte_di_credito_seq;
drop table cliente;
drop table cliente_seq;
drop table conto;
drop table conto_seq;
drop table movimenti_conto;
drop table movimenti_conto_seq;
drop table pagamenti;
drop table pagamenti_seq;
drop table prestiti;
drop table prestiti_seq;
drop table richieste_prestito;
drop table richieste_prestito_seq;
drop table transazioni;
drop table transazioni_seq;
drop table transazioni_bancarie;
drop table transazioni_bancarie_seq;


-- Amministratore
insert into amministratore (cod_admin, nome_admin, cognome_admin, email_admin, password_admin, tentativi_errati, account_bloccato) 
values (1, 'Mario', 'Rossi', 'mario.rossi@admin.com', '$2a$10$Tw7pOZ5AvHVi7Qsz4Gj2vuJ/uGfd5iNFAHPlyuGgPkQLPrIqQm7T.', 0, false);

insert into amministratore (cod_admin, nome_admin, cognome_admin, email_admin, password_admin, tentativi_errati, account_bloccato) 
values (2, 'Giorgio', 'Neri', 'giorgio.neri@admin.com', '$2a$10$Tw7pOZ5AvHVi7Qsz4Gj2vuJ/uGfd5iNFAHPlyuGgPkQLPrIqQm7T.', 0, false);

-- Cliente // password = 'Password01$'
insert into cliente (cod_cliente, nome_cliente, cognome_cliente, email_cliente, password_cliente, tentativi_errati, account_bloccato) 
values (1, 'Mario', 'Rossi', 'mario.rossi@gmail.com', '$2a$10$Tw7pOZ5AvHVi7Qsz4Gj2vuJ/uGfd5iNFAHPlyuGgPkQLPrIqQm7T.', 0, false);

insert into cliente (cod_cliente, nome_cliente, cognome_cliente, email_cliente, password_cliente, tentativi_errati, account_bloccato) 
values (2, 'Giorgio', 'Verdi', 'giorgio.verdi@gmail.com', '$2a$10$Tw7pOZ5AvHVi7Qsz4Gj2vuJ/uGfd5iNFAHPlyuGgPkQLPrIqQm7T.', 0, false);

insert into cliente (cod_cliente, nome_cliente, cognome_cliente, email_cliente, password_cliente, tentativi_errati, account_bloccato) 
values (3, 'Anna', 'Rosa', 'anna.rosa@gmail.com', '$2a$10$Tw7pOZ5AvHVi7Qsz4Gj2vuJ/uGfd5iNFAHPlyuGgPkQLPrIqQm7T.', 0, false);

insert into cliente (cod_cliente, nome_cliente, cognome_cliente, email_cliente, password_cliente, tentativi_errati, account_bloccato) 
values (4, 'Pietro', 'Neri', 'pietro.neri@gmail.com', '$2a$10$Tw7pOZ5AvHVi7Qsz4Gj2vuJ/uGfd5iNFAHPlyuGgPkQLPrIqQm7T.', 0, false);

-- Conto
insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (1, 1, 'CORRENTE', 10000);
insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (1, 2, 'RISPARMIO', 10000);

insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (2, 3, 'CORRENTE', 5000);
insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (2, 4, 'RISPARMIO', 2000);

insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (3, 5, 'CORRENTE', 6000);
insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (3, 6, 'RISPARMIO', 800);

insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (4, 6, 'CORRENTE', 100000);
insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (4, 7, 'RISPARMIO', 50000);

insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (1, 8, 'CORRENTE', 0);
insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (2, 9, 'RISPARMIO', 0);
insert into conto (cod_cliente, cod_conto, tipo_conto, saldo) values (3, 10, 'CORRENTE', 0);

-- Carte di credito
insert into carte_di_credito (cod_cliente, cod_carta, numero_carta, data_scadenza, cvv) 
values (1, 1, '1111 0000 4439 9955', '2029-07-26 12:02:09.268000', '123');

insert into carte_di_credito (cod_cliente, cod_carta, numero_carta, data_scadenza, cvv) 
values (2, 2, '1111 0000 5126 9192', '2029-07-26 12:02:09.268000', '234');

insert into carte_di_credito (cod_cliente, cod_carta, numero_carta, data_scadenza, cvv) 
values (3, 3, '1111 0000 9102 1729', '2029-07-26 12:02:09.268000', '829');

insert into carte_di_credito (cod_cliente, cod_carta, numero_carta, data_scadenza, cvv) 
values (4, 4, '1111 0000 1827 0183', '2029-07-26 12:02:09.268000', '919');

-- Movimenti conto
insert into movimenti_conto (cod_conto, cod_movimento, tipo_movimento, importo, data_movimento) 
values (1, 1, 'ACCREDITO', 1000, '2024-06-01 12:02:09.268000');

insert into movimenti_conto (cod_conto, cod_movimento, tipo_movimento, importo, data_movimento) 
values (2, 2, 'ACCREDITO', 500, '2024-05-01 12:02:09.268000');

insert into movimenti_conto (cod_conto, cod_movimento, tipo_movimento, importo, data_movimento) 
values (3, 3, 'ADDEBITO', 600, '2024-04-01 12:02:09.268000');

insert into movimenti_conto (cod_conto, cod_movimento, tipo_movimento, importo, data_movimento) 
values (4, 4, 'ADDEBITO', 200, '2024-03-01 12:02:09.268000');

-- Pagamenti
insert into pagamenti (cod_cliente, cod_pagamento, metodo_pagamento, importo, data_pagamento)
values (1, 1, 'BONIFICO', 1000, '2024-03-01 12:02:09.268000');

insert into pagamenti (cod_cliente, cod_pagamento, metodo_pagamento, importo, data_pagamento)
values (1, 2, 'CARTA_CREDITO', 200, '2024-02-01 12:02:09.268000');

insert into pagamenti (cod_cliente, cod_pagamento, metodo_pagamento, importo, data_pagamento)
values (1, 3, 'CONTANTI', 1000, '2024-02-19 12:02:09.268000');

-- Prestiti
insert into prestiti (cod_cliente, cod_prestito, importo, tasso_interesse, duratainmesi)
values (1, 1, 10000, 0.22, 24);

insert into prestiti (cod_cliente, cod_prestito, importo, tasso_interesse, duratainmesi)
values (2, 2, 20000, 0.22, 36);

insert into prestiti (cod_cliente, cod_prestito, importo, tasso_interesse, duratainmesi)
values (3, 3, 100000, 0.22, 48);

-- Richieste prestito
insert into richieste_prestito (cod_cliente, cod_richiesta, importo, data_richiesta, stato)
values (1, 1, 10000, '2024-02-19 12:02:09.268000', 'APPROVATO');

insert into richieste_prestito (cod_cliente, cod_richiesta, importo, data_richiesta, stato)
values (2, 2, 20000, '2024-03-19 12:02:09.268000', 'IN_ATTESA');

insert into richieste_prestito (cod_cliente, cod_richiesta, importo, data_richiesta, stato)
values (3, 3, 100000, '2024-04-19 12:02:09.268000', 'RIFIUTATO');

-- Transazioni
insert into transazioni (cod_conto, cod_transazione, importo, data_transazione, tipo_transazione)
values (1, 1, 1000, '2024-04-19 12:02:09.268000', 'ACCREDITO');

insert into transazioni (cod_conto, cod_transazione, importo, data_transazione, tipo_transazione)
values (2, 2, 1000, '2024-04-19 12:02:09.268000', 'ADDEBITO');

-- Transazioni bancarie
insert into transazioni_bancarie (cod_transazione_bancaria, conto_origine, conto_destinazione, importo, data_transazione, tipo_transazione)
values (1, 1, 2, 1000, '2024-04-19 12:02:09.268000', 'ACCREDITO');

insert into transazioni_bancarie (cod_transazione_bancaria, conto_origine, conto_destinazione, importo, data_transazione, tipo_transazione)
values (2, 2, 3, 1000, '2024-05-19 12:02:09.268000', 'ADDEBITO');

