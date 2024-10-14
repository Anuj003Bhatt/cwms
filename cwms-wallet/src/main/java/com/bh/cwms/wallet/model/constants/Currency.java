package com.bh.cwms.wallet.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Currency {
    BITCOIN,
    ETHEREUM;

    @JsonCreator
    public Currency fromText(String source) {
        if (null == source || source.isBlank()) {
            throw new IllegalArgumentException("Invalid Currency");
        }
        return Currency.valueOf(source.trim().toUpperCase());
    }
}
