package com.bh.cwms.wallet.service.wallet;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WalletService {
    WalletDto createWallet(AddWallet newWallet, UUID userId);
    WalletDto addWalletItem(UUID walletId, AddWallet newWallet, UUID userId);
    WalletDto getWalletById(UUID id);
    ListResponse<WalletDto> getWallets(Pageable pageable);
    WalletDto updateWallet(WalletDto newWallet);
    void deleteWallet(UUID id);
}
