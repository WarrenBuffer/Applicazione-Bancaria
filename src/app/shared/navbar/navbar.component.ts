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
    const name = this.authService.getCookieByName('admin');
    return  name !== '' ? name : 'admin';
  }

  isAuthenticated() {
    return this.authService.getCookieByName('bearer') !== '';
  }

  logout() {
    this._router.navigate(['/logout']);
  }
  

}
