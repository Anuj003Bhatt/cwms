package com.bh.cwms.wallet.model.dto.currency;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCurrency {
    @NotBlank(message = "Currency name cannot be empty.")
    private String name;
}
