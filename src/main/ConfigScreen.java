package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfigScreen {
    private Scene scene;
    private Pane root;

    private TextField nameField;
    private Label alertLabel;

    private static GameState gameState;

    public enum Difficulty {
        INVALID,
        IN_STATE,
        OUT_OF_STATE,
        INTERNATIONAL
    }

    public enum Weapon {
        INVALID,
        PENCIL,
        TEXTBOOK,
        CALCULATOR;
    }

    private Difficulty currentDiff = Difficulty.INVALID;
    private Weapon currentWeapon = Weapon.INVALID;

    public ConfigScreen() {

        try {
            root = FXMLLoader.load(ConfigScreen.class.getResource("../resources/configPane.fxml"));
        } catch (IOException except) {
            //the fxml loader can't find the file
        }

        scene = new Scene(root, Main.getScreenWidth(), Main.getScreenHeight());
        startupStartButton();
        addBackgroundImg();
        styleNameField();

        setupDifficultyButtons();
        setupWeaponButtons();
    }


    public Scene getScene() {
        return this.scene;
    }

    private void startupStartButton() {
        Button startButton = (Button) scene.lookup("#buttonStart");
        startButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/startButton.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        startButton.setText(""); //Image already has the text on it so remove it
        this.alertLabel = (Label) scene.lookup("#alertLabel");
        startButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override public void handle(ActionEvent e) {
                if (validation()) {
                    // Change to Initial Game Screen
                    gameState = new GameState(currentWeapon, currentDiff);
                    InventoryScreen.clear(); //reset inventory screen for new game
                    //add weapon to inventory
                    InventoryScreen.addItem(new main.Weapon(currentWeapon));

                    GameScreen1 screen1 = new GameScreen1(currentDiff, currentWeapon, true);
                    gameState.setGameScreen1(screen1);
                    Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Main.changeWindowTo(currentWindow, screen1.getScene());
                } else {
                    // Prompt the user to enter correct name
                    alertLabel.setVisible(true);
                }
            }
        });

    }

    private void styleNameField() {
        nameField = (TextField) scene.lookup("#NameField");
        nameField.setStyle("-fx-background-color: rgba(150, 150, 150, 0.75)"
                + ";\n-fx-text-fill: white;");
    }

    private void addBackgroundImg() {
        //add background image using css
        root.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/techMenu.png").toExternalForm()
                + "');\n-fx-background-position: center center; \n-fx-background-repeat: stretch;");
    }

    private void setupDifficultyButtons() {
        //grab buttons from scene
        Button instate = (Button) scene.lookup("#diffIS");
        Button outofstate = (Button) scene.lookup("#diffOOS");
        Button international = (Button) scene.lookup("#diffIntern");

        //setup button styling (lines separated for checkstyle)
        String defaultStyling = "-fx-background-color: rgba(150, 150, 150, 0.75);"
                + " -fx-text-fill: white;";
        String selectedStyling = "\n-fx-background-color: rgba(50, 50, 50, 0.75);"
                + " -fx-text-fill: white;";
        instate.setStyle(defaultStyling);
        outofstate.setStyle(defaultStyling);
        international.setStyle(defaultStyling);

        //add action listeners to buttons
        instate.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                instate.setStyle(selectedStyling);
                outofstate.setStyle(defaultStyling);
                international.setStyle(defaultStyling);
                currentDiff = Difficulty.IN_STATE;
            }
        });

        outofstate.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                instate.setStyle(defaultStyling);
                outofstate.setStyle(selectedStyling);
                international.setStyle(defaultStyling);
                currentDiff = Difficulty.OUT_OF_STATE;
            }
        });

        international.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                instate.setStyle(defaultStyling);
                outofstate.setStyle(defaultStyling);
                international.setStyle(selectedStyling);
                currentDiff = Difficulty.INTERNATIONAL;
            }
        });
    }

    private void setupWeaponButtons() {
        //grab buttons from scene
        Button pencil = (Button) scene.lookup("#weapPencil");
        Button textbook = (Button) scene.lookup("#weapText");
        Button calc = (Button) scene.lookup("#weapCalc");

        //setup button styling (lines separated for checkstyle)
        String defaultStyling = "-fx-background-color: rgba(150, 150, 150, 0.75);"
                + " -fx-text-fill: white;";
        String selectedStyling = "-fx-background-color: rgba(50, 50, 50, 0.75);"
                + " -fx-text-fill: white;";

        pencil.setStyle(defaultStyling);
        textbook.setStyle(defaultStyling);
        calc.setStyle(defaultStyling);

        //add action listeners to buttons
        pencil.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pencil.setStyle(selectedStyling);
                textbook.setStyle(defaultStyling);
                calc.setStyle(defaultStyling);
                currentWeapon = Weapon.PENCIL;
            }
        });

        textbook.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pencil.setStyle(defaultStyling);
                textbook.setStyle(selectedStyling);
                calc.setStyle(defaultStyling);
                currentWeapon = Weapon.TEXTBOOK;
            }
        });

        calc.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pencil.setStyle(defaultStyling);
                textbook.setStyle(defaultStyling);
                calc.setStyle(selectedStyling);
                currentWeapon = Weapon.CALCULATOR;
            }
        });
    }

    /**
     * Checks if the configuration state allows us to move to the next screen.
     * NameField must not be empty, difficulty and weapon must both be selected
     *
     * @return true if we can move to the next screen, else return false
     */
    public boolean validation() {
        if (nameField == null || nameField.getText().trim().isEmpty()) {
            return false;
        }

        if (this.currentDiff == Difficulty.INVALID || this.currentWeapon == Weapon.INVALID) {
            return false;
        }
        return true;
    }

    public Pane getRoot() {
        return root;
    }

    public Difficulty getDifficulty() {
        return this.currentDiff;
    }

    public Weapon getWeapon() {
        return this.currentWeapon;
    }

    public TextField getNameField() {
        return this.nameField;
    }

    public Label getAlert() {
        return this.alertLabel;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState state) {
        gameState = state;
    }

    public static boolean didChallengeTechTower() {
        return gameState.getDidChallengeTechTowers();
    }

    public static boolean didChallengeHonors() {
        return gameState.getDidChallengeHonors();
    }

    public static void completeTechTower() {
        gameState.setTechTowersChallengeComplete(true);
    }

    public static void completeHonors() {
        gameState.setHonorsComplete(true);
    }

}