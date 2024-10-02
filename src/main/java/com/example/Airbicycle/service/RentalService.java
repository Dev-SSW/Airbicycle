package com.example.Airbicycle.service;

import com.example.Airbicycle.domain.Bicycle;
import com.example.Airbicycle.domain.Rental;
import com.example.Airbicycle.domain.User;
import com.example.Airbicycle.repository.BicycleRepository;
import com.example.Airbicycle.repository.RentalRepository;
import com.example.Airbicycle.repository.UserRepository;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private RentalRepository rentalRepository;
    private BicycleRepository bicycleRepository;
    private UserRepository userRepository;

    // 자전거 대여 시작.
    public Rental startRental(Long userId, Long bicycleId, Timestamp startTime ,Timestamp endTime){
        // 사용자 존재유무 확인
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalStateException("유저 정보가 없습니다.");
        }
        // 자전거 존재하고, 대여 가능한지
        Optional<Bicycle> bicycle = bicycleRepository.findById(bicycleId);

        if(bicycle.isEmpty() || !bicycle.get().isAvailable()){
            throw new IllegalStateException("자전거를 빌리기에 적합하지 않습니다(정보가 없거나 누군가 사용중입니다.");
        }
        Rental rental = new Rental();
        rental.setUser(user.get());
        rental.setBicycle(bicycle.get());
        rental.setStartTime(startTime);
        rental.setEndTime(endTime);

        bicycle.get().setAvailable(false);
        bicycleRepository.save(bicycle.get());

        return rentalRepository.save(rental);
    }

    // 자전거 대여 종료
    public Rental endRental(Long rentalId){
        Optional<Rental> rental = rentalRepository.findById(rentalId);

        if(rental.isEmpty()){
            throw new IllegalStateException("대여 이력을 찾을 수 없습니다.");
        }
        Rental exsistigRental = rental.get();
        Bicycle bicycle = exsistigRental.getBicycle();
        bicycle.setAvailable(true);
        bicycleRepository.save(bicycle);

        return rentalRepository.save(exsistigRental);
    }

    // 특정 사용자의 대여 가록 조회

    public List<Rental> getUserRentalHistory(Long userId){
        return rentalRepository.findByUserId(userId);
    }
    // 특정 자전거의 대여 이력 조회
    public List<Rental> getBicycleRentalHistory(Long bicycleId){
        return rentalRepository.findByBicycleId(bicycleId);
    }

    public Rental extendRental (Long rentalId, Timestamp newEndTime){
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isEmpty()){
            throw new IllegalStateException("대여 기록을 찾을 수 없습니다.");
        }

        Rental existimgRental = rental.get();
        if(existimgRental.getEndTime().after(newEndTime)){
            throw new IllegalStateException("새 종료 시간이 현재 종료 시간보다 이전일 수 없습니다.");
        }

        existimgRental.setEndTime(newEndTime);

        return rentalRepository.save(existimgRental);
    }
}
