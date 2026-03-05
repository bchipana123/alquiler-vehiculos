import { Routes } from '@angular/router';
import { HomeComponent } from '../../shared/components/home/home.component';
import { DashboardComponent } from '../dashboard/dashboard.component';

export const FEATURES_ROUTES: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'clientes/list',
        loadComponent: () =>
          import('../clientes/components/cliente-list/cliente-list.component')
            .then((m) => m.ClienteListComponent)
      },
      {
        path: 'clientes/form',
        loadComponent: () =>
          import('../clientes/components/cliente-form/cliente-form.component')
            .then((m) => m.ClienteFormComponent)
      },
      {
        path: 'clientes/form/:id',
        loadComponent: () =>
          import('../clientes/components/cliente-form/cliente-form.component')
            .then((m) => m.ClienteFormComponent)
      },
      {
        path: 'vehiculos/list',
        loadComponent: () =>
          import('../vehiculos/components/vehiculo-list/vehiculo-list.component')
            .then((m) => m.VehiculoListComponent)
      },
      {
        path: 'vehiculos/form',
        loadComponent: () =>
          import('../vehiculos/components/vehiculo-form/vehiculo-form.component')
            .then((m) => m.VehiculoFormComponent)
      },
      {
        path: 'vehiculos/form/:id',
        loadComponent: () =>
          import('../vehiculos/components/vehiculo-form/vehiculo-form.component')
            .then((m) => m.VehiculoFormComponent)
      },
      {
        path: 'alquileres/list',
        loadComponent: () =>
          import('../alquileres/components/alquiler-list/alquiler-list.component')
            .then((m) => m.AlquilerListComponent)
      },
      {
        path: 'alquileres/form',
        loadComponent: () =>
          import('../alquileres/components/alquiler-form/alquiler-form.component')
            .then((m) => m.AlquilerFormComponent)
      },
      {
        path: 'alquileres/form/:id',
        loadComponent: () =>
          import('../alquileres/components/alquiler-form/alquiler-form.component')
            .then((m) => m.AlquilerFormComponent)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];