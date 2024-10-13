package com.bh.cwms.wallet.model.entity;

import com.bh.cwms.common.bridge.DtoBridge;
import com.bh.cwms.wallet.model.dto.wallet.WalletItemDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wallet_items")
public class WalletItem implements DtoBridge<WalletItemDto> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToMany(mappedBy = "walletItem")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Override
    public WalletItemDto toDto() {
        return WalletItemDto.builder()
                .id(id)
                .currency(currency.toDto())
                .balance(balance)
                .build();
    }
}
