package com.salesianos.FitQuestPrototype.User.Model;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Realiza;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Valoracion;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.ValoracionId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name= "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;

    private String apellido1;
    private String apellido2;

    private String email;

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserRole> roles;

    @Builder.Default
    private boolean enabled = false;

    private String activationToken;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Realiza> entrenosRealizados = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioValorar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Valoracion> entrenosValorados = new ArrayList<>();


    @Builder.Default
    private Instant createdAt = Instant.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuario usuario  = (Usuario) o;
        return Objects.equals(username, usuario.username);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    //Valoracion

    public void addValoracion (Valoracion valoracion){
        valoracion.setUsuarioValorar(this);
        this.getEntrenosValorados().add(valoracion);
    }

    public void removeValoracion (Valoracion valoracion){
        this.getEntrenosValorados().remove(valoracion);
        valoracion.setUsuarioValorar(null);
    }

}
