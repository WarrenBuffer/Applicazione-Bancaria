import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { ApiService } from '../services/api.service';
import { Cliente } from '../model/cliente';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent{
  private cliente: Cliente = new Cliente();
  form = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.pattern("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")]),
    password: new FormControl('', [Validators.required, Validators.pattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?!=])[a-zA-Z0-9@#&%^$?!=]{7,32}$")]),
  })

  constructor(private authenticationService: AuthenticationService, private apiService: ApiService) {
  }

  submit(form: any) {
    this.cliente.emailCliente = form.email;
    this.cliente.passwordCliente = form.password;

    this.apiService.setter(this.cliente);

    this.authenticationService.login(form.email, form.password);
  }
}
