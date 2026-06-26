package com.example.pingstack.auth.service;

import com.example.pingstack.auth.dao.UserPrincipal;
import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
@Service
public class JwtService {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret.key}")
    private String secretkey;

    public String generateToken (String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 ))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractUserName( String token){
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim (String token , Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims (String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token , String email){
        String username = extractUserName(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }


    public List<String> extractRoles (String token){
        return extractAllClaims(token).get("roles", List.class);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }




}
