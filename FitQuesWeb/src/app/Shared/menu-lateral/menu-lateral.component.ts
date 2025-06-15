import { Component } from '@angular/core';
import { filter, Observable } from 'rxjs';
import { AuthService } from '../../Services/login/auth-service.service';
import { NavigationEnd, Router } from '@angular/router';


@Component({
  selector: 'app-menu-lateral',
  templateUrl: './menu-lateral.component.html',
  styleUrl: './menu-lateral.component.css'
})
export class MenuLateralComponent {
  
  isDropdownOpen: boolean = false;
  isAdmin$: Observable<boolean>;
  isLoggedIn$: Observable<boolean>; // Opcional: para mostrar/ocultar elementos basados en si el usuario está logeado

  constructor(private authService: AuthService, private router: Router) {
    this.isAdmin$ = this.authService.isAdmin();
    this.isLoggedIn$ = this.authService.isAuthenticated$;
  }

  ngOnInit(): void {
     if(this.router.url.startsWith('/admin/')){
      this.isDropdownOpen = true;
    };
  }

  logout(): void {
    this.authService.logout();
  }

  toggleAdminPanel(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  
}
