package com.bh.cwms.wallet.service.wallet;

import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;

public interface WalletService {
    WalletDto createWallet(AddWallet newWallet);
}
