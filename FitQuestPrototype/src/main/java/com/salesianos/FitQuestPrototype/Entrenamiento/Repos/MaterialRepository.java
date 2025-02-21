package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    @Query("""
    SELECT m
    FROM Material m
""")
    List<Material> FindAllMateriales();


    @Query("""
    SELECT m
    FROM Material m
    WHERE m.id= :id
""")
    Optional<Material> findMaterialById(@PathVariable("id")Long id);
}
