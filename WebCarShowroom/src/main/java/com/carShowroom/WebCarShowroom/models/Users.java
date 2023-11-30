package com.carShowroom.WebCarShowroom.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String country;
    private String role;
    private String futureFlag;

    @OneToMany(mappedBy = "cards", fetch = FetchType.EAGER)
    private Set<Card> cardsUser = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<History> history = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name="cars_users",
            joinColumns = @JoinColumn(name = "idusers"),
            inverseJoinColumns = @JoinColumn(name = "idcars"))
    private List<Cars> carsUser = new ArrayList<>();

    public Users() {

    }

    public Users(String name, String lastname, String email, String country, String role, List<Cars> carsUser, Set<Card> cardsUser, String futureFlag) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.country = country;
        this.role = role;
        this.carsUser = carsUser;
        this.cardsUser = cardsUser;
        this.futureFlag = futureFlag;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public Set<Card> getCardsUser() {
        return cardsUser;
    }

    public void setCardsUser(Set<Card> cardsUser) {
        this.cardsUser = cardsUser;
    }

    public List<Cars> getCarsUser() {
        return carsUser;
    }

    public void setCarsUser(List<Cars> carsUser) {
        this.carsUser = carsUser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFutureFlag() {
        return futureFlag;
    }

    public void setFutureFlag(String futureFlag) {
        this.futureFlag = futureFlag;
    }
}
