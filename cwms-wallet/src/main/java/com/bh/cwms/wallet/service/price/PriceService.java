package com.bh.cwms.wallet.service.price;


import java.math.BigDecimal;

public interface PriceService {
    BigDecimal getPriceUsd(String currency);
}
