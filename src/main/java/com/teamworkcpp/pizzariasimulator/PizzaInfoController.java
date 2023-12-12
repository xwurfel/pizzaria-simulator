package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;
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

public class PizzaInfoController {

    @FXML
    private TableView<pizzaTableView> pizzaTableView;

    @FXML
    private TableColumn<pizzaTableView, Integer> pizzaIdColumn;

    @FXML
    private TableColumn<pizzaTableView, String> pizzaNameColumn;

    @FXML
    private TableColumn<pizzaTableView, String> pizzaStatusColumn;

    private PizzeriaManager pizzeriaManager;

    private final Timeline updateTimeline = new Timeline(
            new KeyFrame(Duration.seconds(2), event -> updateTable(pizzeriaManager.getCurrentSimulationState().orders))
    );

    public void initialize() {
        pizzaIdColumn.setCellValueFactory(new PropertyValueFactory<>("pizzaId"));
        pizzaNameColumn.setCellValueFactory(new PropertyValueFactory<>("pizzaName"));
        pizzaStatusColumn.setCellValueFactory(new PropertyValueFactory<>("pizzaStatus"));

        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    public void updateTable(List<Order> orders) {
        ObservableList<pizzaTableView> items = FXCollections.observableArrayList();

        for (Order order : orders) {
            for (IPizza pizza : order.getPizzas()) {
                items.add(new pizzaTableView(pizza));
            }
        }

        pizzaTableView.setItems(items);
        pizzaTableView.setItems(items);
        pizzaTableView.getSortOrder().add(pizzaIdColumn);
        pizzaIdColumn.setSortType(TableColumn.SortType.ASCENDING);
        pizzaTableView.sort();
    }

    public void setPizzeriaManager(PizzeriaManager pizzeriaManager) {
        this.pizzeriaManager = pizzeriaManager;
        updateTable(pizzeriaManager.getCurrentSimulationState().orders);
    }
    public class pizzaTableView {
        private Integer pizzaId;
        private String pizzaName;
        private String pizzaStatus;

        public pizzaTableView(IPizza pizza) {
            this.pizzaId = Integer.parseInt(pizza.getId());
            this.pizzaName = pizza.getName();
            this.pizzaStatus = String.valueOf(pizza.getCurrentStatus().getStatusName());
        }

        public Integer getPizzaId() {
            return pizzaId;
        }

        public String getPizzaName() {
            return pizzaName;
        }

        public String getPizzaStatus() {
            return pizzaStatus;
        }
    }
}
