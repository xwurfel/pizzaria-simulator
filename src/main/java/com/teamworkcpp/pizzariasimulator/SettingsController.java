package com.teamworkcpp.pizzariasimulator;

import com.teamworkcpp.pizzariasimulator.backend.enums.CookingMode;
import com.teamworkcpp.pizzariasimulator.backend.enums.SimulationMode;
import com.teamworkcpp.pizzariasimulator.backend.services.PizzaPrototypeRegistry;
import com.teamworkcpp.pizzariasimulator.backend.services.PizzeriaManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SettingsController {

    @FXML
    public ComboBox<String> combobox_cooking_mode;

    @FXML
    private TextField pizzaName;

    @FXML
    private TextField pizzaPrice;

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



            clearInputFields();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        pizzaName.clear();
        pizzaPrice.clear();
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
            int cookingMode = Integer.parseInt(combobox_cooking_mode.getValue());

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

            CookingMode cMode = null;

            if (cookingMode == 1){
                cMode = CookingMode.CONVEYOR_MODE;
            }
            else if (cookingMode == 2){
                cMode = CookingMode.ONE_PIZZAIOLO_MODE;
            }
            helloController.setSimulationSettings(simulationDuration, numberOfCashiers, numberOfCooks, simulationMode, cMode);

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
