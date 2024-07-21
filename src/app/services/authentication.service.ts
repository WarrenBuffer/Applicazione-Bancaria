import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ServerResponse } from '../model/server-response';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private basePath = "http://localhost:8080";
  private httpOptions = {
    headers: new HttpHeaders({
    'Content-Type': 'application/json'
    }),
    withCredentials: true
  };

  constructor(private _http: HttpClient, private _router: Router,  private messageService: MessageService, private cookieService: CookieService) { }

  getCookieByName(name: string): string {
    return this.cookieService.get(name);
  }

  login(email: string, password: string) {
    this._http.post<ServerResponse>(`${this.basePath}/loginAdmin`, {
      email: email,
      password: password
    },this.httpOptions).subscribe({
        next: v => {
          if (v.code !== 0) {
            this.showError(v.message);
          } else {
            this._router.navigate(['/home'])
            this.showSuccess("Logged successfully.")
          }
        },
        error: err => {
          this.showError(err.message);
        }
      });
  }

  logout() {
    this._http.get<ServerResponse>(`${this.basePath}/logoutAdmin`,this.httpOptions).subscribe({
        next: v => {
          if (v.code !== 0) {
            this.showError(v.message);
          } else {
            this._router.navigate(['/'])
            this.showSuccess(v.message)
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
