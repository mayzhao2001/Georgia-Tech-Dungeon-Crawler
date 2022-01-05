package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the loss screen that should be triggered when user's health goes to 0.
 */

public class DieScreen {
    private Scene scene;
    private Pane root;
    private int money;
    private InteriorRoom currRoom;

    public DieScreen() {
        try {
            root = FXMLLoader.load(WelcScreen.class.getResource("../resources/dieScreen.fxml"));
        } catch (IOException except) {
            //the fxml loader can't find the file
        }

        scene = new Scene(root, Main.getScreenWidth(), Main.getScreenHeight());
        addBackgroundImage();
        startOverButton();
        displayStats();

    }

    public Scene getScene() {
        return this.scene;
    }

    private void addBackgroundImage() {
        root.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Die_Screen.png").toExternalForm()
                + "');\n-fx-background-position: center center; \n-fx-background-repeat: stretch;");
    }

    public Pane getRoot() {
        return root;
    }

    //starting over
    private void startOverButton() {
        Button startOverButton = (Button) scene.lookup("#buttonStartOver");
        startOverButton.setText("Start Over");

        startOverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Change to welcome screen
                WelcScreen welcScreen = new WelcScreen();
                Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Main.changeWindowTo(currentWindow, welcScreen.getScene());
            }
        });
    }

    //displaying stats: money, playerHealth, monsters killed
    private void displayStats() {
        money = ConfigScreen.getGameState().getMoney();
        Label moneyLabel = (Label) scene.lookup("#stat1");
        moneyLabel.setText("Money: $" + money);

        int health = ConfigScreen.getGameState().getPlayerHealth();
        Label healthLabel = (Label) scene.lookup("#stat2");
        healthLabel.setText("Remaining health: " + health);

        ConfigScreen.Weapon weapon = ConfigScreen.getGameState().getWeapon();
        Label roomNumLabel = (Label) scene.lookup("#stat3");
        roomNumLabel.setText("Starting Weapon: " + weapon);

        ConfigScreen.Difficulty diff = ConfigScreen.getGameState().getDifficulty();
        Label diffLabel = (Label) scene.lookup("#stat4");
        diffLabel.setText("Starting Difficulty: " + diff);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int num) {
        money = num;
    }
}
