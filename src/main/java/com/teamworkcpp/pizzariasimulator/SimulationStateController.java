package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.helpers.SimulationState;
import com.teamworkcpp.pizzariasimulator.backend.services.PizzeriaManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

public class SimulationStateController {

    @FXML
    private TableView<OrderViewModel> tableView;

    @FXML
    private TableColumn<OrderViewModel, Integer> orderIdColumn;

    @FXML
    private TableColumn<OrderViewModel, String> orderTimeColumn;

    @FXML
    private TableColumn<OrderViewModel, Double> orderAmountColumn;

    @FXML
    private TableColumn<OrderViewModel, String> pizzasInOrderColumn;

    @FXML
    private TableColumn<OrderViewModel, String> orderStatusColumn;

    @FXML
    private TableColumn<OrderViewModel, Integer> checkoutIdColumn;


    private PizzeriaManager pizzeriaManager;

    private final Timeline updateTimeline = new Timeline(
            new KeyFrame(Duration.seconds(3), event -> updateTable(pizzeriaManager.getCurrentSimulationState().orders))
    );

    public void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        orderAmountColumn.setCellValueFactory(new PropertyValueFactory<>("orderAmount"));
        pizzasInOrderColumn.setCellValueFactory(new PropertyValueFactory<>("pizzasInOrder"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        checkoutIdColumn.setCellValueFactory(new PropertyValueFactory<>("checkoutId"));

        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    public void updateTable(List<Order> orders) {
        ObservableList<OrderViewModel> items = FXCollections.observableArrayList();

        for (Order order : orders) {
            items.add(new OrderViewModel(order));
        }

        tableView.setItems(items);
    }

    public void setPizzeriaManager(PizzeriaManager pizzeriaManager) {
        this.pizzeriaManager = pizzeriaManager;
        updateTable(pizzeriaManager.getCurrentSimulationState().orders);
    }

    public class OrderViewModel {
        private int orderId;
        private String orderTime;
        private double orderAmount;
        private String pizzasInOrder;
        private String orderStatus;
        private int checkoutId;
        private boolean isOrderCompleted;

        public OrderViewModel(Order order) {
            this.orderId = order.getId();
            this.orderTime = order.getCreated().toString();  // Adjust based on your 'created' property type
            this.pizzasInOrder = calculatePizzasInOrder(order.getPizzas());
            this.orderStatus = order.getStatus().toString();  // Adjust based on your 'orderStatus' property type
            this.checkoutId = order.getСheckoutId();
            this.isOrderCompleted = order.getStatus() == OrderStatus.DONE;
            this.orderAmount = calculateOrderAmount(order.getPizzas());
        }

        // Implement methods to calculate 'pizzasInOrder' and 'orderAmount' based on the pizzas list
        private String calculatePizzasInOrder(List<IPizza> pizzas) {
            return pizzas.stream().map(IPizza::getName).collect(Collectors.joining(", "));
        }

        private double calculateOrderAmount(List<IPizza> pizzas) {
            return pizzas.stream().mapToDouble(IPizza::getPrice).sum();
        }

        public int getOrderId() {
            return orderId;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public double getOrderAmount() {
            return orderAmount;
        }

        public String getPizzasInOrder() {
            return pizzasInOrder;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public int getCheckoutId() {
            return checkoutId;
        }

        public boolean isOrderCompleted() {
            return isOrderCompleted;
        }
    }
}