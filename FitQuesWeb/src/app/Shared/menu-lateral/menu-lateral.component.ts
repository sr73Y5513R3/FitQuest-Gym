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
  
  isDropdowAdminOpen: boolean = false;
  isDropdownEntrenadorOpen: boolean = false;
  isDropdownProfileOpen: boolean = false;
  isAdmin$: Observable<boolean>;
  isEntrenador$: Observable<boolean>;
  isLoggedIn$: Observable<boolean>;

  constructor(private authService: AuthService, private router: Router) {
    this.isAdmin$ = this.authService.isAdmin();
    this.isEntrenador$ = this.authService.isEntrenador();
    this.isLoggedIn$ = this.authService.isAuthenticated$;
  }

  ngOnInit(): void {
     if(this.router.url.startsWith('/admin/')){
      this.isDropdowAdminOpen = true;
    };
  }

  logout(): void {
    this.authService.logout();
  }

  toggleAdminPanel(): void {
    this.isDropdowAdminOpen = !this.isDropdowAdminOpen;
  }

  toggleEntrenadorPanel(): void { 
    this.isDropdownEntrenadorOpen = !this.isDropdownEntrenadorOpen;
  }

  togglePerfilPanel(): void { 
    this.isDropdownProfileOpen = !this.isDropdownProfileOpen;
  }
  /*
  isPanelAdminActive(): boolean {
    const currentUrl = this.router.url;
    return currentUrl.startsWith('/admin/entrenamiento') ||
           currentUrl.startsWith('/admin/ejercicio') ||
           currentUrl.startsWith('/admin/materiales') ||
           currentUrl.startsWith('/admin/usuarios');
  }
  */
}
