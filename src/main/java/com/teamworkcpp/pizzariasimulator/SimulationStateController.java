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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
        tableView.getSortOrder().add(orderIdColumn);
        orderIdColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableView.sort();

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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            this.orderTime = order.getCreated().format(formatter);
            this.pizzasInOrder = calculatePizzasInOrder(order.getPizzas());
            this.orderStatus = translateOrderStatus(order.getStatus());
            this.checkoutId = order.getСheckoutId();
            this.isOrderCompleted = order.getStatus() == OrderStatus.DONE;
            this.orderAmount = calculateOrderAmount(order.getPizzas());
        }

        private String calculatePizzasInOrder(List<IPizza> pizzas) {
            // Group pizzas by name and count occurrences
            Map<String, Long> pizzaCount = pizzas.stream()
                    .collect(Collectors.groupingBy(IPizza::getName, Collectors.counting()));

            // Format the pizzas for display
            return pizzaCount.entrySet().stream()
                    .map(entry -> entry.getKey() + " " + entry.getValue() + " шт.")
                    .collect(Collectors.joining("\n"));
        }

        private double calculateOrderAmount(List<IPizza> pizzas) {
            return pizzas.stream().mapToDouble(IPizza::getPrice).sum();
        }

        private String translateOrderStatus(OrderStatus status) {
            switch (status) {
                case NEW:
                    return "Новий";
                case IN_PROGRESS:
                    return "В процесі";
                case DONE:
                    return "Готово";
                default:
                    return "Невідомий статус";
            }
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