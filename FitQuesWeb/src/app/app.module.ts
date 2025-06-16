import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
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
import { EjercicioComponent } from './Components/ejercicio/ejercicio.component';
import { EjercicioDetallesComponent } from './Components/ejercicio-detalles/ejercicio-detalles.component';
import { MaterialesComponent } from './Components/materiales/materiales.component';
import { MaterialesDetallesComponent } from './Components/materiales-detalles/materiales-detalles.component';
import { authTokenInterceptor } from './intenceptor/auth-token.interceptor';

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
    ListaClientesComponent,
    EjercicioComponent,
    EjercicioDetallesComponent,
    MaterialesComponent,
    MaterialesDetallesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule, 
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: authTokenInterceptor,
      multi: true 
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
