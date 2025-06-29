import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { RegisterComponent } from './Components/register/register.component';
import { MisEntrenamientosComponent } from './Components/mis-entrenamientos/mis-entrenamientos.component';
import { CrearEntrenamientoComponent } from './Components/crear-entrenamiento/crear-entrenamiento.component';
import { EntrenadorValidaComponent } from './Components/entrenador-valida/entrenador-valida.component';
import { EntrenamientosRealizadosComponent } from './Components/entrenamientos-realizados/entrenamientos-realizados.component';
import { PerfilComponent } from './Components/perfil/perfil.component';

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
    MaterialesDetallesComponent,
    RegisterComponent,
    MisEntrenamientosComponent,
    CrearEntrenamientoComponent,
    EntrenadorValidaComponent,
    EntrenamientosRealizadosComponent,
    PerfilComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule, 
    HttpClientModule,
    ReactiveFormsModule
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
