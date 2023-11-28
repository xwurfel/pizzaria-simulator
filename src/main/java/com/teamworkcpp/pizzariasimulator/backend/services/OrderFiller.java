package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.models.Order;

public class OrderFiller {
    private OrderGenerator _instance;

    //configuration field

    private OrderFiller() {

    }

    public OrderGenerator getInstance() {
        if (_instance == null) {
            _instance = new OrderGenerator();
        }
        return _instance;
    }

    public Order GenerateOrder() {
        return _instance.Generate();
    }
}
