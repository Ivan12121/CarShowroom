package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.History;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.CarsRepository;
import com.carShowroom.WebCarShowroom.repository.HistoryRepositories;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {

    @Autowired
    CarsRepository carsRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    HistoryRepositories historyRepositories;

    public Users userGlobal;
    public Cars carsGlobal;

    @GetMapping("/shop")
    public String shop(@ModelAttribute("user") Users user, Model model) {
        userGlobal = user;

        Iterable<Cars> cars = carsRepository.findAllCarsByEquipment("default");
        model.addAttribute("cars", cars);

        return "shop";
    }

    @GetMapping("/shop/{id}")
    public String buyCar(@PathVariable(value = "id") Long id, Model model) {
    Cars cars = carsRepository.findCarById(id);
    Users user = usersRepository.findByEmail(userGlobal.getEmail());
    List<Cars> carsList = carsRepository.findCarByMark(cars.getMark());
        if(user == null) {
            return "help";
        }
        model.addAttribute("cars", carsList);
        return "shopcarmark";
    }

    @GetMapping("/shop/return")
    public String notcar(RedirectAttributes redirectAttributes, Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        Iterable<Cars> cars = carsRepository.findAllCarsByEquipment("default");
        model.addAttribute("cars", cars);
        redirectAttributes.addAttribute("user", user);

        return "redirect:/shop";
    }

    //заказ авто
    @GetMapping("/toorder")
    public String notcar(Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        Cars cars = carsRepository.findCarByName(carsGlobal.getName());

        History history = new History();
        history.setEmail(user.getEmail());
        history.setAppointment("заказ");
        history.setStatus("ожидание");
        history.getCarsList().add(cars);
        history.setUser(user);
        historyRepositories.save(history);

        return "redirect:/shop";
    }

    //резервирование авто

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/{id}")
    public String reservCar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        if(user == null) {
            return "help";
        }
        else {
            if(cars.getQuantity() <= 0) {
                carsGlobal = cars;
                return "notcar";
            }
            else {
                cars.setQuantity(cars.getQuantity() - 1);

                History history = new History();
                history.setEmail(user.getEmail());
                history.setAppointment("резервирование");
                history.setStatus("ожидание");
                history.getCarsList().add(cars);
                history.setUser(user);
                historyRepositories.save(history);
            }
        }
        redirectAttributes.addAttribute("user", user);
        return "redirect:/shop";
    }

    @GetMapping("/ladabuy")
    public String ladabuy(Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        List<Cars> cars = carsRepository.findCarByMark("lada");
        List<Cars> carsList = new ArrayList<>();

        for (Cars car:cars) {
            if(car.getQuantity() != 0) {
                carsList.add(car);
            }
        }

        model.addAttribute("cars", carsList);
        return "carmarkbuy";
    }

    @GetMapping("/fordbuy")
    public String fordbuy(Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        List<Cars> cars = carsRepository.findCarByMark("ford");
        List<Cars> carsList = new ArrayList<>();

        for (Cars car:cars) {
            if(car.getQuantity() != 0) {
                carsList.add(car);
            }
        }

        model.addAttribute("cars", carsList);
        return "carmarkbuy";
    }

    @GetMapping("/volvobuy")
    public String volvobuy(Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        List<Cars> cars = carsRepository.findCarByMark("volvo");
        List<Cars> carsList = new ArrayList<>();

        for (Cars car:cars) {
            if(car.getQuantity() != 0) {
                carsList.add(car);
            }
        }

        model.addAttribute("cars", carsList);
        return "carmarkbuy";
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/ladabuy/{id}")
    public String ladabuyidreserv(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        else {
            cars.setQuantity(cars.getQuantity() - 1);

            History history = new History();
            history.setEmail(user.getEmail());
            history.setAppointment("резервирование");
            history.setStatus("ожидание");
            history.getCarsList().add(cars);
            history.setUser(user);
            historyRepositories.save(history);
        }
        redirectAttributes.addAttribute("user", user);
        return "redirect:/shop";
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/fordbuy/{id}")
    public String fordbuyidreserv(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        else {
            cars.setQuantity(cars.getQuantity() - 1);

            History history = new History();
            history.setEmail(user.getEmail());
            history.setAppointment("резервирование");
            history.setStatus("ожидание");
            history.getCarsList().add(cars);
            history.setUser(user);
            historyRepositories.save(history);
        }
        redirectAttributes.addAttribute("user", user);
        return "redirect:/shop";
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/reservation/volvobuy/{id}")
    public String volvobuyidreserv(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        else {
            cars.setQuantity(cars.getQuantity() - 1);

            History history = new History();
            history.setEmail(user.getEmail());
            history.setAppointment("резервирование");
            history.setStatus("ожидание");
            history.getCarsList().add(cars);
            history.setUser(user);
            historyRepositories.save(history);
        }
        redirectAttributes.addAttribute("user", user);
        return "redirect:/shop";
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/shop/testDrive/{id}")
    public String testdrive(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        else {
            if(cars.getAvaliable() <= 0) {
                return "help";
            }
            else {
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
        redirectAttributes.addAttribute("user", user);
        return "redirect:/shop";
    }

    @GetMapping("/shop/ladabuy/{id}")
    public String ladabuyid(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        model.addAttribute("car", cars);
        carsGlobal = cars;
        return "carcard";
    }

    @GetMapping("/shop/fordbuy/{id}")
    public String fordbuyid(@PathVariable(value = "id") Long id, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        model.addAttribute("car", cars);
        carsGlobal = cars;
        return "carcard";
    }

    @GetMapping("/shop/volvobuy/{id}")
    public String volvobuyid(@PathVariable(value = "id") Long id, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        model.addAttribute("car", cars);
        carsGlobal = cars;
        return "carcard";
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @GetMapping("/buycar")
    public String buycar(RedirectAttributes redirectAttributes, Model model) {
        Cars cars = carsRepository.findCarByName(carsGlobal.getName());
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        if(user == null) {
            return "help";
        }
        else {
            if(cars.getQuantity() <= 0) {
                carsGlobal = cars;
                return "notcar";
            }
            else {
                cars.setQuantity(cars.getQuantity() - 1);

                user.getCarsUser().add(cars);
                usersRepository.save(user);
            }
        }
        redirectAttributes.addAttribute("user", user);
        return "redirect:/shop";
    }

}

