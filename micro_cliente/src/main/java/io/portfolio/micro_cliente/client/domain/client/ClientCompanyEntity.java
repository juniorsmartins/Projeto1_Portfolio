package io.portfolio.micro_cliente.client.domain.client;

import io.portfolio.micro_cliente.client.domain.dtos.ClientCompanyDTORequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "client_companys")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(referencedColumnName="id")
public final class ClientCompanyEntity extends Client implements PolicyEntity<Long>  {

    @Column(name = "business_name", length = 100, nullable = false)
    private String businessName;

    @Column(name = "fantasy_name", length = 100, nullable = true)
    private String fantasyName;

    @Column(name = "cnpj", length = 20, nullable = false)
    private String cnpj;

    public ClientCompanyEntity(ClientCompanyDTORequest dto) {
        super(dto.id(), dto.birthDate(), new AddressEntity(dto.address()));
        this.setBusinessName(dto.businessName());
        this.setFantasyName(dto.fantasyName());
        this.setCnpj(dto.cnpj());
    }
}