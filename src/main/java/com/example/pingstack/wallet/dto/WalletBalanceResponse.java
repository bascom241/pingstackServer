package com.example.pingstack.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletBalanceResponse {

    private BigDecimal amountPaid;
    private BigDecimal vatCharge;
    private BigDecimal balance;
    private LocalDateTime updatedAt;

    public WalletBalanceResponse(BigDecimal balance, LocalDateTime updatedAt) {
        this.balance = balance;
        this.updatedAt = updatedAt;
    }


    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getVatCharge() {
        return vatCharge;
    }

    public void setVatCharge(BigDecimal vatCharge) {
        this.vatCharge = vatCharge;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
