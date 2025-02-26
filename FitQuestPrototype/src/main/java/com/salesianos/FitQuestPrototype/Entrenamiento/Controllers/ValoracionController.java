package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion.CreateValoracionCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion.GetValoracionDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.ValoracionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/valoracion")
public class ValoracionController {

    public final ValoracionService valoracionService;

    @PostMapping("/add")
    public GetValoracionDto añadirValoracion (@RequestParam @NotNull UUID idUsuario,
                                              @RequestParam @NotNull Long idEntreno,
                                              @RequestBody @Valid CreateValoracionCmd newValoracion) {
        return GetValoracionDto.of(valoracionService.añadirValoracion(idUsuario, idEntreno, newValoracion));
    }
}
