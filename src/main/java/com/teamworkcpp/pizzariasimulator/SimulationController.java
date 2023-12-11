package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.OrderStatus;
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
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static javafx.scene.text.TextAlignment.*;

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

                        placeClientInChair(randomElement, order.getId());
                    }
                } else if (Objects.equals(order.getStatus(), OrderStatus.NEW) && !newOrderIDs.contains(order.getId())) {
                    System.out.println("CHECKOUT ID: " + order.getСheckoutId());
                    int checkoutId = order.getСheckoutId(); // Corrected method name

                    HBox checkoutHBox = findCheckoutHBox(checkoutId);

                    if (checkoutHBox != null) {
//                        Circle circle = new Circle(10, Color.TURQUOISE);
//                        circle.setId("client-" + order.getId());
//                        System.out.println("Order ID: " + order.getId());
//                        newOrderIDs.add(order.getId());
//                        HBox.setMargin(circle, new Insets(0, 0, 0, 10.0));
//                        checkoutHBox.getChildren().add(0, circle);

                        Circle circle = new Circle(15, Color.RED);

                        Text orderIdText = new Text(String.valueOf(order.getId()));
                        orderIdText.setFill(Color.WHITE);
                        orderIdText.setBoundsType(TextBoundsType.VISUAL);

// Set the text to be centered inside the circle
                        orderIdText.setTextOrigin(VPos.CENTER);
                        orderIdText.setTextAlignment(TextAlignment.CENTER);

                        StackPane stackPane = new StackPane(circle, orderIdText);
                        stackPane.setAlignment(Pos.CENTER);

                        stackPane.setId("client-" + order.getId());

// Calculate translation to center the text inside the circle
                        double textCenterX = circle.getCenterX() - orderIdText.getBoundsInLocal().getWidth() / 2;
                        double textCenterY = circle.getCenterY() - orderIdText.getBoundsInLocal().getHeight() / 4;

                        orderIdText.setTranslateX(textCenterX);
                        orderIdText.setTranslateY(textCenterY);

                        checkoutHBox.getChildren().add(0, stackPane);
                        newOrderIDs.add(order.getId());


                    } else {
                        System.out.println("Checkout HBox not found for ID: " + checkoutId);
                    }
                } else if (Objects.equals(order.getStatus(), OrderStatus.DONE)){
                    removeClientGroupById(order.getId());
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


    private void placeClientInChair(int chairId, int orderId) {
        Circle chair = (Circle) chairsBox.lookup("#chair_" + chairId);

        if (chair != null) {
            Circle clientCircle = new Circle(15, Color.RED);
            clientCircle.setId("clientCircle_" + orderId);

            clientCircle.setTranslateX(chair.getLayoutX());
            clientCircle.setTranslateY(chair.getLayoutY());

            // Додавання тексту з orderId
            Text orderIdText = new Text(String.valueOf(orderId));
            orderIdText.setFill(Color.WHITE); // Задайте колір тексту за потребою
            orderIdText.setBoundsType(TextBoundsType.VISUAL);

            // Розташування тексту всередині кола
            orderIdText.setTranslateX(chair.getLayoutX() - orderIdText.getBoundsInLocal().getWidth() / 2);
            orderIdText.setTranslateY(chair.getLayoutY() + orderIdText.getBoundsInLocal().getHeight() / 2);
            orderIdText.setTextAlignment(CENTER);

            // Група, яка об'єднує коло і текст
            Group clientGroup = new Group(clientCircle, orderIdText);
            clientGroup.setId("clientGroup_" + orderId);

            Parent parent = chair.getParent();

            while (parent != null && !(parent instanceof Group)) {
                parent = parent.getParent();
            }

            if (parent instanceof Group) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(clientGroup);
            }

            freeChairs.remove(Integer.valueOf(chairId));
        }
    }

    private void removeClientGroupById(int orderId) {
        Group clientGroup = (Group) chairsBox.lookup("#clientGroup_" + orderId);

        if (clientGroup != null) {
            Parent parent = clientGroup.getParent();

            while (parent != null && !(parent instanceof Group)) {
                parent = parent.getParent();
            }

            if (parent instanceof Group) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().remove(clientGroup);
            }
        }
    }

    public void State_button_click(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SimulationState.fxml"));
            Parent root = loader.load();

            SimulationStateController stateController = loader.getController();
            stateController.setPizzeriaManager(pizzeriaManager);

            List<Order> orders = pizzeriaManager.getCurrentSimulationState().orders;
            stateController.updateTable(orders);

            Stage stateStage = new Stage();
            stateStage.initModality(Modality.APPLICATION_MODAL);
            stateStage.setTitle("Стан симуляції");
            stateStage.setScene(new Scene(root));

            stateStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
