package com.example.asyncApi.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.example.asyncApi.domain.User;
import com.example.asyncApi.repositories.UserRepository;

@Service
public class UserService {
  
  private final UserRepository userRepository;
  
  public UserService(final UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  public CompletableFuture<User> findById(Long id) {
    return CompletableFuture.supplyAsync(() ->  {
      return userRepository.findById(id).orElse(null);
    });
  }
  
}
