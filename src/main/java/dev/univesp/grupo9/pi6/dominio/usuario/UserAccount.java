package dev.univesp.grupo9.pi6.domain.user;

import dev.univesp.grupo9.pi6.domain.AbstractBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(
        name = "user_accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_account_username", columnNames = "username")
        }
)
@Getter @Setter
public class UserAccount extends AbstractBaseEntity {

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String username;

    /**
     * Armazene apenas o HASH (ex.: BCrypt), nunca o plaintext
     */
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String passwordHash;

    /**
     * Conjunto de papéis/perfis (USER, ADMIN, SUPER_ADMIN)
     */
    @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRole.class)
    @CollectionTable(name = "user_account_roles", joinColumns = @JoinColumn(name = "user_account_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private Set<UserRole> roles;
}

