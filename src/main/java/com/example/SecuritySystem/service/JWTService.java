package com.example.SecuritySystem.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.SecuritySystem.model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    private String secretKey = "";

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    

    public JWTService(){
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        
    }


    public String generateToken(Users user) {
        
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(user.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
            .and()
            .signWith(getKey())
            .compact(); 
    }

    public String extractUsername(String jwtoken) {
        return extractClaim(jwtoken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtoken, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(jwtoken);
        return claimResolver.apply(claims);
    }
        
    private Claims extractAllClaims(String jwtoken) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(jwtoken).getPayload();
    }

    public boolean validateToken(String jwtoken, UserDetails userDetails) {
        final String username = extractUsername(jwtoken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtoken));
    }

    private boolean isTokenExpired(String jwtoken) {
        return extractExpiration(jwtoken).before(new Date());
    }

    private Date extractExpiration(String jwtoken) {
        return extractClaim(jwtoken, Claims::getExpiration);
    }
}
