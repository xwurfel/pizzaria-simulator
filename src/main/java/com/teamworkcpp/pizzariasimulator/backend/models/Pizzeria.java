package com.teamworkcpp.pizzariasimulator.backend.models;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzeriaCreationStrategy;
import com.teamworkcpp.pizzariasimulator.backend.services.OrderGenerator;
import com.teamworkcpp.pizzariasimulator.backend.services.UniformDistributionPizzeriaCreationStrategy;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Pizzeria {
    private final List<Checkout> _checkouts;
    private List<Order> _orders;
    private final List<Pizzaiolo> _pizzaiolos;
    private final Duration _simulationTime;
    private OrderGenerator _orderGenerator;
    private IPizzeriaCreationStrategy _pizzeriaCreationStrategy;


    public Pizzeria(List<Checkout> checkouts,
                    List<Pizzaiolo> pizzaiolos,
                    OrderGenerator orderGenerator,
                    Duration simulationTime,
                    IPizzeriaCreationStrategy creationStrategy)
    {
        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._orderGenerator = orderGenerator;
        this._simulationTime = simulationTime;
        this._pizzeriaCreationStrategy = creationStrategy;

        generateOrdersWithDelay(_pizzeriaCreationStrategy, _simulationTime);
    }
    public List<Checkout> getCheckouts()
    {
        return _checkouts;
    }

    public List<Order> getOrders()
    {
        return _orders;
    }

    public List<Pizzaiolo> getPizzaiolos()
    {
        return _pizzaiolos;
    }


    private void generateOrdersWithDelay(IPizzeriaCreationStrategy creationStrategy, Duration simulationDuration) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + simulationDuration.toMillis();

        creationStrategy.generateOrdersWithDelay(this, startTime, endTime, _orderGenerator);
    }


}
