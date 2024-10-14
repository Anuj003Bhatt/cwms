package com.bh.cwms.wallet.model.chain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Transaction implements Serializable {
    private UUID sourceWallet;
    private UUID targetWallet;
    private BigDecimal units;
    private String signature;
}
