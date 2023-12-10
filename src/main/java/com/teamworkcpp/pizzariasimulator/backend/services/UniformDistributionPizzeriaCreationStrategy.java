package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzeriaCreationStrategy;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class UniformDistributionPizzeriaCreationStrategy implements IPizzeriaCreationStrategy {
    private final List<Checkout> _checkouts;
    private final List<Pizzaiolo> _pizzaiolos;
    private final Duration _simulateDuration;
    private int pizzaId = 0;
    private final long SPREAD_GENERATION_TIME = 10000;
    private static final Object lock = new Object();

    public UniformDistributionPizzeriaCreationStrategy(List<Checkout> checkouts,
                                                       List<Pizzaiolo> pizzaiolos,
                                                       Duration simulateDuration) {
        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._simulateDuration = simulateDuration;
    }

    @Override
    public Pizzeria createPizzeria() {
        return new Pizzeria(_checkouts,
                _pizzaiolos,
                _simulateDuration,
                this);
    }

    @Override
    public void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime) {
        Random random = new Random();
        List<Thread> checkoutThreads = new ArrayList<>();
        Thread generationThread = new Thread(() ->
        {
            for (Checkout checkout : _checkouts) {
                Thread checkoutThread = new Thread(() -> {
                    while (System.currentTimeMillis() < endTime) {
                        long delayMillis = random.nextInt((int) (SPREAD_GENERATION_TIME)) + 25000;

                        pizzeria.AddOrder(checkout.Generate(pizzaId++));

                        try {
                            Thread.sleep(delayMillis+ 20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                checkoutThreads.add(checkoutThread);
                checkoutThread.start();
            }

            // Чекаємо, поки всі потоки завершаться
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

