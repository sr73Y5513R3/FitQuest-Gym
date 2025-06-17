import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Entrenador, EditEntrenadorCmd, Page, Usuario } from '../../../models/usuario.model';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../../Services/usuario/usuario.service';

type EditableEntrenador = Entrenador | null;

@Component({
  selector: 'app-lista-clientes',
  templateUrl: './lista-clientes.component.html',
  styleUrls: ['./lista-clientes.component.css']
})
export class ListaClientesComponent implements OnInit {

  currentUsers: Entrenador[] = [];
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  errorMessage: string | null = null;
  successMessage: string | null = null;

  showEditModal: boolean = false;
  selectedEntrenador: EditableEntrenador = null;
  editForm: FormGroup;

  constructor(
    private usuarioService: UsuarioService,
    private fb: FormBuilder
  ) {
    this.editForm = this.fb.group({
      id: [{ value: '', disabled: true }],
      nombre: ['', Validators.required],
      apellido1: ['', Validators.required],
      apellido2: [''],
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
  this.errorMessage = null;
  this.successMessage = null;

  this.usuarioService.findAllEntrenadores(this.currentPage, this.pageSize).subscribe({
    next: (pageData: any) => { 
      this.currentUsers = pageData.content;
      this.totalPages = pageData.totalPages;
      this.totalElements = pageData.totalElements;
      console.log(`Cargados ${this.currentUsers.length} entrenadores. Total: ${this.totalElements}`);
    },
    error: (err: HttpErrorResponse) => {
      console.error('Error al cargar la lista de entrenadores:', err);
      this.errorMessage = 'Error al cargar los datos de entrenadores. Inténtalo de nuevo.';
    }
  });
}

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadUsers();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadUsers();
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadUsers();
    }
  }

  openEditModal(entrenador: Entrenador): void {
    this.selectedEntrenador = entrenador;
    this.showEditModal = true;

    this.editForm.patchValue({
      id: entrenador.id,
      username: entrenador.Username
    });
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.selectedEntrenador = null;
    this.editForm.reset();
  }

  saveEdit(): void {
    if (this.editForm.invalid || !this.selectedEntrenador) {
      this.errorMessage = 'Por favor, completa todos los campos requeridos.';
      return;
    }

    const id: string = this.selectedEntrenador.id;
    const formData = this.editForm.value;

    const entrenadorData: EditEntrenadorCmd = {
      nombre: formData.nombre,
      apellido1: formData.apellido1,
      apellido2: formData.apellido2,
      email: formData.email,
      username: formData.username
    };

    this.usuarioService.editEntrenador(id, entrenadorData).subscribe({
      next: (updatedEntrenador) => {
        this.successMessage = `Entrenador ${formData.nombre || updatedEntrenador.Username} actualizado con éxito.`
        this.closeEditModal();
        this.loadUsers();
        setTimeout(() => this.successMessage = null, 3000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al guardar la edición:', err);
        this.errorMessage = `Error al actualizar: ${err.error?.message || err.message || 'Error desconocido'}`;
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  confirmDelete(entrenador: Entrenador): void {
    if (confirm(`¿Estás seguro de que quieres dar de baja a ${entrenador.Username}? Esta acción lo deshabilitará.`)) {
      this.usuarioService.darDeBaja(entrenador.id).subscribe({
        next: (updatedUser: Usuario) => {
          this.successMessage = `Entrenador ${updatedUser.username} dado de baja (deshabilitado) con éxito.`;
          this.loadUsers();
          setTimeout(() => this.successMessage = null, 3000);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al dar de baja:', err);
          this.errorMessage = `Error al dar de baja: ${err.error?.message || err.message || 'Error desconocido'}`;
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
    }
  }
}