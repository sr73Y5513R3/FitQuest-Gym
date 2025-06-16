import { Component, ViewChild } from '@angular/core';
import { GetMaterialDto, MaterialCreateUpdateDto, Page } from '../../../models/material.model';
import { NgForm, Validators } from '@angular/forms';
import { MaterialService } from '../../../Services/material/material.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-lista-materiales',
  templateUrl: './lista-materiales.component.html',
  styleUrl: './lista-materiales.component.css'
})
export class ListaMaterialesComponent {
  @ViewChild('materialFormRef') materialFormRef!: NgForm; 

  materialesPage: Page<GetMaterialDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 5;
  searchTerm: string = '';

  showMaterialForm: boolean = false;
  // Propiedades para almacenar los datos del formulario
  materialData: { nombre: string; descripcion: string; tipo: string; } = {
    nombre: '',
    descripcion: '',
    tipo: ''
  };
  isEditMode: boolean = false;
  currentMaterialId: number | null = null;

  materialTypes: string[] = ['PESA', 'MAQUINA'];

  constructor(
    private materialService: MaterialService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.loadMateriales();
  }

  // Ya no necesitamos initForm() ni FormBuilder

  loadMateriales(): void {
    this.materialService.findAllMateriales(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe({
        next: (data) => {
          this.materialesPage = data;
          console.log('Materiales cargados (admin):', this.materialesPage);
        },
        error: (err) => {
          console.error('Error al cargar materiales (admin):', err);
          // Manejo de errores
        }
      });
  }

  // --- Métodos de Paginación (iguales) ---
  goToPage(page: number): void {
    if (this.materialesPage && page >= 0 && page < this.materialesPage.totalPages) {
      this.currentPage = page;
      this.loadMateriales();
    }
  }

  nextPage(): void {
    if (this.materialesPage && !this.materialesPage.last) {
      this.currentPage++;
      this.loadMateriales();
    }
  }

  previousPage(): void {
    if (this.materialesPage && !this.materialesPage.first) {
      this.currentPage--;
      this.loadMateriales();
    }
  }

  onSearchChange(): void {
    this.currentPage = 0;
    this.loadMateriales();
  }

  // --- Métodos del Formulario (actualizados) ---
  openCreateForm(): void {
    this.isEditMode = false;
    this.currentMaterialId = null;
    // Limpiar las propiedades del modelo del formulario
    this.materialData = { nombre: '', descripcion: '', tipo: '' };
    this.showMaterialForm = true;
    // Resetear el estado del formulario una vez que se ha mostrado y el DOM está listo
    // Esto se puede hacer en un setTimeout o con ngAfterViewInit si el formulario no está oculto al inicio
    setTimeout(() => {
        if (this.materialFormRef) {
            this.materialFormRef.resetForm();
        }
    });
  }

  openEditForm(material: GetMaterialDto): void {
    this.isEditMode = true;
    this.currentMaterialId = material.id;
    // Rellenar las propiedades del modelo con los datos del material
    this.materialData = {
      nombre: material.nombre,
      descripcion: material.descripcion,
      tipo: material.tipo
    };
    this.showMaterialForm = true;
    setTimeout(() => {
        if (this.materialFormRef) {
            // Usa patchValue si quieres setear solo algunos campos o reset para setear todo
            this.materialFormRef.form.markAsPristine();
            this.materialFormRef.form.markAsUntouched();
            this.materialFormRef.form.updateValueAndValidity();
        }
    });
  }

  cancelForm(): void {
    this.showMaterialForm = false;
    // Limpiar las propiedades del modelo del formulario
    this.materialData = { nombre: '', descripcion: '', tipo: '' };
    // Opcional: Resetear el formulario si aún es visible antes de ocultarse
    if (this.materialFormRef) {
        this.materialFormRef.resetForm();
    }
  }

  saveMaterial(): void {
    // Validación manual
    if (!this.materialData.nombre || !this.materialData.descripcion || !this.materialData.tipo) {
      console.warn('Formulario inválido. Todos los campos son obligatorios.');
      // Aquí puedes añadir lógica para mostrar mensajes de error al usuario
      // (por ejemplo, setting flags o usando un servicio de notificaciones)
      return;
    }

    if (this.isEditMode && this.currentMaterialId) {
      // Modo edición
      this.materialService.updateMaterial(this.currentMaterialId, this.materialData as MaterialCreateUpdateDto)
        .subscribe({
          next: () => {
            console.log('Material actualizado con éxito.');
            this.loadMateriales();
            this.cancelForm();
          },
          error: (err) => {
            console.error('Error al actualizar el material:', err);
            // Manejo de errores
          }
        });
    } else {
      // Modo creación
      this.materialService.createMaterial(this.materialData as MaterialCreateUpdateDto)
        .subscribe({
          next: () => {
            console.log('Material creado con éxito.');
            this.loadMateriales();
            this.cancelForm();
          },
          error: (err) => {
            console.error('Error al crear el material:', err);
            // Manejo de errores
          }
        });
    }
  }

  deleteMaterial(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este material?')) {
      this.materialService.deleteMaterial(id)
        .subscribe({
          next: () => {
            console.log('Material eliminado con éxito.');
            this.loadMateriales();
          },
          error: (err) => {
            console.error('Error al eliminar el material:', err);
            // Manejo de errores
          }
        });
    }
  }
}