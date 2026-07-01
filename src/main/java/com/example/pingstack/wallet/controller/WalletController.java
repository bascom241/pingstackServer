package com.example.pingstack.wallet.controller;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.wallet.service.PayStackService;
import com.example.pingstack.wallet.service.WalletService;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance (@RequestHeader("Authorization") String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        String balance = walletService.getBalanceForUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

}
