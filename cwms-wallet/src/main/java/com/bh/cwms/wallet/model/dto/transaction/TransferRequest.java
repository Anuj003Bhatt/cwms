package com.bh.cwms.wallet.model.dto.transaction;

import com.bh.cwms.wallet.model.constants.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    @NotNull
    private UUID sourceWalletId;
    @NotNull
    private String publicKey;
    @NotNull
    private UUID targetWalletId;
    @NotBlank
    private String pin;
    @NotNull
    private BigDecimal units;
    @NotNull
    private Currency currency;
}
