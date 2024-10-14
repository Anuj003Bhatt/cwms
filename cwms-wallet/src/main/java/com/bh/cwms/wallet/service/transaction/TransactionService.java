package com.bh.cwms.wallet.service.transaction;

import com.bh.cwms.wallet.model.dto.transaction.TransferRequest;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionService {
    boolean transferUnits(TransferRequest transferRequest);
}
