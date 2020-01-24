package com.example.asyncApi.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.asyncApi.domain.Balance;
import com.example.asyncApi.domain.User;
import com.example.asyncApi.services.BalanceService;
import com.example.asyncApi.services.UserService;

@RestController
public class UserController {

  private final BalanceService balanceService;
  private final UserService userService;

  public UserController(final BalanceService balanceService, final UserService userService) {
    super();
    this.balanceService = balanceService;
    this.userService = userService;
  }
  
  @Async
  @GetMapping("/users/{userId}/balance")
  public CompletableFuture<ResponseEntity<Balance>> findBalance(@PathVariable long userId) {
    
    CompletableFuture<Balance> serviceCall = balanceService.findByUserId(userId);
    
    return serviceCall.<ResponseEntity<Balance>>thenApply(balance -> ResponseEntity.ok(balance));
  }
  
  @Async
  @GetMapping("/users/{userId}")
  public CompletableFuture<ResponseEntity<User>> findById(@PathVariable long userId) {
    
    CompletableFuture<User> serviceCall = userService.findById(userId);
    
    return serviceCall.<ResponseEntity<User>>thenApply(user -> ResponseEntity.ok(user));
  }
}
