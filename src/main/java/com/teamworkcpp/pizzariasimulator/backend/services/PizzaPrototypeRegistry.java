package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzaPrototype;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PizzaPrototypeRegistry {
    private PizzaPrototypeRegistry _instance;
    private final List<IPizzaPrototype> _pizzaPrototypes = new ArrayList<>();

    private PizzaPrototypeRegistry() {

    }

    private PizzaPrototypeRegistry getInstance() {
        if (_instance == null) {
            _instance = new PizzaPrototypeRegistry();
        }
        return _instance;
    }

    public List<String> getItemsNames() {
        return _pizzaPrototypes.stream().map(IPizzaPrototype::getName).collect(Collectors.toList());
    }

    public IPizza getByName(String name) {
        return (IPizza) _pizzaPrototypes.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public void addItem(IPizzaPrototype pizzaPrototype) {
        _pizzaPrototypes.add(pizzaPrototype);
    }
}
