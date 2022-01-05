package tests;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.ConfigScreen;
import main.InventoryScreen;
import main.Armour;
import main.Weapon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.testfx.framework.junit.ApplicationTest;

public class AllisonTests extends ApplicationTest {
    private ConfigScreen config;

    @Override
    public void start(Stage primaryStage) throws Exception {
        config = new ConfigScreen();
        primaryStage.setScene(config.getScene());
        primaryStage.show();
    }

    @Before
    public void setUp() {
        config.getNameField().setText("name");
        clickOn("#diffIS");
        clickOn("#weapPencil");
        clickOn("#buttonStart");
        clickOn("#door1");
    }

    //Test that the player should not be able to use any doors to
    // rooms that they have not yet visited if they have not defeated the monster
    @Test
    public void testAdvance() {
        Window currRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        assertEquals(false, currRoom.getScene().lookup("#alertLabel").isVisible());
        clickOn("#nextDoorLeft");
        assertEquals(true, currRoom.getScene().lookup("#alertLabel").isVisible());
    }

    //Test that monster health saves when leave and re-enter room
    @Test
    public void testReEnter() {
        clickOn("#examBoss");
        Window currRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        Label monsterHP = (Label) currRoom.getScene().lookup("#monHealth");
        clickOn("#prevDoorLeft");
        clickOn("#door1");
        assertEquals(monsterHP, (Label) currRoom.getScene().lookup("#monHealth"));
    }

    /** Milestone 5 tests **/
    //tests that correct starting weapon is in inventory when start game
    @Test
    public void testStartingWeapon() {
        assertEquals(((Weapon) InventoryScreen.getItems()[0]).getWeaponType(),
                new Weapon(ConfigScreen.Weapon.PENCIL).getWeaponType());
        assertEquals(((Weapon) InventoryScreen.getItems()[1]), null);
    }

    //Test that armour decreases monster attack damage by 50%
    @Test
    public void testArmour() {
        int dummy = 0;
        int startingHealth = ConfigScreen.getGameState().getPlayerHealth();
        while (ConfigScreen.getGameState().getPlayerHealth() != startingHealth) {
            dummy++;
        }
        int endingHealth = ConfigScreen.getGameState().getPlayerHealth();
        ConfigScreen.getGameState().setArmour(new Armour());
        while (ConfigScreen.getGameState().getPlayerHealth() != endingHealth) {
            dummy++;
        }
        assertEquals(endingHealth - startingHealth,
                (ConfigScreen.getGameState().getPlayerHealth() - endingHealth) * 2);
    }
}
