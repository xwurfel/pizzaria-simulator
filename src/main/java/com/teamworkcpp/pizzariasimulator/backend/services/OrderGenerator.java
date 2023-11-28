package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.models.Order;

public class OrderGenerator {

    //fields

    public Order Generate()
    {
        return new Order(0, null, null, false, 0);
    }
}
