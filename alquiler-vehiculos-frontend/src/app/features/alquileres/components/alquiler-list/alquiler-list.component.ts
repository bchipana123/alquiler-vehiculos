import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import { AlquilerService } from '../../services/alquiler.service';
import { Alquiler, EstadoAlquiler } from '../../model/alquiler.model';
import { PageResponse } from '../../../../core/auth/model/page-response.model';
import { PaginacionComponent } from '../../../../shared/components/paginacion/paginacion.component';



@Component({
  selector: 'app-alquiler-list',
  imports: [CommonModule, FormsModule,PaginacionComponent],
  templateUrl: './alquiler-list.component.html',
  styleUrl: './alquiler-list.component.css'
})
export class AlquilerListComponent implements OnInit {

  private alquilerService = inject(AlquilerService);
  private router = inject(Router);
  private toastr = inject(ToastrService);
  private cdr = inject(ChangeDetectorRef);


  respuesta?: PageResponse<Alquiler>;
  alquileres: Alquiler[] = [];
  busqueda: string = '';
  estadoFiltro: string = '';
  cargando: boolean = false;
  paginaActual: number = 0;
  tamanioPagina: number = 5;

  EstadoAlquiler = EstadoAlquiler;

  ngOnInit(): void {
    this.listar();
  }

  listar() {
    this.cargando = true;
    this.alquilerService.listar(
      this.paginaActual,
      this.tamanioPagina,
      'fechaInicio', 'desc',
      this.busqueda || undefined,
      this.estadoFiltro || undefined
    ).subscribe({
      next: (res) => {
        this.respuesta = res;
        this.alquileres = res.content;
        this.cargando = false;
        this.cdr.detectChanges(); 

      },
      error: () => {
        this.toastr.error('Error al cargar alquileres', 'Error');
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  buscar() {
    this.paginaActual = 0;
    this.listar();
  }

  limpiar() {
    this.busqueda = '';
    this.estadoFiltro = '';
    this.paginaActual = 0;
    this.listar();
  }

  nuevo() {
    this.router.navigate(['/home/alquileres/form']);
  }

  editar(alquiler: Alquiler) {
    this.router.navigate(['/home/alquileres/form', alquiler.id]);
  }

  eliminar(alquiler: Alquiler) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar el alquiler de ${alquiler.clienteNombre}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#0962E5',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.alquilerService.eliminar(alquiler.id!).subscribe({
          next: () => {
            this.toastr.success('Alquiler eliminado correctamente', 'Éxito');
            this.listar();
          },
          error: () => {
            this.toastr.error('Error al eliminar alquiler', 'Error');
          }
        });
      }
    });
  }

  irPagina(pagina: number) {
    this.paginaActual = pagina;
    this.listar();
  }

  get paginas(): number[] {
    const total = this.respuesta?.totalPages ?? 0;
    return Array.from({ length: total }, (_, i) => i);
  }

  // Devuelve la clase CSS del badge según el estado
  getBadgeClass(estado: EstadoAlquiler): string {
    switch (estado) {
      case EstadoAlquiler.ACTIVO:     return 'bg-success';
      case EstadoAlquiler.FINALIZADO: return 'bg-secondary';
      case EstadoAlquiler.CANCELADO:  return 'bg-danger';
      default: return 'bg-secondary';
    }
  }
}