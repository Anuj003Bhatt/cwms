package com.bh.cwms.wallet.repository;

import com.bh.cwms.wallet.model.entity.Wallet;
import com.bh.cwms.wallet.model.entity.WalletItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUserId(UUID userId);
}
