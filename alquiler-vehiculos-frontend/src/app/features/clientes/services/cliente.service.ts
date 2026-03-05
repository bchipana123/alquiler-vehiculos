import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../model/cliente.model';
import { PageResponse } from '../../../core/auth/model/page-response.model';
import { API_ENDPOINTS } from '../../../core/auth/model/api.constants';


@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private http = inject(HttpClient);
  private url = API_ENDPOINTS.clientes;

  listar(page = 0, size = 10, sortBy = 'nombre',
         sortDir = 'asc', busqueda?: string): Observable<PageResponse<Cliente>> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    if (busqueda) params = params.set('busqueda', busqueda);

    return this.http.get<PageResponse<Cliente>>(this.url, { params });
  }

  buscarPorId(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.url}/${id}`);
  }

  crear(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.url, cliente);
  }

  actualizar(id: number, cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.url}/${id}`, cliente);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}