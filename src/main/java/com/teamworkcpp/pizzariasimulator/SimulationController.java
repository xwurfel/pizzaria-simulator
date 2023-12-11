package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
import com.teamworkcpp.pizzariasimulator.backend.helpers.SimulationState;
import com.teamworkcpp.pizzariasimulator.backend.models.Order;
import com.teamworkcpp.pizzariasimulator.backend.services.Checkout;
import com.teamworkcpp.pizzariasimulator.backend.services.PizzeriaManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SimulationController {
    private PizzeriaManager pizzeriaManager;
    List<Integer> newOrderIDs = new ArrayList<>();
    List<Integer> progressOrderIDs = new ArrayList<>();
    private List<Integer> freeChairs;
    @FXML
    private Label durationLabel;

    @FXML
    private Label cashierLabel;

    @FXML
    private Label chefLabel;

    @FXML
    private VBox checkoutBox;

    @FXML
    private VBox chairsBox;

    public void init(PizzeriaManager pizzeriaManager) throws Exception {
        this.pizzeriaManager = pizzeriaManager;

        try {
            this.pizzeriaManager.Build();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

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

        freeChairs = new ArrayList<>();
        for (int i = 1; i <= 48; i++) {
            freeChairs.add(i);
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
            System.out.println("ORDERS LIST: ");
            for (Order order : simulationState.orders) {
                System.out.println("ORDER STATUS: " + order.getStatus());
                if (Objects.equals(order.getStatus(), OrderStatus.IN_PROGRESS) && newOrderIDs.contains(order.getId())){
                    int checkoutId = order.getСheckoutId(); // Corrected method name

                    HBox checkoutHBox = findCheckoutHBox(checkoutId);

                    if (checkoutHBox != null) {
                        String clientId = "client-" + order.getId();
                        checkoutHBox.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(clientId));
                        newOrderIDs.removeIf(id -> id.equals(order.getId()));

                        Random random = new Random();
                        int randomIndex = random.nextInt(freeChairs.size());
                        int randomElement = freeChairs.get(randomIndex);

                        placeClientInChair(randomElement);
                    }
                } else if (Objects.equals(order.getStatus(), OrderStatus.NEW) && !newOrderIDs.contains(order.getId())) {
                    System.out.println("CHECKOUT ID: " + order.getСheckoutId());
                    int checkoutId = order.getСheckoutId(); // Corrected method name

                    HBox checkoutHBox = findCheckoutHBox(checkoutId);

                    if (checkoutHBox != null) {
                        // Додати круг на початок HBox
                        Circle circle = new Circle(10, Color.TURQUOISE);
                        circle.setId("client-" + order.getId());
                        System.out.println("Order ID: " + order.getId());
                        newOrderIDs.add(order.getId());
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
        System.out.println("CIRCLE ID: " + circleId);
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

    private void placeClientInChair(int chairId) {
        Circle chair = (Circle) chairsBox.lookup("#chair_" + chairId);

        if (chair != null) {

            Circle clientCircle = new Circle(10, Color.RED);

            clientCircle.setTranslateX(chair.getLayoutX());
            clientCircle.setTranslateY(chair.getLayoutY());

            Parent parent = chair.getParent();

            while (parent != null && !(parent instanceof Group)) {
                parent = parent.getParent();
            }

            if (parent instanceof Group) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(clientCircle);
            }
            freeChairs.remove(Integer.valueOf(chairId));
        }
    }


    public static Rectangle createCheckoutRectangle(int checkoutId) {
        Rectangle rectangle = new Rectangle(30, 30); // Задайте розміри за вашими потребами
        rectangle.setFill(Color.DODGERBLUE);
        rectangle.setId("checkout-" + checkoutId); // Встановіть унікальний ідентифікатор для прямокутника
        return rectangle;
    }
}
