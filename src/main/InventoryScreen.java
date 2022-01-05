package main;

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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class InventoryScreen {

    private Scene scene;
    private DungeonRoomParent prevRoom;
    private Pane root;
    //to-do: remove starting items in inventory -- this is just for testing
    private static Item[] items = new Item[8];
    private static int[][] itemPoses = {
            {415, 340}, {715, 340}, {1015, 340}, {1315, 340},
            {415, 640}, {715, 640}, {1015, 640}, {1315, 640} };

    public InventoryScreen(DungeonRoomParent prevRoom) {
        try {
            root = FXMLLoader.load(ConfigScreen.class.getResource("../resources/Inventory.fxml"));
        } catch (IOException except) {
            //the fxml loader can't find the file
        }

        scene = new Scene(root, Main.getScreenWidth(), Main.getScreenHeight());
        this.prevRoom = prevRoom;
        addBackgroundImg();

        // when you press i exit the inventory screen
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.I) {
                    // i key was pressed
                    Stage currentWindow = (Stage) Stage.getWindows().stream()
                            .filter(Window::isShowing).findFirst().orElse(null);
                    Main.changeWindowTo(currentWindow, prevRoom.getScene());
                    prevRoom.update();
                }
            }
        });
        updateItemDisplay();

    }

    private void updateMoneyLabel() {
        int money = ConfigScreen.getGameState().getMoney();
        Label moneyLabel = (Label) scene.lookup("#money");
        moneyLabel.setText("Money: $" + money);
    }

    private void updateItemDisplay() {
        this.updateWeaponDisplay();
        this.updateArmourDisplay();
        this.updateMoneyLabel();
        //remove existing items from the display
        for (int i = root.getChildren().size() - 1; i >= 0; --i) {
            Node n = root.getChildren().get(i);
            if (n.getId() != null && n.getId().contains("item")) {
                root.getChildren().remove(i);
            }
        }

        //add items that should exist in the display
        for (int i = 0; i < items.length; ++i) {
            Item item = items[i];
            if (item == null) {
                continue;
            }
            int[] pos = itemPoses[i];
            Button itemButton = new Button();
            itemButton.setStyle("-fx-background-image: url('"
                    + Main.class.getResource(item.getImage()).toExternalForm()
                    + "'); \n-fx-background-position: center center; "
                    + "\n-fx-background-repeat: stretch;"
                    + "\n-fx-background-size: stretch;"
                    + "\n-fx-background-color: transparent;");

            //set item size
            itemButton.setPrefSize(200, 200);

            //set item pos
            itemButton.setLayoutX(pos[0]);
            itemButton.setLayoutY(pos[1]);

            //setup click handler
            int itemIndex = i;
            itemButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    item.useItem(); //use the item
                    if (item.isSingleUse()) {
                        items[itemIndex] = null; //remove item from array
                    }
                    updateItemDisplay(); //update visualization of items
                }
            });

            itemButton.setId("item" + itemIndex);

            root.getChildren().add(itemButton);
        }
    }

    private void addBackgroundImg() {
        //add background image using css
        root.setStyle("-fx-background-image: url('"
                + Main.class.getResource("../resources/Inventory_Screen.png").toExternalForm()
                + "');\n-fx-background-position: center center; \n-fx-background-repeat: stretch;");
    }

    private void exitInventory() {
        System.out.println("exit");

    }

    public Scene getScene() {
        return this.scene;
    }

    public static boolean hasSpaceForItems() {
        for (int i = 0; i < InventoryScreen.items.length; ++i) {
            if (InventoryScreen.items[i] == null) {
                return true;
            }
        }
        return false;
    }

    public static void addItem(Item newItem) {
        for (int i = 0; i < InventoryScreen.items.length; ++i) {
            if (InventoryScreen.items[i] == null) {
                InventoryScreen.items[i] = newItem;
                return; //stop once we add the item to inventory
            }
        }

        //item could not be added - throw exception
        throw new IndexOutOfBoundsException("cannot add item to full inventory!");
    }

    public static void clear() {
        InventoryScreen.items = new Item[8];
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

    public static Item[] getItems() {
        return items;
    }
}
