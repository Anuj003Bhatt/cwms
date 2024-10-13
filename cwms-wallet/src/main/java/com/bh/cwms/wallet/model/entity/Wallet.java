package com.bh.cwms.wallet.model.entity;

import com.bh.cwms.common.bridge.DtoBridge;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wallets", uniqueConstraints = {
        @UniqueConstraint(name = "user_wallet",  columnNames = {"user_id"})
})
public class Wallet implements DtoBridge<WalletDto> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    private UUID id;

    @OneToMany(mappedBy = "wallet")
    private List<WalletItem> walletItems;

    @Column(name = "user_id")
    private UUID userId;

    @Override
    public WalletDto toDto() {
        return WalletDto
                .builder()
                .id(id)
                .walletItems(walletItems.stream().map(WalletItem::toDto).toList())
                .build();
    }
}
