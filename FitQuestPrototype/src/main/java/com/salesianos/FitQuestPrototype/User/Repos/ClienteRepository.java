package com.salesianos.FitQuestPrototype.User.Repos;

import com.salesianos.FitQuestPrototype.User.Model.Cliente;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByActivationToken(String activationToken);
}
