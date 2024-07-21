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
  clientList!: Cliente[];
  
  constructor(private apiService: ApiService) {
    this.apiService.clientList().subscribe({
      next: v => {
        this.clientList = v.message;
        console.log(this.clientList);
      }
    })
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
