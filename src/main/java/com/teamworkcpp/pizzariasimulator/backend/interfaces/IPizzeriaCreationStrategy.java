package com.teamworkcpp.pizzariasimulator.backend.interfaces;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;
import com.teamworkcpp.pizzariasimulator.backend.services.OrderGenerator;

public interface IPizzeriaCreationStrategy {
    Pizzeria createPizzeria();
    void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime,  OrderGenerator orderGenerator);
}
