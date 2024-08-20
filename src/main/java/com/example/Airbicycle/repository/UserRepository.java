package com.example.Airbicycle.repository;

import com.example.Airbicycle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //ID로 User 찾기
    Optional<User> findByUsername(String username);
    //ID, PASSWORD로 USER 찾기
    Optional<User> findByUsernameAndPassword(String username, String password);

}
