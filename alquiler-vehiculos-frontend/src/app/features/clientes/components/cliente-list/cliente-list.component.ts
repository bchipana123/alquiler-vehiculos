
import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';

import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../model/cliente.model';
import { PageResponse } from '../../../../core/auth/model/page-response.model';
import { PaginacionComponent } from '../../../../shared/components/paginacion/paginacion.component';



@Component({
  selector: 'app-cliente-list',
  imports: [CommonModule, FormsModule,PaginacionComponent],
  templateUrl: './cliente-list.component.html',
  styleUrl: './cliente-list.component.css'
})
export class ClienteListComponent implements OnInit {

  private clienteService = inject(ClienteService);
  private router = inject(Router);
  private toastr = inject(ToastrService);
  private cdr = inject(ChangeDetectorRef);


  // Lista de clientes y paginación
  respuesta?: PageResponse<Cliente>;
  clientes: Cliente[] = [];
  busqueda: string = '';
  cargando: boolean = false;

  // Paginación
  paginaActual: number = 0;
  tamanioPagina: number = 5;

  ngOnInit(): void {
    this.listar();
  }

  listar() {
    this.cargando = true;
    this.clienteService.listar(
      this.paginaActual,
      this.tamanioPagina,
      'nombre', 'asc',
      this.busqueda || undefined
    ).subscribe({
      next: (res) => {
        this.respuesta = res;
        this.clientes = res.content;
        this.cargando = false;
        this.cdr.detectChanges(); // ← agrega esta línea
      },
      error: () => {
        this.toastr.error('Error al cargar clientes', 'Error');
        this.cargando = false;
        this.cdr.detectChanges(); // ← y esta
      }
    });
  }

  buscar() {
    // Al buscar volvemos a la página 0
    this.paginaActual = 0;
    this.listar();
  }

  limpiar() {
    this.busqueda = '';
    this.paginaActual = 0;
    this.listar();
  }

  nuevo() {
    this.router.navigate(['/home/clientes/form']);
  }

  editar(cliente: Cliente) {
    this.router.navigate(['/home/clientes/form', cliente.id]);
  }

  eliminar(cliente: Cliente) {
    // SweetAlert2 = librería para modales de confirmación bonitos
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar al cliente ${cliente.nombre}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#0962E5',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.clienteService.eliminar(cliente.id!).subscribe({
          next: () => {
            this.toastr.success('Cliente eliminado correctamente', 'Éxito');
            this.listar(); // recarga la lista
          },
          error: () => {
            this.toastr.error('Error al eliminar cliente', 'Error');
          }
        });
      }
    });
  }

  // Paginación
  irPagina(pagina: number) {
    this.paginaActual = pagina;
    this.listar();
  }

  // Genera array de números de páginas para el HTML
  get paginas(): number[] {
    const total = this.respuesta?.totalPages ?? 0;
    return Array.from({ length: total }, (_, i) => i);
  }
}