package models;
import interfaces.IPizza;

import java.util.List;

public class Order {
    private int _id;
    private List<IPizza> _pizzas;
    private OrderStatus _orderStatus;
    private boolean _isDone;
    private int _checkoutId;

    public Order(int id, List<IPizza> pizzas, OrderStatus orderStatus, boolean isDone, int checkoutId) {
        this._id = id;
        this._pizzas = pizzas;
        this._orderStatus = orderStatus;
        this._isDone = isDone;
        this._checkoutId = checkoutId;
    }

    public int getId() {
        return _id;
    }

    public List<IPizza> getPizzas() {
        return _pizzas;
    }

    public OrderStatus getOrderStatus() {
        return _orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this._orderStatus = orderStatus;
    }

    public boolean isDone() {
        return _isDone;
    }

    public void setDone(boolean done) {
        _isDone = done;
    }
}
enum OrderStatus {
    NEW,
    DONE
}
