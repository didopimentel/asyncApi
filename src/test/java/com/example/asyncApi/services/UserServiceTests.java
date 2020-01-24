package com.example.asyncApi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.asyncApi.domain.User;
import com.example.asyncApi.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
  
  @MockBean
  private UserRepository userRepository;  
  
  @Test
  public void findByUserId() throws InterruptedException, ExecutionException {
    User user = new User();
    user.setName("testeName");
    
    Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
    
    UserService service = new UserService(userRepository);
    
    CompletableFuture<User> futureUser = service.findById((long) 1);
    
    User returnedUser = futureUser.get();
    
    assertEquals("testeName", returnedUser.getName());
  }
}
