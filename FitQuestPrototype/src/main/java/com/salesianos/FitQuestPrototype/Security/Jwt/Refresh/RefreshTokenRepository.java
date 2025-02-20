package com.salesianos.FitQuestPrototype.Security.Jwt.Refresh;

import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @Modifying
    @Transactional
    void deleteByUsuario(Usuario usuario);
}
