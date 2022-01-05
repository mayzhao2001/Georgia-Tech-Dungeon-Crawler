package main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChallengeRoom {
    private Scene scene;
    private Pane root;
    private String backGroundImage;
    private int monsters;
    private List<Monster> monstersList = new ArrayList<>();

    private Timeline monsterAttackThread;
    private DungeonRoomParent prevRoom;

    private int roomType;

    public ChallengeRoom(int roomType, DungeonRoomParent prevRoom) {
        this.roomType = roomType;
        this.prevRoom = prevRoom;

        try {
            root = FXMLLoader.load(
                    GameScreen1.class.getResource("../resources/ChallengeRoom.fxml")
            );
        } catch (IOException except) {
            //the fxml loader can't find the file
        }

        scene = new Scene(root, Main.getScreenWidth(), Main.getScreenHeight());
        addBackgroundImage("../resources/Challenge_Room.png");

        update();
        yesButton();
        noButton();

        //add current weapon to screen
        this.updateWeaponDisplay();

        //add current armour to screen
        this.updateArmourDisplay();
    }

    public void setupChallenge() {
        if (roomType == 1) {
            ConfigScreen.completeTechTower();
        } else {
            ConfigScreen.completeHonors();
        }
        monsters = 3;
        for (int i = 1; i <= monsters; i++) {
            Monster monster = new Monster(String.valueOf(i));
            monster.setHealthLabelID("#monHealth" + i);
            monstersList.add(monster);
            monsterButton("#examBoss" + i, monster);
            setMonsterHealth("#monHealth" + i, monster, 100);
        }

        monsterAttackThread = new Timeline(
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
                            currGameState.damagePlayer(5 * monsters);
                        } else {
                            currGameState.damagePlayer(10 * monsters);
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

        if (monsters > 0) {
            monsterAttackThread.play();
        } else {
            monsterAttackThread.stop();
        }
    }

    /**
     * Run whenever the room is entered
     */
    public void update() {
        //reload money, health
        this.updateLabels();

        //reload current weapon display
        this.updateWeaponDisplay();

        //reload current armour display
        this.updateArmourDisplay();

    }

    public Scene getScene() {
        return this.scene;
    }

    public Pane getRoot() {
        return root;
    }

    private void addBackgroundImage(String background) {
        root.setStyle("-fx-background-image: url('"
                + Main.class.getResource(background)
                .toExternalForm()
                + "');\n-fx-background-position: center center; \n-fx-background-repeat: stretch;");
        backGroundImage = background;
    }

    public ChallengeItem getChallengeItem() {
        return new ChallengeItem(roomType);
    }



    protected void updateLabels() {

        int health = ConfigScreen.getGameState().getPlayerHealth();
        Label healthLabel = (Label) scene.lookup("#playerHealth");
        healthLabel.setText("Player Health: " + health);
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

    public String getBackgroundImage() {
        return backGroundImage;
    }

    private void setMonsterHealth(String id, Monster monster, int health) {
        Label monHealthLab = (Label) scene.lookup(id);
        monster.setHealth(health);
        monHealthLab.setText("Health: " + monster.getHealth());
        monHealthLab.setVisible(true);
    }

    //setting up yes button
    private void yesButton() {
        Button yesButton = (Button) scene.lookup("#yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                (scene.lookup("#acceptLabel")).setVisible(false);
                (scene.lookup("#yes")).setVisible(false);
                (scene.lookup("#no")).setVisible(false);
                setupChallenge();
            }
        });
    }

    //setting up no button
    private void noButton() {
        Button yesButton = (Button) scene.lookup("#no");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                (scene.lookup("#acceptLabel")).setVisible(false);
                (scene.lookup("#yes")).setVisible(false);
                (scene.lookup("#no")).setVisible(false);
                (scene.lookup("#noLabel")).setVisible(true);

                //go back to the original room
                Stage currentWindow = (Stage) Stage.getWindows().stream()
                        .filter(Window::isShowing).findFirst().orElse(null);
                Main.changeWindowTo(currentWindow, prevRoom.getScene());
                prevRoom.update();
            }
        });
    }

    //setting up monster
    private void monsterButton(String monsterID, Monster monster) {
        Button monsterButton = (Button) scene.lookup(monsterID);
        monsterButton.setStyle("-fx-background-image: url('"
                    + Main.class.getResource("../resources/Security_Guard.png").toExternalForm()
                    + "'); \n-fx-background-position: center center;"
                    + "\n-fx-background-repeat: stretch;"
                    + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        monsterButton.setVisible(true);
        monsterButton.setPrefSize(100, 130);
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
                setMonsterHealth(monster.getHealthLabelID(), monster, monster.getHealth());
                if (!monster.getIsAlive()) {
                    monster.die();
                    monsters--;
                    if (monsters == 0) {
                        monsterAttackThread.stop();
                        dropItem();
                        exitRoomButton();
                    }
                }
            }
        });
    }

    private void dropItem() {
        GameState currGameState = ConfigScreen.getGameState();
        int currRoomOrder = currGameState.getRoomOrder();
        int currRoomIndex = currGameState.getRoomIndex();
        Item dropItem = new ChallengeItem(roomType);
        Button itemButton = (Button) scene.lookup("#reward");
        itemButton.setVisible(true);
        itemButton.setVisible(true);
        itemButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource(dropItem.getImage()).toExternalForm()
                + "'); \n-fx-background-position: center center; "
                + "\n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        //set item size
        itemButton.setPrefSize(100, 100);
        Item finalDropItem = dropItem;
        itemButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (InventoryScreen.hasSpaceForItems()) {
                    InventoryScreen.addItem(finalDropItem);
                    itemButton.setVisible(false);
                }
                if (currRoomIndex < 5) {
                    InteriorRoom currRoom =
                            currGameState.getInteriorRoom(currRoomOrder, currRoomIndex);
                    currRoom.updateLabels();
                } else {
                    LastRoom lastRoom = currGameState.getLastRoom();
                    lastRoom.update();
                }
            }
        });
    }

    private void exitRoomButton() {
        Button exitButton = (Button) scene.lookup("#Door");
        exitButton.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Door.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        exitButton.setText("");
        exitButton.setVisible(true);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                //go back to the previous room
                Stage currentWindow = (Stage) Stage.getWindows().stream()
                        .filter(Window::isShowing).findFirst().orElse(null);
                Main.changeWindowTo(currentWindow, prevRoom.getScene());
                prevRoom.update();
            }
        });
    }

    private boolean hasAttackPotion(GameState currGameState) {
        return currGameState.getAttackPotion();
    }

}
