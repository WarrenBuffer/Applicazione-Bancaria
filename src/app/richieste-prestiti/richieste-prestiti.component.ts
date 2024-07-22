import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { ToastService } from '../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-richieste-prestiti',
  templateUrl: './richieste-prestiti.component.html',
  styleUrl: './richieste-prestiti.component.css'
})
export class RichiestePrestitiComponent implements OnInit {
  richiestePrestiti: any[] = []; // Proprietà per memorizzare le richieste di prestito

  constructor(private apiService: ApiService, private toastService: ToastService, private _router: Router) { }

  ngOnInit(): void {
    this.getRichiestePrestiti();
  }

  getRichiestePrestiti() {
    this.apiService.getRichiestePrestiti()
      .subscribe({
        next: (v: any) => {
          if (v.code !== 0) {
            this.toastService.showError(v.message);
          } else {
            this.richiestePrestiti = v.message;
          }
        }
      })
  }

  approvaPrestito(id: number) {
    this.apiService.approvaPrestito(id)
      .subscribe({
        next: (v: any) => {
          if (v.code !== 0) {
            this.toastService.showError(v.message);
          } else {
            this.toastService.showSuccess(v.message);
            this.getRichiestePrestiti();
          }
        }
      })
  }

  declinaPrestito(id: number) {
    this.apiService.declinaPrestito(id)
      .subscribe({
        next: (v: any) => {
          if (v.code !== 0) {
            this.toastService.showError(v.message);
          } else {
            this.toastService.showSuccess(v.message);
            this.getRichiestePrestiti();
          }
        }
      })
  }
}
