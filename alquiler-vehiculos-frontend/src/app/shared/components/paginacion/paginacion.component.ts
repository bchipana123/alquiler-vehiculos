import { Component, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-paginacion',
  imports: [CommonModule],
  templateUrl: './paginacion.component.html',
  styleUrl: './paginacion.component.css'
})
export class PaginacionComponent implements OnChanges {

  @Input() totalPages: number = 0;
  @Input() paginaActual: number = 0;
  @Input() totalElements: number = 0;
  @Input() itemsEnPagina: number = 0;
  @Input() labelItems: string = 'registros';

  // @Output = emite eventos al componente padre
  @Output() paginaCambiada = new EventEmitter<number>();

  paginas: number[] = [];

  // Se ejecuta cuando cambian los @Input
  ngOnChanges(): void {
    this.paginas = Array.from({ length: this.totalPages }, (_, i) => i);
  }

  irPagina(pagina: number): void {
    if (pagina < 0 || pagina >= this.totalPages) return;
    this.paginaCambiada.emit(pagina);
  }

  get esUltimaPagina(): boolean {
    return this.paginaActual >= this.totalPages - 1;
  }
}