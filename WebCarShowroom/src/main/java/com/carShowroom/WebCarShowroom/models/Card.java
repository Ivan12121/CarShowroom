package com.carShowroom.WebCarShowroom.models;


import javax.persistence.*;
import java.util.Properties;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cardNumber;
    private String validity;
    private String cvc;

    @ManyToOne (optional=false, cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "iduser")
    private Users cards;

    public Card() {
    }

    public Card(String cardNumber, String validity, String cvc, Users cards) {
        this.cardNumber = cardNumber;
        this.validity = validity;
        this.cvc = cvc;
        this.cards = cards;
    }

    public Users getCards() {
        return cards;
    }

    public void setCards(Users cards) {
        this.cards = cards;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getValidity() {
        return validity;
    }

    public String getCvc() {
        return cvc;
    }
}
