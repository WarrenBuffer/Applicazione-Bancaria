import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-clienti',
  templateUrl: './clienti.component.html',
  styleUrl: './clienti.component.css'
})
export class ClientiComponent {
  @Input() stats: any;

  visible: boolean = false;
  selectedClient: any;
  
  showDialog(client: any) {
    this.visible = true;
    this.selectedClient = client;
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
