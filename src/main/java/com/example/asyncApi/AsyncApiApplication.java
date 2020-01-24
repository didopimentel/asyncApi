package com.example.asyncApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.asyncApi.domain.Balance;
import com.example.asyncApi.domain.User;
import com.example.asyncApi.repositories.BalanceRepository;
import com.example.asyncApi.repositories.UserRepository;

@SpringBootApplication
@EnableAsync
public class AsyncApiApplication implements CommandLineRunner{

  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private BalanceRepository balanceRepository;
  
	public static void main(String[] args) {
		SpringApplication.run(AsyncApiApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {
    Balance balance1 = new Balance();
    User user1 = new User("Rodrigo");
    user1.setBalance(balance1);
    balance1.setTotal(100);
    balance1.setUser(user1);
    
    balanceRepository.save(balance1);
    userRepository.save(user1);
    
    System.out.println(user1.getId());
  }
	
	

}
