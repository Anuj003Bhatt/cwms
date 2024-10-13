package com.bh.cwms.wallet.model.entity;

import com.bh.cwms.common.bridge.DtoBridge;
import com.bh.cwms.wallet.model.dto.currency.CurrencyDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "currencies", uniqueConstraints = {
        @UniqueConstraint(name = "currency_name_uniqye",  columnNames = {"name"})
})
public class Currency implements DtoBridge<CurrencyDto> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public CurrencyDto toDto() {
        return CurrencyDto.builder()
                .id(id)
                .name(name)
                .build();
    }
}
