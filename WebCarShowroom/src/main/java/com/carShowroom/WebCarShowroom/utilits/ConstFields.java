package com.carShowroom.WebCarShowroom.utilits;

import com.carShowroom.WebCarShowroom.helpers.ApiClient;
import com.carShowroom.WebCarShowroom.repository.CardRepository;
import com.carShowroom.WebCarShowroom.repository.CarsRepository;
import com.carShowroom.WebCarShowroom.repository.HistoryRepositories;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ConstFields {
    @Autowired
    protected UsersRepository usersRepository;
    @Autowired
    protected CarsRepository carsRepository;
    @Autowired
    protected CardRepository cardRepository;
    @Autowired
    protected HistoryRepositories historyRepositories;
    @Autowired
    protected ApiClient apiClient;
}
