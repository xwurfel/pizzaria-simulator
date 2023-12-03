package com.teamworkcpp.pizzariasimulator.backend.helpers;

import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.services.Checkout;

import java.util.List;

public class SimulationState {
    public List<Order> orders;
    public List<Checkout> checkouts;
    public List<Pizzaiolo> pizzaiolos;
    public boolean isEnded;

    public SimulationState(List<Order> o, List<Checkout> c, List<Pizzaiolo> p, boolean e)
    {
        orders = o;
        checkouts = c;
        pizzaiolos = p;
        isEnded = e;
    }
}
