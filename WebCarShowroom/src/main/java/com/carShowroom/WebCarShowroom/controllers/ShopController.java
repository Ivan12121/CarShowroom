package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.History;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.utilits.ConstFields;
import com.carShowroom.WebCarShowroom.utilits.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController extends ConstFields {

    @Autowired
    Functions func;

//    public Users userGlobal;
    public Cars carGlobal;

    @GetMapping("/shop")
    public String shop(HttpSession session, Model model) {
        session.getAttribute("user");
        Iterable<Cars> cars = carsRepository.findAllCarsByEquipment("default");
        model.addAttribute("cars", cars);

        return "shop";
    }

    @GetMapping("/shop/{id}")
    public String buyCar(HttpSession session, @PathVariable(value = "id") Long id, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = (Users) session.getAttribute("user");
        List<Cars> carsList = carsRepository.findCarByMark(cars.getMark());

        if (user == null) {
            return "help";
        }

        model.addAttribute("cars", carsList);
        return "shopcarmark";
    }

    @GetMapping("/shop/return")
    public String notcar(HttpSession session, Model model) {
        session.getAttribute("user");
        Iterable<Cars> cars = carsRepository.findAllCarsByEquipment("default");
        model.addAttribute("cars", cars);

        return "redirect:/shop";
    }

    @GetMapping("/toorder")
    public String notcar(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        session.setAttribute("user", func.saveOrderToHistory(user.getEmail(), carGlobal.getName()));
        return "redirect:/shop";
    }

    @GetMapping("/ladabuy")
    public String ladabuy(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");

        List<Cars> cars = carsRepository.findCarByMark("lada");
        List<Cars> carsList = new ArrayList<>();

        for (Cars car : cars) {
            if (car.getQuantity() != 0) {
                carsList.add(car);
            }
        }

        model.addAttribute("cars", carsList);
        return "carmarkbuy";
    }

    @GetMapping("/fordbuy")
    public String fordbuy(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");

        List<Cars> cars = carsRepository.findCarByMark("ford");
        List<Cars> carsList = new ArrayList<>();

        for (Cars car : cars) {
            if (car.getQuantity() != 0) {
                carsList.add(car);
            }
        }

        model.addAttribute("cars", carsList);
        return "carmarkbuy";
    }

    @GetMapping("/volvobuy")
    public String volvobuy(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");

        List<Cars> cars = carsRepository.findCarByMark("volvo");
        List<Cars> carsList = new ArrayList<>();

        for (Cars car : cars) {
            if (car.getQuantity() != 0) {
                carsList.add(car);
            }
        }

        model.addAttribute("cars", carsList);
        return "carmarkbuy";
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/{id}")
    public String reservCar(@PathVariable(value = "id") Long id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        carGlobal = carsRepository.findCarById(id);
        return func.carReservation(user.getEmail(), id, session);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/ladabuy/{id}")
    public String ladabuyidreserv(@PathVariable(value = "id") Long id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        carGlobal = carsRepository.findCarById(id);
        return func.carReservation(user.getEmail(), id, session);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/fordbuy/{id}")
    public String fordbuyidreserv(@PathVariable(value = "id") Long id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        carGlobal = carsRepository.findCarById(id);
        return func.carReservation(user.getEmail(), id, session);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/volvobuy/{id}")
    public String volvobuyidreserv(@PathVariable(value = "id") Long id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        carGlobal = carsRepository.findCarById(id);
        return func.carReservation(user.getEmail(), id, session);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/testDrive/{id}")
    public String testdrive(@PathVariable(value = "id") Long id, HttpSession session, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            return "help";
        } else {
            if (cars.getAvaliable() <= 0) {
                return "help";
            } else {
                cars.setAvaliable(cars.getAvaliable() - 1);

                History history = new History();
                history.setEmail(user.getEmail());
                history.setAppointment("тест драйв");
                history.setStatus("ожидание");
                history.getCarsList().add(cars);
                history.setUser(user);
                historyRepositories.save(history);
            }
        }
        session.setAttribute("user", user);
        return "redirect:/shop";
    }

    @GetMapping("/shop/ladabuy/{id}")
    public String ladabuyid(HttpSession session, @PathVariable(value = "id") Long id, Model model) {
        Users user = (Users) session.getAttribute("user");
        carGlobal = carsRepository.findCarById(id);
        return func.switchToCarCard(id, model, user.getEmail());
    }

    @GetMapping("/shop/fordbuy/{id}")
    public String fordbuyid(HttpSession session, @PathVariable(value = "id") Long id, Model model) {
        Users user = (Users) session.getAttribute("user");
        carGlobal = carsRepository.findCarById(id);
        return func.switchToCarCard(id, model, user.getEmail());
    }

    @GetMapping("/shop/volvobuy/{id}")
    public String volvobuyid(HttpSession session, @PathVariable(value = "id") Long id, Model model) {
        Users user = (Users) session.getAttribute("user");
        carGlobal = carsRepository.findCarById(id);
        return func.switchToCarCard(id, model, user.getEmail());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/buycar")
    public String buycar(HttpSession session, Model model) {
        Cars cars = carsRepository.findCarByName(carGlobal.getName());
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            return "help";
        } else {
            if (cars.getQuantity() <= 0) {
                carGlobal = cars;
                return "notcar";
            } else {
                cars.setQuantity(cars.getQuantity() - 1);

                user.getCarsUser().add(cars);
                usersRepository.save(user);
            }
        }
        session.setAttribute("user", user);
        return "redirect:/shop";
    }

}

