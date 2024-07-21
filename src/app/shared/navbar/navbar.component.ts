import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  constructor(private authService: AuthenticationService) {}

  getName() {
    const name = this.authService.getCookieByName('admin');
    return  name !== '' ? name : 'admin';
  }

  isAuthenticated() {
    return this.authService.getCookieByName('bearer') !== '';
  }

  logout() {
    this.authService.logout();
  }
}
