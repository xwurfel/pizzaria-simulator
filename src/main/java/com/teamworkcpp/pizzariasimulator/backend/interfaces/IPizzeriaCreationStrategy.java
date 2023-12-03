package com.teamworkcpp.pizzariasimulator.backend.interfaces;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzeria;

public interface IPizzeriaCreationStrategy {
    Pizzeria createPizzeria();
    void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime);
}
