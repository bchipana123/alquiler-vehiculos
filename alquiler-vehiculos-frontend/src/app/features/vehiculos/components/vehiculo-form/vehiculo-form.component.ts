import { Component, inject, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AbstractControl, FormBuilder,
         ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { VehiculoService } from '../../services/vehiculo.service';
import { EstadoVehiculo } from '../../model/vehiculo.model';
import { placaValidator } from '../../validator/placa.validator';

@Component({
  selector: 'app-vehiculo-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './vehiculo-form.component.html',
  styleUrl: './vehiculo-form.component.css'
})
export class VehiculoFormComponent implements OnInit {

  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private formBuilder = inject(FormBuilder);
  private vehiculoService = inject(VehiculoService);
  private toastr = inject(ToastrService);

  id?: number;
  submitted = false;
  cargando = false;

  estados = Object.values(EstadoVehiculo);

  vehiculoForm = this.formBuilder.group({
    placa: ['', [
      Validators.required,
      placaValidator()
    ]],
    marca: ['', [
      Validators.required,
      Validators.minLength(2)
    ]],
    modelo: ['', [
      Validators.required,
      Validators.minLength(2)
    ]],
    precioPorDia: [0, [
      Validators.required,
      Validators.min(1)
    ]],
    estado: [EstadoVehiculo.DISPONIBLE]
  });

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.id = params['id'] ? +params['id'] : undefined;
      if (this.id) {
        this.cargarVehiculo(this.id);
      }
    });
  }

  cargarVehiculo(id: number) {
    this.vehiculoService.buscarPorId(id).subscribe({
      next: (vehiculo) => {
        this.vehiculoForm.patchValue({
          placa: vehiculo.placa,
          marca: vehiculo.marca,
          modelo: vehiculo.modelo,
          precioPorDia: vehiculo.precioPorDia,
          estado: vehiculo.estado
        });
      },
      error: () => {
        this.toastr.error('Error al cargar vehículo', 'Error');
        this.router.navigate(['/home/vehiculos/list']);
      }
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.vehiculoForm.invalid) {
      this.toastr.warning('Por favor corrija los errores', 'Aviso');
      return;
    }

    this.cargando = true;
    const datos = this.vehiculoForm.value;

    const vehiculo = {
      placa: datos.placa!,
      marca: datos.marca!,
      modelo: datos.modelo!,
      precioPorDia: datos.precioPorDia!,
      estado: datos.estado!
    };

    if (this.id) {
      this.vehiculoService.actualizar(this.id, vehiculo).subscribe({
        next: () => {
          this.toastr.success('Vehículo actualizado correctamente', 'Éxito');
          this.router.navigate(['/home/vehiculos/list']);
        },
        error: (err) => {
          this.cargando = false;
          const msg = err.error?.error ?? 'Error al actualizar';
          this.toastr.error(msg, 'Error');
        }
      });
    } else {
      this.vehiculoService.crear(vehiculo).subscribe({
        next: () => {
          this.toastr.success('Vehículo creado correctamente', '✅ Éxito');
          this.router.navigate(['/home/vehiculos/list']);
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
    this.router.navigate(['/home/vehiculos/list']);
  }

  get cf(): { [key: string]: AbstractControl } {
    return this.vehiculoForm.controls;
  }

  campoInvalido(campo: string): boolean {
    return !!(this.cf[campo].invalid &&
             (this.cf[campo].touched || this.submitted));
  }

  campoValido(campo: string): boolean {
    return !!(this.cf[campo].valid && this.cf[campo].touched);
  }
}