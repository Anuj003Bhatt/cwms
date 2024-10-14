package com.bh.cwms.wallet.converter;

import com.bh.cwms.wallet.model.constants.Currency;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {
    @Override
    public String convertToDatabaseColumn(Currency attribute) {
        return attribute.name();
    }

    @Override
    public Currency convertToEntityAttribute(String dbData) {
        return Currency.valueOf(dbData.trim().toUpperCase());
    }
}
