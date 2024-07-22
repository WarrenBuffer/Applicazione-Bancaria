import { Component } from '@angular/core';
import { ApiService } from '../services/api.service';
import { Cliente } from '../model/cliente';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.css'
})
export class StatsComponent {
  stats: any;
  loading = true;
  transazioniPerMese = new Array<number>(12).fill(0);
  tableTransactions = [this.transazioniPerMese];

  constructor(private apiService: ApiService) {
    this.apiService.getStats().subscribe({
      next: v => {
        this.stats = v.message;
        this.stats.importoTransazioniPerMese.forEach((mese: any) => {
          this.transazioniPerMese[Number(mese.id) - 1] = mese.importo;
        });
        this.transazioniPerMese.forEach(mese => console.log("Mese", mese))
        this.loading = false;
      }
    });
  }

  calcTotPagamenti(codCliente: number) {
    let tot = 0;
    this.stats.clienti.forEach((cliente: any) => {
      if (cliente.codCliente == codCliente) {
        cliente.pagamenti.forEach((pagamento: any) => {
          tot += pagamento.importo;
        })
      }
    });
    return tot;
  }
  
  calcTotPrestiti(codCliente: number) {
    let tot = 0;
    this.stats.clienti.forEach((cliente: any) => {
      if (cliente.codCliente == codCliente) {
        cliente.prestiti.forEach((prestito: any) => {
          tot += prestito.importo;
        })
      }
    });
    return tot;
  }
}
