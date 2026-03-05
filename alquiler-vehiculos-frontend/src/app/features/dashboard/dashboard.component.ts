import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { ClienteService } from '../clientes/services/cliente.service';
import { VehiculoService } from '../vehiculos/services/vehiculo.service';
import { AlquilerService } from '../alquileres/services/alquiler.service';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  private clienteService = inject(ClienteService);
  private vehiculoService = inject(VehiculoService);
  private alquilerService = inject(AlquilerService);
  private cdr = inject(ChangeDetectorRef);


  totalClientes = 0;
  totalVehiculos = 0;
  alquileresActivos = 0;
  vehiculosDisponibles = 0;

  ngOnInit(): void {
    this.cargarTotales();
  }

  cargarTotales() {
    this.clienteService.listar().subscribe({
      next: (res) => { this.totalClientes = res.totalElements; this.cdr.detectChanges(); }
    });
    this.vehiculoService.listar().subscribe({
      next: (res) => { this.totalVehiculos = res.totalElements; this.cdr.detectChanges(); }
    });
    this.vehiculoService.listar(0, 10, 'marca', 'asc', undefined, 'DISPONIBLE').subscribe({
      next: (res) => { this.vehiculosDisponibles = res.totalElements; this.cdr.detectChanges(); }
    });
    this.alquilerService.listar(0, 10, 'fechaInicio', 'desc', undefined, 'ACTIVO').subscribe({
      next: (res) => { this.alquileresActivos = res.totalElements; this.cdr.detectChanges(); }
    });
  }
}