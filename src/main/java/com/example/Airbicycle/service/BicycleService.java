package com.example.Airbicycle.service;

import com.example.Airbicycle.domain.Bicycle;
import com.example.Airbicycle.domain.User;
import com.example.Airbicycle.repository.BicycleRepository;
import com.example.Airbicycle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BicycleService {
    private final BicycleRepository bicycleRepository;
    private final UserRepository userRepository;

    // 특정 자전거 조회
    public Bicycle getBicycleById(Long id){
        return bicycleRepository.findById(id).orElse(null);
    }
    // 자전거 등록
    public Bicycle saveBicycle(Bicycle bicycle){
        return bicycleRepository.save(bicycle);
    }

    //자전거 삭제
    public void deleteBicycle(Long id){
        bicycleRepository.deleteById(id);
    }
    //모든 자전거 조회
    public List<Bicycle> getAllBicycles(){
        return bicycleRepository.findAll();
    }
    // 특정 유저가 대여한 자전거 모두조회(과거이력 모두포함)
    public List<Bicycle> getAllBicycleByUsername(String username){
        return bicycleRepository.findAllByUsername(username);
    }

    // 사용 가능한 자전거 모두조회
    public List<Bicycle> getAvailableBicycles(){
        return bicycleRepository.findByAvailable(true);
    }

    // 현재 사용자가 자전거를 대여 중인지 확인.
    public Optional<Bicycle> getCurrentBicycleByUsername (String username){
        return bicycleRepository.findByUsernameAndAvailable(username,false);
    }
    // 사용자가 자전거를 대여할 수 있는지
    public boolean canRentBicycle(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return !user.gethasRented();
    }

    // 자전거 대여
    public Bicycle rentBicycle(Bicycle bicycle , String username){
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 자전거를 대여 중인지
        if (user.gethasRented()){
            throw new IllegalStateException("사용자가 이미 자전거를 대여 중입니다.");
        }
        // 자전거 대여
        bicycle.setUsername(user);
        bicycle.setAvailable(false);
        user.setHasRented(true);
         // 이용시간 설정 필요

        userRepository.save(user);
        return bicycleRepository.save(bicycle);
    }

    // 자전거 반납
    public Bicycle returnBicycle (String username){
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Optional<Bicycle> currentBicycle = getCurrentBicycleByUsername(username);
        if (currentBicycle.isEmpty()){
            throw new IllegalStateException("반납할 자전거가 없습니다.");
        }
        Bicycle bicycle = currentBicycle.get();
        bicycle.setAvailable(true);
        user.setHasRented(false);

        userRepository.save(user);
        return bicycleRepository.save(bicycle);
    }
}
