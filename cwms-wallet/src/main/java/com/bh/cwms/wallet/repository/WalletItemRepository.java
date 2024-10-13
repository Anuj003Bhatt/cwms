package com.bh.cwms.wallet.repository;

import com.bh.cwms.wallet.model.entity.WalletItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletItemRepository extends JpaRepository<WalletItem, UUID> {
}
