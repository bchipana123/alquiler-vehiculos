import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehiculo } from '../model/vehiculo.model';
import { PageResponse } from '../../../core/auth/model/page-response.model';
import { API_ENDPOINTS } from '../../../core/auth/model/api.constants';

@Injectable({
  providedIn: 'root'
})
export class VehiculoService {

  private http = inject(HttpClient);
  private url = API_ENDPOINTS.vehiculos;

  listar(page = 0, size = 10, sortBy = 'marca',
         sortDir = 'asc', busqueda?: string,
         estado?: string): Observable<PageResponse<Vehiculo>> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    if (busqueda) params = params.set('busqueda', busqueda);
    if (estado)   params = params.set('estado', estado);

    return this.http.get<PageResponse<Vehiculo>>(this.url, { params });
  }

  buscarPorId(id: number): Observable<Vehiculo> {
    return this.http.get<Vehiculo>(`${this.url}/${id}`);
  }

  crear(vehiculo: Vehiculo): Observable<Vehiculo> {
    return this.http.post<Vehiculo>(this.url, vehiculo);
  }

  actualizar(id: number, vehiculo: Vehiculo): Observable<Vehiculo> {
    return this.http.put<Vehiculo>(`${this.url}/${id}`, vehiculo);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}