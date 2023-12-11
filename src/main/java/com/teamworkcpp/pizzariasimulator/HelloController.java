package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
import com.teamworkcpp.pizzariasimulator.backend.enums.SimulationMode;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.models.SimplePizza;
import com.teamworkcpp.pizzariasimulator.backend.services.PizzeriaManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class HelloController {

    @FXML
    private Button button_settings;

    @FXML
    private Button button_start;

    private int simulationDuration;
    private int numberOfCashiers;
    private int numberOfCooks;
    private SimulationMode simulationMode;
    private CookingMode cookingMode;

    PizzeriaManager pizzeriaManager = new PizzeriaManager();
    private List<Map<String, Object>> pizzasToAdd;

    @FXML
    void initialize() {
        button_start.setDisable(true);
    }

    @FXML
    void Setting_button_click(ActionEvent event) {
        openSettingsWindow();
    }

    @FXML
    void Start_button_click(ActionEvent event) {
        System.out.println("Start with settings: " + simulationDuration + ", " + numberOfCashiers + ", " + numberOfCooks + ", " + simulationMode);
        pizzeriaManager.AddSimulationDuration(Duration.ofMinutes(simulationDuration));
        pizzeriaManager.AddCheckoutCount(numberOfCashiers);
        pizzeriaManager.AddPizzaioloCount(numberOfCooks);
        pizzeriaManager.AddLevel(simulationMode);
        pizzeriaManager.AddCookingMode(cookingMode);
        pizzeriaManager.AddPizza("Margarita", 32, Duration.ofMinutes(1));
        for (Map<String, Object> pizzaData : pizzasToAdd) {
            pizzeriaManager.AddPizza(
                    (String) pizzaData.get("name"),
                    (Double) pizzaData.get("price"),
                    (Duration) pizzaData.get("cookingTime")
            );
        }

        try {
            FXMLLoader simulationLoader = new FXMLLoader(this.getClass().getResource("SimulationWindow.fxml"));
            Parent resultRoot = (Parent) simulationLoader.load();
            SimulationController simulationController = (SimulationController) simulationLoader.getController();
            simulationController.init(this.pizzeriaManager);

            Stage currentStage = (Stage) button_start.getScene().getWindow();
            currentStage.close();

            Stage resultStage = new Stage();
            resultStage.initModality(Modality.APPLICATION_MODAL);
            resultStage.setTitle("Результати симуляції");
            resultStage.setScene(new Scene(resultRoot));
            resultStage.showAndWait();

        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private void openSettingsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsWindow.fxml"));
            Parent root = loader.load();

            SettingsController settingsController = loader.getController();

            settingsController.init(this);

            Stage settingsStage = new Stage();
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.setTitle("Налаштування симуляції");
            settingsStage.setScene(new Scene(root));

            settingsStage.setOnHidden(e -> button_start.setDisable(false));

            settingsStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSimulationSettings(int duration, int cashiers, int cooks, SimulationMode mode, CookingMode cmode) {
        simulationDuration = duration;
        numberOfCashiers = cashiers;
        numberOfCooks = cooks;
        simulationMode = mode;
        cookingMode = cmode;
    }

    public void setPizzasToAdd(List<Map<String, Object>> pizzasToAdd) {
        this.pizzasToAdd = pizzasToAdd;
    }
}