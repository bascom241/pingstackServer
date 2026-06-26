package com.example.pingstack.wallet.service;


import com.example.pingstack.wallet.model.Wallet;
import com.example.pingstack.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletTopUpService {
    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void creditWalletBalance(UUID userID, BigDecimal verifiedNairaAmount){
        Wallet wallet = walletRepository.findWalletForUpdate(userID);
        if(wallet == null){
            throw new RuntimeException("Target system wallet structure missing");
        }
        BigDecimal updatedBalance = wallet.getBalance().add(verifiedNairaAmount);
        wallet.setBalance(updatedBalance);
        walletRepository.save(wallet);
    }


}
