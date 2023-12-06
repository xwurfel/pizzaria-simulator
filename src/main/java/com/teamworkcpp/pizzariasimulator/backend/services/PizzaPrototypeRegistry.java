package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzaPrototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class PizzaPrototypeRegistry {
    private static PizzaPrototypeRegistry _instance;
    private final List<IPizzaPrototype> _pizzaPrototypes = new ArrayList<>();

    private PizzaPrototypeRegistry() {

    }

    public static PizzaPrototypeRegistry getInstance() {
        if (_instance == null) {
            _instance = new PizzaPrototypeRegistry();
        }
        return _instance;
    }

    public Optional<IPizzaPrototype> getRandomPizza() {
        Random random = new Random();
        return Optional.ofNullable(_pizzaPrototypes.get(random.nextInt(_pizzaPrototypes.size())));
    }
    public List<String> getItemsNames() {
        return _pizzaPrototypes.stream().map(IPizzaPrototype::getName).collect(Collectors.toList());
    }

    public IPizzaPrototype getByName(String name) {
        return _pizzaPrototypes.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public void addItem(IPizzaPrototype pizzaPrototype) {
        _pizzaPrototypes.add(pizzaPrototype);
    }
}
