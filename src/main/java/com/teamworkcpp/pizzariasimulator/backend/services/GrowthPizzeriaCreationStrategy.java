package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzeriaCreationStrategy;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrowthPizzeriaCreationStrategy implements IPizzeriaCreationStrategy {
    private final List<Checkout> _checkouts;
    private final List<Pizzaiolo> _pizzaiolos;
    private final Duration _simulateDuration;
    private int pizzaId = 0;

    public GrowthPizzeriaCreationStrategy(List<Checkout> checkouts,
                                            List<Pizzaiolo> pizzaiolos,
                                            Duration _simulateDuration)
    {
        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._simulateDuration = _simulateDuration;
    }

    @Override
    public Pizzeria createPizzeria() {
        return new Pizzeria(_checkouts,
                _pizzaiolos,
                _simulateDuration,
                this );
    }

    @Override
    public void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime) {
        long firstThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.2);
        long secondThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.5);
        long thirdThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.75);

        Random random = new Random();

        List<Thread> checkoutThreads = new ArrayList<>();

        for (Checkout checkout : _checkouts) {
            Thread checkoutThread = new Thread(() -> {
                while (System.currentTimeMillis() < endTime) {
                    long currentTime = System.currentTimeMillis();

                    pizzeria.AddOrder(checkout.Generate(pizzaId++));

                    long delayMillis;
                    if (currentTime < firstThreshold) {
                        delayMillis = random.nextInt((int) (30000 - 25000 + 1)) + 25000;
                    } else if (currentTime < secondThreshold) {
                        delayMillis = random.nextInt((int) (25000 - 22000 + 1)) + 22000;
                    } else if (currentTime < thirdThreshold) {
                        delayMillis = random.nextInt((int) (20000 - 18000 + 1)) + 18000;
                    } else {
                        delayMillis = random.nextInt((int) (14000 - 13000 + 1)) + 13000;
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
    }
}