import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ToastService } from '../../services/toast.service';
import { Cliente } from '../../model/cliente';

@Component({
  selector: 'app-prestiti',
  templateUrl: './prestiti.component.html',
  styleUrl: './prestiti.component.css',
})
export class PrestitiComponent implements OnInit {
  @Input() stats: any;
  clienti!: Cliente[];
  prestiti: any;
  richiesteAttesa: number = 0;
  richiesteRifiutate: number = 0;
  richiesteApprovate: number = 0;
  
  constructor(private apiService: ApiService, private toastService: ToastService) {
  }

  ngOnInit(): void {
    this.clienti = this.stats.clienti;
    this.apiService.getPrestiti().subscribe({
      next: v => {
        if (v.code !== 0) this.toastService.showError(v.message);
        else {
          this.prestiti = v.message;
          this.prestiti.forEach((prestito: any) => {
            if (prestito.stato === 'IN_ATTESA') this.richiesteAttesa++;
            else if (prestito.stato === 'RIFIUTATO') this.richiesteRifiutate++;
            else if (prestito.stato === 'APPROVATO') this.richiesteApprovate++;
          });
        }  
      },
      error: err => this.toastService.showError(err.message)
    })
  }

  calcImporto(cliente: any) {
    let tot: number = 0;
    cliente.richiestePrestito.forEach((richiesta: any) => {
      if (richiesta.stato === 'APPROVATO')
        tot += richiesta.importo;
    });
    return tot;
  }
}
