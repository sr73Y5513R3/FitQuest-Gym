import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../../Services/login/auth-service.service';

@Component({
  selector: 'app-menu-lateral',
  templateUrl: './menu-lateral.component.html',
  styleUrl: './menu-lateral.component.css'
})
export class MenuLateralComponent {
  
  isAdmin$: Observable<boolean>;
  isLoggedIn$: Observable<boolean>; // Opcional: para mostrar/ocultar elementos basados en si el usuario está logeado

  constructor(private authService: AuthService) {
    this.isAdmin$ = this.authService.isAdmin();
    this.isLoggedIn$ = this.authService.isAuthenticated$; // Puedes usar isAuthenticated$ para esto
  }

  ngOnInit(): void {
    // Si necesitas alguna lógica de inicialización aquí
  }

  logout(): void {
    this.authService.logout();
  }
}
