package com.bh.cwms.wallet.model.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletDto {
    private UUID id;
    private List<WalletItemDto> walletItems;
    private BigDecimal balanceInUsd;
}
