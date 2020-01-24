package com.example.asyncApi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.asyncApi.domain.Balance;
import com.example.asyncApi.domain.User;
import com.example.asyncApi.domain.UserAuthenticationRequest;
import com.example.asyncApi.domain.UserAuthenticationResponse;
import com.example.asyncApi.security.AuthUserDetailsService;
import com.example.asyncApi.services.BalanceService;
import com.example.asyncApi.services.UserService;
import com.example.asyncApi.utils.JwtUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class UserControllerTests {

  
  @Autowired
  public MockMvc mockMvc;
  
  @Autowired
  private AuthUserDetailsService userDetailsService;
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private JwtUtils jwtUtil;
  
  
  @Test
  public void getBalance() throws Exception {
    // Authentication
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    UserAuthenticationRequest authRequestModel = new UserAuthenticationRequest("foo", "foo"); 
    
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(new AuthenticationController(userDetailsService, authenticationManager, jwtUtil))
        .build();
    
    MockHttpServletRequestBuilder requestAuth = MockMvcRequestBuilders
        .post("/authenticate")
        .content(mapper.writeValueAsString(authRequestModel))
        .contentType(MediaType.APPLICATION_JSON);
    
    MvcResult authResult = mockMvc.perform(requestAuth)
        .andReturn();
    
    UserAuthenticationResponse authResponseModel = mapper.readValue(authResult.getResponse().getContentAsString(), UserAuthenticationResponse.class);
    
    // GET Balance
    
    UserService userService = Mockito.mock(UserService.class);  
    BalanceService balanceService = Mockito.mock(BalanceService.class);  
    this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(balanceService, userService)).build();
    Balance balance = new Balance((float) 100.5);
    Mockito.when(balanceService.findByUserId(Mockito.any(long.class))).thenReturn(CompletableFuture.completedFuture(balance));
    
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .get("/users/100/balance")
        .header("Authentication", "Bearer " + authResponseModel.getToken())
        .contentType(MediaType.APPLICATION_JSON);
    
    final MvcResult result = mockMvc.perform(request)
        .andExpect(MockMvcResultMatchers.request().asyncStarted())
        .andReturn();
    
    final MvcResult finalAsyncResult = mockMvc.perform(asyncDispatch(result)).andReturn();
    
    // Assert
    
    assertEquals(200, finalAsyncResult.getResponse().getStatus());
    assertEquals((float) 100.5, mapper.readValue(finalAsyncResult.getResponse().getContentAsString(), Balance.class).getTotal());
  }
  
  @Test
  public void findById() throws Exception {
    // Authentication
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    UserAuthenticationRequest authRequestModel = new UserAuthenticationRequest("foo", "foo"); 
    
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(new AuthenticationController(userDetailsService, authenticationManager, jwtUtil))
        .build();
    
    MockHttpServletRequestBuilder requestAuth = MockMvcRequestBuilders
        .post("/authenticate")
        .content(mapper.writeValueAsString(authRequestModel))
        .contentType(MediaType.APPLICATION_JSON);
    
    MvcResult authResult = mockMvc.perform(requestAuth)
        .andReturn();
    
    UserAuthenticationResponse authResponseModel = mapper.readValue(authResult.getResponse().getContentAsString(), UserAuthenticationResponse.class);
    
    // GET Balance
    
    UserService userService = Mockito.mock(UserService.class);  
    BalanceService balanceService = Mockito.mock(BalanceService.class);  
    this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(balanceService, userService)).build();
    
    User user = new User();
    user.setName("name1");
    Mockito.when(userService.findById(Mockito.any(long.class))).thenReturn(CompletableFuture.completedFuture(user));
    
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .get("/users/1")
        .header("Authentication", "Bearer " + authResponseModel.getToken())
        .contentType(MediaType.APPLICATION_JSON);
    
    // Act
    final MvcResult result = mockMvc.perform(request)
        .andExpect(MockMvcResultMatchers.request().asyncStarted())
        .andReturn();
    
    final MvcResult finalAsyncResult = mockMvc.perform(asyncDispatch(result)).andReturn();
    
    // Assert
    assertEquals(200, finalAsyncResult.getResponse().getStatus());
    assertEquals("name1", mapper.readValue(finalAsyncResult.getResponse().getContentAsString(), User.class).getName());
  }
}
