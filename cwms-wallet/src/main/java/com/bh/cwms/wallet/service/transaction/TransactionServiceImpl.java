package com.bh.cwms.wallet.service.transaction;

import com.bh.cwms.common.exception.BadRequestException;
import com.bh.cwms.common.exception.NotFoundException;
import com.bh.cwms.common.util.EncryptionUtil;
import com.bh.cwms.wallet.model.chain.Transaction;
import com.bh.cwms.wallet.model.dto.transaction.TransferRequest;
import com.bh.cwms.wallet.model.entity.Wallet;
import com.bh.cwms.wallet.model.entity.WalletItem;
import com.bh.cwms.wallet.repository.WalletItemRepository;
import com.bh.cwms.wallet.repository.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final WalletRepository walletRepository;
    private final WalletItemRepository walletItemRepository;


    private String verifyKey(String privateKey, String publicKey, Transaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            byte[] challange = byteArrayOutputStream.toByteArray();

            KeyFactory kf = KeyFactory.getInstance("RSA");
            Signature signature = Signature.getInstance("SHA256WithRSA");
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(privateKey));

            PrivateKey key = kf.generatePrivate(keySpecPKCS8);
            signature.initSign(key);
            signature.update(challange);
            byte[] signatureBytes = signature.sign();

            X509EncodedKeySpec keySpecPub = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKey));
            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecPub);
            signature.initVerify(pubKey);
            signature.update(challange);

            if(!signature.verify(signatureBytes)) {
                throw new BadRequestException("Invalid/Corrupt Transfer request");
            }
            return EncryptionUtil.encrypt(mapper.writer().writeValueAsString(transaction), privateKey);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to perform transaction");
        } catch (Exception e) {
            throw new BadRequestException("Corrupt transaction");
        }

    }
    @Override
    @Transactional
    public boolean transferUnits(TransferRequest transferRequest) {
        Wallet sourceWallet = walletRepository.findById(transferRequest.getSourceWalletId()).orElseThrow(
                () -> new NotFoundException("No wallet found for ID '{}'", transferRequest.getSourceWalletId())
        );
        WalletItem item = sourceWallet.getWalletItems().stream().filter(it -> it.getCurrency() == transferRequest.getCurrency())
                .findAny().orElseThrow(
                        () -> new NotFoundException("No wallet found for ID '{}'", transferRequest.getSourceWalletId())
                );
        if (transferRequest.getUnits().compareTo(item.getBalance()) > 0) {
            throw new RuntimeException("Insufficient Balance");
        }
        String privateKey = EncryptionUtil.decrypt(sourceWallet.getPrivateKey(), transferRequest.getPin());
        Wallet targetWallet = walletRepository.findById(transferRequest.getTargetWalletId()).orElseThrow(
                () -> new NotFoundException("No wallet found for ID '{}'", transferRequest.getTargetWalletId())
        );
        WalletItem targetItem = targetWallet.getWalletItems().stream().filter(it -> it.getCurrency() == transferRequest.getCurrency())
                .findAny().orElseThrow(
                        () -> new NotFoundException("No wallet found for ID '{}'", transferRequest.getSourceWalletId())
                );
        Transaction transaction = Transaction
                .builder()
                .sourceWallet(sourceWallet.getId())
                .targetWallet(targetWallet.getId())
                .units(transferRequest.getUnits())
                .build();

        String sign = verifyKey(privateKey, transferRequest.getPublicKey(), transaction);
        transaction.setSignature(sign);
        item.setBalance(item.getBalance().subtract(transaction.getUnits()));
        targetItem.setBalance(targetItem.getBalance().add(transferRequest.getUnits()));
        walletItemRepository.save(item);
        walletItemRepository.save(targetItem);
        return false;
    }
}
