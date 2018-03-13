/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m2miage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Anthony
 */
@Entity
public class Action {
    @Id
    private String id;
    private String nom;
    private String personnecharge;
    private String etat;

    public Action(String id, String nom, String personnecharge, String etat, String date) {
        this.id = id;
        this.nom = nom;
        this.personnecharge = personnecharge;
        this.etat = etat;
        this.date = date;
    }
    private String date;

    public Action() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPersonnecharge() {
        return personnecharge;
    }

    public void setPersonnecharge(String personneEnCharge) {
        this.personnecharge = personneEnCharge;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}