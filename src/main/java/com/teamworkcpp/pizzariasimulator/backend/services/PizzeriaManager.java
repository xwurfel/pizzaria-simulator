package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.models.Checkout;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PizzeriaManager
{
    private int _checkoutCount;
    private int _pizzaioloCount;
    private int _pizzaCount;
    private SimulationMode _simulationMode;
    private Duration _minimalCookingTime;
    private Duration _simulationDuration;
    private Pizzeria _pizzeria;
    private OrderGenerator _orderGenerator;

    public void AddCheckoutCount(int count)
    {
        _checkoutCount = count;
    }

    public void AddPizzaioloCount(int count)
    {
        _pizzaioloCount = count;
    }

    public void AddPizzaCount(int count)
    {
        _pizzaCount = count;
    }

    public void AddLevel(SimulationMode level)
    {
        _simulationMode = level;
    }

    public void AddMininalCookindTime(Duration duration)
    {
        _minimalCookingTime = duration;
    }
    public void AddSimulationDuration(Duration duration)
    {
        _simulationDuration = duration;
    }

    public void Build() throws Exception {

        ///Add implementation for building OrderGenerator, PizzaPrototypeRegistry,

        switch(_simulationMode)
        {
            case SimulationMode.UNIFORM_DISTRIBUTION:
                _pizzeria = new UniformDistributionPizzeriaCreationStrategy(
                    BuildCheckouts(_checkoutCount),
                    BuildPizzaiolos(_pizzaioloCount),
                    BuildOrderGenerator(),
                    _simulationDuration
                ).createPizzeria();
                break;

            case SimulationMode.GROWTH:
                _pizzeria = new GrowthPizzeriaCreationStrategy(
                        BuildCheckouts(_checkoutCount),
                        BuildPizzaiolos(_pizzaioloCount),
                        BuildOrderGenerator(),
                        _simulationDuration
                ).createPizzeria();
                break;

            case SimulationMode.RUSH_HOUR:
                _pizzeria = new RushHourPizzeriaCreationStrategy(
                        BuildCheckouts(_checkoutCount),
                        BuildPizzaiolos(_pizzaioloCount),
                        BuildOrderGenerator(),
                        _simulationDuration
                ).createPizzeria();
                break;

            default:
                throw new Exception("Invalid simulation mode");
        }
    }

    private List<Pizzaiolo> BuildPizzaiolos(int count)
    {
        List<Pizzaiolo> pizzaiolos = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Pizzaiolo pizzaiolo = new Pizzaiolo(i,
                    true,
                    Duration.ofSeconds(ThreadLocalRandom.current().nextInt(20, 36)));

            pizzaiolos.add(pizzaiolo);
        }

        return pizzaiolos;
    }

    private List<Checkout> BuildCheckouts(int count)
    {
        List<Checkout> checkouts = new ArrayList<>();

        for(int i = 1; i <= count; i++)
        {
            Checkout checkout = new Checkout(i);
            checkouts.add(checkout);
        }

        return  checkouts;
    }
    private OrderGenerator BuildOrderGenerator() throws Exception
    {
        throw new Exception("Not implemented");
    }
}

enum SimulationMode
{
    UNIFORM_DISTRIBUTION,
    GROWTH,
    RUSH_HOUR
}
