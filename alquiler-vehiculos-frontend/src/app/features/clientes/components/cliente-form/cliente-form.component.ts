import { Component, inject, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AbstractControl, FormBuilder, 
         ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { ClienteService } from '../../services/cliente.service';
import { dniValidator } from '../../validator/dni.validator';



@Component({
  selector: 'app-cliente-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cliente-form.component.html',
  styleUrl: './cliente-form.component.css'
})
export class ClienteFormComponent implements OnInit {

  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private formBuilder = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private toastr = inject(ToastrService);

  id?: number;         
  submitted = false;  
  cargando = false;

  // Definición del formulario reactivo con validaciones
  clienteForm = this.formBuilder.group({
    nombre: ['', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(100)
    ]],
    dni: ['', [
        Validators.required,
        dniValidator()
      ]],

    email: ['', [
      Validators.required,
      Validators.email
    ]],
    telefono: ['', [
      Validators.required,
      // Debe empezar con 9 y tener 9 dígitos total
      Validators.pattern(/^9\d{8}$/)
    ]]
  });

  ngOnInit(): void {
    // Leemos el parámetro :id de la URL
    this.activatedRoute.params.subscribe(params => {
      this.id = params['id'] ? +params['id'] : undefined;
      if (this.id) {
        this.cargarCliente(this.id);
      }
    });
  }

  cargarCliente(id: number) {
    this.clienteService.buscarPorId(id).subscribe({
      next: (cliente) => {
        // patchValue = llena el formulario con los datos del cliente
        this.clienteForm.patchValue({
          nombre: cliente.nombre,
          dni: cliente.dni,
          email: cliente.email,
          telefono: cliente.telefono
        });
      },
      error: () => {
        this.toastr.error('Error al cargar cliente', 'Error');
        this.router.navigate(['/home/clientes/list']);
      }
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.clienteForm.invalid) {
      this.toastr.warning('Por favor corrija los errores del formulario', 'Aviso');
      return;
    }

    this.cargando = true;
    const datos = this.clienteForm.value;

    if (this.id) {
      // Modo edición → PUT
      this.clienteService.actualizar(this.id, {
        nombre: datos.nombre!,
        dni: datos.dni!,
        email: datos.email!,
        telefono: datos.telefono!
      }).subscribe({
        next: () => {
          this.toastr.success('Cliente actualizado correctamente', '✅ Éxito');
          this.router.navigate(['/home/clientes/list']);
        },
        error: (err) => {
          this.cargando = false;
          const msg = err.error?.error ?? 'Error al actualizar';
          this.toastr.error(msg, 'Error');
        }
      });
    } else {
      // Modo creación → POST
      this.clienteService.crear({
        nombre: datos.nombre!,
        dni: datos.dni!,
        email: datos.email!,
        telefono: datos.telefono!
      }).subscribe({
        next: () => {
          this.toastr.success('Cliente creado correctamente', '✅ Éxito');
          this.router.navigate(['/home/clientes/list']);
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
    this.router.navigate(['/home/clientes/list']);
  }

  // Getter para acceder fácilmente a los controles en el HTML
  // En lugar de escribir clienteForm.controls['nombre']
  // escribimos cf['nombre']
  get cf(): { [key: string]: AbstractControl } {
    return this.clienteForm.controls;
  }

  // ¿El campo es inválido y fue tocado o el form fue enviado?
  campoInvalido(campo: string): boolean {
    return !!(this.cf[campo].invalid &&
             (this.cf[campo].touched || this.submitted));
  }

  // ¿El campo es válido?
  campoValido(campo: string): boolean {
    return !!(this.cf[campo].valid && this.cf[campo].touched);
  }
}