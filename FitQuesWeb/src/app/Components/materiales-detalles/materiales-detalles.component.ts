import { Component } from '@angular/core';
import { MaterialService } from '../../Services/material/material.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GetMaterialDto } from '../../models/material.model';

@Component({
  selector: 'app-materiales-detalles',
  templateUrl: './materiales-detalles.component.html',
  styleUrl: './materiales-detalles.component.css'
})
export class MaterialesDetallesComponent {

  material: GetMaterialDto | undefined;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private materialService: MaterialService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        const materialId = +idParam; // Convierte el string a number
        this.loadMaterialDetails(materialId);
      } else {
        this.errorMessage = 'No se proporcionó un ID de material.';
      }
    });
  }

  loadMaterialDetails(id: number): void {
    this.materialService.getMaterialById(id).subscribe({
      next: (data) => {
        this.material = data;
      },
      error: (err) => {
        console.error('Error al cargar los detalles del material:', err);
        this.errorMessage = 'No se pudo cargar los detalles del material. Inténtalo de nuevo más tarde.';
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/materiales']); 
  }
}

