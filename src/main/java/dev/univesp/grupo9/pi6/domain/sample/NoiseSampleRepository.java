package dev.univesp.grupo9.pi6.domain.sample;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NoiseSampleRepository extends JpaRepository<NoiseSample, Long> {
    Optional<NoiseSample> findByPublicId(UUID publicId);
}
