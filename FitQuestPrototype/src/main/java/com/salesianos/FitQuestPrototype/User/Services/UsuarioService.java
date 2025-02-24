package com.salesianos.FitQuestPrototype.User.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import com.salesianos.FitQuestPrototype.User.Dto.CreateClienteRequest;
import com.salesianos.FitQuestPrototype.User.Dto.CreateUserRequest;
import com.salesianos.FitQuestPrototype.User.Error.ActivationExpiredException;
import com.salesianos.FitQuestPrototype.User.Model.Cliente;
import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import com.salesianos.FitQuestPrototype.User.Model.UserRole;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import com.salesianos.FitQuestPrototype.User.Repos.ClienteRepository;
import com.salesianos.FitQuestPrototype.User.Repos.EntrenadorRepository;
import com.salesianos.FitQuestPrototype.User.Repos.UsuarioRepository;
import com.salesianos.FitQuestPrototype.User.Util.MailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final NivelRepository nivelRepository;
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

        Optional<Nivel> nivelOpt = nivelRepository.findById(createClienteRequest.nivelId());
        if (nivelOpt.isEmpty())
            throw new EntityNotFoundException("Nivel no encontrado");

        Cliente cliente = Cliente.builder()
                .username(createClienteRequest.username())
                .password(passwordEncoder.encode(createClienteRequest.password()))
                .email(createClienteRequest.email())
                .altura(createClienteRequest.altura())
                .peso(createClienteRequest.peso())
                .edad(createClienteRequest.edad())
                .genero(createClienteRequest.genero())
                .roles(Set.of(UserRole.CLIENTE))
                .nivel(nivelOpt.get())
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




    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public List<Cliente> findAllClientes(){
        return clienteRepository.findAll();
    }

    public List<Entrenador> findAllEntrenadores(){
        return entrenadorRepository.findAll();
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



}
