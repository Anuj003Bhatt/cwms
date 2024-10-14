package com.bh.cwms.wallet.controller;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.common.model.security.UserContext;
import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;
import com.bh.cwms.wallet.service.wallet.WalletService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Wallets")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public WalletDto findWalletById(
            @AuthenticationPrincipal UserContext context,
            @PathVariable("id") UUID id
    ){
        return walletService.getWalletById(UUID.fromString(context.getUserId()),id);
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ListResponse<WalletDto> getWalletPage(
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public WalletDto createWallet(
            @RequestBody AddWallet addWallet,
            @AuthenticationPrincipal UserContext context
    ){
        return walletService.createWallet(addWallet, UUID.fromString(context.getUserId()));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete wallet",
            description = "Delete a wallet for the user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet created successfully")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public Boolean deleteWallet(
            @PathVariable("id") UUID walletId
    ){
        walletService.deleteWallet(walletId);
        return true;
    }
}
