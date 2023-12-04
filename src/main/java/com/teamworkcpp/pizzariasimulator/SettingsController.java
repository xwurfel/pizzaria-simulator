package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.SimulationMode;
import com.teamworkcpp.pizzariasimulator.backend.services.PizzaPrototypeRegistry;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

public class SettingsController {
    @FXML
    private TextField pizzaName;

    @FXML
    private TextField pizzaPrice;

    @FXML
    private TextField minTimeDoughKneeding;

    @FXML
    private TextField minTimeFillingBeforeBaking;

    @FXML
    private TextField minTimeBaking;

    @FXML
    private TextField minTimeFillingAfterBaking;

    @FXML
    private TextField minTimePackaging;

    @FXML
    private Button button_addPizza;

    @FXML
    private Button button_apply;

    @FXML
    private ComboBox<String> combobox_duration;

    @FXML
    private ComboBox<String> combobox_checkouts;

    @FXML
    private ComboBox<String> combobox_pizzaiolos;

    @FXML
    private RadioButton radiobutton_strategy1;

    @FXML
    private RadioButton radiobutton_strategy2;

    @FXML
    private RadioButton radiobutton_strategy3;

    private ToggleGroup strategyToggleGroup;

    private HelloController helloController;

    //@FXML
    //private TableView<Pizza> menuTable;

    private Map<RadioButton, SimulationMode> strategyMapping;

    public void init(HelloController helloController) {
        this.helloController = helloController;

        strategyMapping = new HashMap<>();
        strategyMapping.put(radiobutton_strategy1, SimulationMode.UNIFORM_DISTRIBUTION);
        strategyMapping.put(radiobutton_strategy2, SimulationMode.GROWTH);
        strategyMapping.put(radiobutton_strategy3, SimulationMode.RUSH_HOUR);

        strategyToggleGroup = new ToggleGroup();
        radiobutton_strategy1.setToggleGroup(strategyToggleGroup);
        radiobutton_strategy2.setToggleGroup(strategyToggleGroup);
        radiobutton_strategy3.setToggleGroup(strategyToggleGroup);

        radiobutton_strategy1.setGraphic(createImageView("file:images/strategy1.png", 150, 250));
        radiobutton_strategy2.setGraphic(createImageView("file:images/strategy2.png", 150, 250));
        radiobutton_strategy3.setGraphic(createImageView("file:images/strategy3.png", 150, 250));

        /*TableColumn<Pizza, String> nameColumn = new TableColumn<>("Назва");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pizza, Double> priceColumn = new TableColumn<>("Вартість");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        menuTable.getColumns().add(nameColumn);
        menuTable.getColumns().add(priceColumn);

        menuTable.setItems(PizzaPrototypeRegistry.getInstance().getPizzas());*/
    }

    void addPizza() {
        try {
            String name = pizzaName.getText();
            double price = Double.parseDouble(pizzaPrice.getText());
            int minTimeDough = Integer.parseInt(minTimeDoughKneeding.getText());
            int minTimeFillingBefore = Integer.parseInt(minTimeFillingBeforeBaking.getText());
            int minTimeBake = Integer.parseInt(minTimeBaking.getText());
            int minTimeFillingAfter = Integer.parseInt(minTimeFillingAfterBaking.getText());
            int minTimePackage = Integer.parseInt(minTimePackaging.getText());

            // Зберегти піцу в меню
            //PizzaPrototypeRegistry.getInstance().addItem(name, price, minTimeDough, minTimeFillingBefore,
            //        minTimeBake, minTimeFillingAfter, minTimePackage);

            clearInputFields();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        pizzaName.clear();
        pizzaPrice.clear();
        minTimeDoughKneeding.clear();
        minTimeFillingBeforeBaking.clear();
        minTimeBaking.clear();
        minTimeFillingAfterBaking.clear();
        minTimePackaging.clear();
    }

    private ImageView createImageView(String imagePath, double fitWidth, double fitHeight) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        return imageView;
    }

    @FXML
    void Apply_button_click() {
        try {
            int simulationDuration = Integer.parseInt(combobox_duration.getValue());
            int numberOfCashiers = Integer.parseInt(combobox_checkouts.getValue());
            int numberOfCooks = Integer.parseInt(combobox_pizzaiolos.getValue());

            if (simulationDuration <= 0 || numberOfCashiers <= 0 || numberOfCooks <= 0) {
                showAlert("Invalid input", "Будь ласка, заповніть поля коректними значеннями.");
                return;
            }

            if (numberOfCashiers > 10 || numberOfCooks > 15) {
                showAlert("Value too large", "Кількість кас не повинна перевищувати 10, кількість кухарів не повинна перевищувати 15.");
                return;
            }

            RadioButton selectedRadioButton = (RadioButton) strategyToggleGroup.getSelectedToggle();

            SimulationMode simulationMode = strategyMapping.get(selectedRadioButton);

            helloController.setSimulationSettings(simulationDuration, numberOfCashiers, numberOfCooks, simulationMode);

            closeSettingsWindow();
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Будь ласка, введіть коректні числові значення.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void closeSettingsWindow() {
        ((Stage) button_apply.getScene().getWindow()).close();
    }
}
