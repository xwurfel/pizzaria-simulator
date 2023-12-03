package com.teamworkcpp.pizzariasimulator.backend.models;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
import com.teamworkcpp.pizzariasimulator.backend.helpers.Logger;
import com.teamworkcpp.pizzariasimulator.backend.helpers.SimulationState;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzeriaCreationStrategy;
import com.teamworkcpp.pizzariasimulator.backend.services.Checkout;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pizzeria {
    private final List<Checkout> _checkouts;
    private final List<Pizzaiolo> _pizzaiolos;
    private final Duration _simulationTime;
    private final IPizzeriaCreationStrategy _pizzeriaCreationStrategy;
    private CookingMode cookingMode;
    private List<Order> _orders;
    private boolean _simulationEnded;

    public Pizzeria(List<Checkout> checkouts,
                    List<Pizzaiolo> pizzaiolos,
                    Duration simulationTime,
                    IPizzeriaCreationStrategy creationStrategy
                    )
    {
        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._simulationTime = simulationTime;
        this._pizzeriaCreationStrategy = creationStrategy;
        this._simulationEnded = false;
        this._orders = new ArrayList<>();
    }

    public SimulationState getSimulationState()
    {
        return new SimulationState(_orders, _checkouts, _pizzaiolos, _simulationEnded);
    }
    public void setCookingMode(CookingMode cookingMode)
    {
        this.cookingMode = cookingMode;
    }

    public Pizzeria getInstance() { return this; }

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

    public void AddOrder(Order order)
    {
        _orders.add(order);
    }

    private void generateOrdersWithDelay(IPizzeriaCreationStrategy creationStrategy, Duration simulationDuration) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + simulationDuration.toMillis();
        Thread.sleep(5000);
        creationStrategy.generateOrdersWithDelay(this, startTime, endTime);
    }

    public void start()
    {
        try {
            generateOrdersWithDelay(_pizzeriaCreationStrategy, _simulationTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(cookingMode == CookingMode.CONVEYOR_MODE)
        {
            ConveyorMode();
        }

        if(cookingMode == CookingMode.ONE_PIZZAIOLO_MODE)
        {
            OnePizzaioloMode();
        }

    }

    private void RefreshPizzaioloDelays()
    {
        for(Pizzaiolo pizzaiolo : _pizzaiolos)
        {
            pizzaiolo.CheckAvailable();
        }
    }

    private void OnePizzaioloMode()
    {
        try {
            Logger.log("New simulation started, simulation mode: SINGLE");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Thread simulationThread = new Thread(() -> {
            while (true) {

                boolean allOrdersDone = _orders.stream().allMatch(order ->
                        Objects.equals(order.getStatus(), OrderStatus.DONE));

                if(allOrdersDone)
                {
                    int pizzasDone = 0;
                    double price = 0;
                    for (Order order : _orders)
                    {
                        for(IPizza pizza : order.getPizzas())
                        {
                            pizzasDone++;
                            price += pizza.getPrice();
                        }
                    }

                    try {
                        Logger.log("Simulation ended\n" +
                                "orders done: " + pizzasDone +
                                "money earned: " + price);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    _simulationEnded = true;
                    return;
                }

                RefreshPizzaioloDelays();
                for (Order order : _orders) {
                    boolean allPizzasDone = order.getPizzas().stream().allMatch(pizza -> Objects.equals(pizza
                            .getCurrentStatus()
                            .getStatusName(), "Done"));

                    if (allPizzasDone) {

                        order.setOrderStatus(OrderStatus.DONE);

                        try {
                            Logger.log("Order: " + order.getId() + " is ready");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        continue;
                    }

                    for(IPizza pizza : order.getPizzas())
                    {
                        if(Objects.equals(pizza
                                .getCurrentStatus()
                                .getStatusName(), "Done"))
                        {
                            try {
                                Logger.log("Pizza: " + pizza.getId() + " is ready");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        }

                        if(Objects.equals(pizza.getCurrentStatus().getStatusName(), "Waiting"))
                        {
                            RefreshPizzaioloDelays();

                            for(Pizzaiolo pizzaiolo : _pizzaiolos)
                            {
                                if(pizzaiolo.isAvailable())
                                {
                                    if(Objects.equals(order.getStatus(), OrderStatus.NEW))
                                    {
                                        order.setOrderStatus(OrderStatus.IN_PROGRESS);
                                        try {
                                            Logger.log("Order: " + order.getId() + "set status: " + "In progress");
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    pizzaiolo.ReservePizzaiolo(10000);
                                    pizza.addPizzaiolo(pizzaiolo);
                                    pizzaiolo.cook(pizza);
                                    break;
                                }
                            }
                        }

                        RefreshPizzaioloDelays();

                        if(pizza.getPizzaioloList().getFirst().isOnTechnicalStop())
                        {
                            pizza.getPizzaioloList().clear();
                            pizza.stopCookingStage();

                            try {
                                Logger.log("Pizza: " + pizza.getId() + "pizzaiolo is on technical stop, searching available pizzaiolo ");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            continue;
                        }

                        if(System.currentTimeMillis() < pizza.getNextTime()
                                .atDate(LocalDate.now())
                                .atZone(ZoneId.systemDefault())
                                .toInstant().toEpochMilli())
                        {
                            continue;
                        }

                        pizza.getPizzaioloList().getFirst().cook(pizza);

                        try {
                            Logger.log("Pizza: " + pizza.getId() + " set status "+ pizza.getCurrentStatus().getStatusName());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
        simulationThread.start();
    }

    private void ConveyorMode()
    {
        try {
            Logger.log("New simulation started, simulation mode: CONVEYOR");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread simulationThread = new Thread(() -> {
            while (true) {

                boolean allOrdersDone = _orders.stream().allMatch(order ->
                        Objects.equals(order.getStatus(), OrderStatus.DONE));

                if(allOrdersDone)
                {
                    int pizzasDone = 0;
                    double price = 0;
                    for (Order order : _orders)
                    {
                        for(IPizza pizza : order.getPizzas())
                        {
                            pizzasDone++;
                            price += pizza.getPrice();
                        }
                    }

                    try {
                        Logger.log("Simulation ended\n" +
                                "orders done: " + pizzasDone +
                                "money earned: " + price);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    _simulationEnded = true;
                    return;
                }

                RefreshPizzaioloDelays();
                for (Order order : _orders)
                {
                    if (order.getPizzas().stream().allMatch(pizza -> Objects.equals(pizza
                            .getCurrentStatus()
                            .getStatusName(), "Done")))
                    {

                        order.setOrderStatus(OrderStatus.DONE);

                        try {
                            Logger.log("Order: " + order.getId() + " is ready");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        continue;
                    }

                    for(IPizza pizza : order.getPizzas())
                    {
                         if(Objects.equals(pizza
                                 .getCurrentStatus()
                                 .getStatusName(), "Done"))
                         {
                             try {
                                 Logger.log("Pizza: " + pizza.getId() + " is ready");
                             } catch (IOException e) {
                                 throw new RuntimeException(e);
                             }
                             continue;
                         }

                         if(Objects.equals(pizza.getCurrentStatus().getStatusName(), "Waiting"))
                         {
                             RefreshPizzaioloDelays();

                             for(Pizzaiolo pizzaiolo : _pizzaiolos)
                             {
                                 if(pizzaiolo.isAvailable())
                                 {
                                     if(Objects.equals(order.getStatus(), OrderStatus.NEW))
                                     {
                                         order.setOrderStatus(OrderStatus.IN_PROGRESS);

                                         try {
                                             Logger.log("Order: " + order.getId() + "set status: " + "In progress");
                                         } catch (IOException e) {
                                             throw new RuntimeException(e);
                                         }
                                     }

                                     pizzaiolo.ReservePizzaiolo(10000);
                                     pizza.addPizzaiolo(pizzaiolo);
                                     pizzaiolo.cook(pizza);

                                     try {
                                         Logger.log("Pizza: " + pizza.getId() + "cooking by pizzaiolo: " + pizzaiolo.GetId());
                                     } catch (IOException e) {
                                         throw new RuntimeException(e);
                                     }

                                     break;
                                 }
                             }
                         }

                         RefreshPizzaioloDelays();

                         if(pizza.getPizzaioloList().getLast().isOnTechnicalStop())
                         {
                             pizza.getPizzaioloList().removeLast();
                             pizza.stopCookingStage();

                             try {
                                 Logger.log("Pizza: " + pizza.getId() + "pizzaiolo is on technical stop, searching available pizzaiolo ");
                             } catch (IOException e) {
                                 throw new RuntimeException(e);
                             }

                             continue;
                         }

                         if(System.currentTimeMillis() < pizza.getNextTime()
                                 .atDate(LocalDate.now())
                                 .atZone(ZoneId.systemDefault())
                                 .toInstant().toEpochMilli())
                         {
                             continue;
                         }

                         RefreshPizzaioloDelays();

                         Pizzaiolo missingPizzaiolo = findMissingElementById(_pizzaiolos, pizza.getPizzaioloList());

                         if(missingPizzaiolo == null)
                         {
                             pizza.stopCookingStage();
                             try {
                                 Logger.log("Pizza: " + pizza.getId() + " no pizzaiolo available");
                             } catch (IOException e) {
                                 throw new RuntimeException(e);
                             }
                            continue;
                         }

                         missingPizzaiolo.ReservePizzaiolo(10000);
                         missingPizzaiolo.cook(pizza);

                         try {
                             Logger.log("Pizza: " + pizza.getId() + " set status "+ pizza.getCurrentStatus().getStatusName());
                         } catch (IOException e) {
                             throw new RuntimeException(e);
                         }
                    }
                }
            }
        });
        simulationThread.start();
    }

    private Pizzaiolo findMissingElementById(List<Pizzaiolo> list1, List<Pizzaiolo> list2)
    {
        RefreshPizzaioloDelays();
        for (Pizzaiolo pizzaiolo : list1) {
            if(!pizzaiolo.isAvailable())
            {
                continue;
            }

            boolean found = false;

            for (Pizzaiolo pizzaiolo2 : list2) {

                if (pizzaiolo.GetId() == pizzaiolo2.GetId()) {
                    found = true;
                    break;
                }

            }

            if (!found) {
                return pizzaiolo;
            }
        }

        return null;
    }

}