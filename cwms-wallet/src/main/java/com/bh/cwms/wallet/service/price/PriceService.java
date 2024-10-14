package com.bh.cwms.wallet.service.price;


import com.bh.cwms.wallet.model.constants.Currency;

import java.math.BigDecimal;

public interface PriceService {
    BigDecimal getPriceUsd(Currency currency);
}
