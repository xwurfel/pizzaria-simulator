package com.teamworkcpp.pizzariasimulator;

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

public class PizzaioloInfoController {

    @FXML
    private TableView<PizzaioloInfoViewModel> pizzaioloTableView;

    @FXML
    private TableColumn<PizzaioloInfoViewModel, Integer> pizzaioloIdColumn;

    @FXML
    private TableColumn<PizzaioloInfoViewModel, String> availabilityColumn;

    @FXML
    private TableColumn<PizzaioloInfoViewModel, String> technicalStopColumn;


    private PizzeriaManager pizzeriaManager;

    private final Timeline updateTimeline = new Timeline(
            new KeyFrame(Duration.seconds(2), event -> updateTable(pizzeriaManager.getCurrentSimulationState().pizzaiolos))
    );

    public void initialize() {
        pizzaioloIdColumn.setCellValueFactory(new PropertyValueFactory<>("pizzaioloId"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        technicalStopColumn.setCellValueFactory(new PropertyValueFactory<>("technicalStop"));

        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    public void updateTable(List<Pizzaiolo> pizzaiolos) {
        ObservableList<PizzaioloInfoViewModel> items = FXCollections.observableArrayList();

        for (Pizzaiolo pizzaiolo : pizzaiolos) {
            items.add(new PizzaioloInfoViewModel(pizzaiolo));
        }

        pizzaioloTableView.setItems(items);
        pizzaioloTableView.setItems(items);
        pizzaioloTableView.getSortOrder().add(pizzaioloIdColumn);
        pizzaioloIdColumn.setSortType(TableColumn.SortType.ASCENDING);
        pizzaioloTableView.sort();
    }

    public void setPizzeriaManager(PizzeriaManager pizzeriaManager) {
        this.pizzeriaManager = pizzeriaManager;
        updateTable(pizzeriaManager.getCurrentSimulationState().pizzaiolos);
    }
    public class PizzaioloInfoViewModel {
        private int pizzaioloId;
        private String availability;
        private String technicalStop;

        public PizzaioloInfoViewModel(Pizzaiolo pizzaiolo) {
            this.pizzaioloId = pizzaiolo.GetId();
            this.availability = pizzaiolo.isAvailable() ? "Доступний" : "Недоступний";
            this.technicalStop = pizzaiolo.isOnTechnicalStop() ? "На технічній зупинці" : " ";
        }

        public int getPizzaioloId() {
            return pizzaioloId;
        }

        public String getAvailability() {
            return availability;
        }

        public String getTechnicalStop() {
            return technicalStop;
        }
    }
}
