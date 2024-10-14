package com.bh.cwms.wallet.controller;

import com.bh.cwms.wallet.model.dto.transaction.TransferRequest;
import com.bh.cwms.wallet.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public boolean transfer(
            @RequestBody @Valid TransferRequest transferRequest
    ) {
        transactionService.transferUnits(transferRequest);
        return true;
    }
}
