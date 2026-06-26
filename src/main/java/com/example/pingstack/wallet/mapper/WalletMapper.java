package com.example.pingstack.wallet.mapper;

import com.example.pingstack.wallet.dto.WalletBalanceResponse;
import com.example.pingstack.wallet.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public static WalletBalanceResponse toResponse(Wallet wallet){
        return new WalletBalanceResponse(
                wallet.getBalance(),
                wallet.getUpdatedAt()
        );
    }
}
