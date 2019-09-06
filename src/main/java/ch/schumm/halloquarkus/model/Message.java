package ch.schumm.halloquarkus.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity(name = "message")
public class Message extends PanacheEntity {
    public String text;
    public LocalDate date; 
}