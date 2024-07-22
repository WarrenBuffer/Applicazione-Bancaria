import { Component } from '@angular/core';
import { ApiService } from '../services/api.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../services/toast.service';
import { Cliente } from '../model/cliente';

@Component({
  selector: 'app-find-client',
  templateUrl: './find-client.component.html',
  styleUrl: './find-client.component.css'
})
export class FindClientComponent {
  cliente=new Array<any>(1);
  constructor(private _router:Router, private apiService: ApiService,  private toastService: ToastService){}

  form = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.pattern("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")]),
  })

  findCliente(form: any) {
    this.apiService.getClientByEmail(form.email).subscribe({
      next: v => {
        if (v.code !== 0) {
          this.toastService.showError(v.message);
        } else {
          console.log(v.message);
          this.cliente[0]=v.message;
        }
      },
      error: err => this.toastService.showError("Errore interno del server\n" + err.message)
    });
  }
}
