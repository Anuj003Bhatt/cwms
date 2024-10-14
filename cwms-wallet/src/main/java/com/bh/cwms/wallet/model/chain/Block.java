package com.bh.cwms.wallet.model.chain;

import com.bh.cwms.common.util.EncryptionUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Block {
    private String hash;
    private String previousHash;
    private Transaction data;
    private long timeStamp;

    public Block(String previousHash, Transaction data) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return EncryptionUtil.applySha256(
                previousHash + Long.toString(timeStamp) + data.hashCode()
        );
    }
}
