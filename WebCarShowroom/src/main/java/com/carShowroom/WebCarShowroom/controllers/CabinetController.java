package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.models.Card;
import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.History;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.CardRepository;
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
public class CabinetController extends ConstFields {

    @Autowired
    Functions func;

    @GetMapping("/cabinet")
    public String cabinet(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if(user.getFutureFlag().equals("off")) {
            model.addAttribute("futureFlagInfo", "Доступ в магазин закрыт, по вопросам обращайтесь в поддержку");
        } else  model.addAttribute("futureFlagInfo", "");
        model.addAttribute("name", user.getName());
        model.addAttribute("lastname", user.getLastname());
        return "cabinet";
    }

    @GetMapping("/cabinet/shop")
    public String cabinetShop(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if(user.getFutureFlag().equals("off")) {
            session.setAttribute("user", user);
            return "redirect:/cabinet";
        }
        session.setAttribute("user", user);
        return "redirect:/shop";
    }

    @GetMapping("/cardelete")
    public String cardelete(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        session.setAttribute("user", user);
        return "redirect:/cabinet";
    }

    //Page with a buyer's car
    @GetMapping("/mycarsbuy")
    public String mycarsbuy(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        return func.carsInShoppingCart(user, model);
    }

    //удаление машины из списка
    @GetMapping("/mycarsbuy/{id}")
    public String mycarsdelete(HttpSession session, @PathVariable(value="id") Long id, Model model) {
        Cars cars = carsRepository.findCarById(id);
        Users userSession = (Users) session.getAttribute("user");
        Users user = usersRepository.findUserById(userSession.getId());
        model.addAttribute("name", cars.getName());
        System.out.println(userSession.getId());

        cars.setQuantity(cars.getQuantity() + 1);

        user.getCarsUser().remove(cars);
        usersRepository.save(user);

        return "cardelete";
    }

    //покупка по карте
    @GetMapping("/mycarsbuy/buy/{id}")
    public String carsBuy(HttpSession session, @PathVariable(value="id") Long id, Model model) {
        Users user = (Users) session.getAttribute("user");
        Card card = cardRepository.findCardByid(id);
        return func.buyingCarsInTheCart(user, card, model);
    }

    @GetMapping("/mycarsbuy/orders")
    public String myorders(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        Iterable<History> allHistory = user.getHistory();
        List<History> history = new ArrayList<>();
        model.addAttribute("orders", func.returnListOfActiveOrders(allHistory, history));
        return "myorders";
    }

    @GetMapping("/addcarduser")
    public String addcarduser(Model model) {
        model.addAttribute("str", apiClient.getTemplate("http://localhost:8082/addcarduser"));
        return "addcarduser";
    }

    //удаление заказа из истории клиента
    @GetMapping("/myorders/{id}")
    public String myorderRevoke(HttpSession session, @PathVariable(value="id") Long id, Model model) {
        Users user = (Users) session.getAttribute("user");
        History history = historyRepositories.findHistoryUserByIdhistory(id);
        return func.removingOrderFromHistory(user, history);
    }

    @GetMapping("/out")
    public String out(HttpSession session, Model model) {
        session.removeAttribute("user");
        return "redirect:/shop";
    }


    @PostMapping("/addcarduser")
    public String addcarduserPost(HttpSession session, @RequestParam String number, @RequestParam String validityMonth, @RequestParam String validityYear, @RequestParam String cvc, Model model) {
        Users user = (Users) session.getAttribute("user");
        Card card = cardRepository.findCardByCardNumber(number);
        return func.addingCreditCard(user, card, number, validityMonth, validityYear, cvc, session, model);
    }

}
