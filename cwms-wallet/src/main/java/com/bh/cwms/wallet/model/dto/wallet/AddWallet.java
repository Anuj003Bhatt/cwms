package com.bh.cwms.wallet.model.dto.wallet;

import com.bh.cwms.wallet.model.constants.Currency;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddWallet {
    private Currency currency;
    @NotBlank(message = "Pin cannot be blank.")
    private String pin;
}
