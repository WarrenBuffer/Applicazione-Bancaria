import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { ToastService } from '../services/toast.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cambio-password',
  templateUrl: './cambio-password.component.html',
  styleUrl: './cambio-password.component.css'
})
export class CambioPasswordComponent{
  form = new FormGroup({
    password: new FormControl('', [Validators.required, Validators.pattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?!=])[a-zA-Z0-9@#&%^$!?=]{7,32}$")]),
  })
  
  constructor(private apiService: ApiService, private toastService: ToastService, private router: Router) { }

  submit(form: any) {
    this.apiService.confermaNuovaPassword(form.password)
      .subscribe({
        next: (v: any) => {
          if (v.code !== 0) {
            this.toastService.showError(v.message);
          } else {
            this.toastService.showSuccess(v.message);
            this.router.navigate(['home']);
          }
        }
      })
  }
}
