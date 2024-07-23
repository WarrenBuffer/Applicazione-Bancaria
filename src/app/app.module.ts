import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToolbarModule } from 'primeng/toolbar';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { PasswordModule } from 'primeng/password';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { HomeComponent } from './home/home.component';
import { CookieService } from 'ngx-cookie-service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ChipModule } from 'primeng/chip';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { FindClientComponent } from './find-client/find-client.component';
import { AddClientComponent } from './add-client/add-client.component';
import { DeleteContoComponent } from './delete-conto/delete-conto.component';
import { StatsComponent } from './stats/stats.component';
import { TableModule } from 'primeng/table';
import { RichiestePrestitiComponent } from './richieste-prestiti/richieste-prestiti.component';
import { LoadingComponent } from './shared/loading/loading.component';
import { DropdownModule } from 'primeng/dropdown';
import { CambioPasswordComponent } from './cambio-password/cambio-password.component';
import { LogoutComponent } from './logout/logout.component';
import { DialogModule } from 'primeng/dialog';
import { SelectButtonModule } from 'primeng/selectbutton';
import { GeneraliComponent } from './stats/generali/generali.component';
import { ClientiComponent } from './stats/clienti/clienti.component';
import { PrestitiComponent } from './stats/prestiti/prestiti.component';
import { TransazioniComponent } from './stats/transazioni/transazioni.component';
import { ChartModule } from 'primeng/chart';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    HomeComponent,
    FindClientComponent,
    AddClientComponent,
    DeleteContoComponent,
    StatsComponent,
    LoadingComponent,
    RichiestePrestitiComponent,
    CambioPasswordComponent,
    LogoutComponent,
    GeneraliComponent,
    ClientiComponent,
    PrestitiComponent,
    TransazioniComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ToastModule,
    ButtonModule,
    CardModule,
    ToolbarModule,
    PasswordModule,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    MessageModule,
    ProgressSpinnerModule,
    ChipModule,
    TagModule,
    TooltipModule,
    TableModule,
    DropdownModule,
    SelectButtonModule,
    ChartModule,
    DialogModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withFetch()),
    MessageService,
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
