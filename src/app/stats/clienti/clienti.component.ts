import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-clienti',
  templateUrl: './clienti.component.html',
  styleUrl: './clienti.component.css'
})
export class ClientiComponent {
  @Input() stats: any;
  
  visible: boolean = false;
  selectedClient: any;
  transazioni = new Array<any>();
  transazioniBancarie = new Array<any>();

  showDialog(client: any) {
    this.visible = true;
    this.selectedClient = client;

    this.selectedClient.conti.forEach((conto: any) => {
      conto.transazioni.forEach((transazione: any) => {
        this.transazioni.push(transazione);
      })
      conto.transazioniBancarie.forEach((transazione: any) => {
        this.transazioniBancarie.push(transazione);
      })
    });    
  }

  calcTotPagamenti(cliente: any) {
    let tot = 0;
    cliente.pagamenti.forEach((pagamento: any) => {
      tot += pagamento.importo;
    })
    return tot;
  }

  calcTotPrestiti(cliente: any) {
    let tot = 0;
    cliente.prestiti.forEach((prestito: any) => {
      tot += prestito.importo;
    })
    return tot;
  }
}
