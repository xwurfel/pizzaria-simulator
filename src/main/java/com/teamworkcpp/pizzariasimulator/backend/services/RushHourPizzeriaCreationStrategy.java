package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzeriaCreationStrategy;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RushHourPizzeriaCreationStrategy implements IPizzeriaCreationStrategy {
    private final List<Checkout> _checkouts;
    private final List<Pizzaiolo> _pizzaiolos;
    private final Duration _simulateDuration;
    private int pizzaId = 0;
    private final  long SPREAD_GENERATION_TIME = 10000;
    RushHourPizzeriaCreationStrategy strategy;
    public RushHourPizzeriaCreationStrategy(List<Checkout> checkouts,
                                                       List<Pizzaiolo> pizzaiolos,
                                                        Duration _simulateDuration)
    {

        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._simulateDuration = _simulateDuration;
        this.strategy = this;
    }
    @Override
    public Pizzeria createPizzeria() {
        return new Pizzeria(_checkouts,
                _pizzaiolos,
                _simulateDuration,
                strategy );
    }

    @Override
    public void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime) {

        long firstThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.2);
        long secondThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.8);

        List<Thread> checkoutThreads = new ArrayList<>();
        Random random = new Random();
        Thread generationThread = new Thread(() ->
        {
            for (Checkout checkout : _checkouts) {
                Thread checkoutThread = new Thread(() -> {
                    while (System.currentTimeMillis() < endTime) {
                        long currentTime = System.currentTimeMillis();

                        pizzeria.AddOrder(checkout.Generate(pizzaId++));

                        long delayMillis;
                        if (currentTime < firstThreshold) {
                            delayMillis = random.nextInt((int) (SPREAD_GENERATION_TIME)) + 30000;
                        } else if (currentTime < secondThreshold) {
                            delayMillis = random.nextInt((int) (SPREAD_GENERATION_TIME)) + 15000;
                        } else {
                            delayMillis = random.nextInt((int) (SPREAD_GENERATION_TIME)) + 30000;
                        }

                        try {
                            Thread.sleep(delayMillis);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                checkoutThreads.add(checkoutThread);
                checkoutThread.start();
            }

            for (Thread thread : checkoutThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        generationThread.start();
    }
}

