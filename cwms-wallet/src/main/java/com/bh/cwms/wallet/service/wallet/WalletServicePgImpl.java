package com.bh.cwms.wallet.service.wallet;

import com.bh.cwms.common.exception.BadRequestException;
import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.common.util.BridgeUtil;
import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;
import com.bh.cwms.wallet.model.dto.wallet.WalletItemDto;
import com.bh.cwms.wallet.model.entity.Currency;
import com.bh.cwms.wallet.model.entity.Wallet;
import com.bh.cwms.wallet.model.entity.WalletItem;
import com.bh.cwms.wallet.repository.CurrencyRepository;
import com.bh.cwms.wallet.repository.WalletItemRepository;
import com.bh.cwms.wallet.repository.WalletRepository;
import com.bh.cwms.wallet.service.price.PriceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServicePgImpl implements WalletService {
    private final PriceService priceService;
    private final WalletRepository walletRepository;
    private final WalletItemRepository walletItemRepository;

    private final CurrencyRepository currencyRepository;

    @Override
    @Transactional
    public WalletDto createWallet(AddWallet newWallet, UUID userId) {
        Wallet wallet = walletRepository.findByUserId(userId).orElse(null);
        if (wallet != null) {
            throw new BadRequestException("Wallet for user '{}' already exists. Try creating a wallet item instead", userId);
        }
        Currency currency = currencyRepository.findByName(newWallet.getCurrency()).orElseThrow(
                () -> new BadRequestException("Invalid currency '{}'", newWallet.getCurrency())
        );


        wallet = Wallet
                .builder()
                .userId(userId)
                .build();

        List<WalletItem> walletItems = new ArrayList<>();
        WalletItem item = walletItemRepository.save(WalletItem
                .builder()
                .wallet(wallet)
                .currency(currency)
                .build());

        walletItems.add(item);
        wallet.setWalletItems(walletItems);
        return walletRepository.save(wallet).toDto();
    }

    @Override
    @Transactional
    public WalletDto addWalletItem(UUID walletId, AddWallet newWallet, UUID userId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(
                () -> new BadRequestException("Wallet for user '{}' already exists.", userId)
        );
        Currency currency = currencyRepository.findByName(newWallet.getCurrency()).orElseThrow(
                () -> new BadRequestException("Invalid currency '{}'", newWallet.getCurrency())
        );

        wallet.getWalletItems().add(
                WalletItem
                        .builder()
                        .wallet(wallet)
                        .currency(currency)
                        .build()
        );
        return walletRepository.save(wallet).toDto();
    }

    private BigDecimal getWalletBalance(WalletDto walletDto) {
        BigDecimal walletBalance = BigDecimal.ZERO;
        Map<String, BigDecimal> currencies = new HashMap<>();

        for (WalletItemDto item: walletDto.getWalletItems()) {
            String currencyName = item.getCurrency().getName();
            if (!currencies.containsKey(currencyName)) {
               currencies.put(currencyName, priceService.getPriceUsd(currencyName));
            }
            if (currencies.get(currencyName) != null && item.getBalance() != null) {
                walletBalance = walletBalance.add(
                        currencies.get(currencyName).multiply(item.getBalance())
                );
            }

        }
        return walletBalance;
    }

    @Override
    public WalletDto getWalletById(UUID id) {
        WalletDto walletDto = walletRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No wallet found for ID '{}'", id)
        ).toDto();
        BigDecimal balance = getWalletBalance(walletDto);
        walletDto.setBalanceInUsd(balance);
        return walletDto;
    }

    @Override
    public ListResponse<WalletDto> getWallets(Pageable pageable) {
        Page<Wallet> page = walletRepository.findAll(pageable);
        return BridgeUtil.buildPaginatedResponse(page);
    }

    @Override
    public WalletDto updateWallet(WalletDto newWallet) {
        return null;
    }

    @Override
    public void deleteWallet(UUID id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No wallet found for ID '{}'", id)
        );
        walletRepository.delete(wallet);
    }
}
