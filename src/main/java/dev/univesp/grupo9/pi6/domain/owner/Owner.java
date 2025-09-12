package dev.univesp.grupo9.pi6.domain.owner;

import dev.univesp.grupo9.pi6.domain.AbstractBaseEntity;
import dev.univesp.grupo9.pi6.domain.user.UserAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "owners",
        indexes = {
                @Index(name = "ix_owner_email", columnList = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Owner extends AbstractBaseEntity {

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String name;

    @NotBlank
    @Email
    @Size(max = 120)
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Size(max = 30)
    @Column(length = 30)
    private String phone;

    /** Dono opcionalmente vinculado a uma conta de usuário do sistema */
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_account_id", unique = true)
    private UserAccount userAccount;
}
