package com.bh.cwms.wallet.service.currency;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.wallet.model.dto.currency.AddCurrency;
import com.bh.cwms.wallet.model.dto.currency.CurrencyDto;
import com.bh.cwms.wallet.model.entity.Currency;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CurrencyService {
    CurrencyDto addCurrency(AddCurrency addCurrency);
    CurrencyDto getCurrency(UUID id);
    ListResponse<CurrencyDto> getCurrencies(Pageable pageable);
    CurrencyDto getCurrency(String name);
    void deleteCurrency(String name);
    void deleteCurrency(UUID id);
}
