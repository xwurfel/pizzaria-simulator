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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;
public class SimulationController {
    private PizzeriaManager pizzeriaManager;
    @FXML
    private Pane draggablePane;
    @FXML
    private Label durationLabel;

    @FXML
    private Label cashierLabel;

    @FXML
    private Label chefLabel;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void handleMouseDragged(MouseEvent event) {
        draggablePane.setTranslateX(event.getSceneX() - xOffset);
        draggablePane.setTranslateY(event.getSceneY() - yOffset);
    }

    public void init(PizzeriaManager pizzeriaManager) {
        this.pizzeriaManager = pizzeriaManager;
        durationLabel.setText("Новий текст для тривалості симуляції: ");
        cashierLabel.setText("Новий текст для кількості кас: ");
        chefLabel.setText("Кількість кухарів: ");
    }
}
