import { Component } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { AuthenticationService } from '../services/authentication.service';

@Component({
	selector: 'app-logout',
	templateUrl: './logout.component.html',
	styleUrl: './logout.component.css'
})
export class LogoutComponent {

	constructor(private	cookieService: CookieService, private authService: AuthenticationService){
	}
	submit() {
		this.authService.logout();
	}

}
