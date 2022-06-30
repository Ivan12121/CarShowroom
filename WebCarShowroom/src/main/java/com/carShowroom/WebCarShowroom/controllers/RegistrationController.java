package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Card;
import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("name", "Регистрация");
        return "registration";
    }

    @Transactional
    @PostMapping("/registration")
    public String registrationUser(@RequestParam String name, @RequestParam String lastname, @RequestParam String email, @RequestParam String country, Model model) {
        Users user = usersRepository.findByEmail(email);
        List<Cars> carsList = new ArrayList<>();
        String role = "user";
        Set<Card> cardsUser = new HashSet<>();
        if(user == null) {
            Users usrs = new Users(name, lastname,email, country, role, carsList, cardsUser);
            usersRepository.save(usrs);
            return "home";
        }
        else {
            return "help";
        }
    }
}
