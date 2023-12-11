package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
import com.teamworkcpp.pizzariasimulator.backend.enums.SimulationMode;
import com.teamworkcpp.pizzariasimulator.backend.helpers.SimulationState;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.services.Checkout;
import com.teamworkcpp.pizzariasimulator.backend.services.PizzeriaManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

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

    @FXML
    private VBox checkoutBox;

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

        SimulationState simulationState = pizzeriaManager.getCurrentSimulationState();

        cashierLabel.setText("Кількість кас: " + simulationState.checkouts.size());
        chefLabel.setText("Кількість кухарів: " + simulationState.pizzaiolos.size());

        for (Checkout checkout : simulationState.checkouts) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setId("checkout-hbox-" + checkout.id);

            Rectangle rectangle = new Rectangle(30, 30);
            rectangle.setFill(Color.DODGERBLUE);
            rectangle.setId("checkout-" + checkout.id);

            hbox.getChildren().add(rectangle);
            checkoutBox.getChildren().add(hbox);
        }

        setSimulationInterval();
    }

    private void setSimulationInterval() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::getCurrentSimulationState, 0, 1, TimeUnit.SECONDS);
    }

    private void getCurrentSimulationState() {
        Platform.runLater(() -> {
            SimulationState simulationState = pizzeriaManager.getCurrentSimulationState();

            for (Order order : simulationState.orders) {
                System.out.println("ORDER STATUS: " + order.getStatus());
                if (Objects.equals(order.getStatus(), OrderStatus.NEW) && !(findCircleById(order.getId()))) {
                    System.out.println("CHECKOUT ID: " + order.getcheckoutId());
                    int checkoutId = order.getcheckoutId(); // Corrected method name

                    HBox checkoutHBox = findCheckoutHBox(checkoutId);

                    if (checkoutHBox != null) {
                        // Додати круг на початок HBox
                        Circle circle = new Circle(10, Color.TURQUOISE);
                        circle.setId("client-" + order.getId());
                        HBox.setMargin(circle, new Insets(0, 0, 0, 10.0));
                        checkoutHBox.getChildren().add(0, circle);
                    } else {
                        System.out.println("Checkout HBox not found for ID: " + checkoutId);
                    }
                }
            }
        });
    }

    private HBox findCheckoutHBox(int checkoutId) {
        ObservableList<Node> children = checkoutBox.getChildren();
        System.out.println("CHECKOUT ID: " + checkoutId);
        for (Node node : children) {
            System.out.println("NODE ID: " + node.getId());
            if (node instanceof HBox && node.getId().equals("checkout-hbox-" + checkoutId)) {
                System.out.println("FOUND HBOX: " + node.getId());
                return (HBox) node;
            }
        }
        System.out.println("HBOX NOT FOUND!");
        return null;
    }

    private boolean findCircleById(int circleId) {
        ObservableList<Node> hboxChildren = checkoutBox.getChildren();

        for (Node hboxNode : hboxChildren) {
            if (hboxNode instanceof HBox) {
                ObservableList<Node> circleChildren = ((HBox) hboxNode).getChildren();

                for (Node circleNode : circleChildren) {
                    if (circleNode instanceof Circle && Objects.equals(circleNode.getId(), circleId)) {
                        System.out.println("CIRCLE ALREADY EXIST!");
                        return true;
                    }
                }
            }
        }

        // Circle з вказаним ID не знайдено
        return false;
    }


    public static Rectangle createCheckoutRectangle(int checkoutId) {
        Rectangle rectangle = new Rectangle(30, 30); // Задайте розміри за вашими потребами
        rectangle.setFill(Color.DODGERBLUE);
        rectangle.setId("checkout-" + checkoutId); // Встановіть унікальний ідентифікатор для прямокутника
        return rectangle;
    }
}
