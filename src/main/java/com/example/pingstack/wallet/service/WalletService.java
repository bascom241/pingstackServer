package com.example.pingstack.wallet.service;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.wallet.model.Wallet;
import com.example.pingstack.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public void createWallet(User user){
        Optional<Wallet> existingWallet = walletRepository.findWalletByUser(user);
        Wallet walletToCreate;
        if(existingWallet.isPresent()){
            throw new RuntimeException("Wallet exist for user Already");
        }else {
            walletToCreate = new Wallet();
            walletToCreate.setBalance(new BigDecimal("10.0000"));
            walletToCreate.setUser(user);
            walletRepository.save(walletToCreate);
        }
    }

}
