package com.salesianos.FitQuestPrototype.User.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Security.Jwt.Access.JwtService;
import com.salesianos.FitQuestPrototype.Security.Jwt.Refresh.RefreshToken;
import com.salesianos.FitQuestPrototype.Security.Jwt.Refresh.RefreshTokenRequest;
import com.salesianos.FitQuestPrototype.Security.Jwt.Refresh.RefreshTokenService;
import com.salesianos.FitQuestPrototype.User.Dto.*;
import com.salesianos.FitQuestPrototype.User.Model.Cliente;
import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import com.salesianos.FitQuestPrototype.User.Model.Mensualidad;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import com.salesianos.FitQuestPrototype.User.Services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Registra un nuevo entrenador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Entrenador registrado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para registrar un entrenador",
                    content = @Content)
    })
    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la solicitud para registrar un entrenador", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserRequest.class),
                            examples = @ExampleObject(value = """
                {
                    "nombre": "Juan",
                    "apellido1": "Pérez",
                    "apellido2": "Gómez",
                    "email": "juan@example.com",
                    "username": "juanperez",
                    "password": "password123",
                    "verifyPassword": "password123"
                }
            """)))
            @RequestBody @Valid CreateUserRequest createUserRequest) {
        Entrenador user = usuarioService.createEntrenador(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(user));
    }

    @Operation(summary = "Registra un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Cliente registrado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para registrar un cliente",
                    content = @Content)
    })
    @PostMapping("/auth/register/cliente")
    public ResponseEntity<UserResponse> registerCliente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la solicitud para registrar un cliente", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateClienteRequest.class),
                            examples = @ExampleObject(value = """
                {
                    "nombre": "Ana",
                    "apellido1": "López",
                    "apellido2": "Martínez",
                    "email": "ana@example.com",
                    "username": "analopez",
                    "password": "password123",
                    "verifyPassword": "password123",
                    "peso": 65.5,
                    "altura": 1.70,
                    "edad": 25,
                    "genero": "FEMENINO",
                    "nivelId": 1
                }
            """)))
            @RequestBody @Valid CreateClienteRequest createClienteRequest) {
        Cliente cliente = usuarioService.createCliente(createClienteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(cliente));
    }

    @Operation(summary = "Inicia sesión y genera tokens de acceso y refresco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Inicio de sesión exitoso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Credenciales inválidas",
                    content = @Content)
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la solicitud para iniciar sesión", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
                {
                    "username": "juanperez",
                    "password": "password123"
                }
            """)))
            @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario user = (Usuario) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));
    }

    @Operation(summary = "Refresca el token de acceso usando un token de refresco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Token de acceso refrescado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Token de refresco inválido",
                    content = @Content)
    })
    @PostMapping("/auth/refresh/token")
    public ResponseEntity<?> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la solicitud para refrescar el token", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(value = """
                {
                    "refreshToken": "token_de_refresco"
                }
            """)))
            @RequestBody @Valid RefreshTokenRequest req) {
        String token = req.refreshToken();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));
    }

    @Operation(summary = "Activa una cuenta usando un token de activación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Cuenta activada con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Token de activación inválido",
                    content = @Content)
    })
    @PostMapping("/activate/account")
    public ResponseEntity<?> activateAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la solicitud para activar la cuenta", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivateAccountRequest.class),
                            examples = @ExampleObject(value = """
                {
                    "token": "token_de_activacion"
                }
            """)))
            @RequestBody @Valid ActivateAccountRequest req) {
        String token = req.token();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(usuarioService.activateAccount(token)));
    }

    @Operation(summary = "Obtiene una lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de usuarios obtenida con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUsuarioDto.class))})
    })
    @GetMapping("/usuarios/all")
    public List<GetUsuarioDto> findAll() {
        return usuarioService.findAll().stream()
                .map(GetUsuarioDto::of).toList();
    }

    @Operation(summary = "Obtiene una lista de todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de clientes obtenida con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetClienteDto.class))})
    })
    @GetMapping("/cliente/all")
    public List<GetClienteDto> findAllClientes() {
        return usuarioService.findAllClientes().stream()
                .map(GetClienteDto::of).toList();
    }

    @Operation(summary = "Obtiene una lista de todos los entrenadores con sus entrenamientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de entrenadores obtenida con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenadorConEntrenoDto.class))})
    })
    @GetMapping("/entrenador/all")
    public List<GetEntrenadorConEntrenoDto> findAllEntrenadores() {
        return usuarioService.findAllEntrenadores().stream()
                .map(GetEntrenadorConEntrenoDto::of).toList();
    }

    @Operation(summary = "Obtiene un cliente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente obtenido con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetClienteDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content)
    })
    @GetMapping("/cliente/{id}")
    public GetClienteDto findClienteById(
            @Parameter(description = "ID del cliente", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID id) {
        return GetClienteDto.of(usuarioService.findClienteById(id));
    }

    @Operation(summary = "Obtiene un entrenador por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Entrenador obtenido con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenadorConEntrenoDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Entrenador no encontrado",
                    content = @Content)
    })
    @GetMapping("/entrenador/{id}")
    public GetEntrenadorConEntrenoDto findEntrenadorById(
            @Parameter(description = "ID del entrenador", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID id) {
        return GetEntrenadorConEntrenoDto.of(usuarioService.findEntrenadorById(id));
    }

    @Operation(summary = "Obtiene un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario obtenido con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUsuarioDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content)
    })
    @GetMapping("/usuario/{id}")
    public GetUsuarioDto findUsuarioById(
            @Parameter(description = "ID del usuario", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID id) {
        return GetUsuarioDto.of(usuarioService.findUsuarioById(id));
    }

    @Operation(summary = "Cambia la mensualidad de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Mensualidad cambiada con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetClienteDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para cambiar la mensualidad",
                    content = @Content)
    })
    @PutMapping("/cliente/{idCliente}/mensualidad")
    public GetClienteDto cambiarMensualidad(
            @Parameter(description = "ID del cliente", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID idCliente,
            @Parameter(description = "Nueva mensualidad", required = true, example = "PREMIUM")
            @RequestParam @NotNull Mensualidad mensualidad) {
        return GetClienteDto.of(usuarioService.cambiarMensualidad(idCliente, mensualidad));
    }

    @Operation(summary = "Edita la información de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente editado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetClienteDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para editar el cliente",
                    content = @Content)
    })
    @PutMapping("cliente/edit/{idCliente}")
    @PreAuthorize("(#idCliente == authentication.principal.id) or hasRole('ADMIN')")
    public GetClienteDto editCliente(
            @Parameter(description = "ID del cliente", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID idCliente,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la solicitud para editar el cliente", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditClienteCmd.class),
                            examples = @ExampleObject(value = """
                {
                    "nombre": "Ana",
                    "apellido1": "López",
                    "apellido2": "Martínez",
                    "email": "ana@example.com",
                    "username": "analopez",
                    "peso": 65.5,
                    "altura": 1.70,
                    "edad": 25,
                    "genero": "FEMENINO"
                }
            """)))
            @RequestBody @Valid EditClienteCmd editClienteCmd) {
        return GetClienteDto.of(usuarioService.editarCliente(idCliente, editClienteCmd));
    }

    @Operation(summary = "Edita la información de un entrenador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Entrenador editado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenadorConEntrenoDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para editar el entrenador",
                    content = @Content)
    })
    @PutMapping("/entrenador/edit/{idEntrenador}")
    @PreAuthorize("(#idEntrenador == authentication.principal.id) or hasRole('ADMIN')")
    public GetEntrenadorConEntrenoDto editEntrenador(
            @Parameter(description = "ID del entrenador", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID idEntrenador,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la solicitud para editar el entrenador", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditEntrenadorCmd.class),
                            examples = @ExampleObject(value = """
                {
                    "nombre": "Juan",
                    "apellido1": "Pérez",
                    "apellido2": "Gómez",
                    "email": "juan@example.com",
                    "username": "juanperez"
                }
            """)))
            @RequestBody @Valid EditEntrenadorCmd editEntrenadorCmd) {
        return GetEntrenadorConEntrenoDto.of(usuarioService.editEntrenador(idEntrenador, editEntrenadorCmd));
    }

    @Operation(summary = "Da de baja a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Usuario dado de baja con éxito",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content)
    })
    @PutMapping("usuario/baja/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> darDeBaja(
            @Parameter(description = "ID del usuario", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID idUsuario) {
        usuarioService.darDeBaja(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambia el nivel de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Nivel cambiado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetClienteDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para cambiar el nivel",
                    content = @Content)
    })
    @PutMapping("cliente/edit/{idCliente}/nivel")
    @PreAuthorize("hasRole('ADMIN')")
    public GetClienteDto cambiarNivel(
            @Parameter(description = "ID del cliente", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable @NotNull UUID idCliente,
            @Parameter(description = "Nuevo nivel", required = true, example = "2")
            @RequestParam @NotNull Long nivel) {
        return GetClienteDto.of(usuarioService.cambiarNivel(idCliente, nivel));
    }
}
