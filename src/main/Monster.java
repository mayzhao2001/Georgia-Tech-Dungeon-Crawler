package main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.event.ActionEvent;
import java.util.Random;

public class Monster {

    //instance variables for attributes
    private int health;
    private boolean isAlive;
    private String healthLabelID;
    @FXML
    private Label healthLabel;
    private Scene scene;
    private Item dropItem;
    private String extension = "";

    public Monster() {
        health = 100;
        isAlive = true;
        dropItem = null;
        healthLabelID = "#monHealth";
    }

    public Monster(String extension) {
        this();
        this.extension = extension;
    }

    public void setHealthLabelID(String h) {
        this.healthLabelID = h;
    }

    public void setDropItem(Item item) {
        //if dropItem == null, drop random item, else drop specified item
        dropItem = item;
    }

    public String getHealthLabelID() {
        return this.healthLabelID;
    }

    public void attack(int damage) {
        this.scene = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null).getScene();

        health -= damage;
        updateHealthLabel();
        checkStatus();
        //implement this later when we have person class
        //person.setHealth(person.getHealth - Math.random() * (10 - 0 + 1) + 1));

        //also - do we want the attacker's health to go down too?
        //this.setHealth((int) (this.getHealth() - Math.random() * ((5 - 0 + 1) + 1)));
        //checkStatus();
        //updateHealthLabel();

    }

    public void die() {
        //make monster disappear
        Label monsterHealth = (Label) scene.lookup(healthLabelID);
        monsterHealth.setVisible(false);

        Button monsterButton = (Button) scene.lookup("#examBoss" + extension);
        monsterButton.setVisible(false);
        // Item (random money, weapon, potion) drop
        dropItem();
    }

    // Get the drop item
    private void dropItem() {
        if (dropItem == null) {
            Random rand = new Random();
            int randomNum = rand.nextInt(5);
            processRandom(randomNum);
        } else {
            Button itemButton = (Button) scene.lookup("#examBoss");
            itemButton.setVisible(true);
            itemButton.setStyle("-fx-background-image: url('"
                    + Main.class.getResource(dropItem.getImage()).toExternalForm()
                    + "'); \n-fx-background-position: center center; "
                    + "\n-fx-background-repeat: stretch;"
                    + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
            //set item size
            itemButton.setPrefSize(100, 100);
            itemButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    InventoryScreen.addItem(dropItem);
                    itemButton.setVisible(false);
                }
            });
        }

    }

    private void processRandom(int randomNum) {
        GameState currGameState = ConfigScreen.getGameState();
        int currRoomOrder = currGameState.getRoomOrder();
        int currRoomIndex = currGameState.getRoomIndex();
        if (randomNum == 0) {
            // Money Drop (+100), maybe (the number of room index can change money?
            Button itemButton = (Button) scene.lookup("#examBoss" + extension);
            itemButton.setVisible(true);
            itemButton.setStyle("-fx-background-image: url('"
                    + Main.class.getResource("../resources/Coin.png").toExternalForm()
                    + "'); \n-fx-background-position: center center; "
                    + "\n-fx-background-repeat: stretch;"
                    + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;");
            //set item size
            itemButton.setPrefSize(100, 100);
            itemButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int currMoney = currGameState.getMoney();
                    currGameState.setMoney(currMoney + 100);
                    if (currRoomIndex < 5) {
                        InteriorRoom currRoom =
                                currGameState.getInteriorRoom(currRoomOrder, currRoomIndex);
                        currRoom.updateLabels();
                    } else {
                        LastRoom lastRoom = currGameState.getLastRoom();
                        lastRoom.update();
                    }
                    itemButton.setVisible(false);
                }
            });
        } else {
            Item dropItem;
            if (randomNum == 1) {
                // Potion Drop (+10 Health)
                dropItem = new HealthPotion();
            } else if (randomNum == 2) {
                // health potion
                dropItem = new Armour();
            } else if (randomNum == 3) {
                // weapon
                Random rand = new Random();
                int randNum = rand.nextInt(3);
                ConfigScreen.Weapon randWeapon = randomizeWeapon(randNum);
                dropItem = new Weapon(randWeapon);
            } else {
                // attack potion
                dropItem = new AttackPotion();
            }
            Button itemButton = (Button) scene.lookup("#examBoss" + extension);
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



        /*
        Time wait
            Timeline timeline = new Timeline
                (new KeyFrame(Duration.seconds(1), new EventHandler<>() {
                @Override
                        public void handle(ActionEvent e) {
                            int currHealth = currGameState.getPlayerHealth();
                            currGameState.setPlayerHealth(currHealth + 10);
                            itemButton.setVisible(false);
                            currRoom.updateLabels();
                }
            }));
            timeline.play();
        else {  attack potion drop
            // Attack Potion : increase attack power of current weapon (+10)
            //- Problem: cannot access weapon;

            ConfigScreen.Weapon currWeapon = currGameState.getWeapon();
            int currDam = currWeapon.getDamage();

            Weapon currWeapon = new Weapon(currGameState.getWeapon());
            int currDam = currWeapon.getDamage();
            int newDam = currDam + 10;
            currWeapon.setDamage(newDam);
            currGameState.setWeapon(currWeapon);
        }
        */

    }

    private ConfigScreen.Weapon randomizeWeapon(int randNum) {
        if (randNum == 0) {
            return ConfigScreen.Weapon.PENCIL;
        } else if (randNum == 1) {
            return ConfigScreen.Weapon.CALCULATOR;
        } else {
            return ConfigScreen.Weapon.TEXTBOOK;
        }
    }

    public void checkStatus() {
        if (health <= 0) {
            isAlive = false;
            this.die();
        }
    }

    public void updateHealthLabel() {
        healthLabel = (Label) scene.lookup(healthLabelID);
        healthLabel.setText("Health: " + getHealth());
    }


    //getters and setters for instance variables
    public boolean getIsAlive() {
        return this.isAlive;
    }

    public void setIsAlive(boolean val) {
        this.isAlive = val;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int num) {
        this.health = num;
    }

}
