import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {
  private token: string | null = null;

  constructor(@Inject(DOCUMENT) private document: Document, private _router: Router) {     
    
  }

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    this.token = this.document.defaultView?.sessionStorage ? sessionStorage.getItem('token') : null;

    for (let i = 0; i < route.url.length; ++i) {
      const path = route.url[i].path;
      switch (path) {
        case 'login': return this.token !== null ? this._router.createUrlTree(['/home']) : true;
        case 'home': return this.token !== null ? true : this._router.createUrlTree(['/']);
        default: return false;
      }
    }

    return this._router.createUrlTree(['/']);
}
}
