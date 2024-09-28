package com.example.Airbicycle.repository;

import com.example.Airbicycle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //ID로 User 찾기
    Optional<User> findByUsername(String username);

    //Optional 없이 가져오기
    @Query("select m from User m where m.username = :username")
    User findUsername(@Param("username") String username);

    //ID, PASSWORD로 USER 찾기
    Optional<User> findByUsernameAndPassword(String username, String password);

    //Email로 User 찾기
    Optional<User> findByEmail(String email);
}
