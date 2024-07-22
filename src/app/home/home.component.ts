import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ApiService } from '../services/api.service';
import { Cliente } from '../model/cliente';
import { ToastService } from '../services/toast.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  clientList!: Cliente[];
  
  constructor(private apiService: ApiService, private toastService:ToastService) {
    this.listaClient();
  }
  
  listaClient(){
    this.apiService.clientList().subscribe({
      next: v => {
        this.clientList = v.message;
      }
    })
  }

  lockUnlock(email: String) {
    this.apiService.lockUnlock(email).subscribe({
      next: v => {
        if (v.code !== 0) {
          this.toastService.showError(v.message);
        }else{
          this.listaClient();
        }
      }
    })
  }
  
}
