import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ServerResponse } from '../model/server-response';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private basePath = "http://localhost:8080/api";
  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });
  private httpOptions = {
    headers: this.headers,
    withCredentials: true,
  }

  constructor(private _http: HttpClient, private messageService: MessageService) { }

  login(email: string, password: string) {
    this._http.post<ServerResponse>(`${this.basePath}/loginAdmin`, {
      email: email,
      password: password
    },
      this.httpOptions).subscribe({
        next: v => {
          if (v.code !== 0) {
            this.showError(v.message);
          } else {
            this.showSuccess("Logged successfully. TODO: implement redirect")
          }
        },
        error: err => {
          this.showError(err.message);
        }
      });
  }

  showError(message: string) {
    this.messageService.add({key: 'br', severity:'error', summary: 'Error', detail: message});
  }

  showSuccess(message: string) {
    this.messageService.add({key: 'br', severity:'success', summary: 'Success', detail: message});
  }
}
