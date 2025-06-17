package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.AceptarRealizaDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.CreateRealizaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.GetRealizaDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.RealizaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RealizaController {

    private final RealizaService realizaService;

    @PostMapping("/realizado")
    public ResponseEntity<GetRealizaDto> entrenamientoRealizado(@RequestBody @Valid CreateRealizaCmd realizaCmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetRealizaDto.of(realizaService.createRealiza(realizaCmd)));
    }

    @PutMapping("/realizado/aceptar")
    @PreAuthorize("hasRole('ENTRENADOR')")
    public ResponseEntity<GetRealizaDto> aceptarRealizado(@RequestParam @NotNull(message = "El ID del entrenador no puede ser nulo") UUID idEntrenador,
                                                          @RequestParam @NotNull(message = "El ID del usuario no puede ser nulo") UUID idUsuario,
                                                          @RequestParam @NotNull(message = "El ID del entrenamiento no puede ser nulo") Long idEntrenamiento) {
        return ResponseEntity.ok(GetRealizaDto.of(realizaService.aceptarEntrenamiento(idEntrenador, idUsuario, idEntrenamiento)));
    }

    @GetMapping("/realizado/all")
    @PreAuthorize("hasRole('ENTRENADOR')")
    public Page<GetRealizaDto> getAllSinRealizar(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return realizaService.findAllSinAceptar(pageable)
                .map(GetRealizaDto::of);
    }

    @GetMapping("/realizado/all/aceptados")
    @PreAuthorize("hasRole('ENTRENADOR')")
    public Page<GetRealizaDto> getAllAceptados(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return realizaService.findAllAceptados(pageable)
                .map(GetRealizaDto::of);
    }

    @GetMapping("/realizado/all/{idUsuario}")
    public Page<GetRealizaDto> getAllByUser(@PathVariable UUID idUsuario, @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return realizaService.findAllByUser(pageable, idUsuario)
                .map(GetRealizaDto::of);
    }
}
