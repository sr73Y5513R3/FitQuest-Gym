package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.CreateMateriaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.GetMaterialDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping("/all")
    public List<GetMaterialDto> findAllMateriales(){
        return materialService.findAllMateriales()
                .stream()
                .map(GetMaterialDto::of)
                .toList();
    }

    @PostMapping("/add")
    public ResponseEntity<GetMaterialDto> createMaterial(@RequestBody CreateMateriaCmd newMaterial) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetMaterialDto.of(materialService.save(newMaterial)));
    }



}
