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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class InteriorRoom extends DungeonRoomParent {
    private Scene scene;
    private Pane root;
    private Label alertLabel;

    private int money;
    private int roomIndex; // 0-5 sequentially in mazOrder
    private int roomNum; // 0-5 randomly to create a "random maze"
    private int order;
    private String backGroundImage;
    private ConfigScreen.Weapon weapon;
    private ConfigScreen.Difficulty difficulty;

    private InteriorRoom currRoom;

    private Monster monster = new Monster();

    public InteriorRoom(int roomIndex, ConfigScreen.Difficulty difficulty,
                        ConfigScreen.Weapon weapon, int money, int order) {
        try {
            root = FXMLLoader.load(
                    GameScreen1.class.getResource("../resources/LectureRoom.fxml")
            );
        } catch (IOException except) {
            //the fxml loader can't find the file
        }

        // We can change weapon, difficulty, and money
        this.weapon = weapon;
        this.difficulty = difficulty;
        this.money = money;
        this.roomIndex = roomIndex;
        this.order = order;
        checkOrder(order);

        scene = new Scene(root, Main.getScreenWidth(), Main.getScreenHeight());
        addBackgroundImage("../resources/" + GameScreen1.getBackgroundImgs()[roomNum]);
        updateLabels();

        setupDoors();
        // Monster image and health label
        monsterButton();
        setHealthLabel();

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

        //add current weapon to screen
        this.updateWeaponDisplay();

        //add current armour to screen
        this.updateArmourDisplay();

        //add challenge room entrance if needed
        if (GameScreen1.getChallengeEntryRoom1() == this.roomNum) {
            this.addChallengeEntrance(1);
        } else if (GameScreen1.getChallengeEntryRoom2() == this.roomNum) {
            this.addChallengeEntrance(2);
        }
    }

    private void checkOrder(int order) {
        if (order == 1) {
            this.roomNum = GameScreen1.getMazeOrder1().get(this.roomIndex);
        } else if (order == 2) {
            this.roomNum = GameScreen1.getMazeOrder2().get(this.roomIndex);
        } else if (order == 3) {
            this.roomNum = GameScreen1.getMazeOrder3().get(this.roomIndex);
        } else {
            this.roomNum = GameScreen1.getMazeOrder4().get(this.roomIndex);
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

    private void addBackgroundImage(String background) {
        root.setStyle("-fx-background-image: url('"
                + Main.class.getResource(background)
                .toExternalForm()
                + "');\n-fx-background-position: center center; \n-fx-background-repeat: stretch;");
        backGroundImage = background;
    }

    public int getMoney() {
        return money;
    }

    public ConfigScreen.Weapon getWeapon() {
        return weapon;
    }

    public ConfigScreen.Difficulty getDifficulty() {
        return difficulty;
    }


    protected void updateLabels() {
        money = ConfigScreen.getGameState().getMoney();
        Label moneyLabel = (Label) scene.lookup("#money");
        moneyLabel.setText("Money: $" + money);

        Label roomNumLabel = (Label) scene.lookup("#roomNum");
        roomNumLabel.setText("Room: " + roomNum);

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


    private void setupDoors() {
        Button nextDoorLeft = (Button) scene.lookup("#nextDoorLeft");
        nextDoorLeft.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Open_Door_Left.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        this.alertLabel = (Label) scene.lookup("#alertLabel");
        nextDoorLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!monster.getIsAlive()) {
                    Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Main.changeWindowTo(currentWindow, loadRoom(roomIndex + 1));
                } else {
                    alertLabel.setVisible(true);
                }

            }
        });

        Button nextDoorRight = (Button) scene.lookup("#nextDoorRight");
        nextDoorRight.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Open_Door_Right.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        nextDoorRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!monster.getIsAlive()) {
                    Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Main.changeWindowTo(currentWindow, loadRoom(roomIndex + 1));
                } else {
                    alertLabel.setVisible(true);
                }
            }
        });

        Button prevDoorLeft = (Button) scene.lookup("#prevDoorLeft");
        prevDoorLeft.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Open_Door.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        prevDoorLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                // New change
                Main.changeWindowTo(currentWindow, loadRoom(roomIndex - 1));
                monsterAttackThread.stop();
            }
        });
        Button prevDoorRight = (Button) scene.lookup("#prevDoorRight");
        prevDoorRight.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Open_Door.png").toExternalForm()
                + "'); \n-fx-background-position: center center; \n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        prevDoorRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Stage currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
                // New change
                Main.changeWindowTo(currentWindow, loadRoom(roomIndex - 1));
                monsterAttackThread.stop();
            }
        });

    }

    private Scene loadRoom(int roomIndex) {
        GameState currGameState = ConfigScreen.getGameState();
        if (roomIndex == -1) {
            return currGameState.getGameScreen1().getScene();
        } else if (roomIndex < 5) {
            if (currGameState.getInteriorRoom(order, roomIndex) == null) {
                InteriorRoom nextRoom = new InteriorRoom(roomIndex, difficulty,
                        weapon, money, order);
                nextRoom.update();
                currGameState.setRoomOrder(order);
                currGameState.setRoomIndex(roomIndex);
                currGameState.setInteriorRoom(order, roomIndex, nextRoom);
                return nextRoom.getScene();
            } else {
                InteriorRoom nextRoom = currGameState.getInteriorRoom(order, roomIndex);
                nextRoom.update();
                return nextRoom.getScene();
            }
        } else {
            LastRoom lastRoom = new LastRoom(difficulty, weapon, money);
            currGameState.setLastRoom(lastRoom);
            return lastRoom.getScene();
        }
    }

    public String getBackgroundImage() {
        return backGroundImage;
    }

    public int getRoomNum() {
        return this.roomNum;
    }

    private void setHealthLabel() {
        Label monHealthLab = (Label) scene.lookup("#monHealth");
        monster.setHealth((roomIndex + 1) * 20);
        monHealthLab.setText("Health: " + monster.getHealth());
    }

    private Monster getMonster() {
        return monster;
    }

    //setting up monster
    private void monsterButton() {
        Button monsterButton = (Button) scene.lookup("#examBoss");
        Random rand = new Random();
        int random = rand.nextInt(2);
        if (random == 0) {
            monsterButton.setStyle("-fx-background-image: url('"
                    + Main.class.getResource("../resources/Exam_Boss.png").toExternalForm()
                    + "'); \n-fx-background-position: center center;"
                    + "\n-fx-background-repeat: stretch;"
                    + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        } else if (random == 1) {
            monsterButton.setStyle("-fx-background-image: url('"
                    + Main.class.getResource("../resources/Quiz_Boss.png").toExternalForm()
                    + "'); \n-fx-background-position: center center; "
                    + "\n-fx-background-repeat: stretch;"
                    + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
        }

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


    private void addChallengeEntrance(int type) {
        Button securityGuard = new Button();
        securityGuard.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Security_Guard.png").toExternalForm()
                + "'); \n-fx-background-position: center center; "
                + "\n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;"
                + "\n-fx-background-color: transparent;");

        //set guard size
        securityGuard.setPrefSize(100, 130);

        //set guard pos
        securityGuard.setLayoutX(1625);
        securityGuard.setLayoutY(450);

        DungeonRoomParent currRoom = this;

        //setup click handler
        securityGuard.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (root.lookup("#speech") == null) {

                    Rectangle rect = new Rectangle();
                    rect.setHeight(120);
                    rect.setWidth(270);
                    rect.setFill(Color.WHITE);
                    rect.setLayoutX(1620);
                    rect.setLayoutY(600);

                    root.getChildren().add(rect);

                    Label speech = new Label();
                    if (type == 1) {
                        speech.setText("I could let you into tech tower\n"
                                + "But it'll cost ya $300\nWhat do you say?");
                    } else {
                        speech.setText("Interested in getting\non the honors list?\n"
                                + "But it'll cost ya $200 to try\nWhat do you say?");
                    }

                    //set label pos
                    speech.setLayoutX(1625);
                    speech.setLayoutY(600);

                    speech.setFont(new Font(20));

                    //set label id
                    speech.setId("speech");

                    root.getChildren().add(speech);

                } else {
                    if ((ConfigScreen.didChallengeTechTower() && type == 1)
                            || (ConfigScreen.didChallengeHonors() && type != 1)) {
                        Label speech = (Label) root.lookup("#speech");
                        if (type == 1) {
                            //player already defeated tech tower
                            speech.setText("You can't come through here,\n"
                                    + "There's an investigation \n"
                                    + "going on! Someone stole the \nT from tech towers!");
                        } else {
                            speech.setText("You already got Honors!\nCongrats!");

                        }
                    } else {
                        if (money >= 300) {
                            // we have enough money - use to enter the room
                            money -= 300;

                            GameState state = ConfigScreen.getGameState();
                            state.setMoney(money);
                            ConfigScreen.setGameState(state);

                            ChallengeRoom cRoom = new ChallengeRoom(type, currRoom);
                            Stage currentWindow = (Stage) Stage.getWindows().stream()
                                    .filter(Window::isShowing).findFirst().orElse(null);
                            Main.changeWindowTo(currentWindow, cRoom.getScene());
                            monsterAttackThread.stop();
                        } else {
                                Label speech = (Label) root.lookup("#speech");
                                speech.setText("you think $" + money + " is going to \n"
                                        + "get you into tech tower?\nGet out of here kid!");
                            }
                        }
                    }


            }
        });

        securityGuard.setId("securityGuard");

        root.getChildren().add(securityGuard);
    }
}