package tests;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.ConfigScreen;
import main.GameState;
import org.junit.Before;
import org.junit.Test;

import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;


public class TriM4Tests extends ApplicationTest {
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
    public void testKillMonster() {
        // Check visibility of the monster health
        verifyThat("#monHealth", (Label monsterHealth) -> monsterHealth.isVisible());
        // Attacking the monster 2 times for the first monster
        int numAttack = 2;
        for (int i = 0; i < numAttack; i++) {
            clickOn("#examBoss");
        }
        // Check if the monster disappear
        verifyThat("#monHealth", (Label monsterHealth) -> !monsterHealth.isVisible());
        verifyThat("#examBoss", (Button monsterImage) -> !monsterImage.isVisible());
    }

    // Test when in a room with living monsters in it,
    // the player should be able to retreat to the previous room
    @Test
    public void testReturnInMonsterRoom() {
        // Attacking the monster to go to the second room
        int numAttack = 2;
        for (int i = 0; i < numAttack; i++) {
            clickOn("#examBoss");
        }
        // Go to next room
        clickOn("#nextDoorRight");
        // Go back to the previous room
        clickOn("#prevDoorLeft");
        // Check the room number
        GameState currGameState = ConfigScreen.getGameState();
        Label expectedRoom0 = (Label) currGameState.getInteriorRoom(0, 0)
                .getScene().lookup("#roomNum");
        verifyThat("#roomNum", (Label roomNum) ->
                roomNum.getText().contains(expectedRoom0.getText()));
    }
}
