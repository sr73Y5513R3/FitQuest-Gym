package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.*;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.EntrenamientoService;
import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import com.salesianos.FitQuestPrototype.User.Services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entrenamiento")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;
    private final UsuarioService usuarioService;


    @Operation(summary = "Obtiene todas los entrenamientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todos los entrenamientos",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEntrenoConEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado entrenamientos",
                    content = @Content)
    })
    @GetMapping("/all")
    public Page<GetEntrenoConEjercicioDto> findAllEntrenamientos(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return entrenamientoService.findAllEntrenamientos(pageable)
                .map(GetEntrenoConEjercicioDto::of);

    }

    @Operation(summary = "Obtiene todas los entrenamientos de un entrenador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todos los entrenamientos",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEntrenoConEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado entrenamientos",
                    content = @Content)
    })
    @GetMapping("/all/entrenador/{idEntrenador}")
    public Page<GetEntrenoConEjercicioDto> findAllEntrenamientosByEntrenador(@PathVariable UUID idEntrenador,@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size ){
        Pageable pageable = PageRequest.of(page, size);
        Entrenador entrenador = usuarioService.findEntrenadorById(idEntrenador);

        return entrenamientoService.findAllEntrenamientosByEntrenador(entrenador, pageable)
                .map(GetEntrenoConEjercicioDto::of);
    }

    @Operation(summary = "Busca un entrenamietno por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el entrenamiento buscado",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEntrenoConEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado el entrenamiento con ese id",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public GetEntrenoConEjercicioDto findById(@PathVariable Long id){
        return GetEntrenoConEjercicioDto.of(entrenamientoService.findEntrenamientoById(id));
    }

    @Operation(summary = "Crea un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Entrenamiento creado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenamientoDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para crear un entrenamiento",
                    content = @Content)
    })
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or #createEntrenoCmd.entrenadorId() == authentication.principal.id")
    public ResponseEntity<GetEntrenamientoDto> save (@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del entrenamiento", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateEntrenoCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """)))@RequestBody @Valid CreateEntrenoCmd createEntrenoCmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetEntrenamientoDto.of(entrenamientoService.save(createEntrenoCmd)));
    }

    @Operation(summary = "Edita un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Entrenamiento editado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenamientoDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para editar el enetrenamiento",
                    content = @Content)
    })
    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTRENADOR')")
    public GetEntrenamientoDto update (@PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del entrenamiento", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EditEntrenoCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """))) @RequestBody @Valid EditEntrenoCmd editEntrenoCmd){
        return GetEntrenamientoDto.of(entrenamientoService.edit(id, editEntrenoCmd));
    }

    @Operation(summary = "Añade un ejercicio a un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha añadido correctamente el ejercicio",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenoConEjercicioDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Alguno de los id son inválidos",
                    content = @Content)
    })
    @PostMapping("{idEntrenamiento}/ejercicio/{idEjercicio}")
    @PreAuthorize("hasRole('ADMIN') or @entrenamientoService.isEntrenador(#idEntrenamiento)")
    public ResponseEntity<GetEntrenoConEjercicioDto> addEjercicio (@PathVariable Long idEntrenamiento, @PathVariable Long idEjercicio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetEntrenoConEjercicioDto.of(entrenamientoService.añadirEjercicio(idEntrenamiento, idEjercicio)));
    }

    @Operation(summary = "Elimina un ejercicio de un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Ejercicio eliminado con éxito",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado las entidades con esos valores",
                    content = @Content)
    })
    @DeleteMapping("{idEntrenamiento}/ejercicio/{idEjercicio}")
    @PreAuthorize("hasRole('ADMIN') or @entrenamientoService.isEntrenador(#idEntrenamiento)")
    public ResponseEntity<?> removeEjercicio(@PathVariable Long idEntrenamiento, @PathVariable Long idEjercicio) {
        entrenamientoService.eliminarEjercicio(idEntrenamiento, idEjercicio);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Edita el nivel de un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha actualizado correctamente el nivel",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenoConNivelDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Alguno de los id son inválidos",
                    content = @Content)
    })
    @PostMapping("/{id}/editNivel/{idNivel}")
    @PreAuthorize("hasRole('ADMIN') or @entrenamientoService.isEntrenador(#id)")
    public GetEntrenoConNivelDto editNivel (@PathVariable Long id, @PathVariable Long idNivel) {
        return GetEntrenoConNivelDto.of(entrenamientoService.actualizarNivel(id, idNivel));
    }

    @Operation(summary = "Obtiene un entrenamiento buscado por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado un entrenamiento con ese nombre",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEntrenoConEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado entrenamientos con ese nombre",
                    content = @Content)
    })
    @GetMapping("/nombre")
    public GetEntrenoConEjercicioDto findByNombre(@RequestParam @NotBlank(message = "El nombre no puede estar vacío")
                                                      @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
                                                      String nombre) {
        return GetEntrenoConEjercicioDto.of(entrenamientoService.findByNombre(nombre));
    }

    @Operation(summary = "Elimina un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Entrenamiento eliminado con éxito",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado el entrenamiento",
                    content = @Content)
    })
    @DeleteMapping("/delete/{idEntrenamiento}")
    @PreAuthorize("hasRole('ADMIN') or @entrenamientoService.isEntrenador(#idEntrenamiento)")
    public ResponseEntity<?> deleteEntrenamiento (@PathVariable Long idEntrenamiento) {
        entrenamientoService.removeEntrenamietno(idEntrenamiento);
        return ResponseEntity.noContent().build();
    }

}
