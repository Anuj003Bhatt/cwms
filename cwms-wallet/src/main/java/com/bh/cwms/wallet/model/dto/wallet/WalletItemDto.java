package com.bh.cwms.wallet.model.dto.wallet;

import com.bh.cwms.wallet.model.constants.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletItemDto {
    private UUID id;
    private Currency currency;
    private BigDecimal balance;
}
