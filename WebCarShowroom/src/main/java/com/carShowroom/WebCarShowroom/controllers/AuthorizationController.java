package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import com.carShowroom.WebCarShowroom.utilits.ConstFields;
import com.carShowroom.WebCarShowroom.utilits.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class AuthorizationController extends ConstFields {

    @Autowired
    Functions func;

    @GetMapping("/authorization")
    public String authPage(Model model) {
        model.addAttribute("title", "Авторизация");
        return "authorization";
    }


    @PostMapping("/authorization")
    public String authorization(HttpSession session, @RequestParam String email, @RequestParam String name, RedirectAttributes redirectAttributes, Model model) {
        Users user = usersRepository.findByEmail(email);
        session.setAttribute("user", user);
        return func.authUser(user, name, redirectAttributes, model);
    }

}
