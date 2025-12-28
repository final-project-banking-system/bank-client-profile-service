package org.example.repository;

import org.example.model.ClientProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientProfileRepository extends JpaRepository<ClientProfileEntity, UUID> {
    Optional<ClientProfileEntity> findByEmail(String email);
}
