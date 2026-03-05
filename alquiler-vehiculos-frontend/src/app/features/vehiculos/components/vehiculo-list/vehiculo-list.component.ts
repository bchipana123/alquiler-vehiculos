import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import { VehiculoService } from '../../services/vehiculo.service';
import { Vehiculo, EstadoVehiculo } from '../../model/vehiculo.model';
import { PageResponse } from '../../../../core/auth/model/page-response.model';
import { PaginacionComponent } from '../../../../shared/components/paginacion/paginacion.component';



@Component({
  selector: 'app-vehiculo-list',
  imports: [CommonModule, FormsModule,PaginacionComponent],
  templateUrl: './vehiculo-list.component.html',
  styleUrl: './vehiculo-list.component.css'
})
export class VehiculoListComponent implements OnInit {

  private vehiculoService = inject(VehiculoService);
  private router = inject(Router);
  private toastr = inject(ToastrService);
  private cdr = inject(ChangeDetectorRef);

  respuesta?: PageResponse<Vehiculo>;
  vehiculos: Vehiculo[] = [];
  busqueda: string = '';
  estadoFiltro: string = '';
  cargando: boolean = false;
  paginaActual: number = 0;
  tamanioPagina: number = 5;

  // Para usar el enum en el HTML
  EstadoVehiculo = EstadoVehiculo;

  ngOnInit(): void {
    this.listar();
  }

  listar() {
    this.cargando = true;
    this.vehiculoService.listar(
      this.paginaActual,
      this.tamanioPagina,
      'marca', 'asc',
      this.busqueda || undefined,
      this.estadoFiltro || undefined
    ).subscribe({
      next: (res) => {
        this.respuesta = res;
        this.vehiculos = res.content;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.toastr.error('Error al cargar vehículos', 'Error');
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
    this.router.navigate(['/home/vehiculos/form']);
  }

  editar(vehiculo: Vehiculo) {
    this.router.navigate(['/home/vehiculos/form', vehiculo.id]);
  }

  eliminar(vehiculo: Vehiculo) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar el vehículo ${vehiculo.placa}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#0962E5',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.vehiculoService.eliminar(vehiculo.id!).subscribe({
          next: () => {
            this.toastr.success('Vehículo eliminado correctamente', 'Éxito');
            this.listar();
          },
          error: () => {
            this.toastr.error('Error al eliminar vehículo', 'Error');
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
}