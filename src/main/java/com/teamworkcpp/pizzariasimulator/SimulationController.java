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
    private List<Integer> freeChairs;

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
            for (Order order : simulationState.orders) {
                if (Objects.equals(order.getStatus(), OrderStatus.IN_PROGRESS) && newOrderIDs.contains(order.getId())){
                    int checkoutId = order.getСheckoutId();

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
                    int checkoutId = order.getСheckoutId();

                    HBox checkoutHBox = findCheckoutHBox(checkoutId);

                    if (checkoutHBox != null) {
                        Circle circle = new Circle(15, Color.RED);

                        Text orderIdText = new Text(String.valueOf(order.getId()));
                        orderIdText.setFill(Color.WHITE);
                        orderIdText.setBoundsType(TextBoundsType.VISUAL);

                        orderIdText.setTextOrigin(VPos.CENTER);
                        orderIdText.setTextAlignment(TextAlignment.CENTER);

                        StackPane stackPane = new StackPane(circle, orderIdText);
                        stackPane.setAlignment(Pos.CENTER);

                        stackPane.setId("client-" + order.getId());

                        double textCenterX = circle.getCenterX() - orderIdText.getBoundsInLocal().getWidth() / 2;
                        double textCenterY = circle.getCenterY() - orderIdText.getBoundsInLocal().getHeight() / 4;

                        orderIdText.setTranslateX(textCenterX);
                        orderIdText.setTranslateY(textCenterY);

                        checkoutHBox.getChildren().add(0, stackPane);
                        newOrderIDs.add(order.getId());


                    }
                } else if (Objects.equals(order.getStatus(), OrderStatus.DONE)){
                    removeClientGroupById(order.getId());
                }
            }
        });
    }

    private HBox findCheckoutHBox(int checkoutId) {
        ObservableList<Node> children = checkoutBox.getChildren();
        for (Node node : children) {
            if (node instanceof HBox && node.getId().equals("checkout-hbox-" + checkoutId)) {
                return (HBox) node;
            }
        }
        return null;
    }

    private void placeClientInChair(int chairId, int orderId) {
        Circle chair = (Circle) chairsBox.lookup("#chair_" + chairId);

        if (chair != null) {
            Circle clientCircle = new Circle(15, Color.RED);
            clientCircle.setId("clientCircle_" + orderId);

            clientCircle.setTranslateX(chair.getLayoutX());
            clientCircle.setTranslateY(chair.getLayoutY());

            Text orderIdText = new Text(String.valueOf(orderId));
            orderIdText.setFill(Color.WHITE);
            orderIdText.setBoundsType(TextBoundsType.VISUAL);

            orderIdText.setTranslateX(chair.getLayoutX() - orderIdText.getBoundsInLocal().getWidth() / 2);
            orderIdText.setTranslateY(chair.getLayoutY() + orderIdText.getBoundsInLocal().getHeight() / 2);
            orderIdText.setTextAlignment(CENTER);

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
    public void Pinfo_button_click(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PizzaioloInfo.fxml"));
            Parent root = loader.load();

            PizzaioloInfoController pizzaioloInfoController = loader.getController();
            pizzaioloInfoController.setPizzeriaManager(pizzeriaManager);
            pizzaioloInfoController.updateTable(pizzeriaManager.getCurrentSimulationState().pizzaiolos);

            Stage pizzaioloInfoStage = new Stage();
            pizzaioloInfoStage.initModality(Modality.APPLICATION_MODAL);
            pizzaioloInfoStage.setTitle("Інформація про кухарів");
            pizzaioloInfoStage.setScene(new Scene(root));

            pizzaioloInfoStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PizzaInfo_button_click(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PizzaInfo.fxml"));
            Parent root = loader.load();

            PizzaInfoController pizzaInfoController = loader.getController();
            pizzaInfoController.setPizzeriaManager(pizzeriaManager);
            pizzaInfoController.updateTable(pizzeriaManager.getCurrentSimulationState().orders);

            Stage pizzaInfoStage = new Stage();
            pizzaInfoStage.initModality(Modality.APPLICATION_MODAL);
            pizzaInfoStage.setTitle("Інформація про піци");
            pizzaInfoStage.setScene(new Scene(root));

            pizzaInfoStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}