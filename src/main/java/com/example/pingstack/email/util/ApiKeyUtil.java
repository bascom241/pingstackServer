package com.example.pingstack.email.util;


import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

@Component
public class ApiKeyUtil {

    private static final String PREFIX = "ps_live_";

    public String generateRawKey(){
        return PREFIX + UUID.randomUUID().toString().replace("_","")
                +UUID.randomUUID().toString().replace("_", "");
    }

    public String hashKey(String rawKey){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch ( NoSuchAlgorithmException e) {
            throw new RuntimeException("Error Hashing API key", e );
        }
    }

    public String extractPrefix (String rawKey){
        return rawKey.substring(0, Math.min(rawKey.length(),16)) + "...";
    }
}
