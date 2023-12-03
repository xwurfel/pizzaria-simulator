package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Checkout {
    // fields
    public int id;
    private final int _maxPizzaCount;
    private final OrderFiller _orderFiller;

    public Checkout(int id, int maxPizzaCount, OrderFiller orderFiller)
    {
        this.id = id;
        this._maxPizzaCount = maxPizzaCount;
        this._orderFiller = orderFiller;
    }
    public Order Generate(int id)
    {
        List<IPizza> pizzas = new ArrayList<>();

        Random random = new Random();

        for(int i = 0; i < random.nextInt(1, _maxPizzaCount); ++i)
        {
            pizzas.add(_orderFiller.GenerateOrder());
        }

        return new Order(id,
                pizzas,
                OrderStatus.NEW,
                false,
                id);
    }
}
