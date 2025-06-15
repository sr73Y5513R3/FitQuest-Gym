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
import { ListaEntrenamientosComponent } from './Components/admin/lista-entrenamientos/lista-entrenamientos.component';
import { ListaEjerciciosComponent } from './Components/admin/lista-ejercicios/lista-ejercicios.component';
import { ListaMaterialesComponent } from './Components/admin/lista-materiales/lista-materiales.component';
import { ListaClientesComponent } from './Components/admin/lista-clientes/lista-clientes.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TopMenuComponent,
    FooterComponent,
    InicioComponent,
    EntrenamientoComponent,
    MenuLateralComponent,
    EntrenamientoDetallesComponent,
    ListaEntrenamientosComponent,
    ListaEjerciciosComponent,
    ListaMaterialesComponent,
    ListaClientesComponent
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
