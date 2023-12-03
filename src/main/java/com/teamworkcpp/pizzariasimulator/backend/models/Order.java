package com.teamworkcpp.pizzariasimulator.backend.models;
import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;

import java.util.List;

public class Order {
    private final int _id;
    private final List<IPizza> _pizzas;
    private OrderStatus _orderStatus;
    private int _checkoutId;

    public Order(int id, List<IPizza> pizzas, OrderStatus orderStatus, int checkoutId) {
        this._id = id;
        this._pizzas = pizzas;
        this._orderStatus = orderStatus;
        this._checkoutId = checkoutId;
    }

    public int getId() {
        return _id;
    }

    public List<IPizza> getPizzas() {
        return _pizzas;
    }

    public OrderStatus getStatus() {
        return _orderStatus;
    }

    public int getcheckoutId() {return _checkoutId; }

    public void setOrderStatus(OrderStatus orderStatus) {
        this._orderStatus = orderStatus;
    }
}
