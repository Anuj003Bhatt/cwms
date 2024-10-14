package com.bh.cwms.wallet.service.wallet;

import com.bh.cwms.common.exception.BadRequestException;
import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.common.util.BridgeUtil;
import com.bh.cwms.common.util.EncryptionUtil;
import com.bh.cwms.wallet.model.constants.Currency;
import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.dto.wallet.WalletDto;
import com.bh.cwms.wallet.model.dto.wallet.WalletItemDto;
import com.bh.cwms.wallet.model.entity.Wallet;
import com.bh.cwms.wallet.model.entity.WalletItem;
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
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
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

    @Override
    @Transactional
    public WalletDto createWallet(AddWallet newWallet, UUID userId) {
        Wallet wallet = walletRepository.findByUserId(userId).orElse(null);
        if (wallet != null) {
            throw new BadRequestException("Wallet for user '{}' already exists. Try creating a wallet item instead", userId);
        }

        KeyPair pair = EncryptionUtil.generateKeyPair();
        String privateKey = Base64.getMimeEncoder().encodeToString( pair.getPrivate().getEncoded());
        wallet = Wallet
                .builder()
                .privateKey(EncryptionUtil.encrypt(privateKey, newWallet.getPin()))
                .userId(userId)
                .build();

        List<WalletItem> walletItems = new ArrayList<>();
        WalletItem item = walletItemRepository.save(WalletItem
                .builder()
                .wallet(wallet)
                .balance(BigDecimal.ZERO)
                .currency(newWallet.getCurrency())
                .build());

        walletItems.add(item);
        wallet.setWalletItems(walletItems);
        WalletDto walletDto = walletRepository.save(wallet).toDto();
        walletDto.setPublicKey(Base64.getMimeEncoder().encodeToString( pair.getPublic().getEncoded()));
        return walletDto;
    }

    @Override
    @Transactional
    public WalletDto addWalletItem(UUID walletId, AddWallet newWallet, UUID userId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(
                () -> new BadRequestException("Wallet for user '{}' already exists.", userId)
        );
        wallet.getWalletItems().add(
                WalletItem
                        .builder()
                        .wallet(wallet)
                        .currency(newWallet.getCurrency())
                        .balance(BigDecimal.ZERO)
                        .build()
        );
        return walletRepository.save(wallet).toDto();
    }

    private BigDecimal getWalletBalance(WalletDto walletDto) {
        BigDecimal walletBalance = BigDecimal.ZERO;
        Map<Currency, BigDecimal> currencies = new HashMap<>();

        for (WalletItemDto item: walletDto.getWalletItems()) {
            if (!currencies.containsKey(item.getCurrency())) {
               currencies.put(item.getCurrency(), priceService.getPriceUsd(item.getCurrency()));
            }
            if (currencies.get(item.getCurrency()) != null && item.getBalance() != null) {
                walletBalance = walletBalance.add(
                        currencies.get(item.getCurrency()).multiply(item.getBalance())
                );
            }

        }
        return walletBalance;
    }

    @Override
    public WalletDto getWalletById(UUID userId,UUID id) {
        WalletDto walletDto = walletRepository.findByUserIdAndId(userId, id).orElseThrow(
                () -> new BadRequestException("No wallet found for ID '{}' for user '{}'", id, userId)
        ).toDto();

        BigDecimal balance = getWalletBalance(walletDto);
        walletDto.setBalanceInUsd(balance);
        return walletDto;
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
