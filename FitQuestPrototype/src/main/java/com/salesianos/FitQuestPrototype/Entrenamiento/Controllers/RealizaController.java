package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.CreateRealizaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.GetRealizaDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.RealizaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RealizaController {

    private final RealizaService realizaService;

    @PostMapping("/realizado/{idUsuario}/{idEntrenamiento}")
    @PreAuthorize("(#idUsuario == authentication.principal.id)")
    public ResponseEntity<GetRealizaDto> entrenamientoRealizado(@PathVariable UUID idUsuario,
                                                                @PathVariable Long idEntrenamiento,
                                                                @RequestBody @Valid CreateRealizaCmd realizaCmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetRealizaDto.of(realizaService.createRealiza(idUsuario, idEntrenamiento, realizaCmd)));
    }

    @PutMapping("/realizado/aceptar")
    @PreAuthorize("hasRole('ENTRENADOR')")
    public ResponseEntity<GetRealizaDto> aceptarRealizado(@RequestParam @NotNull(message = "El ID del entrenador no puede ser nulo") UUID idEntrenador,
                                                          @RequestParam @NotNull(message = "El ID del usuario no puede ser nulo") UUID idUsuario,
                                                          @RequestParam @NotNull(message = "El ID del entrenamiento no puede ser nulo") Long idEntrenamiento) {
        return ResponseEntity.ok(GetRealizaDto.of(realizaService.aceptarEntrenamiento(idEntrenador, idUsuario, idEntrenamiento)));
    }
}
