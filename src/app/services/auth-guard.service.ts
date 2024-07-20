import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {
  private token: string | null = null;

  constructor(@Inject(DOCUMENT) private document: Document) {     
    
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    let canActivate = false;
    this.token = this.document.defaultView?.sessionStorage ? sessionStorage.getItem('token') : null;

    route.url.forEach(path => {
        const url = path.path;
        if (url === '' ) canActivate = true;
        else if (url === 'home') canActivate = this.token !== null;
    }); 
    
    return canActivate;
}
}
