package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.CreateRealizaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Realiza;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EntrenamientoRepository;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.RealizarRepository;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import com.salesianos.FitQuestPrototype.User.Repos.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RealizaService {

    private final RealizarRepository realizarRepository;
    private final EntrenamientoRepository entrenamientoRepository;
    private final UsuarioRepository usuarioRepository;

    @PreAuthorize("#idUsuario == authentication.principal.id")
    @Transactional
    public Realiza createRealiza(UUID idUsuario, Long idEntrenamiento, CreateRealizaCmd realizaCmd) {

        Optional<Entrenamiento> entrenamientoOpt = entrenamientoRepository.findEntrenamientoById(idEntrenamiento);

        Optional <Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if(entrenamientoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            throw new EntityNotFoundException("Entidades no encontradas con ese id");
        }

        Entrenamiento entrenamiento = entrenamientoOpt.get();
        Usuario usuario = usuarioOpt.get();

        Realiza realiza = new Realiza().builder()
                .realizado(false)
                .imagen(realizaCmd.imagen())
                .usuario(usuario)
                .entrenamiento(entrenamiento)
                .build();

        return realizarRepository.save(realiza);
    }

}
