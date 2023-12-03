package com.teamworkcpp.pizzariasimulator.backend.services;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
import com.teamworkcpp.pizzariasimulator.backend.enums.SimulationMode;
import com.teamworkcpp.pizzariasimulator.backend.helpers.Logger;
import com.teamworkcpp.pizzariasimulator.backend.helpers.SimulationState;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PizzeriaManager
{
    private int _checkoutCount;
    private int _pizzaioloCount;
    private SimulationMode _simulationMode;
    private Duration _simulationDuration;
    private Pizzeria _pizzeria;
    private int _maxPizzaCountInOrder;
    private CookingMode _cookingMode;

    public PizzeriaManager(){}

    public void AddMaxPizzaCountInOrder(int count) {_maxPizzaCountInOrder = count;}

    public void AddCheckoutCount(int count)
    {
        _checkoutCount = count;
    }

    public void AddPizzaioloCount(int count)
    {
        _pizzaioloCount = count;
    }

    public void AddLevel(SimulationMode level)
    {
        _simulationMode = level;
    }

    public void AddSimulationDuration(Duration duration)
    {
        _simulationDuration = duration;
    }

    public void AddCookingMode(CookingMode cookingMode)
    {
        _cookingMode = cookingMode;
    }

    public SimulationState getCurrentSimulationState() { return _pizzeria.getSimulationState(); }

    private List<Pizzaiolo> BuildPizzaiolos(int count)
    {
        List<Pizzaiolo> pizzaiolos = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Pizzaiolo pizzaiolo = new Pizzaiolo(i,
                    true,
                    Duration.ofSeconds(ThreadLocalRandom.current().nextInt(20, 36)));

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
        OrderFiller orderFiller = BuildOrderFiller();
        for(int i = 1; i <= count; i++)
        {
            Checkout checkout = new Checkout(i, _maxPizzaCountInOrder, orderFiller);
            checkouts.add(checkout);
        }

        try {
            Logger.log(" BUILDER: Checkouts were built");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  checkouts;
    }

    private OrderFiller BuildOrderFiller()
    {
        // return new OrderFiller(params)
        return null;
    }

    public void Build() throws Exception {

        switch(_simulationMode)
        {
            case SimulationMode.UNIFORM_DISTRIBUTION:
                _pizzeria = new UniformDistributionPizzeriaCreationStrategy(
                        BuildCheckouts(_checkoutCount),
                        BuildPizzaiolos(_pizzaioloCount),
                        _simulationDuration
                ).createPizzeria();
                _pizzeria.setCookingMode(_cookingMode);
                break;

            case SimulationMode.GROWTH:
                _pizzeria = new GrowthPizzeriaCreationStrategy(
                        BuildCheckouts(_checkoutCount),
                        BuildPizzaiolos(_pizzaioloCount),
                        _simulationDuration
                ).createPizzeria();
                _pizzeria.setCookingMode(_cookingMode);
                break;

            case SimulationMode.RUSH_HOUR:
                _pizzeria = new RushHourPizzeriaCreationStrategy(
                        BuildCheckouts(_checkoutCount),
                        BuildPizzaiolos(_pizzaioloCount),
                        _simulationDuration
                ).createPizzeria();
                _pizzeria.setCookingMode(_cookingMode);
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
            Logger.log(" BUILDER: Pizzeria were built" +
                    "\n BUILDER: Starting new simulation.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        _pizzeria.start();
    }
}

