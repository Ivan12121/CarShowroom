package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthorizationController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/authorization")
    public String authorization(Model model) {
        model.addAttribute("title", "Авторизация");
        return "authorization";
    }

    @PostMapping("/authorization")
    public String authorizationUser(@RequestParam String email, @RequestParam String name, RedirectAttributes redirectAttributes, Model model) {
        Users user = usersRepository.findByEmail(email);
        if(user == null) {
            model.addAttribute("errorEmail", "Такого email не найдено");
            return "authorization";
        }
        else {
            if(user.getName().equals(name)) {
                if(user.getRole().equals("user")) {
                    redirectAttributes.addAttribute("user", user);
                    return "redirect:/cabinet";
                }
                else {
                    redirectAttributes.addAttribute("user", user);
                    return "redirect:/adminpage";
                }

            }
            else {
                model.addAttribute("errorName", "Имя введено неверно");
                return "authorization";
            }
        }

    }

}
