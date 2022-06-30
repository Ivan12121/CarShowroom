package com.carShowroom.WebCarShowroom.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Cars {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long cost;
    private String text;
    private String equipment;
    private String fileName;
    private int quantity;
    private String mark;
    private int avaliable;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (name="history_cars",
            joinColumns=@JoinColumn (name="idcars"),
            inverseJoinColumns=@JoinColumn(name="idhistory"))
    private Set<History> historyList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="cars_users",
            joinColumns = @JoinColumn(name = "idcars"),
            inverseJoinColumns = @JoinColumn(name = "idusers"))
    private List<Users> usersCar = new ArrayList<>();

    public Cars() {

    }

    public Cars(String name, Long cost, String text, String equipment, String fileName, int quantity, String mark) {
        this.name = name;
        this.cost = cost;
        this.text = text;
        this.equipment = equipment;
        this.fileName = fileName;
        this.quantity = quantity;
        this.mark = mark;
    }

    public int getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(int avaliable) {
        this.avaliable = avaliable;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(Set<History> historyList) {
        this.historyList = historyList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public List<Users> getUsersCar() {
        return usersCar;
    }

    public void setUsersCar(List<Users> usersCar) {
        this.usersCar = usersCar;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCost() {
        return cost;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return name + " " + cost;
    }
}
