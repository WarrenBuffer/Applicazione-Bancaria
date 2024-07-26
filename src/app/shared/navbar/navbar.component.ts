import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit{
  items: MenuItem[] | undefined;

  constructor(private authService: AuthenticationService, private _router:Router) {
  }
  ngOnInit(): void {
    this.items = [
      {
        label: 'Cerca cliente',
        icon: 'pi pi-search',
        routerLink: '/findClient'
      },
      {
        label: 'Lista clienti',
        icon: 'pi pi-users',
        routerLink: '/home'
      },
      {
        label: 'Aggiungi cliente',
        icon: 'pi pi-user-plus',
        routerLink: '/addClient'
      },
      {
        label: 'Elimina conto',
        icon: 'pi pi-user-minus',
        routerLink: '/deleteConto'
      },
      {
        label: 'Statistiche',
        icon: 'pi pi-chart-bar',
        routerLink: '/stats'
      },
      {
        label: 'Richieste prestiti',
        icon: 'pi pi-money-bill',
        routerLink: '/richiestePrestiti'
      },
      {
        label: 'Cambia password',
        icon: 'pi pi-key',
        routerLink: '/cambioPassword'
      },
    ]
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
