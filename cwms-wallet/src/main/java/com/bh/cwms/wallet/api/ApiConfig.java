package com.bh.cwms.wallet.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig extends BaseApiConfig {
    @Bean
    public BitcoinApi userApi(
            @Value("${bitcoin.api}") String baseUrl,
            @Value("${bitcoin.api.timeout}") long timeout
    ) {
        return createApi(baseUrl, timeout, BitcoinApi.class);
    }
}
