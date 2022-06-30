package com.carShowroom.WebCarShowroom.repository;

import com.carShowroom.WebCarShowroom.models.History;
import com.carShowroom.WebCarShowroom.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepositories extends JpaRepository<History, Long> {
    List<History> findHistoryUserByEmail(String email);
    History findHistoryUserByIdhistory(Long id);
    List<History> findHistoryByAppointment(String appointment);
}
