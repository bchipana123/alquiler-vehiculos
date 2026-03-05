import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest } from '../model/login.request';
import { LoginResponse } from '../model/login.response';
import { API_ENDPOINTS } from '../model/api.constants';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private http = inject(HttpClient);
  private url = API_ENDPOINTS.auth;

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.url}/login`, request).pipe(

      tap((res) => {
        sessionStorage.setItem('token', res.token);
        sessionStorage.setItem('username', res.username);
        sessionStorage.setItem('rol', res.rol);
      })
    );
  }

  logout(): void {
    sessionStorage.clear();
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
