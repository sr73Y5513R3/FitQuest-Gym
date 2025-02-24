package com.salesianos.FitQuestPrototype.User.Services;

import com.salesianos.FitQuestPrototype.User.Dto.CreateClienteRequest;
import com.salesianos.FitQuestPrototype.User.Dto.CreateUserRequest;
import com.salesianos.FitQuestPrototype.User.Error.ActivationExpiredException;
import com.salesianos.FitQuestPrototype.User.Model.Cliente;
import com.salesianos.FitQuestPrototype.User.Model.UserRole;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import com.salesianos.FitQuestPrototype.User.Repos.ClienteRepository;
import com.salesianos.FitQuestPrototype.User.Repos.EntrenadorRepository;
import com.salesianos.FitQuestPrototype.User.Repos.UsuarioRepository;
import com.salesianos.FitQuestPrototype.User.Util.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${activation.duration}")
    private int activationDuration;

    public Usuario createUser(CreateUserRequest createUserRequest) {
        Usuario user = Usuario.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(UserRole.USER))
                .activationToken(generateRandomActivationCode())
                .build();

        mailService.sendMail(
                createUserRequest.email(),
                "Activación de cuenta",
                "Tu código de activación es: " + user.getActivationToken()
        );

        return usuarioRepository.save(user);
    }

    public Cliente createCliente(CreateClienteRequest createClienteRequest){
        Cliente cliente = Cliente.builder()
                .username(createClienteRequest.username())
                .password(passwordEncoder.encode(createClienteRequest.password()))
                .email(createClienteRequest.email())
                .altura(createClienteRequest.altura())
                .peso(createClienteRequest.peso())
                .edad(createClienteRequest.edad())
                .genero(createClienteRequest.genero())
                .roles(Set.of(UserRole.USER))
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

}
