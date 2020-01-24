package com.example.asyncApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.asyncApi.domain.UserAuthenticationRequest;
import com.example.asyncApi.domain.UserAuthenticationResponse;
import com.example.asyncApi.security.AuthUserDetailsService;
import com.example.asyncApi.utils.JwtUtils;

@RestController
public class AuthenticationController {
  
  @Autowired
  private AuthUserDetailsService userDetailsService;
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private JwtUtils jwtUtil;
  
  
  
  public AuthenticationController(AuthUserDetailsService userDetailsService,
    AuthenticationManager authenticationManager, JwtUtils jwtUtil) {
    this.userDetailsService = userDetailsService;
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }



  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody UserAuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
      );
    } catch (BadCredentialsException bce) {
      throw bce;
    }
    
    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);
    
    return ResponseEntity.ok(new UserAuthenticationResponse(jwt));
  }
}
