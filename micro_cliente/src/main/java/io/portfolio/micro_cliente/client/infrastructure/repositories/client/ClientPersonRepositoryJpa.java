package io.portfolio.micro_cliente.client.infrastructure.repositories.client;

import io.portfolio.micro_cliente.client.domain.entities.client.ClientPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPersonRepositoryJpa extends JpaRepository<ClientPersonEntity, Long> {

    Optional<ClientPersonEntity> findByCpf(String cpf);
}
