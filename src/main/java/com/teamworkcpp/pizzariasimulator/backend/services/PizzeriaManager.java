package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
import com.teamworkcpp.pizzariasimulator.backend.enums.SimulationMode;
import com.teamworkcpp.pizzariasimulator.backend.helpers.Logger;
import com.teamworkcpp.pizzariasimulator.backend.helpers.SimulationState;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;
import com.teamworkcpp.pizzariasimulator.backend.models.SimplePizza;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PizzeriaManager
{
    private int checkoutCount;
    private int pizzaioloCount;
    private SimulationMode simulationMode;
    private Duration simulationDuration;
    private Pizzeria pizzeria;
    private int maxPizzaCountInOrder = 2;
    private CookingMode cookingMode;
    private OrderFiller orderFiller;

    public PizzeriaManager(){}

    public void AddMaxPizzaCountInOrder(int count) {
        maxPizzaCountInOrder = count;
    }

    public void AddCheckoutCount(int count)
    {
        checkoutCount = count;
    }

    public void AddPizzaioloCount(int count)
    {
        pizzaioloCount = count;
    }

    public void AddLevel(SimulationMode level)
    {
        simulationMode = level;
    }

    public void AddSimulationDuration(Duration duration)
    {
        simulationDuration = duration;
    }

    public void AddCookingMode(CookingMode cookingMode)
    {
        this.cookingMode = cookingMode;
    }

    public SimulationState getCurrentSimulationState() { return pizzeria.getSimulationState(); }

    private List<Pizzaiolo> BuildPizzaiolos(int count)
    {
        List<Pizzaiolo> pizzaiolos = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Pizzaiolo pizzaiolo = new Pizzaiolo(i,
                    true,
                    Duration.ofSeconds(0));

            pizzaiolos.add(pizzaiolo);
        }

        try {
            Logger.log(" BUILDER: Pizzaiolos were built");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pizzaiolos;
    }

    private List<Checkout> BuildCheckouts(int count)
    {
        List<Checkout> checkouts = new ArrayList<>();

        for(int i = 1; i <= count; i++)
        {
            Checkout checkout = new Checkout(i, maxPizzaCountInOrder);
            checkouts.add(checkout);
        }

        try {
            Logger.log(" BUILDER: Checkouts were built");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  checkouts;
    }


    public void AddPizza(String name, double price, Duration cookingTime)
    {
        Random r = new Random();

        long maxKnittingTime = (long) (cookingTime.toMillis() * 0.2);
        long randomKnittingTime = r.nextInt((int) maxKnittingTime) + 1;
        Duration knittingTime = Duration.ofMillis(Math.max(randomKnittingTime, (long) (0.2 * cookingTime.toMillis())) + 5000);
        Duration fillingTime = Duration.ofMillis(r.nextInt((int) (cookingTime.toMillis() * 0.2)) + + 5000);
        Duration bakingTime = Duration.ofMillis(r.nextInt((int) (cookingTime.toMillis() * 0.2)) + 5000);
        Duration afterBakingTime = Duration.ofMillis(r.nextInt((int) (cookingTime.toMillis() * 0.2)) + 5000);
        Duration packagingTime = Duration.ofMillis(r.nextInt((int) (cookingTime.toMillis() * 0.2)) + 5000);



        PizzaPrototypeRegistry.getInstance().addItem(new SimplePizza(name, price, knittingTime,
                fillingTime, bakingTime, afterBakingTime, packagingTime));

        try {
            Logger.log(" BUILDER: Pizza added: "+ "name: "+ name + "price: "+ price + "cookingTime "+cookingTime +
                    "\nknittingTime: " + knittingTime.toMillis()+
                    "\nfillingTime: " + fillingTime.toMillis()+
                    "\nbakingTime: " + bakingTime.toMillis()+
                    "\nafterTime: " + afterBakingTime.toMillis()+
                    "\npackagingTime: " + packagingTime.toMillis());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Build() throws Exception {

        switch(simulationMode)
        {
            case SimulationMode.UNIFORM_DISTRIBUTION:
                pizzeria = new UniformDistributionPizzeriaCreationStrategy(
                        BuildCheckouts(checkoutCount),
                        BuildPizzaiolos(pizzaioloCount),
                        simulationDuration
                ).createPizzeria();
                pizzeria.setCookingMode(cookingMode);
                break;

            case SimulationMode.GROWTH:
                pizzeria = new GrowthPizzeriaCreationStrategy(
                        BuildCheckouts(checkoutCount),
                        BuildPizzaiolos(pizzaioloCount),
                        simulationDuration
                ).createPizzeria();
                pizzeria.setCookingMode(cookingMode);
                break;

            case SimulationMode.RUSH_HOUR:
                pizzeria = new RushHourPizzeriaCreationStrategy(
                        BuildCheckouts(checkoutCount),
                        BuildPizzaiolos(pizzaioloCount),
                        simulationDuration
                ).createPizzeria();
                pizzeria.setCookingMode(cookingMode);
                break;

            default:
                try {
                    Logger.log(" BUILDER: Error while building pizzeria");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                throw new Exception("Invalid simulation mode");
        }

        try {
            Logger.log(" BUILDER: Pizzeria were built");
            Logger.log("BUILDER: Starting new simulation.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pizzeria.start();
    }
}

