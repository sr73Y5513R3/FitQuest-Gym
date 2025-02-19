package com.salesianos.FitQuestPrototype.Security.Jwt.Refresh;

import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id;

    //@MapsId
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /*@NaturalId
    @Column(nullable = false, unique = true)
    private String token;
    */
    @Column(nullable = false)
    private Instant expireAt;

    @Builder.Default
    private Instant createdAt = Instant.now();

    public String getToken() {
        return id.toString();
    }
}
