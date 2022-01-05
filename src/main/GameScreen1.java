package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameScreen1 extends DungeonRoomParent {
    private Scene scene;
    private Pane root;

    private int startingMoney;
    private ConfigScreen.Weapon weapon;
    private ConfigScreen.Difficulty difficulty;

    private static String[] backgroundImgs;
    private static List<Integer> mazeOrder1;
    private static List<Integer> mazeOrder2;
    private static List<Integer> mazeOrder3;
    private static List<Integer> mazeOrder4;

    //room number that we allow entry into the challenge room 1
    private static int challengeEntryRoom1 = 0;
    //room number that we allow entry into the challenge room 2
    private static int challengeEntryRoom2 = 0;

    private GameState currGameState;

    public GameScreen1(
            ConfigScreen.Difficulty difficulty, ConfigScreen.Weapon weapon, boolean setUp) {
        if (setUp) {
            doSetUp();
        }

        try {
            root = FXMLLoader.load(
                    GameScreen1.class.getResource("../resources/InitialGameScreen.fxml")
            );
        } catch (IOException except) {
            //the fxml loader can't find the file
        }

        this.weapon = weapon;
        this.difficulty = difficulty;

        scene = new Scene(root, Main.getScreenWidth(), Main.getScreenHeight());
        addBackgroundImage();
        setStartingMoney(difficulty);
        setMoneyLabel();


        setDoor("#door1", 0);
        setDoor("#door2", 1);
        setDoor("#door3", 2);
        setDoor("#door4", 3);

        DungeonRoomParent thisRoom = this;
        // when you press i enter the inventory screen
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.I) {
                    // i key was pressed
                    InventoryScreen inv = new InventoryScreen(thisRoom);
                    Stage currentWindow = (Stage) Stage.getWindows().stream()
                            .filter(Window::isShowing).findFirst().orElse(null);
                    Main.changeWindowTo(currentWindow, inv.getScene());
                }
            }
        });

        this.updateWeaponDisplay();
        this.updateArmourDisplay();
        this.setupChallengeRoomEntrances();
    }

    private void doSetUp() {
        backgroundImgs = new String[]{
            "Blue_Room.png",
            "Pink_Room.png",
            "Green_Room.png",
            "Yellow_Room.png",
            "Orange_Room.png",
            "Purple_Room.png"
        };

        mazeOrder1 = new ArrayList<>();
        mazeOrder2 = new ArrayList<>();
        mazeOrder3 = new ArrayList<>();
        mazeOrder4 = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            mazeOrder1.add(i);
            mazeOrder2.add(i);
            mazeOrder3.add(i);
            mazeOrder4.add(i);
        }

        Collections.shuffle(mazeOrder1);
        Collections.shuffle(mazeOrder2);
        Collections.shuffle(mazeOrder3);
        Collections.shuffle(mazeOrder4);
    }

    @Override
    void update() {
        updateWeaponDisplay();
        updateArmourDisplay();
    }

    public Scene getScene() {
        return this.scene;
    }

    public Pane getRoot() {
        return root;
    }

    private void addBackgroundImage() {
        root.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/InitialGameScreenBackground.png")
                .toExternalForm()
                + "');\n-fx-background-position: center center; \n-fx-background-repeat: stretch;");
    }

    private void setStartingMoney(ConfigScreen.Difficulty difficulty) {
        switch (difficulty) {
        case IN_STATE:
            startingMoney = 500;
            break;
        case OUT_OF_STATE:
            startingMoney = 300;
            break;
        case INTERNATIONAL:
            startingMoney = 100;
            break;
        default:
            break;
        }
    }

    public int getStartingMoney() {
        return startingMoney;
    }

    public ConfigScreen.Weapon getWeapon() {
        return weapon;
    }

    public ConfigScreen.Difficulty getDifficulty() {
        return difficulty;
    }


    private void setMoneyLabel() {
        Label moneyLabel = (Label) scene.lookup("#startingMoney");
        moneyLabel.setText("StartingMoney: $" + startingMoney);
    }


    private void setDoor(String id, int pathNum) {

        Button doorButton = (Button) scene.lookup(id);
        doorButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Door.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        doorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // New change
                GameState currGameState = ConfigScreen.getGameState();
                InteriorRoom next;
                if (currGameState.getInteriorRoom(pathNum, 0) == null) {
                    next = new InteriorRoom(0, difficulty,
                            weapon, startingMoney, pathNum);
                    next.update();
                    currGameState.setRoomOrder(pathNum);
                    currGameState.setRoomIndex(0);
                    currGameState.setInteriorRoom(pathNum, 0, next);
                } else {
                    next = currGameState.getInteriorRoom(pathNum, 0);
                    next.update();
                }
                Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Main.changeWindowTo(currentWindow, next.getScene());
            }
        });
    }

    public static String[] getBackgroundImgs() {
        return backgroundImgs;
    }
    public static List<Integer> getMazeOrder1() {
        return mazeOrder1;
    }
    public static List<Integer> getMazeOrder2() {
        return mazeOrder2;
    }
    public static List<Integer> getMazeOrder3() {
        return mazeOrder3;
    }
    public static List<Integer> getMazeOrder4() {
        return mazeOrder4;
    }

    protected void updateWeaponDisplay() {
        //add current weapon label
        Label weaponLabel = (Label) scene.lookup("weaponLabel");
        if (weaponLabel == null) {
            //weapon label does not exit - setup
            weaponLabel = new Label();
            weaponLabel.setPrefSize(150, 20);
            weaponLabel.setLayoutX(1675);
            weaponLabel.setLayoutY(925);
            weaponLabel.setFont(new Font(20));
            weaponLabel.setText("Current Weapon:");
            root.getChildren().add(weaponLabel);
        }

        //remove old weapon image display from screen
        for (int i = root.getChildren().size() - 1; i >= 0; --i) {
            Node n = root.getChildren().get(i);
            if (n != null && n.getId() != null && n.getId().equals("currentWeapon")) {
                root.getChildren().remove(i);
                break;
            }
        }

        //add new weapon display to screen
        Button itemButton = new Button();
        Weapon currWeapon = new Weapon(ConfigScreen.getGameState().getWeapon());
        itemButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource(currWeapon.getImage()).toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");

        //set item size
        itemButton.setPrefSize(100, 100);

        //set item pos
        itemButton.setLayoutX(1700);
        itemButton.setLayoutY(950);

        itemButton.setId("currentWeapon");

        root.getChildren().add(itemButton);
    }

    protected void updateArmourDisplay() {
        //add current armour label
        Label armourLabel = (Label) scene.lookup("armourLabel");
        if (armourLabel == null) {
            //weapon label does not exit - setup
            armourLabel = new Label();
            armourLabel.setPrefSize(150, 20);
            armourLabel.setLayoutX(1475);
            armourLabel.setLayoutY(925);
            armourLabel.setFont(new Font(20));
            armourLabel.setText("Current Armour:");
            root.getChildren().add(armourLabel);
        }

        //remove old armour image display from screen
        for (int i = root.getChildren().size() - 1; i >= 0; --i) {
            Node n = root.getChildren().get(i);
            if (n != null && n.getId() != null && n.getId().equals("currentArmour")) {
                root.getChildren().remove(i);
                break;
            }
        }

        //add new armour display to screen
        Button itemButton = new Button();
        Armour currArmour = ConfigScreen.getGameState().getArmour();
        if (currArmour == null) {
            return;
        }

        itemButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource(currArmour.getImage()).toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");

        //set item size
        itemButton.setPrefSize(100, 100);

        //set item pos
        itemButton.setLayoutX(1500);
        itemButton.setLayoutY(950);

        itemButton.setId("currentArmour");

        root.getChildren().add(itemButton);
    }

    private void setupChallengeRoomEntrances() {
        Random r = new Random();
        challengeEntryRoom1 = r.nextInt(6);
        challengeEntryRoom2 = r.nextInt(6);
        while (challengeEntryRoom2 == challengeEntryRoom1) {
            challengeEntryRoom2 = r.nextInt(6);
        }
    }

    public static int getChallengeEntryRoom1() {
        return challengeEntryRoom1;
    }
    public static int getChallengeEntryRoom2() {
        return challengeEntryRoom2;
    }
}