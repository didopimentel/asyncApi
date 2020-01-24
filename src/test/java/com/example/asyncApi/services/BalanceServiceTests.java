package com.example.asyncApi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.asyncApi.domain.Balance;
import com.example.asyncApi.repositories.BalanceRepository;


@ExtendWith(SpringExtension.class)
public class BalanceServiceTests {
  
  @MockBean
  private BalanceRepository balanceRepository;
  
  @Test
  public void findByUserId() throws InterruptedException, ExecutionException {
    Balance balance = new Balance((float) 500.10);
    Mockito.when(balanceRepository.findByUserId(Mockito.anyLong())).thenReturn(balance);
    
    BalanceService service = new BalanceService(balanceRepository);
    
    CompletableFuture<Balance> futureBalance = service.findByUserId((long) 100);
    
    Balance returnedBalance = futureBalance.get();
    
    assertEquals((float) 500.10, returnedBalance.getTotal());
  }
}
