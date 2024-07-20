import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ApiService } from '../services/api.service';
import { Cliente } from '../model/cliente';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(private apiService: ApiService) {}

  clientList() {
    this.apiService.clientList();
  }

  addClient() {
    const client: Cliente = {
      codCliente: 10,
      nomeCliente: 'Giorgio',
      cognomeCliente: 'Verdi',
      emailCliente: 'admin@gmail.com',
      passwordCliente: 'Piero01$',
      accountBloccato: false,
      tentativiErrati: 0
    }
    this.apiService.addClient(client)
  }
  getClient() {
    this.apiService.getClient(102);
  }
  deleteConto() {
    this.apiService.deleteConto(1);
  }
  getStats() {
    this.apiService.getStats();
  }


}
