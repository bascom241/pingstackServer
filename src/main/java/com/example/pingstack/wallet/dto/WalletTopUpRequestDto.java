package com.example.pingstack.wallet.dto;

import java.math.BigDecimal;

public class WalletTopUpRequestDto {
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
