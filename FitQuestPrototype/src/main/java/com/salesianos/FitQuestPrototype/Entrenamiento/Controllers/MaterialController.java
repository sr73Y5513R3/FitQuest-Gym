package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.CreateMateriaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.EditMaterialCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.EditTipoMaterial;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.GetMaterialDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    @Operation(summary = "Obtiene todas los materiales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todos los materiales",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetMaterialDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado materiales",
                    content = @Content)
    })
    @GetMapping("/all")
    public Page<GetMaterialDto> findAllMateriales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return materialService.findAllMateriales(pageable)
                .map(GetMaterialDto::of);
    }



    @Operation(summary = "Busca un material por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el material buscado",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetMaterialDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado materiales con ese id",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public GetMaterialDto findMaterialById(@PathVariable Long id){
        return GetMaterialDto.of(materialService.findMaterialById(id));
    }

    @Operation(summary = "Crea un material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Material creado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEjercicioDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para crear un material",
                    content = @Content)
    })
    @PostMapping("/add")
    @PreAuthorize("hasRole('ENTRENADOR') or hasRole('ADMIN')")
    public ResponseEntity<GetMaterialDto> createMaterial(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del material", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateMateriaCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """)))@RequestBody @Valid CreateMateriaCmd newMaterial) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetMaterialDto.of(materialService.save(newMaterial)));
    }

    @Operation(summary = "Edita el tipo de un material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Tipo de material actualizado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetMaterialDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para editar el tipo de un material",
                    content = @Content)
    })
    @PutMapping("/{id}/editTipo")
    @PreAuthorize("hasRole('ENTRENADOR') or hasRole('ADMIN')")
    public GetMaterialDto editTipo (@PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del material", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateMateriaCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """)))@RequestBody @Valid EditTipoMaterial newTipo){
        return GetMaterialDto.of(materialService.editTipoMaterial(id, newTipo));
    }


    @Operation(summary = "Edita un material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Material actualizado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetMaterialDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para editar un material",
                    content = @Content)
    })
    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ENTRENADOR') or hasRole('ADMIN')")
    public GetMaterialDto edit(@PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del material", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateMateriaCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """)))@RequestBody @Valid EditMaterialCmd newMaterial){
        return GetMaterialDto.of(materialService.editMaterial(id, newMaterial));
    }

    @DeleteMapping("/delete/{idMaterial}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long idMaterial){
        materialService.borrarMaterial(idMaterial);
        return ResponseEntity.noContent().build();
    }
}
