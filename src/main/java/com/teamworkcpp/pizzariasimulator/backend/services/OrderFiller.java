package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;

public class OrderFiller {
    private OrderFiller _instance;

    //configuration field

    private OrderFiller() {

    }

    public OrderFiller getInstance() {
        if (_instance == null) {
            _instance = new OrderFiller();
        }
        return _instance;
    }

    public IPizza GenerateOrder() {
        return null;
    }
}
