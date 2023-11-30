package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.History;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.CarsRepository;
import com.carShowroom.WebCarShowroom.repository.HistoryRepositories;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import com.carShowroom.WebCarShowroom.utilits.ConstFields;
import com.carShowroom.WebCarShowroom.utilits.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminPageController extends ConstFields {

    @Autowired
    Functions func;
    Users userGlobal;

    @GetMapping("/adminpage")
    public String adminpage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user.getEmail() == null) {
            return "help";
        } else {
            userGlobal = user;
            model.addAttribute("hi", "Здраствуйте, " + user.getName());
            return "adminpage";
        }

    }

    @GetMapping("/allbuyeradmin")
    public String showbuyer(Model model) {
        Iterable<Users> users = usersRepository.findAll();
        model.addAttribute("users", users);
        return "allbuyeradmin";
    }

    @GetMapping("/allorderuser")
    public String allorderuser(Model model) {
        Iterable<History> history = historyRepositories.findAll();
        model.addAttribute("history", history);
        return "allorderuser";
    }

    @GetMapping("/allcarorder")
    public String allcarorder(Model model) {
        List<History> allHistory = historyRepositories.findHistoryByAppointment("заказ");
        List<History> history = new ArrayList<>();
        model.addAttribute("history", func.returnListOfActiveOrders(allHistory, history));
        return "allcarorder";
    }

    @GetMapping("/allcarorder/{id}")
    public String allcarorderId(@PathVariable(value = "id") Long id, Model model) {
        return "editstatus";
    }

    @GetMapping("/allcarsadmin")
    public String showcars(Model model) {
        Iterable<Cars> cars = carsRepository.findAll();
        model.addAttribute("cars", cars);
        return "allcarsadmin";
    }

    @GetMapping("/addoreditcar")
    public String addoreditcar(Model model) {
        return "addoreditcar";
    }

    @PostMapping("/allcarorder/{id}")
    public String allcarorderIdPost(@PathVariable(value = "id") Long id, @RequestParam(value = "status") String status, RedirectAttributes redirectAttributes, Model model) {
        return func.changeOrderStatus(userGlobal.getEmail(), id, status, redirectAttributes);
    }

    //
    //
    //change!!!!
    @PostMapping("/addoreditcar")
    public String addoreditcarPost(@RequestParam String name, @RequestParam String mark, @RequestParam int quantity, @RequestParam Long cost, @RequestParam(value = "textCar") String text, @RequestParam(value = "equipment") String equip, @RequestParam(value = "fileName") String fileName, Model model) {
        Cars car = carsRepository.findCarByName(name);
        if (car == null) {
            Cars newCar = new Cars(name, cost, text, equip, fileName, quantity, mark);
            carsRepository.save(newCar);
        } else {
            car.setCost(cost);
            car.setName(name);
            car.setText(text);
            car.setEquipment(equip);
            car.setFileName(fileName);
            car.setQuantity(quantity);
            carsRepository.save(car);
        }
        return "addoreditcar";
    }
    //
    //
    //

    @GetMapping("/allbuyeradmin/{id}")
    public String addoreditcar(@PathVariable(value = "id") Long id, Model model) {
        Users user = usersRepository.findUserById(id);
        model.addAttribute("userInfo", user.getName() + " " + user.getLastname());
        if(user.getRole().equals("user")) {
            user.setRole("admin");
        }
        else if(user.getRole().equals("admin")) {
            user.setRole("user");
        }
        else return "adminpage";

        usersRepository.save(user);
        return "newadmin";
    }

    @GetMapping("/shopoff/{id}")
    public String shopOff(@PathVariable(value = "id") Long id, Model model) {
        Users user = usersRepository.findUserById(id);
        model.addAttribute("userInfo", user.getName() + " " + user.getLastname());
        if(!user.getRole().equals("superAdmin")) {
            if(user.getFutureFlag().equals("on")) {
                user.setFutureFlag("off");
            }
            else {
                user.setFutureFlag("on");
            }
        } else return "adminpage";

        usersRepository.save(user);
        return "adminpage";
    }

}
