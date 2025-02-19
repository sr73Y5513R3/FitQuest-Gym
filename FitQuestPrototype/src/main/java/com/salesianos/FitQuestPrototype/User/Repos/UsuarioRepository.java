package com.salesianos.FitQuestPrototype.User.Repos;

import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByUsername(String username);

    Optional<Usuario> findByActivationToken(String activationToken);
}
