package com.bh.cwms.wallet.controller;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.wallet.model.dto.currency.AddCurrency;
import com.bh.cwms.wallet.model.dto.currency.CurrencyDto;
import com.bh.cwms.wallet.service.currency.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/currency")
@Tag(name = "Currency")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/_byId/{id}")
    @Operation(
            summary = "Fetch currency by ID",
            description = "Find a currency by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found currency by ID"),
            @ApiResponse(responseCode = "404", description = "No currency found for given ID")
    })
    public CurrencyDto findUserById(@PathVariable("id") UUID id){
        return currencyService.getCurrency(id);
    }

    @GetMapping
    @Operation(
            summary = "List Currencies",
            description = "Find the list of all currencies"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found currencies"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ListResponse<CurrencyDto> getUsersPage(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable){
        return currencyService.getCurrencies(pageable);
    }

    @PostMapping
    @Operation(
            summary = "Add Currency",
            description = "Add a new currency in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Currency added successfully")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public CurrencyDto addCurrency(@RequestBody AddCurrency addCurrency){
        return currencyService.addCurrency(addCurrency);
    }
}
