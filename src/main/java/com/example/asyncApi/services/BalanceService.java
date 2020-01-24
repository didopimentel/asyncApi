package com.example.asyncApi.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.example.asyncApi.domain.Balance;
import com.example.asyncApi.repositories.BalanceRepository;

@Service
public class BalanceService {

  private final BalanceRepository balanceRepository;
  
  public BalanceService(final BalanceRepository balanceRepository) {
    super();
    this.balanceRepository = balanceRepository;
  }

  public CompletableFuture<Balance> findByUserId(Long userId) {
    return CompletableFuture.supplyAsync(() -> {
      return balanceRepository.findByUserId(userId);
    });
  }

}
