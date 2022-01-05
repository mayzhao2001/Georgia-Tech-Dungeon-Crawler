package tests;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.ConfigScreen;
import main.GameState;
import main.Weapon;
import org.junit.Before;
import org.junit.Test;

import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;


public class TriM5Tests extends ApplicationTest {
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

    // Test if monster disappear when monster health = 0
    @Test
    public void testWeaponDamage() {
        // Check visibility of the monster health
        verifyThat("#monHealth", (Label monsterHealth) -> monsterHealth.isVisible());
        // Attacking the monster 2 time for the first monster
        int numAttack = 2;
        for (int i = 0; i < numAttack; i++) {
            clickOn("#examBoss");
        }
        // Our weapon is Pencil -> so 2 damage
        GameState currGameState = ConfigScreen.getGameState();
        Weapon currWeapon = new Weapon(currGameState.getWeapon());
        int currDam = currWeapon.getDamage();
        int monsterCurrHealth = 100;
        int expectedWeaponDamage = monsterCurrHealth - numAttack * currDam;
        verifyThat("#monHealth",
            (Label monsterHealth) -> !monsterHealth.getText().contains("" + expectedWeaponDamage));
    }

    // Test when in a room with living monsters in it,
    // the player should be able to retreat to the previous room
    @Test
    public void testSwappedWeapon() {
        // Attacking the monster to go to the second room
        // Check visibility of the monster health
        verifyThat("#monHealth", (Label monsterHealth) -> monsterHealth.isVisible());
        // Attacking the monster 2 time for the first monster
        int numAttack = 2;
        for (int i = 0; i < numAttack; i++) {
            clickOn("#examBoss");
        }
        // Current weapon is Pencil -> damage inflict : 2
        GameState currGameState = ConfigScreen.getGameState();
        Weapon currWeapon = new Weapon(currGameState.getWeapon());
        int currDam = currWeapon.getDamage();
        int monsterCurrHealth = 100;
        int expectedWeaponDamage = monsterCurrHealth - numAttack * currDam;
        verifyThat("#monHealth",
            (Label monsterHealth) -> !monsterHealth.getText().contains("" + expectedWeaponDamage));

        // Swap weapon to Calculator -> damage inflict : 10
        ConfigScreen.Weapon newWeapon = ConfigScreen.Weapon.CALCULATOR;
        currGameState.setWeapon(newWeapon);
        Weapon swap = new Weapon(currGameState.getWeapon());
        int newDamage = swap.getDamage();
        int remainMonsterHealth = expectedWeaponDamage;
        // Attacking the monster 2 time for the first monster
        for (int i = 0; i < numAttack; i++) {
            clickOn("#examBoss");
        }
        int expectedNewWeaponDamage = remainMonsterHealth - numAttack * newDamage;
        verifyThat("#monHealth", (Label monsterHealth) ->
            !monsterHealth.getText().contains("" + expectedNewWeaponDamage));
    }
}
