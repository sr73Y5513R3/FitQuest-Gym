import { Component } from '@angular/core';
import { MaterialService } from '../../Services/material/material.service';
import { Router } from '@angular/router';
import { GetMaterialDto, Page } from '../../models/material.model';

@Component({
  selector: 'app-materiales',
  templateUrl: './materiales.component.html',
  styleUrl: './materiales.component.css'
})
export class MaterialesComponent {


  materialesPage: Page<GetMaterialDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 6; 
  searchTerm: string = '';

  constructor(
    private materialService: MaterialService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadMateriales();
  }

  loadMateriales(): void {
    this.materialService.findAllMateriales(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe({
        next: (data) => {
          this.materialesPage = data;
          console.log('Materiales cargados:', this.materialesPage);
        },
        error: (err) => {
          console.error('Error al cargar materiales:', err);
      
        }
      });
  }

  goToPage(page: number): void {
    if (page >= 0 && this.materialesPage && page < this.materialesPage.totalPages) {
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

  
  navigateToMaterialDetails(id: number): void {
    this.router.navigate(['/materiales', id]); 
  }
}


