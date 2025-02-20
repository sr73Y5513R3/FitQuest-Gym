package com.salesianos.FitQuestPrototype.Security.Jwt.Refresh;

import com.salesianos.FitQuestPrototype.Security.Jwt.Access.JwtService;
import com.salesianos.FitQuestPrototype.User.Dto.UserResponse;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import com.salesianos.FitQuestPrototype.User.Repos.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh.duration}")
    private int durationInMinutes;

    public RefreshToken create(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .usuario(usuario)
                        .expireAt(Instant.now().plusSeconds(durationInMinutes*60))
                        .build()
        );
    }

    public RefreshToken verify(RefreshToken refreshToken) {
        if(refreshToken.getExpireAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Token de refresco caducado. Por favor, vuelva a iniciar sesión");
        }
        return refreshToken;
    }

    public UserResponse refreshToken(String token){

        return refreshTokenRepository.findById(UUID.fromString(token))
                .map(this::verify)
                .map(RefreshToken::getUsuario)
                .map(usuario -> {
                    String accessToken = jwtService.generateAccessToken(usuario);
                    RefreshToken refreshToken = this.create(usuario);
                    return UserResponse.of(usuario, accessToken, refreshToken.getToken());
                })
                .orElseThrow(() -> new RefreshTokenException("No se ha podido refrescar el token. Por favor, vuelva a iniciar sesion"));
    }
}
