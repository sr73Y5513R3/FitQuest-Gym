import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InicioComponent } from './Components/inicio/inicio.component';
import { LoginComponent } from './Components/login/login.component';
import { EntrenamientoComponent } from './Components/entrenamiento/entrenamiento.component';
import { EntrenamientoDetallesComponent } from './Components/entrenamiento-detalles/entrenamiento-detalles.component';
import { ListaEntrenamientosComponent } from './Components/admin/lista-entrenamientos/lista-entrenamientos.component';
import { ListaEjerciciosComponent } from './Components/admin/lista-ejercicios/lista-ejercicios.component';
import { EjercicioComponent } from './Components/ejercicio/ejercicio.component';
import { EjercicioDetallesComponent } from './Components/ejercicio-detalles/ejercicio-detalles.component';
import { MaterialesComponent } from './Components/materiales/materiales.component';
import { MaterialesDetallesComponent } from './Components/materiales-detalles/materiales-detalles.component';
import { ListaMaterialesComponent } from './Components/admin/lista-materiales/lista-materiales.component';
import { RegisterComponent } from './Components/register/register.component';
import { MisEntrenamientosComponent } from './Components/mis-entrenamientos/mis-entrenamientos.component';
import { CrearEntrenamientoComponent } from './Components/crear-entrenamiento/crear-entrenamiento.component';
import { EntrenadorValidaComponent } from './Components/entrenador-valida/entrenador-valida.component';
import { EntrenamientosRealizadosComponent } from './Components/entrenamientos-realizados/entrenamientos-realizados.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'inicio', component: InicioComponent },
  { path: 'entrenamientos', component: EntrenamientoComponent},
  { path: 'ejercicios', component: EjercicioComponent},
  { path: 'materiales', component: MaterialesComponent},
  { path: 'entrenamientos/:id', component: EntrenamientoDetallesComponent },
  { path: 'ejercicios/:id', component: EjercicioDetallesComponent },
  { path: 'materiales/:id', component: MaterialesDetallesComponent },
  { path: 'admin/entrenamiento', component: ListaEntrenamientosComponent},
  { path: 'admin/ejercicio', component: ListaEjerciciosComponent},
  { path: 'admin/material', component: ListaMaterialesComponent},
  { path: 'entrenador/mis-entrenamientos', component: MisEntrenamientosComponent},
  { path: 'entrenador/crear-entrenamiento', component: CrearEntrenamientoComponent},
  { path: 'entrenador/validar', component: EntrenadorValidaComponent},
  { path: 'perfil/entrenamientos-realizados', component: EntrenamientosRealizadosComponent},
  


  // { path: 'register', component: RegisterComponent }, // Descomenta si usas un componente de registro
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }