package com.example.asyncApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.asyncApi.domain.Balance;

@Repository
public interface BalanceRepository extends CrudRepository<Balance, Long> {
  Balance findByUserId(Long userId);
}
