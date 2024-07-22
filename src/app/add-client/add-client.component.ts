import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { Cliente } from '../model/cliente';

@Component({
  selector: 'app-add-client',
  templateUrl: './add-client.component.html',
  styleUrl: './add-client.component.css'
})
export class AddClientComponent {
  form = new FormGroup({
    nome: new FormControl('', [Validators.required, Validators.pattern("^[a-zA-Z ,.'-]{2,30}$")]),
    cognome: new FormControl('', [Validators.required, Validators.pattern("^[a-zA-Z ,.'-]{2,30}$")]),
    email: new FormControl('', [Validators.required, Validators.pattern("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")]),
    password: new FormControl('', [Validators.required, Validators.pattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?=])[a-zA-Z0-9@#&%^$?=]{8,32}$")]),
  })

  constructor(private apiService: ApiService) {}

  submit(form: any) {
    this.apiService.addClient({
      nomeCliente: form.nome,
      cognomeCliente: form.cognome,
      emailCliente: form.email,
      passwordCliente: form.password,
      accountBloccato: false,
      tentativiErrati: 0,
      codCliente: 0,
    });
  }
}
