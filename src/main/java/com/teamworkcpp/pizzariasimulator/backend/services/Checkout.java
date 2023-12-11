package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.helpers.Logger;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Checkout {
    // fields
    public int id;
    private final int maxPizzaCount;

    public Checkout(int id, int maxPizzaCount)
    {
        this.id = id;
        this.maxPizzaCount = maxPizzaCount;
    }
    public synchronized Order Generate(int id)
    {
        List<IPizza> pizzas = new ArrayList<>();

        Random random = new Random();

        for(int i = 0; i < random.nextInt(maxPizzaCount)+1; i++)
        {
            IPizza pizza = OrderFiller.getInstance().generatePizza();
            pizza.setId(Integer.toString(id) + Integer.toString(i));

            pizzas.add(pizza);
        }

        try {
            Logger.log(" Order: "+ id + " generated/ pizza count: " + pizzas.size() + "/ pizza name: " + pizzas.getFirst().getName() + "/ checkout id: "+ this.id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Order(id,
                pizzas,
                OrderStatus.NEW,
                this.id,
                LocalTime.now());
    }
}
