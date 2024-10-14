package com.bh.cwms.wallet.model.chain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockChain {
    private static final List<Block> CHAIN = new ArrayList<>(){{
        add(new Block(
                "hash",
                new Transaction(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ZERO, "signature")));
    }};

    public void addTransaction(Transaction transaction) {
        Block block= new Block(CHAIN.get(CHAIN.size()-1).getHash(), transaction);
        if (isChainValid()) {
            CHAIN.add(block);
        }
        throw new RuntimeException("Transaction is corrupt");
    }
    public boolean isChainValid() {
        Block currentBlock, previousBlock;
        if (CHAIN.size() == 1) {
            return true;
        }
        for(int i=1; i<CHAIN.size(); i++) {
            currentBlock = CHAIN.get(i);
            previousBlock = CHAIN.get(i-1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                return false;
            }
        }
        return true;
    }
}
