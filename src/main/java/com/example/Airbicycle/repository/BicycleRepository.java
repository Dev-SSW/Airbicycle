package com.example.Airbicycle.repository;

import com.example.Airbicycle.domain.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@Repository
public interface BicycleRepository extends JpaRepository<Bicycle,Long> {

    // ID로 자전거 찾기
    Optional<Bicycle> findById(String bicycleId);

    // 특정 유저가 대여한 자전거 조회 (현재 대여중인 자전거만)
    Optional<Bicycle> findByUsernameAndAvailable(String username,Boolean available);

    // 사용 가능한 자전거 조회(리스트)
   List<Bicycle> findByAvailable(Boolean available);

    //특정 유저가 대여한 자전거 리스트 조회
    List<Bicycle> findAllByUsername(String username);
}
