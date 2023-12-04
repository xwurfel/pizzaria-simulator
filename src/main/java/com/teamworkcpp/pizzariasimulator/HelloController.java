package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.SimulationMode;
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

public class HelloController {

    @FXML
    private Button button_settings;

    @FXML
    private Button button_start;

    private int simulationDuration;
    private int numberOfCashiers;
    private int numberOfCooks;
    private SimulationMode simulationMode;

    private PizzeriaManager pizzeriaManager;

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
        pizzeriaManager = new PizzeriaManager();
        pizzeriaManager.AddSimulationDuration(Duration.ofDays(simulationDuration));
        pizzeriaManager.AddCheckoutCount(numberOfCashiers);
        pizzeriaManager.AddPizzaioloCount(numberOfCooks);
        pizzeriaManager.AddLevel(simulationMode);
        try {
            pizzeriaManager.Build();
        } catch (Exception e) {
            e.printStackTrace();
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

    public void setSimulationSettings(int duration, int cashiers, int cooks, SimulationMode mode) {
        simulationDuration = duration;
        numberOfCashiers = cashiers;
        numberOfCooks = cooks;
        simulationMode = mode;
    }
}
