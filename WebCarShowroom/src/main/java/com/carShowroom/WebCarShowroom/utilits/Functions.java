package com.carShowroom.WebCarShowroom.utilits;

import com.carShowroom.WebCarShowroom.models.Card;
import com.carShowroom.WebCarShowroom.models.Cars;
import com.carShowroom.WebCarShowroom.models.History;
import com.carShowroom.WebCarShowroom.models.Users;
import com.carShowroom.WebCarShowroom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Functions extends ConstFields {

    //user registration function
    public String regUser(Users user, String name, String lastname, String email, String country, RedirectAttributes redirectAttributes, Model model) {
        List<Cars> carsList = new ArrayList<>();
        String role = "user";
        Set<Card> cardsUser = new HashSet<>();

        if (user == null) {
            if (name.isEmpty()) {
                model.addAttribute("errorName", "Введите имя");
                return "registration";
            } else if (email.isEmpty()) {
                model.addAttribute("errorEmail", "Введите email");
                return "registration";
            } else {
                Users usrs = new Users(name, lastname, email, country, role, carsList, cardsUser, "on");
                usersRepository.save(usrs);
                redirectAttributes.addAttribute("user", usrs);
                return "redirect:/cabinet";
            }
        } else return "help";
    }

    //user authorization function
    public String authUser(Users user, String name, RedirectAttributes redirectAttributes, Model model) {
        if (user == null) {
            model.addAttribute("errorEmail", "Такого email не найдено");
            return "authorization";
        } else {
            if (user.getName().equals(name)) {
                if (user.getRole().equals("user")) {
                    return "redirect:/cabinet";
                } else {
                    return "redirect:/adminpage";
                }

            } else {
                model.addAttribute("errorName", "Имя введено неверно");
                return "authorization";
            }
        }
    }







    //view cars in the cart
    public String carsInShoppingCart(Users user, Model model) {
        Iterable<Cars> cars = user.getCarsUser();
        model.addAttribute("user", cars);
        Iterable<Card> cards = user.getCardsUser();
        model.addAttribute("card", cards);
        int allCost = 0;

        for (Cars cost : cars) {
            allCost += cost.getCost();
        }

        model.addAttribute("cost", allCost);

        return "mycarsbuy";
    }

    //removing a car from the cart
    public String removingCarFromTheCart(Users user, Cars cars, Model model) {
        model.addAttribute("name", cars.getName());
        model.addAttribute("user", cars);

        cars.setQuantity(cars.getQuantity() + 1);

        user.getCarsUser().remove(cars);
        usersRepository.save(user);

        return "cardelete";
    }

    public String buyingCarsInTheCart(Users user, Card card, Model model) {
        List<Cars> allCars = new ArrayList<>();
        allCars.addAll(user.getCarsUser());


        model.addAttribute("name", card.getCardNumber());
        model.addAttribute("cars", allCars);

        //saving purchases to history
        History history = new History();
        history.setEmail(user.getEmail());
        history.setAppointment("покупка");
        history.setStatus("в автосалоне");
        history.getCarsList().addAll(allCars);
        history.setUser(user);
        historyRepositories.save(history);

        for (Cars cars : allCars) {
            user.getCarsUser().clear();
            usersRepository.save(user);
        }


        return "complitebuy";
    }

    public String removingOrderFromHistory(Users user, History history) {
        List<Cars> carList = history.getCarsList();

        history.setStatus("отмена");

        if (history.getAppointment().equals("тест драйв")) {
            for (Cars c : carList) {
                c.setAvaliable(c.getAvaliable() + 1);
            }
        }
        else if(history.getAppointment().equals("заказ")) {
            return "revokeorder";
        }

        else {
            for (Cars c : carList) {
                c.setQuantity(c.getQuantity() + 1);
            }
        }

        historyRepositories.save(history);
        usersRepository.save(user);

        return "revokeorder";
    }

    public String addingCreditCard(Users user, Card card, String number, String validityMonth, String validityYear, String cvc, HttpSession session, Model model) {
        if (number.length() == 16 && (validityMonth.length() == 1 || validityMonth.length() == 2) && validityYear.length() == 2 && cvc.length() == 3) {
            if (card == null) {
                String date = validityMonth + "/" + validityYear;
                Card newCard = new Card(number, date, cvc, user);
                user.getCardsUser().add(newCard);
                cardRepository.save(newCard);
                usersRepository.save(user);
            } else {
                model.addAttribute("error", "Такая карта уже есть");
            }
        } else {
            model.addAttribute("error", "Введите правильное количество цифр");
        }
        session.setAttribute("user", user);
        return "redirect:/cabinet";
    }


    public List<History> returnListOfActiveOrders(Iterable<History> allHistory, List<History> history) {
        for (History h : allHistory) {
            if (!h.getStatus().equals("отмена")) {
                history.add(h);
            }
        }
        return history;
    }

    public String changeOrderStatus(String email, Long id, String status, RedirectAttributes redirectAttributes) {
        Users user = usersRepository.findByEmail(email);
        History history = historyRepositories.findHistoryUserByIdhistory(id);
        history.setStatus(status);
        historyRepositories.save(history);
        redirectAttributes.addAttribute("user", user);
        return "redirect:/adminpage";
    }

    public String setUserRole(Long id, Model model) {
        Users user = usersRepository.findUserById(id);
        model.addAttribute("userInfo", user.getName() + " " + user.getLastname());
        user.setRole("admin");
        usersRepository.save(user);
        return "newadmin";
    }

    public Users saveOrderToHistory(String email, String name) {
        Users user = usersRepository.findByEmail(email);
        Cars cars = carsRepository.findCarByName(name);

        History history = new History();
        history.setEmail(user.getEmail());
        history.setAppointment("заказ");
        history.setStatus("ожидание");
        history.getCarsList().add(cars);
        history.setUser(user);
        historyRepositories.save(history);

        return user;
    }

    public String carReservation(String email, Long id, HttpSession session) {
        Users user = usersRepository.findByEmail(email);
        Cars car = carsRepository.findCarById(id);

        if (user == null) {
            return "help";
        } else {
            if (car.getQuantity() <= 0) {
                return "notcar";
            } else {
                car.setQuantity(car.getQuantity() - 1);

                History history = new History();
                history.setEmail(user.getEmail());
                history.setAppointment("резервирование");
                history.setStatus("ожидание");
                history.getCarsList().add(car);
                history.setUser(user);
                historyRepositories.save(history);
            }
        }
        session.setAttribute("user", user);
        return "redirect:/shop";
    }

    public String switchToCarCard(Long id, Model model, String email) {
        Cars cars = carsRepository.findCarById(id);
        Users user = usersRepository.findByEmail(email);

        if (user == null) {
            return "help";
        }
        model.addAttribute("car", cars);
        return "carcard";
    }

    // shop...

}
