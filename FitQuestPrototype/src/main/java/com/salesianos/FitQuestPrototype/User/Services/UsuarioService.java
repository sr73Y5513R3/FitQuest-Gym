package com.salesianos.FitQuestPrototype.User.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.NivelService;
import com.salesianos.FitQuestPrototype.User.Dto.CreateClienteRequest;
import com.salesianos.FitQuestPrototype.User.Dto.CreateUserRequest;
import com.salesianos.FitQuestPrototype.User.Dto.EditClienteCmd;
import com.salesianos.FitQuestPrototype.User.Dto.EditEntrenadorCmd;
import com.salesianos.FitQuestPrototype.User.Error.ActivationExpiredException;
import com.salesianos.FitQuestPrototype.User.Error.EqualLevelException;
import com.salesianos.FitQuestPrototype.User.Error.UserNotAuthorizedException;
import com.salesianos.FitQuestPrototype.User.Model.*;
import com.salesianos.FitQuestPrototype.User.Repos.ClienteRepository;
import com.salesianos.FitQuestPrototype.User.Repos.EntrenadorRepository;
import com.salesianos.FitQuestPrototype.User.Repos.UsuarioRepository;
import com.salesianos.FitQuestPrototype.User.Util.MailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final NivelService nivelService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${activation.duration}")
    private int activationDuration;

    public Entrenador createEntrenador(CreateUserRequest createUserRequest) {
        Entrenador entrenador = Entrenador.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(UserRole.ENTRENADOR))
                //.activationToken(generateRandomActivationCode())
                .build();

                entrenador.setEnabled(true);

        /*mailService.sendMail(
                createUserRequest.email(),
                "Activación de cuenta",
                "Tu código de activación es: " + user.getActivationToken()
        );*/



        return entrenadorRepository.save(entrenador);
    }

    public Cliente createCliente(CreateClienteRequest createClienteRequest){

        Nivel nivel = nivelService.findNivelById(createClienteRequest.nivelId());


        Cliente cliente = Cliente.builder()
                .username(createClienteRequest.username())
                .password(passwordEncoder.encode(createClienteRequest.password()))
                .email(createClienteRequest.email())
                .altura(createClienteRequest.altura())
                .peso(createClienteRequest.peso())
                .edad(createClienteRequest.edad())
                .genero(createClienteRequest.genero())
                .roles(Set.of(UserRole.CLIENTE))
                .nivel(nivel)
                .activationToken(generateRandomActivationCode())
                .build();

        mailService.sendMail(
                createClienteRequest.email(),
                "Activación de cuenta",
                "Tu código de activación es: " + cliente.getActivationToken()
        );

        return clienteRepository.save(cliente);
    }


    public String generateRandomActivationCode() {
        return UUID.randomUUID().toString();
    }

    public Cliente activateAccount(String token) {

        return clienteRepository.findByActivationToken(token)
                .filter(cliente -> ChronoUnit.MINUTES.between(Instant.now(), cliente.getCreatedAt()) - activationDuration < 0)
                .map(cliente -> {
                    cliente.setEnabled(true);
                    cliente.setActivationToken(null);
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new ActivationExpiredException("El código de activación no existe o ha caducado"));
    }

    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Page<Cliente> findAllClientes(Pageable pageable){
        return clienteRepository.findAllCliente(pageable);
    }

    public Page<Entrenador> findAllEntrenadores(Pageable pageable){
        return entrenadorRepository.findAllEntrenador(pageable);
    }

    public Cliente findClienteById(UUID id){
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if(cliente.isEmpty())
            throw new EntityNotFoundException("Cliente no encontrado");

        return cliente.get();
    }

    public Entrenador findEntrenadorById(UUID id){
        Optional<Entrenador> entrenador = entrenadorRepository.findEntrenadorById(id);

        if (entrenador.isEmpty())
            throw new EntityNotFoundException("Entrenador no encontrado");

        return entrenador.get();
    }

    public Usuario findUsuarioById(UUID id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isEmpty())
            throw new EntityNotFoundException("Usuario no encontrado");

        return usuario.get();
    }

    public Cliente cambiarMensualidad (UUID idCliente, Mensualidad mensualidad){
        Cliente cliente = findClienteById(idCliente);

        cliente.setMensualidad(mensualidad);

        return clienteRepository.save(cliente);
    }


    public Cliente editarCliente(UUID idCliente, EditClienteCmd editCliente){
        Cliente cliente = findClienteById(idCliente);

        cliente.setNombre(editCliente.nombre());
        cliente.setApellido1(editCliente.apellido1());
        cliente.setApellido2(editCliente.apellido2());
        cliente.setEmail(editCliente.email());
        cliente.setUsername(editCliente.username());
        cliente.setPeso(editCliente.peso());
        cliente.setAltura(editCliente.altura());
        cliente.setEdad(editCliente.edad());
        cliente.setGenero(editCliente.genero());

        return clienteRepository.save(cliente);
    }

    public Entrenador editEntrenador (UUID idEntrenador, EditEntrenadorCmd editEntrenador){
        Entrenador entrenador = findEntrenadorById(idEntrenador);

        entrenador.setNombre(editEntrenador.nombre());
        entrenador.setApellido1(editEntrenador.apellido1());
        entrenador.setApellido2(editEntrenador.apellido2());
        entrenador.setEmail(editEntrenador.email());
        entrenador.setUsername(editEntrenador.username());

        return entrenadorRepository.save(entrenador);
    }

    public Usuario darDeBaja(UUID idUsuario){
        Usuario usuario = findUsuarioById(idUsuario);

        usuario.setEnabled(false);

        return usuarioRepository.save(usuario);
    }

    public Cliente cambiarNivel (UUID idCliente, Long idNivel){

        Cliente cliente = findClienteById(idCliente);

        Nivel nivel = nivelService.findNivelById(idNivel);

        if(cliente.getNivel().getId() == nivel.getId())
            throw new EqualLevelException("No puedes cambiar el nivel porque es el mismo al del usuario");

        cliente.setNivel(nivel);
        return clienteRepository.save(cliente);
    }
}
