package com.carShowroom.WebCarShowroom.repository;

import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findUserById(Long id);
}
