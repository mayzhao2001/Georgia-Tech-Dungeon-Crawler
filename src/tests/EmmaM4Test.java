package tests;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.*;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class EmmaM4Test extends ApplicationTest {
    private static final int TIMEOUT = 500;
    private GameScreen1 startScreen;



    @Override
    public void start(Stage primaryStage) throws Exception {

        ConfigScreen.setGameState(new GameState(ConfigScreen.Weapon.PENCIL,
             ConfigScreen.Difficulty.IN_STATE));

        startScreen = new GameScreen1(ConfigScreen.Difficulty.IN_STATE,
             ConfigScreen.Weapon.CALCULATOR, true);
        primaryStage.setScene(startScreen.getScene());
        primaryStage.show();

    }

    //test if monster health goes down by correct amount when clicked
    @Test
    public void testMonsterAttack() {
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);

        Window firstRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        Button monsterButton = (Button) firstRoom.getScene().lookup("#examBoss");
        Label monHealthLabel = (Label) firstRoom.getScene().lookup("#monHealth");

        //attack the monster
        int numAttacks = 2; //clicks needed to kill the monster
        int health = 20;
        int currHealth;

        for (int i = 0; i < numAttacks; ++i) {
            clickOn(monsterButton);
            health -= 10;
            currHealth = Integer.parseInt(monHealthLabel.getText().substring(8));
            assert (currHealth == health);

        }


    }


}
