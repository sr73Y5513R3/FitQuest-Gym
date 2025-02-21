package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.CreateMateriaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.GetMaterialDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping("/add")
    public ResponseEntity<GetMaterialDto> createMaterial(@RequestBody CreateMateriaCmd newMaterial) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetMaterialDto.of(materialService.save(newMaterial)));
    }

}
