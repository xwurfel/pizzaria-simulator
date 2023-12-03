package com.teamworkcpp.pizzariasimulator.backend.models;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
import com.teamworkcpp.pizzariasimulator.backend.helpers.Logger;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizzeriaCreationStrategy;
import com.teamworkcpp.pizzariasimulator.backend.services.Checkout;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public class Pizzeria {
    private final List<Checkout> _checkouts;
    private final List<Pizzaiolo> _pizzaiolos;
    private final Duration _simulationTime;
    private final IPizzeriaCreationStrategy _pizzeriaCreationStrategy;
    private CookingMode cookingMode;
    private List<Order> _orders;

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

    private void generateOrdersWithDelay(IPizzeriaCreationStrategy creationStrategy, Duration simulationDuration) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + simulationDuration.toMillis();

        creationStrategy.generateOrdersWithDelay(this, startTime, endTime);
    }

    public void start()
    {
        generateOrdersWithDelay(_pizzeriaCreationStrategy, _simulationTime);

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
        Thread simulationThread = new Thread(() -> {
            while (true) {

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

                    }
                    else {

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
        Thread simulationThread = new Thread(() -> {
            while (true) {

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
                                     pizzaiolo.ReservePizzaiolo(10000);
                                     pizza.addPizzaiolo(pizzaiolo);
                                     pizzaiolo.cook(pizza);
                                     break;
                                 }
                             }
                         }

                         RefreshPizzaioloDelays();

                         if(pizza.getPizzaioloList().getLast().isOnTechnicalStop())
                         {
                             pizza.getPizzaioloList().removeLast();
                             pizza.stopCookingStage();
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