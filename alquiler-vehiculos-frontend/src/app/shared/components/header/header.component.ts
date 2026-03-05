import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from '../../../core/auth/service/login.service';


@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  private router = inject(Router);
  private loginService = inject(LoginService);
  private toastr = inject(ToastrService);

  username = sessionStorage.getItem('username') ?? 'Admin';

  get inicial(): string {
    return this.username.charAt(0).toUpperCase();
  }

  cerrarSesion() {
    this.loginService.logout();
    this.toastr.info('Sesión cerrada', 'Hasta luego');
    this.router.navigate(['/login']);
  }
}