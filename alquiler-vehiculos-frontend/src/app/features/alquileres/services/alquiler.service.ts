import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alquiler } from '../model/alquiler.model';
import { PageResponse } from '../../../core/auth/model/page-response.model';
import { API_ENDPOINTS } from '../../../core/auth/model/api.constants';

@Injectable({
  providedIn: 'root'
})
export class AlquilerService {

  private http = inject(HttpClient);
  private url = API_ENDPOINTS.alquileres;

  listar(page = 0, size = 10, sortBy = 'fechaInicio',
         sortDir = 'desc', busqueda?: string,
         estado?: string): Observable<PageResponse<Alquiler>> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    if (busqueda) params = params.set('busqueda', busqueda);
    if (estado)   params = params.set('estado', estado);

    return this.http.get<PageResponse<Alquiler>>(this.url, { params });
  }

  buscarPorId(id: number): Observable<Alquiler> {
    return this.http.get<Alquiler>(`${this.url}/${id}`);
  }

  crear(alquiler: Alquiler): Observable<Alquiler> {
    return this.http.post<Alquiler>(this.url, alquiler);
  }

  actualizar(id: number, alquiler: Alquiler): Observable<Alquiler> {
    return this.http.put<Alquiler>(`${this.url}/${id}`, alquiler);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}