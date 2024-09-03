package com.example.Airbicycle.service;

import com.example.Airbicycle.domain.Bicycle;
import com.example.Airbicycle.domain.User;
import com.example.Airbicycle.repository.BicycleRepository;
import com.example.Airbicycle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.dnd.InvalidDnDOperationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BicycleService {
    private final BicycleRepository bicycleRepository;

    // 자전거 등록
    public Bicycle addBicycle(Bicycle bicycle){
        return bicycleRepository.save(bicycle);
    }
    // 자전거 정보 수정
    public Bicycle updateBicycle(Long id, Bicycle updateBicycle){
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("자전거를 찾을 수 없습니다."));

        bicycle.setBicycleModel(updateBicycle.getBicycleModel());
        bicycle.setLocation(updateBicycle.getLocation());
        bicycle.setAvailable(updateBicycle.isAvailable());

        return bicycleRepository.save(bicycle);
    }

    // 자전거 삭제
    public void deleteBicycle(Long id){
        if (!bicycleRepository.existsById(id)){
            throw new IllegalStateException("삭제할 자전거를 찾을 수 없습니다.");
        }

        bicycleRepository.deleteById(id);
    }

    // 자전거 조회(등록된 모든 자전거)

    public List<Bicycle> getAllBicycles(){
        return bicycleRepository.findAll();
    }

    // 특정 자전거 조회 (id)로 조회
    public Bicycle getBicycleById(Long id){
        return bicycleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("id로 특정 자전거를 찾을 수 없습니다"));
    }
}
