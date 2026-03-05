import { Routes } from '@angular/router';



export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./core/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'home',
    loadChildren: () =>
      import('./features/routes/features.routes').then((m) => m.FEATURES_ROUTES),
  },
  {
    path: '404',
    loadComponent: () =>
      import('./core/errors/page-not-found/page-not-found.component')
        .then((m) => m.PageNotFoundComponent),
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/404', pathMatch: 'full' },
];