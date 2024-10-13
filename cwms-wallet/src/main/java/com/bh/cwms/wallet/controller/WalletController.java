package com.bh.cwms.wallet.controller;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;
import com.bh.cwms.wallet.service.wallet.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Slf4j
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/_byId/{id}")
    @Operation(
            summary = "Fetch wallet by ID",
            description = "Find a wallet by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found wallet by ID"),
            @ApiResponse(responseCode = "404", description = "No wallet found for given ID")
    })
    public WalletDto findUserById(@PathVariable("id") UUID id){
        return walletService.getWalletById(id);
    }

    @GetMapping
    @Operation(
            summary = "List User Wallets",
            description = "Find the list of all wallet for a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found wallets for user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ListResponse<WalletDto> getUsersPage(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable){
        return walletService.getWallets(pageable);
    }

    @PostMapping
    @Operation(
            summary = "Create wallet for user",
            description = "Create a wallet for the user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet created successfully")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDto createUser(@RequestBody AddWallet addWallet){
        return walletService.createWallet(addWallet, UUID.fromString("7826182f-e0b1-4edf-98e0-87cd871f1999"));
    }

    @PostMapping("/{id}")
    @Operation(
            summary = "Add wallet item for user",
            description = "Add a wallet to the user's wallet"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet item added successfully")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDto addWalletItem(
            @PathVariable("id") UUID walletId,
            @RequestBody AddWallet addWallet
    ){
        return walletService.addWalletItem(
                walletId,
                addWallet,
                UUID.fromString("7826182f-e0b1-4edf-98e0-87cd871f1999")
        );
    }
}
