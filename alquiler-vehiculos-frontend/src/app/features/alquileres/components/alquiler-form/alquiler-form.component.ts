import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AbstractControl, FormBuilder,
         ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { AlquilerService } from '../../services/alquiler.service';
import { ClienteService } from '../../../clientes/services/cliente.service';
import { VehiculoService } from '../../../vehiculos/services/vehiculo.service';
import { Cliente } from '../../../clientes/model/cliente.model';
import { Vehiculo, EstadoVehiculo } from '../../../vehiculos/model/vehiculo.model';
import { EstadoAlquiler } from '../../model/alquiler.model';
import { fechaFinValidator } from '../../validator/fecha-fin.validator';

@Component({
  selector: 'app-alquiler-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './alquiler-form.component.html',
  styleUrl: './alquiler-form.component.css'
})
export class AlquilerFormComponent implements OnInit {

  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private formBuilder = inject(FormBuilder);
  private alquilerService = inject(AlquilerService);
  private clienteService = inject(ClienteService);
  private vehiculoService = inject(VehiculoService);
  private toastr = inject(ToastrService);
  private cdr = inject(ChangeDetectorRef);

  id?: number;
  submitted = false;
  cargando = false;

  // Listas para los dropdowns
  clientes: Cliente[] = [];
  vehiculos: Vehiculo[] = [];

  // Para mostrar el total estimado
  totalEstimado: number = 0;

  estados = Object.values(EstadoAlquiler);

  alquilerForm = this.formBuilder.group({
    clienteId: ['', Validators.required],
    vehiculoId: ['', Validators.required],
    fechaInicio: ['', Validators.required],
    fechaFin: ['', Validators.required],
    estado: [EstadoAlquiler.ACTIVO]
  }, {
    // Validador a nivel de grupo: valida fechaFin > fechaInicio
    validators: fechaFinValidator()
  });

  ngOnInit(): void {
    this.cargarClientes();
    this.cargarVehiculos();

    this.activatedRoute.params.subscribe(params => {
      this.id = params['id'] ? +params['id'] : undefined;
      if (this.id) {
        this.cargarAlquiler(this.id);
      }
    });

    // Escucha cambios en fechas y vehículo para calcular total
    this.alquilerForm.valueChanges.subscribe(() => {
      this.calcularTotal();
    });
  }

  cargarClientes() {
    // Cargamos todos los clientes para el dropdown
    this.clienteService.listar(0, 100).subscribe({
        next: (res) => {
            this.clientes = res.content;
            this.cdr.detectChanges();
          }
    });
  }

  cargarVehiculos() {
    // Solo mostramos vehículos DISPONIBLES al crear
    this.vehiculoService.listar(0, 100, 'marca', 'asc',
      undefined, 'DISPONIBLE').subscribe({
        next: (res) => {
            this.vehiculos = res.content;
            this.cdr.detectChanges();
          }
    });
  }

  cargarAlquiler(id: number) {
    // En edición cargamos todos los vehículos
    this.vehiculoService.listar(0, 100).subscribe({
        next: (res) => {
            this.vehiculos = res.content;
            this.cdr.detectChanges();
          }
    });

    this.alquilerService.buscarPorId(id).subscribe({
      next: (alquiler) => {
        this.alquilerForm.patchValue({
          clienteId: alquiler.clienteId.toString(),
          vehiculoId: alquiler.vehiculoId.toString(),
          fechaInicio: alquiler.fechaInicio,
          fechaFin: alquiler.fechaFin,
          estado: alquiler.estado
        });
        this.cdr.detectChanges(); 
      },
      error: () => {
        this.toastr.error('Error al cargar alquiler', 'Error');
        this.router.navigate(['/home/alquileres/list']);
      }
    });
  }

  calcularTotal() {
    const vehiculoId = this.alquilerForm.value.vehiculoId;
    const fechaInicio = this.alquilerForm.value.fechaInicio;
    const fechaFin = this.alquilerForm.value.fechaFin;

    if (!vehiculoId || !fechaInicio || !fechaFin) {
      this.totalEstimado = 0;
      return;
    }

    const vehiculo = this.vehiculos.find(v => v.id === +vehiculoId);
    if (!vehiculo) return;

    const inicio = new Date(fechaInicio);
    const fin = new Date(fechaFin);
    const dias = Math.ceil(
      (fin.getTime() - inicio.getTime()) / (1000 * 60 * 60 * 24)
    );

    // Solo calcula si las fechas son válidas
    this.totalEstimado = dias > 0 ? dias * vehiculo.precioPorDia : 0;
  }

  onSubmit() {
    this.submitted = true;

    if (this.alquilerForm.invalid) {
      this.toastr.warning('Por favor corrija los errores', 'Aviso');
      return;
    }

    this.cargando = true;
    const datos = this.alquilerForm.value;

    const alquiler = {
      clienteId: +datos.clienteId!,
      vehiculoId: +datos.vehiculoId!,
      fechaInicio: datos.fechaInicio!,
      fechaFin: datos.fechaFin!,
      estado: datos.estado!
    };

    if (this.id) {
      this.alquilerService.actualizar(this.id, alquiler).subscribe({
        next: () => {
          this.toastr.success('Alquiler actualizado correctamente', '✅ Éxito');
          this.router.navigate(['/home/alquileres/list']);
        },
        error: (err) => {
          this.cargando = false;
          const msg = err.error?.error ?? 'Error al actualizar';
          this.toastr.error(msg, 'Error');
        }
      });
    } else {
      this.alquilerService.crear(alquiler).subscribe({
        next: () => {
          this.toastr.success('Alquiler creado correctamente', '✅ Éxito');
          this.router.navigate(['/home/alquileres/list']);
        },
        error: (err) => {
          this.cargando = false;
          const msg = err.error?.error ?? 'Error al crear';
          this.toastr.error(msg, 'Error');
        }
      });
    }
  }

  cancelar() {
    this.router.navigate(['/home/alquileres/list']);
  }

  get cf(): { [key: string]: AbstractControl } {
    return this.alquilerForm.controls;
  }

  campoInvalido(campo: string): boolean {
    return !!(this.cf[campo].invalid &&
             (this.cf[campo].touched || this.submitted));
  }

  campoValido(campo: string): boolean {
    return !!(this.cf[campo].valid && this.cf[campo].touched);
  }
}