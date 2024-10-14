package com.bh.cwms.wallet.service.transaction;

import com.bh.cwms.common.exception.BadRequestException;
import com.bh.cwms.common.exception.NotFoundException;
import com.bh.cwms.common.util.EncryptionUtil;
import com.bh.cwms.wallet.model.chain.Transaction;
import com.bh.cwms.wallet.model.dto.transaction.TransferRequest;
import com.bh.cwms.wallet.model.entity.Wallet;
import com.bh.cwms.wallet.model.entity.WalletItem;
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
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private WalletRepository walletRepository;


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
        Transaction transaction = Transaction
                .builder()
                .sourceWallet(sourceWallet.getId())
                .targetWallet(targetWallet.getId())
                .units(transferRequest.getUnits())
                .build();


        ObjectMapper mapper = new ObjectMapper();
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            Signature signature = Signature.getInstance("SHA1WithRSA");
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            PrivateKey key = kf.generatePrivate(keySpecPKCS8);
            signature.initSign(key);

            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(transferRequest.getPublicKey()));
            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
            objectOutputStream.writeObject(transaction);
            objectOutputStream.flush();

            signature.update(byteArrayOutputStream.toByteArray());
            byte[] signatureBytes = signature.sign();
            signature.initVerify(pubKey);
            objectOutputStream.writeObject(transaction);
            objectOutputStream.flush();
            signature.update(byteArrayOutputStream.toByteArray());

            if(!signature.verify(signatureBytes)) {
                throw new BadRequestException("Invalid/Corrupt Transfer request");
            }
            transaction.setSignature(EncryptionUtil.encrypt(mapper.writer().writeValueAsString(transaction), privateKey));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to perform transaction");
        } catch (Exception e) {
            throw new RuntimeException("Corrupt transaction");
        }

        return false;
    }
}
