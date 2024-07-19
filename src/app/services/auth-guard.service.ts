import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor() { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    let canActivate = true;
    // TODO: fai vera autenticazione
    /* route.url.forEach(path => {
        const url = path.path;
        if (url === '' ) canActivate = !isLoggedIn;
    }); */
    
    return canActivate;
}
}
