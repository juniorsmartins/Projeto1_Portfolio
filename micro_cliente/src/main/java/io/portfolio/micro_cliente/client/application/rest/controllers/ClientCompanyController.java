package io.portfolio.micro_cliente.client.application.rest.controllers;

import io.portfolio.micro_cliente.client.domain.client.ClientCompanyEntity;
import io.portfolio.micro_cliente.client.domain.dtos.ClientCompanyDTORequest;
import io.portfolio.micro_cliente.client.domain.dtos.ClientCompanyDTOResponse;
import io.portfolio.micro_cliente.client.domain.filter.ClientCompanyFilter;
import io.portfolio.micro_cliente.client.domain.services.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "${app.api.base}/clients/companys", produces = {"application/json"})
public final class ClientCompanyController extends PolicyControllers<ClientCompanyDTORequest, ClientCompanyFilter, ClientCompanyDTOResponse, Long> {

    @Autowired
    private PolicyService<ClientCompanyDTORequest, ClientCompanyFilter, ClientCompanyDTOResponse,
            ClientCompanyEntity, Long> service;

    @Override
    public ResponseEntity<ClientCompanyDTOResponse> create(@RequestBody @Valid ClientCompanyDTORequest dto) {
        var response = this.service.create(dto);
        return ResponseEntity
                .created(URI.create("/" + response.id()))
                .body(response);
    }

    @Override
    public ResponseEntity<ClientCompanyDTOResponse> update(@RequestBody @Valid ClientCompanyDTORequest dto) {
        return this.service.update(dto);
    }

    @Override
    public ResponseEntity<ClientCompanyDTOResponse> searchById(@PathVariable(value = "id") Long id) {
        return this.service.searchById(id);
    }

    @Override
    public ResponseEntity<Page<ClientCompanyDTOResponse>> searchAll(ClientCompanyFilter filter,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pagination) {
        return this.service.searchAll(filter, pagination);
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
        return this.service.deleteById(id);
    }
}