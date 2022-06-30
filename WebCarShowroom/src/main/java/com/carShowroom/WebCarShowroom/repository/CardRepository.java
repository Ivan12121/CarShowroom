package com.carShowroom.WebCarShowroom.repository;

import com.carShowroom.WebCarShowroom.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findCardByCardNumber(String number);
    Card findCardByid(Long id);
}
