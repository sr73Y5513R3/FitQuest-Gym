import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { TopMenuComponent } from './Shared/top-menu/top-menu.component';
import { FooterComponent } from './Shared/footer/footer.component';
import { InicioComponent } from './Components/inicio/inicio.component';
import { LoginComponent } from './Components/login/login.component';
import { EntrenamientoComponent } from './Components/entrenamiento/entrenamiento.component';
import { MenuLateralComponent } from './Shared/menu-lateral/menu-lateral.component';
import { EntrenamientoDetallesComponent } from './Components/entrenamiento-detalles/entrenamiento-detalles.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TopMenuComponent,
    FooterComponent,
    InicioComponent,
    EntrenamientoComponent,
    MenuLateralComponent,
    EntrenamientoDetallesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule, 
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
