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
}
