package com.bh.cwms.wallet.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Single;

public interface BitcoinApi {
    @GET("v1/bpi/currentprice.json")
    @Headers({"Content-Type: application/json"})
    Single<String> getBitCoinPrice();

}
