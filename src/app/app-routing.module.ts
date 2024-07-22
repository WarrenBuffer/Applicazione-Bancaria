import { NgModule } from '@angular/core';
import { mapToCanActivate, RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './services/auth-guard.service';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { FindClientComponent } from './find-client/find-client.component';
import { DeleteContoComponent } from './delete-conto/delete-conto.component';
import { AddClientComponent } from './add-client/add-client.component';
import { StatsComponent } from './stats/stats.component';
import { RichiestePrestitiComponent } from './richieste-prestiti/richieste-prestiti.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full'},
  { path: 'login', canActivate: mapToCanActivate([AuthGuardService]), component: LoginComponent  },
  { path: 'home', canActivate: mapToCanActivate([AuthGuardService]), component: HomeComponent  },
  { path: 'findClient', canActivate: mapToCanActivate([AuthGuardService]), component: FindClientComponent  },
  { path: 'deleteConto', canActivate: mapToCanActivate([AuthGuardService]), component: DeleteContoComponent  },
  { path: 'addClient', canActivate: mapToCanActivate([AuthGuardService]), component: AddClientComponent  },
  { path: 'stats', canActivate: mapToCanActivate([AuthGuardService]), component: StatsComponent },
  { path: 'richiestePrestiti', canActivate: mapToCanActivate([AuthGuardService]), component: RichiestePrestitiComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
