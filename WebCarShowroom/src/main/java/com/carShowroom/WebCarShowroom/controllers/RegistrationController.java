package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Card;
import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import com.carShowroom.WebCarShowroom.utilits.ConstFields;
import com.carShowroom.WebCarShowroom.utilits.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@SessionAttributes("user")
public class RegistrationController extends ConstFields {

    @Autowired
    Functions func;

    @GetMapping("/registration")
    public String regPage(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("name", "Регистрация");
        return "registration";
    }

    @Transactional
    @PostMapping("/registration")
    public String registration(@RequestParam String name, @RequestParam String lastname, @RequestParam String email, @RequestParam String country, RedirectAttributes redirectAttributes, Model model) {
        Users user = usersRepository.findByEmail(email);
        return func.regUser(user, name, lastname, email, country, redirectAttributes, model);
    }
}
