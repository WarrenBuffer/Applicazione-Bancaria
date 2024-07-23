import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Cliente } from '../model/cliente';
import { AuthenticationService } from './authentication.service';
import { catchError, Observable, of } from 'rxjs';
import { ToastService } from './toast.service';
import { ServerResponse } from '../model/server-response';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private cliente!: Cliente;
  private basePath = "http://localhost:8080/api";
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
    withCredentials: true
  };

  constructor(private _http: HttpClient, private _router: Router, private toastService: ToastService, private authService: AuthenticationService) { }

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
    this._http.post<ServerResponse>(`${this.basePath}/clienti`, cliente, this.httpOptions).subscribe({
      next: v => {
        if (v.code !== 0) {
          this.toastService.showError(v.message);
        } else {
          this.toastService.showSuccess(v.message);
        }
      },
      error: err => this.toastService.showError(err.message)
    })
  }
  contiList(): Observable<any> {
    return this._http.get(`${this.basePath}/conti`, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      })
    )
  }
  deleteConto(id: number): Observable<any>{
    return this._http.delete(`${this.basePath}/conti/${id}`, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      })
    )
  }
  getClientByEmail(email: String): Observable<any> {
    return this._http.get<ServerResponse>(`${this.basePath}/clienti/email/${email}`, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      })
    )
  }

  getStats(): Observable<any> {
    return this._http.get(`${this.basePath}/statistiche`, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      })
    )
  }

  getRichiestePrestiti(): Observable<any> {
    return this._http.get<ServerResponse>(`${this.basePath}/richiestePrestito`, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      }))
  }

  approvaPrestito(id: number): Observable<any> {
    return this._http.get<ServerResponse>(`${this.basePath}/approvaPrestito/` + id, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      }))
  }

  declinaPrestito(id: number): Observable<any> {
    console.log(id);
    return this._http.get<ServerResponse>(`${this.basePath}/declinaPrestito/` + id, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      }))
  }

  confermaNuovaPassword(password: string): Observable<any> {
    //console.log(password);
    return this._http.get<ServerResponse>(`${this.basePath}/confermaNuovaPassword/` + this.getter().emailCliente + "/" + password, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      }))
  }

  //metodi per impostare l'admin a livello globale e ottenere informazioni di esso in diversi punti del programma
  setter(cliente: Cliente) {
    this.cliente = cliente;
  }

  getter() {
    return this.cliente;
  }
  lockUnlock(email:String) : Observable<any>{
    return this._http.post<ServerResponse>(`${this.basePath}/clienti/lock`, email, this.httpOptions).pipe(
      catchError((err) => {
        this.toastService.showError("Errore interno del server\n" + err.message);
        this.authService.logout();
        return of(undefined);
      })
    )
  }

}