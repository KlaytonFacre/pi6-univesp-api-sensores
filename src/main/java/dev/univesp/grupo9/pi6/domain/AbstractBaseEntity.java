package dev.univesp.grupo9.pi6.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    protected Long id;

    @Column(nullable = false, updatable = false, unique = true, columnDefinition = "BINARY(16)")
    @EqualsAndHashCode.Include
    protected UUID publicId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Instant createdAt;

    @CreatedBy
    @Column(updatable = false)
    protected Long createdBy;

    @LastModifiedDate
    @Column(nullable = false)
    protected Instant updatedAt;

    @LastModifiedBy
    protected Long updatedBy;

    protected Instant deletedAt;
    protected Long deletedBy;

    @Column(length = 255)
    protected String deleteReason;

    @Version
    protected Long version;

    @PrePersist
    void prePersist() {
        if (publicId == null) publicId = UUID.randomUUID();
    }
}
