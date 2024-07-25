import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent{

  constructor(private authService: AuthenticationService, private _router:Router) {
  }

  getName() {
    const admin = this.authService.getCookieByName('admin'); 
    if (admin !== '') {
      const jsonAdmin = JSON.parse(atob(admin));
      return jsonAdmin.nomeAdmin;
    }
    return 'admin';
  }

  isAuthenticated() {
    return this.authService.getCookieByName('bearer') !== '';
  }

  logout() {
    this._router.navigate(['/logout']);
  }
  

}
