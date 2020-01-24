package com.example.asyncApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.asyncApi.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

}
