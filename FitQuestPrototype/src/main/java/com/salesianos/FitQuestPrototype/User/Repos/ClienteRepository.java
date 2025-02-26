package com.salesianos.FitQuestPrototype.User.Repos;

import com.salesianos.FitQuestPrototype.User.Model.Cliente;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    @Query("""
    SELECT c 
    FROM Cliente c 
    WHERE c.enabled=true
""")
    Page<Cliente> findAllCliente(Pageable pageable);

    Optional<Cliente> findByActivationToken(String activationToken);
}
