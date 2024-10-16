package com.bh.cwms.wallet.service.price;

import com.bh.cwms.wallet.api.BitcoinApi;
import com.bh.cwms.wallet.model.constants.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class PriceServiceImpl implements PriceService {
    private final BitcoinApi bitcoinApi;
    private final ObjectMapper mapper;

    @Override
    public BigDecimal getPriceUsd(Currency currency) {
        try {
            // TODO: This can be replaced with a proper interface and module to fetch real time market prices
            return BigDecimal.valueOf(
                    Double.parseDouble(
                            ((Map)((Map)mapper.readValue(bitcoinApi.getBitCoinPrice().toBlocking().value(), Map.class).get("bpi")).get("USD")).get("rate_float").toString()
                    )
            );
        } catch (Exception ex) {
            throw new RuntimeException("Unable to fetch price");
        }
    }
}
