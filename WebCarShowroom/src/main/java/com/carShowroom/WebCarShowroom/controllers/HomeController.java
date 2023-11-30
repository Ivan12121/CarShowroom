package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "redirect:/shop";
    }

    @GetMapping("/help")
    public String help(Model model) {
        model.addAttribute("title", "Помощь");
        return "help";
    }

    @GetMapping("/home/shop")
    public String shopPage(Model model) {
        model.addAttribute("title", "Эконом класс");
        return "redirect:/shop";
    }



}