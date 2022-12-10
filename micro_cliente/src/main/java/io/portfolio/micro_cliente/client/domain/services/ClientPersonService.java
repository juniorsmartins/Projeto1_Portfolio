package io.portfolio.micro_cliente.client.domain.services;

import io.portfolio.micro_cliente.client.domain.client.ClientPersonEntity;
import io.portfolio.micro_cliente.client.domain.dtos.ClientPersonDTORequest;
import io.portfolio.micro_cliente.client.domain.dtos.ClientPersonDTOResponse;
import io.portfolio.micro_cliente.client.domain.filter.ClientPersonFilter;
import io.portfolio.micro_cliente.client.domain.ports.PolicyRepository;
import io.portfolio.micro_cliente.shared.exceptions.BusinessRuleViolationCustomException;
import io.portfolio.micro_cliente.shared.exceptions.ResourceNotFoundCustomException;
import io.portfolio.micro_cliente.shared.messages.MessagesProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public non-sealed class ClientPersonService implements PolicyService<ClientPersonDTORequest, ClientPersonFilter,
        ClientPersonDTOResponse, ClientPersonEntity, Long> {

    @Autowired
    private PolicyRepository<ClientPersonEntity, Long> repository;

    @Autowired
    private MessagesProperties messages;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public ClientPersonDTOResponse create(ClientPersonDTORequest dto) {
        return Optional.of(dto)
                .map(ClientPersonEntity::new)
                .map(clientNew -> {
                    validateUniqueCPFRule(clientNew.getCpf());
                    clientNew.getAddress().setClient(clientNew);
                    return this.repository.saveEntity(clientNew);
                })
                .map(ClientPersonDTOResponse::new)
                .orElseThrow();
    }

        private void validateUniqueCPFRule(String cpf) {
            if(!this.repository.searchByDocument(cpf).isEmpty())
                throw new BusinessRuleViolationCustomException(messages.getSingleCpfRuleViolation());
        }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public ResponseEntity<ClientPersonDTOResponse> update(ClientPersonDTORequest dto) {
        validateUniqueCPFRuleByUpdate(dto);

        return this.repository.searchById(dto.id())
                .map(client -> {
                    client.setFirstName(dto.firstName());
                    client.setLastName(dto.lastName());
                    client.setCpf(dto.cpf());
                    client.setSex(dto.sex());
                    client.setGenre(dto.genre());
                    client.setBirthDate(dto.birthDate());
                    client.setMaritalStatus(dto.maritalStatus());
                    client.setEducation(dto.education());
                    return client;
                })
                .map(client -> ResponseEntity
                        .ok()
                        .body(new ClientPersonDTOResponse(client))
                )
                .orElseThrow(() -> new ResourceNotFoundCustomException(messages.getResourceNotFound()));
    }

        private void validateUniqueCPFRuleByUpdate(ClientPersonDTORequest dto) {
            var clientByCPF = this.repository.searchByDocument(dto.cpf());
            if(!clientByCPF.isEmpty() && clientByCPF.get().getId() != dto.id()) {
                throw new BusinessRuleViolationCustomException(messages.getSingleCpfRuleViolation());
            }
        }

    @Override
    public ResponseEntity<ClientPersonDTOResponse> searchById(Long id) {
        return this.repository.searchById(id)
                .map(client -> ResponseEntity
                        .ok()
                        .body(new ClientPersonDTOResponse(client))
                )
                .orElseThrow(() -> new ResourceNotFoundCustomException(messages.getResourceNotFound()));
    }

    @Override
    public ResponseEntity<Page<ClientPersonDTOResponse>> searchAll(ClientPersonFilter filter, Pageable pagination) {
        return ResponseEntity
                .ok()
                .body(this.repository.searchAll(configureFilter(filter), pagination)
                        .map(ClientPersonDTOResponse::new));
    }

        private Example<ClientPersonEntity> configureFilter(ClientPersonFilter filter) {
            // ExampleMatcher - permite configurar condições para serem aplicadas nos filtros
            ExampleMatcher exampleMatcher = ExampleMatcher
                    .matchingAll()
                    .withIgnoreCase() // Ignorar caixa alta ou baixa - quando String
                    .withIgnoreNullValues() // Ignorar valores nulos
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // permite encontrar palavras parecidas - tipo Like do SQL

            // Example - pega campos populados para criar filtros
            return Example.of(ClientPersonEntity.builder()
                        .firstName(filter.firstName())
                        .lastName(filter.lastName())
                        .cpf(filter.cpf())
                        .sex(filter.sex())
                        .genre(filter.genre())
                        .maritalStatus(filter.maritalStatus())
                        .education(filter.education())
                        .build(), exampleMatcher);
        }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public ResponseEntity<?> deleteById(Long id) {
        return this.repository.searchById(id)
                .map(client -> {
                    this.repository.deleteById(client.getId());
                    return ResponseEntity
                            .ok()
                            .body(messages.getResourceDeletedSuccessfully());
                })
                .orElseThrow(() -> new ResourceNotFoundCustomException(messages.getResourceNotFound()));
    }
}