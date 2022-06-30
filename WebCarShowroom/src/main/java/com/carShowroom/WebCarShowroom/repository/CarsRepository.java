package com.carShowroom.WebCarShowroom.repository;

import com.carShowroom.WebCarShowroom.models.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarsRepository extends JpaRepository<Cars, Long> {
    Cars findCarByName(String name);
    Cars findCarById(Long id);
    List<Cars> findListCarByName(String name);
    List<Cars> findAllCarsByEquipment(String equipment);
    List<Cars> findCarByMark(String mark);
}
