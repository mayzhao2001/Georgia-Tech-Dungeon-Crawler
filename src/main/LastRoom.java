package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;

public class LastRoom extends DungeonRoomParent {
    private Scene scene;
    private Pane root;
    private Label alertLabel;

    private int money;
    private ConfigScreen.Difficulty difficulty;
    private ConfigScreen.Weapon weapon;
    private Monster monster = new Monster();

    //constructor for last room
    //ConfigScreen.Difficulty difficulty, ConfigScreen.Weapon weapon, int money
    public LastRoom(ConfigScreen.Difficulty difficulty, ConfigScreen.Weapon weapon, int money) {
        try {
            root = FXMLLoader.load(
                    GameScreen1.class.getResource("../resources/Last_Room.fxml")
            );
        } catch (IOException except) {
            //the fxml loader can't find the file
        }

        this.weapon = weapon;
        this.difficulty = difficulty;
        this.money = money;
        this.monster.setHealth(125);
        this.monster.setDropItem(new DiplomaItem());

        scene = new Scene(root, Main.getScreenWidth(), Main.getScreenHeight());
        addBackgroundImage();
        exitRoomButton();
        updateLabels();
        monsterButton();
        setHealthLabel();
        GameState currGameState = ConfigScreen.getGameState();
        currGameState.setRoomIndex(6);

        this.monsterAttackThread = new Timeline(
                new KeyFrame(Duration.seconds(2),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            scene = Stage.getWindows().stream().filter(Window::isShowing)
                                    .findFirst().orElse(null).getScene();
                            GameState currGameState = ConfigScreen.getGameState();

                            if (currGameState.getArmour() != null
                                    && currGameState.getArmour().getAlive()) {
                                //reduce damage by half if the player is wearing armor
                                currGameState.damagePlayer(5);
                            } else {
                                currGameState.damagePlayer(10);
                            }

                            if (!currGameState.isPlayerAlive()) {
                                DieScreen screen = new DieScreen();
                                Stage currentWindow = (Stage) Stage.getWindows().stream()
                                        .filter(Window::isShowing).findFirst().orElse(null);
                                Main.changeWindowTo(currentWindow, screen.getScene());
                                monsterAttackThread.stop();
                            } else {
                                updateLabels();
                            }
                        }
                }));
        monsterAttackThread.setCycleCount(Timeline.INDEFINITE);

        if (this.monster.getIsAlive()) {
            monsterAttackThread.play();
        }

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
                    monsterAttackThread.stop();
                }
            }
        });

        this.updateWeaponDisplay();
        this.updateArmourDisplay();

    }

    @Override
    void update() {
        //reload money, health
        this.updateLabels();

        //reload current weapon indicator
        this.updateWeaponDisplay();
        this.updateArmourDisplay();

        //restart monster attacks
        if (monster.getIsAlive()) {
            monsterAttackThread.play();
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    public Pane getRoot() {
        return root;
    }

    private void addBackgroundImage() {
        root.setStyle("-fx-background-image: url('"
                + Main.class.getResource(
                        "../resources/InitialGameScreenBackground.png").toExternalForm()
                + "');\n-fx-background-position: center center; \n-fx-background-repeat: stretch;");
    }

    //getters for money, weapon, difficulty
    public int getMoney() {
        return money;
    }

    public ConfigScreen.Weapon getWeapon() {
        return weapon;
    }

    public ConfigScreen.Difficulty getDifficulty() {
        return difficulty;
    }

    //setter for money label
    private void updateLabels() {
        money = ConfigScreen.getGameState().getMoney();
        Label moneyLabel = (Label) scene.lookup("#money");
        moneyLabel.setText("Money: $" + money);

        Label roomNumLabel = (Label) scene.lookup("#roomNum");
        roomNumLabel.setText("Room: Final Room");

        int health = ConfigScreen.getGameState().getPlayerHealth();
        Label healthLabel = (Label) scene.lookup("#playerHealth");
        healthLabel.setText("Player Health: " + health);
    }

    private void setHealthLabel() {
        Label monHealthLab = (Label) scene.lookup("#monHealth");
        monHealthLab.setText("Health: " + monster.getHealth());
    }

    public Timeline getMonsterAttackThread() {
        return this.monsterAttackThread;
    }

    //setting up monster
    private void monsterButton() {
        Button monsterButton = (Button) scene.lookup("#examBoss");
        monsterButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Final_Boss.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");

        monsterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // when monster is clicked (attacked), health declines by 10
                GameState currGameState = ConfigScreen.getGameState();
                Weapon currWeapon = new Weapon(currGameState.getWeapon());
                int currDam = currWeapon.getDamage();
                if (hasAttackPotion(currGameState)) {
                    currDam *= 2;
                }
                // Change to weapon damage?
                monster.attack(currDam);
                if (!monster.getIsAlive()) {
                    monsterAttackThread.stop();
                }
            }
        });
    }
    private boolean hasAttackPotion(GameState currGameState) {
        return currGameState.getAttackPotion();
    }
    //exit room door to go to congrats screen :)
    private void exitRoomButton() {
        Button exitButton = (Button) scene.lookup("#Door");
        exitButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Door.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        exitButton.setText("");

        this.alertLabel = (Label) scene.lookup("#alertLabel");
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (monster.getHealth() <= 0) {
                    // Change to Config Screen
                    WinScreen winScreen = new WinScreen();
                    Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Main.changeWindowTo(currentWindow, winScreen.getScene());
                } else {
                    alertLabel.setVisible(true);
                }

            }
        });
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
}
