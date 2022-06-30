package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Card;
import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.History;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.CardRepository;
import com.carShowroom.WebCarShowroom.repository.CarsRepository;
import com.carShowroom.WebCarShowroom.repository.HistoryRepositories;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class CabinetController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CarsRepository carsRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    HistoryRepositories historyRepositories;

    public Users userGlobal;

    @GetMapping("/cabinet")
    public String cabinet(@ModelAttribute("user") Users user, Model model) {
        userGlobal = user;
        model.addAttribute("name", user.getName());
        model.addAttribute("lastname", user.getLastname());
        return "cabinet";
    }

    @GetMapping("/cabinet/shop")
    public String cabinetShop(RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addAttribute("user", userGlobal);
        return "redirect:/shop";
    }

    @GetMapping("/cabinet/authorization")
    public String authorization(RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addAttribute("user", userGlobal);
        if(userGlobal == null) {
            return "redirect:/authorization";
        }
        return "redirect:/cabinet";
    }

    @GetMapping("/cardelete")
    public String cardelete(RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addAttribute("user", userGlobal);
        return "redirect:/cabinet";
    }

    @GetMapping("/mycarsbuy")
    public String mycarsbuy(Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        Iterable<Cars> cars = user.getCarsUser();
        model.addAttribute("user", cars);
        Iterable<Card> cards = user.getCardsUser();
        model.addAttribute("card", cards);
        int allCost = 0;

        for (Cars cost: cars) {
            allCost += cost.getCost();
        }

        model.addAttribute("cost", allCost);

        return "mycarsbuy";
    }

    //удаление машины из списка
    @GetMapping("/mycarsbuy/{id}")
    public String mycarsdelete(@PathVariable(value="id") Long id, Model model) {
        Cars cars = carsRepository.findCarById(id);
        model.addAttribute("name", cars.getName());
        model.addAttribute("user", cars);

        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        cars.setQuantity(cars.getQuantity() + 1);

        user.getCarsUser().remove(cars);
        usersRepository.save(user);

        return "cardelete";
    }

    //покупка по карте
    @GetMapping("/mycarsbuy/buy/{id}")
    public String carsBuy(@PathVariable(value="id") Long id, Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        Card card = cardRepository.findCardByid(id);
        List<Cars> allCars = new ArrayList<>();
        allCars.addAll(user.getCarsUser());


        model.addAttribute("name", card.getCardNumber());
        model.addAttribute("cars", allCars);

        History history = new History();
        history.setEmail(user.getEmail());
        history.setAppointment("покупка");
        history.setStatus("в автосалоне");
        history.getCarsList().addAll(allCars);
        history.setUser(user);
        historyRepositories.save(history);

        for (Cars cars: allCars) {
            user.getCarsUser().clear();
            usersRepository.save(user);
        }


        return "complitebuy";
    }

    @GetMapping("/mycarsbuy/orders")
    public String myorders(Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        Iterable<History> myOrders = user.getHistory();
        List<History> hist = new ArrayList<>();

        for (History h: myOrders) {
            if(!h.getStatus().equals("отмена")) {
                hist.add(h);
            }
        }

        model.addAttribute("orders", hist);

        return "myorders";
    }

    @GetMapping("/addcarduser")
    public String addcarduser(Model model) {
        Iterable<Card> myCards = userGlobal.getCardsUser();
        model.addAttribute("myCards", myCards);
        return "addcarduser";
    }

    //удаление заказа из истории клиента
    @GetMapping("/myorders/{id}")
    public String myorderRevoke(@PathVariable(value="id") Long id, Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());
        History history = historyRepositories.findHistoryUserByIdhistory(id);
        List<Cars> carList = history.getCarsList();

        history.setStatus("отмена");

        if(history.getAppointment().equals("тест драйв")) {
            for (Cars c: carList) {
                c.setAvaliable(c.getAvaliable() + 1);
            }
        }
        else {
            for (Cars c: carList) {
                c.setQuantity(c.getQuantity() + 1);
            }
        }

        historyRepositories.save(history);
        usersRepository.save(user);

        return "revokeorder";
    }

    @PostMapping("/addcarduser")
    public String addcarduserPost(RedirectAttributes redirectAttributes, @RequestParam String number, @RequestParam String validityMonth, @RequestParam String validityYear, @RequestParam String cvc, Model model) {
        Users user = usersRepository.findByEmail(userGlobal.getEmail());

        Card card = cardRepository.findCardByCardNumber(number);

        redirectAttributes.addAttribute("user", userGlobal);


        if(number.length() == 16 && (validityMonth.length() == 1 || validityMonth.length() == 2) && validityYear.length() == 2 && cvc.length() == 3) {
            if(card == null) {
                String date = validityMonth + "/" + validityYear;
                Card newCard = new Card(number, date, cvc, user);
                user.getCardsUser().add(newCard);
                cardRepository.save(newCard);
                usersRepository.save(user);
            }
            else {
                model.addAttribute("error", "Такая карта уже есть");
            }
        }
        else {
            model.addAttribute("error", "Введите правильное количество цифр");
        }
        return "redirect:/cabinet";
    }

}
