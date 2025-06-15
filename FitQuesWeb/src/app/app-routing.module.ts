import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InicioComponent } from './Components/inicio/inicio.component';
import { LoginComponent } from './Components/login/login.component';
import { EntrenamientoComponent } from './Components/entrenamiento/entrenamiento.component';
import { EntrenamientoDetallesComponent } from './Components/entrenamiento-detalles/entrenamiento-detalles.component';
import { ListaEntrenamientosComponent } from './Components/admin/lista-entrenamientos/lista-entrenamientos.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'inicio', component: InicioComponent },
  { path: 'entrenamientos', component: EntrenamientoComponent},
  { path: 'entrenamientos/:id', component: EntrenamientoDetallesComponent },
  { path: 'admin/entrenamiento', component: ListaEntrenamientosComponent},
  // { path: 'register', component: RegisterComponent }, // Descomenta si usas un componente de registro
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }