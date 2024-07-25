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
