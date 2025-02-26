package com.salesianos.FitQuestPrototype.User.Repos;

import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Page<Usuario> findAll(Pageable pageable);

    Optional<Usuario> findFirstByUsername(String username);

    Optional<Usuario> findByActivationToken(String activationToken);
}
