import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Services/login/auth-service.service';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss'] // O .css
})
export class InicioComponent implements OnInit {

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    const username = this.authService.getUsername();
    if (username) {
      console.log(`¡Bienvenido a la pantalla de inicio, ${username}!`);
    }
  }

  logout(): void {
    this.authService.logout(); // Llama al método de cerrar sesión de tu AuthService
  }
}
