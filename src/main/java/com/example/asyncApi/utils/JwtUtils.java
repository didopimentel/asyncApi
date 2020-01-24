package com.example.asyncApi.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtils {
  
  private final String SECRET_KEY = "this_is_a_secret_key";
  
  private Claims getClaims(String jwt) {
    return Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(jwt)
        .getBody();
  }
  
  public String getUsername(String jwt) {
    return getClaims(jwt).getSubject();
  }
  
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername());
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }
  
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getClaims(token).getSubject();
    return username.equals(userDetails.getUsername());
  }
}
