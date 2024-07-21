import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Cliente } from '../model/cliente';
import { AuthenticationService } from './authentication.service';
import { catchError, Observable, of } from 'rxjs';
import { ToastService } from './toast.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private basePath = "http://localhost:8080/api";
  private httpOptions = {
    headers: new HttpHeaders({
    'Content-Type': 'application/json',
    }),
    withCredentials: true
  };

  constructor(private _http: HttpClient, private _router: Router,  private toastService: ToastService, private authService: AuthenticationService) { }

  clientList(): Observable<any> {
    return this._http.get(`${this.basePath}/clienti`, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      })
    )
  }

  addClient(cliente: Cliente) {
    this._http.post(`${this.basePath}/clienti`, cliente, this.httpOptions).subscribe({
      next: v => console.log(v),
      error: err => console.log(err)
    })
  }

  getClient(id: number) {
    this._http.get(`${this.basePath}/clienti/${id}`, this.httpOptions).subscribe({
      next: v => console.log(v),
      error: err => console.log(err)
    })
  }

  deleteConto(id: number) {
    this._http.delete(`${this.basePath}/conti/${id}`, this.httpOptions).subscribe({
      next: v => console.log(v),
      error: err => console.log(err)
    })
  }

  getStats() {
    this._http.get(`${this.basePath}/statistiche`, this.httpOptions).subscribe({
      next: v => console.log(v),
      error: err => console.log(err)
    })
  }
}
