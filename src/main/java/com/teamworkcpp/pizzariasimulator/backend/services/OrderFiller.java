package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzaPrototype;

import java.util.Optional;

public class OrderFiller {
    private static OrderFiller _instance;
    private final PizzaPrototypeRegistry prototypeRegistry;

    private OrderFiller(PizzaPrototypeRegistry registry) {
        this.prototypeRegistry = registry;
    }

    public static OrderFiller getInstance() {
        if (_instance == null) {
            _instance = new OrderFiller(PizzaPrototypeRegistry.getInstance());
        }
        return _instance;
    }

    public IPizza generatePizza() {
        Optional<IPizzaPrototype> iPizza = prototypeRegistry.getRandomPizza();
        if (iPizza.isEmpty()) {
            throw new RuntimeException("Pizza doesn't exist ");
        }
        return (IPizza) iPizza.get();
    }
}
