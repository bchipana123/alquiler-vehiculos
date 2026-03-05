import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../service/login.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  private router = inject(Router);
  private loginService = inject(LoginService);
  private toastr = inject(ToastrService);

  username: string = '';
  password: string = '';
  cargando: boolean = false;

  ingresar() {
    if (!this.username || !this.password) {
      this.toastr.warning('Ingrese usuario y contraseña', 'Aviso');
      return;
    }

    this.cargando = true;

    this.loginService.login({
      username: this.username,
      password: this.password
    }).subscribe({
      next: () => {
        this.toastr.success('Bienvenido al sistema', 'Login exitoso');
        this.router.navigate(['/home']);
      },
      error: () => {
        this.cargando = false;
        this.toastr.error('Usuario o contraseña incorrectos', 'Error');
      }
    });
  }
}