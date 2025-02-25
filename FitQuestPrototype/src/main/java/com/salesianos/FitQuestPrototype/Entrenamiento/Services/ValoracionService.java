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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValoracionService {

    private final EntrenamientoService entrenamientoService;
    private final UsuarioService usuarioService;
   // private final EntrenamientoRepository entrenamientoRepository;
    //private final UsuarioRepository usuarioRepository;

    @PreAuthorize("#idUsuario == authentication.principal.id")
    public Valoracion añadirValoracion (UUID idUsuario, Long idEntrenamietno, CreateValoracionCmd newValoracion){
        Usuario usuario = usuarioService.findUsuarioById(idUsuario);

        Entrenamiento entrenamiento = entrenamientoService.findEntrenamientoById(idEntrenamietno);

        Valoracion valoracion = new Valoracion().builder()
                .usuarioValorar(usuario)
                .entrenamientoValorado(entrenamiento)
                .notaValoracion(newValoracion.notaValoracion())
                .textoDescriptivo(newValoracion.textoDescriptivo())
                .build();

        usuario.addValoracion(valoracion);
        entrenamiento.addValoracion(valoracion);

        return valoracion;

    }
}
