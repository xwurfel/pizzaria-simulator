package com.teamworkcpp.pizzariasimulator.backend.models;
import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;

import java.time.LocalTime;
import java.util.List;

public class Order {
    private final int id;
    private final List<IPizza> pizzas;
    private OrderStatus orderStatus;
    private final int checkoutId;
    private LocalTime created;

    public Order(int id, List<IPizza> pizzas, OrderStatus orderStatus, int checkoutId, LocalTime created) {
        this.id = id;
        this.pizzas = pizzas;
        this.orderStatus = orderStatus;
        this.checkoutId = checkoutId;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public List<IPizza> getPizzas() {
        return pizzas;
    }

    public LocalTime getCreated() {
        return created;
    }

    public OrderStatus getStatus() {
        return orderStatus;
    }

    public int getСheckoutId() {return checkoutId; }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
