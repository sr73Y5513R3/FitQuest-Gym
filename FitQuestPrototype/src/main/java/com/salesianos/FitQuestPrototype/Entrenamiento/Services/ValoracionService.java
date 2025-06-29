package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion.CreateValoracionCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Valoracion;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EntrenamientoRepository;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import com.salesianos.FitQuestPrototype.User.Repos.UsuarioRepository;
import com.salesianos.FitQuestPrototype.User.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValoracionService {

    private final EntrenamientoService entrenamientoService;
    private final UsuarioService usuarioService;
    private final EntrenamientoRepository entrenamientoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    @PreAuthorize("#newValoracion.idUsuario == authentication.principal.id")
    public Valoracion añadirValoracion (CreateValoracionCmd newValoracion){

        
        Usuario usuario = usuarioService.findUsuarioById(newValoracion.idUsuario());

        Entrenamiento entrenamiento = entrenamientoService.findEntrenamientoById(newValoracion.idEntrenamiento());

        Valoracion valoracion = new Valoracion().builder()
                .usuarioValorar(usuario)
                .entrenamientoValorado(entrenamiento)
                .notaValoracion(newValoracion.notaValoracion())
                .build();

        usuario.addValoracion(valoracion);
        entrenamiento.addValoracion(valoracion);

        usuarioRepository.save(usuario);
        entrenamientoRepository.save(entrenamiento);

        return valoracion;

    }
}
