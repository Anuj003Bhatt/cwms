package com.bh.cwms.wallet.controller;

import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;
import com.bh.cwms.wallet.service.wallet.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/wallet_items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Wallets")
public class WalletItemController {
    private final WalletService walletService;

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
