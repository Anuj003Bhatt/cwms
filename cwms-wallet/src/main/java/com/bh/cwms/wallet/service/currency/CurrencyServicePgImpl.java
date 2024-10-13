package com.bh.cwms.wallet.service.currency;

import com.bh.cwms.common.exception.BadRequestException;
import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.common.util.BridgeUtil;
import com.bh.cwms.wallet.model.dto.currency.AddCurrency;
import com.bh.cwms.wallet.model.dto.currency.CurrencyDto;
import com.bh.cwms.wallet.model.entity.Currency;
import com.bh.cwms.wallet.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServicePgImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Override
    public CurrencyDto addCurrency(AddCurrency addCurrency) {
        Currency existingCurrency = currencyRepository.findByName(addCurrency.getName()).orElse(null);

        if (existingCurrency != null) {
            throw new BadRequestException("Currency with the name '{}' already exists.", addCurrency.getName());
        }

        return currencyRepository.save(
                Currency
                        .builder()
                        .name(addCurrency.getName())
                        .build()
        ).toDto();
    }

    @Override
    public CurrencyDto getCurrency(UUID id) {
        return currencyRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No currency found for ID '{}'", id)
        ).toDto();
    }

    @Override
    public ListResponse<CurrencyDto> getCurrencies(Pageable pageable) {
        Page<Currency> page = currencyRepository.findAll(pageable);
        return BridgeUtil.buildPaginatedResponse(page);
    }

    @Override
    public CurrencyDto getCurrency(String name) {
        return currencyRepository.findByName(name).orElseThrow(
                () -> new BadRequestException("No currency found with name '{}'", name)
        ).toDto();
    }

    @Override
    @Transactional
    public void deleteCurrency(String name) {
        Currency currency = currencyRepository.findByName(name).orElseThrow(
                () -> new BadRequestException("No currency found with name '{}'", name)
        );
        currencyRepository.delete(currency);
    }

    @Override
    @Transactional
    public void deleteCurrency(UUID id) {
        Currency currency = currencyRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No currency found for ID '{}'", id)
        );
        currencyRepository.delete(currency);
    }
}
