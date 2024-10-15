package com.bh.cwms.wallet.config;

import com.bh.cwms.common.util.EncryptionUtil;
import com.bh.cwms.wallet.model.constants.Currency;
import com.bh.cwms.wallet.model.dto.wallet.AddWallet;
import com.bh.cwms.wallet.model.entity.Wallet;
import com.bh.cwms.wallet.model.entity.WalletItem;
import com.bh.cwms.wallet.repository.WalletItemRepository;
import com.bh.cwms.wallet.repository.WalletRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoad {
    @Value("${loadSuperAdmin}")
    private Boolean load;

    private final WalletRepository walletRepository;
    private final WalletItemRepository walletItemRepository;


    @Transactional
    private void saveWallet(UUID userId, AddWallet newWallet) {
        if (walletItemRepository.findById(userId).isPresent()) {
            log.info("Wallet already present for user '{}'", userId);
            return;
        }
        KeyPair pair = EncryptionUtil.generateKeyPair();
        String privateKey = Base64.getMimeEncoder().encodeToString( pair.getPrivate().getEncoded());
        Wallet wallet = Wallet
                .builder()
                .privateKey(EncryptionUtil.encrypt(privateKey, newWallet.getPin()))
                .userId(userId)
                .build();

        List<WalletItem> walletItems = new ArrayList<>();
        WalletItem item = walletItemRepository.save(WalletItem
                .builder()
                .wallet(wallet)
                .balance(BigDecimal.valueOf(100))
                .currency(newWallet.getCurrency())
                .build());

        walletItems.add(item);
        wallet.setWalletItems(walletItems);
        walletRepository.save(wallet);
        String publicKey = Base64.getMimeEncoder().encodeToString( pair.getPublic().getEncoded());
        log.debug("Public key for user '{}' is '{}'", userId, publicKey);
    }
    @PostConstruct
    public void loadData() {
        if(!load) return;
        UUID user1Id = UUID.fromString("7826182f-e0b1-4edf-98e0-87cd871f1999");
        UUID user2Id = UUID.fromString("7826182f-e0b1-4edf-98e0-87cd871f1900");
        String pin = "1234";
        AddWallet newWallet = AddWallet
                .builder()
                .pin(pin)
                .currency(Currency.BITCOIN)
                .build();
        try {
            saveWallet(user1Id, newWallet);
            saveWallet(user2Id, newWallet);
        } catch (Exception ex) {
            log.error("Error while loading data");
        }
    }
}
