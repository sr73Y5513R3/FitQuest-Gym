package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.CreateRealizaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.GetRealizaDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.RealizaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RealizaController {

    private final RealizaService realizaService;

    @PostMapping("/realizado/{idUsuario}/{idEntrenamiento}")
    public ResponseEntity<GetRealizaDto> entrenamientoRealizado(@PathVariable UUID idUsuario,
                                                                @PathVariable Long idEntrenamiento,
                                                                @RequestBody CreateRealizaCmd realizaCmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetRealizaDto.of(realizaService.createRealiza(idUsuario, idEntrenamiento, realizaCmd)));
    }

    @PutMapping("/realizado/aceptar")
    public ResponseEntity<GetRealizaDto> aceptarRealizado(@RequestParam UUID idEntrenador,
                                                          @RequestParam UUID idUsuario,
                                                          @RequestParam Long idEntrenamiento) {
        return ResponseEntity.ok(GetRealizaDto.of(realizaService.aceptarEntrenamiento(idEntrenador, idUsuario, idEntrenamiento)));
    }
}
