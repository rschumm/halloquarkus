package ch.schumm.halloquarkus;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HalloService {

    public String sagHallo(String name) {
        return "Hoi, " + name;
    }

}