package com.carShowroom.WebCarShowroom.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idhistory;
    private String email;
    private String appointment;
    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idusers")
    private Users user;

    @ManyToMany
    @JoinTable (name="history_cars",
            joinColumns=@JoinColumn (name="idhistory"),
            inverseJoinColumns=@JoinColumn(name="idcars"))
    private List<Cars> carsList = new ArrayList<>();

    public History() {

    }

    public History(String email, String appointment, String status, List<Cars> carsList) {
        this.email = email;
        this.appointment = appointment;
        this.status = status;
        this.carsList = carsList;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Cars> getCarsList() {
        return carsList;
    }

    public void setCarsList(List<Cars> carsList) {
        this.carsList = carsList;
    }

    public Long getIdhistory() {
        return idhistory;
    }

    public void setIdhistory(Long idhistory) {
        this.idhistory = idhistory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
