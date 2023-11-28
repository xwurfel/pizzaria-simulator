package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzeriaCreationStrategy;
import com.teamworkcpp.pizzariasimulator.backend.models.Checkout;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class RushHourPizzeriaCreationStrategy implements IPizzeriaCreationStrategy {
    private List<Checkout> _checkouts;
    private List<Pizzaiolo> _pizzaiolos;
    private OrderGenerator _orderGenerator;
    private Duration _simulateDuration;

    public RushHourPizzeriaCreationStrategy()
    {

    }
    public RushHourPizzeriaCreationStrategy(List<Checkout> checkouts,
                                                       List<Pizzaiolo> pizzaiolos,
                                                       OrderGenerator orderGenerator,
                                                        Duration _simulateDuration)
    {
        this._orderGenerator = orderGenerator;
        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._simulateDuration = _simulateDuration;
    }
    @Override
    public Pizzeria createPizzeria() {
        return new Pizzeria(_checkouts,
                _pizzaiolos,
                _orderGenerator,
                _simulateDuration,
                this );
    }

    @Override
    public void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime, OrderGenerator orderGenerator) {

        long firstThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.2);
        long secondThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.8);

        while (System.currentTimeMillis() < endTime) {
            long currentTime = System.currentTimeMillis();
            Random random = new Random();
            if (currentTime < firstThreshold) {
                orderGenerator.Generate();
                long delayMillis = random.nextInt((int) (10000 - 5000 + 1)) + 5000; // Випадкова затримка від 5 до 10 секунд

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (currentTime < secondThreshold) {
                orderGenerator.Generate();
                long delayMillis = random.nextInt((int) (6000 - 3000 + 1)) + 3000; // Випадкова затримка від 3 до 6 секунд

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                orderGenerator.Generate();
                long delayMillis = random.nextInt((int) (10000 - 5000 + 1)) + 5000; // Випадкова затримка від 5 до 10 секунд

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

