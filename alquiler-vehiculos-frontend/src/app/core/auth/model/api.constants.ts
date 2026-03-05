import { environment } from '../../../../environments/environment';

export const API_BASE_URL = environment.API_BASE;

export const API_ENDPOINTS = {
  clientes: `${API_BASE_URL}/clientes`,
  vehiculos: `${API_BASE_URL}/vehiculos`,
  alquileres: `${API_BASE_URL}/alquileres`,
  auth: `${API_BASE_URL}/auth`
};