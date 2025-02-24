package com.salesianos.FitQuestPrototype.User.Controllers;

import com.salesianos.FitQuestPrototype.Security.Jwt.Access.JwtService;
import com.salesianos.FitQuestPrototype.Security.Jwt.Refresh.RefreshToken;
import com.salesianos.FitQuestPrototype.Security.Jwt.Refresh.RefreshTokenRequest;
import com.salesianos.FitQuestPrototype.Security.Jwt.Refresh.RefreshTokenService;
import com.salesianos.FitQuestPrototype.User.Dto.*;
import com.salesianos.FitQuestPrototype.User.Model.Cliente;
import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import com.salesianos.FitQuestPrototype.User.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class



UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest createUserRequest) {
        Entrenador user = usuarioService.createEntrenador(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @PostMapping("/auth/register/cliente")
    public ResponseEntity<UserResponse> registerCliente(@RequestBody CreateClienteRequest createClienteRequest) {
        Cliente cliente = usuarioService.createCliente(createClienteRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(cliente));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {


        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario user = (Usuario) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        // Generar el token de refresco
        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));

    }

    @PostMapping("/auth/refresh/token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest req) {
        String token = req.refreshToken();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));

    }

    @PostMapping("/activate/account")
    public ResponseEntity<?> activateAccount(@RequestBody ActivateAccountRequest req) {
        String token = req.token();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(usuarioService.activateAccount(token)));
    }

    @GetMapping("/usuarios/all")
    public List<GetUsuarioDto> findAll(){
        return usuarioService.findAll().stream()
                .map(GetUsuarioDto::of).toList();
    }

    @GetMapping("/cliente/all")
    public List<GetClienteDto> findAllClientes(){
        return usuarioService.findAllClientes().stream()
                .map(GetClienteDto::of).toList();
    }

    @GetMapping("/entrenador/all")
    public List<GetEntrenadorConEntrenoDto> findAllEntrenadores(){
        return usuarioService.findAllEntrenadores().stream()
                .map(GetEntrenadorConEntrenoDto::of).toList();
    }

    @GetMapping("/cliente/{id}")
    public GetClienteDto findClienteById(@PathVariable UUID id) {
        return GetClienteDto.of(usuarioService.findClienteById(id));
    }

    @GetMapping("/entrenador/{id}")
    public GetEntrenadorConEntrenoDto findEntrenadorById(@PathVariable UUID id) {
        return GetEntrenadorConEntrenoDto.of(usuarioService.findEntrenadorById(id));
    }
}
