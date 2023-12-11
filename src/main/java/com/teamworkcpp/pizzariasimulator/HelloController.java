package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
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
    private CookingMode cookingMode;

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
        int var10001 = this.simulationDuration;
        System.out.println("Start with settings: " + var10001 + ", " + this.numberOfCashiers + ", " + this.numberOfCooks + ", " + String.valueOf(this.simulationMode));
        this.pizzeriaManager = new PizzeriaManager();
        this.pizzeriaManager.AddSimulationDuration(Duration.ofMinutes((long)this.simulationDuration));
        this.pizzeriaManager.AddCheckoutCount(this.numberOfCashiers);
        this.pizzeriaManager.AddPizzaioloCount(this.numberOfCooks);
        this.pizzeriaManager.AddLevel(this.simulationMode);
        this.pizzeriaManager.AddCookingMode(this.cookingMode);
        pizzeriaManager.AddPizza("Margarita", 32, Duration.ofMinutes(1));

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
}
