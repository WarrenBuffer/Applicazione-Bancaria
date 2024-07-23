import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, UrlTree } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {
  private token: string = '';

  constructor(private _router: Router, private authenticationService: AuthenticationService ) {     
    
  }

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    this.token = this.authenticationService.getCookieByName("bearer");
    
    for (let i = 0; i < route.url.length; ++i) {
      const path = route.url[i].path;
      switch (path) {
        case 'login': return this.token !== '' ? this._router.createUrlTree(['/home']) : true;
        case 'home': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        case 'findClient': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        case 'addClient': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        case 'deleteConto': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        case 'stats': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        case 'richiestePrestiti': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        case 'cambioPassword': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        case 'logout': return this.token !== '' ? true : this._router.createUrlTree(['/']);
        default: return false;
      }
    }

    return this._router.createUrlTree(['/']);
}
}
