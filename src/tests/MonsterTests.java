package tests;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.ConfigScreen;
import main.GameScreen1;
import main.GameState;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class MonsterTests extends ApplicationTest {
    private GameScreen1 startScreen;
    @Override
    public void start(Stage stage) throws Exception {
        ConfigScreen.setGameState(new GameState(ConfigScreen.Weapon.PENCIL,
                ConfigScreen.Difficulty.IN_STATE));

        startScreen = new GameScreen1(ConfigScreen.Difficulty.IN_STATE,
                ConfigScreen.Weapon.CALCULATOR, true);
        stage.setScene(startScreen.getScene());
        stage.show();
    }

    // Test written by Nathan Malta
    @Test
    public void testDeadMonstersNoAttack() throws InterruptedException {

        //advance to the first room
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);

        Window firstRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        Button monsterButton = (Button) firstRoom.getScene().lookup("#examBoss");
        Label healthLabel = (Label) firstRoom.getScene().lookup("#playerHealth");

        //attack the monster
        int numAttacks = 5; //clicks needed to kill the monster
        for (int i = 0; i < numAttacks; ++i) {
            clickOn(monsterButton);
        }

        int initHealth = Integer.parseInt(healthLabel.getText().substring(15));
        Thread.sleep(5000); //wait 5 seconds ensure the monster attacks
        int finalHealth = Integer.parseInt(healthLabel.getText().substring(15));

        //if the initial health and final health are equal, the monster didn't attack
        assert (initHealth == finalHealth);
    }

    //test written by Nathan Malta
    @Test
    public void testMonsterAttackInterval() throws InterruptedException {

        //advance to the first room
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);

        Window firstRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        Label healthLabel = (Label) firstRoom.getScene().lookup("#playerHealth");

        //record initial health and time
        int initHealth = Integer.parseInt(healthLabel.getText().substring(15));
        long initTime = System.currentTimeMillis();

        //wait until the health changes (monster attacks)
        while (Integer.parseInt(healthLabel.getText().substring(15)) == initHealth) {
            String checkstyle = "do nothing"; //make the checkstyle happy
        }

        //record time it took monster to attack
        long attackTime = System.currentTimeMillis() - initTime;
        long correctAttackTime = 2000;
        double percentError = Math.abs((attackTime - correctAttackTime)) / (double) attackTime;

        System.out.println(attackTime);
        System.out.println(percentError);

        //ensure the the time required for the monster to attack is within 10% of the correct time
        //(will not exactly equal expected time because of thread scheduling imperfections)
        assert (percentError < 0.1);
    }
}
