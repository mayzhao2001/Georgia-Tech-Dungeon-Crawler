package tests;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.*;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class MeilingTests extends ApplicationTest {
    private GameScreen1 startScreen;
    private ChallengeRoom room;


    @Override
    public void start(Stage stage) throws Exception {
        ConfigScreen.setGameState(new GameState(ConfigScreen.Weapon.TEXTBOOK,
                ConfigScreen.Difficulty.OUT_OF_STATE));

        startScreen = new GameScreen1(ConfigScreen.Difficulty.OUT_OF_STATE,
                ConfigScreen.Weapon.TEXTBOOK, true);
        stage.setScene(startScreen.getScene());
        stage.show();
    }

    // tests to see if player's health decreases when the monster attacks
    @Test
    public void playerHealthDecrease() throws InterruptedException {

        //advance to the first room
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);

        Window firstRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        Label healthLabel = (Label) firstRoom.getScene().lookup("#playerHealth");

        int initHealth = Integer.parseInt(healthLabel.getText().substring(15));
        Thread.sleep(2500); //wait 2.5 seconds ensure the monster attacks
        int finalHealth = Integer.parseInt(healthLabel.getText().substring(15));

        // if the initial health is greater than final health the player's
        // health decreased when the monster attacked
        assert (initHealth > finalHealth);
    }

    //tests to see if the death screen pops up when player's health = 0
    @Test
    public void dieScreen() throws InterruptedException {

        //advance to the first room
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);

        GameState currGameState = ConfigScreen.getGameState();
        currGameState.damagePlayer(90);

        Thread.sleep(5000); //wait 5 seconds ensure the monster attacks

        Window die = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        assert (die.getScene().lookup("#DieLabel") != null);
    }

    // milestone 5 tests
    @Test
    public void skipDroppedItem() throws InterruptedException {
        //advance to the first room
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);

        Window room = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        Button monsterButton = (Button) room.getScene().lookup("#examBoss");
        clickOn(monsterButton);
        clickOn(monsterButton);
        clickOn(monsterButton);
        clickOn(monsterButton);

        Button nextRoom = (Button) room.getScene().lookup("#nextDoorLeft");
        clickOn(nextRoom);

        Window next = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        assert (next.getScene().lookup("#examBoss") != null);
    }

    @Test
    public void testAttackPotion() throws InterruptedException {
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);

        Window room = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        GameState currGameState = ConfigScreen.getGameState();
        currGameState.setAttackPotion();

        Button monsterButton = (Button) room.getScene().lookup("#examBoss");
        clickOn(monsterButton);
        clickOn(monsterButton);

        Button nextRoom = (Button) room.getScene().lookup("#nextDoorLeft");
        clickOn(nextRoom);

        Window next = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        assert (next.getScene().lookup("#examBoss") != null);
    }

    @Test
    public void securityGuard() throws InterruptedException {
        room = new ChallengeRoom(1, startScreen);
        room.setupChallenge();
        assert (room.getScene().lookup("#examBoss1").getStyle().equals("-fx-background-image: url('"
                + Main.class.getResource("../resources/Security_Guard.png").toExternalForm()
                + "'); \n-fx-background-position: center center;"
                + "\n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;"));
        assert (room.getScene().lookup("#examBoss2").getStyle().equals("-fx-background-image: url('"
                + Main.class.getResource("../resources/Security_Guard.png").toExternalForm()
                + "'); \n-fx-background-position: center center;"
                + "\n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;"));
        assert (room.getScene().lookup("#examBoss3").getStyle().equals("-fx-background-image: url('"
                + Main.class.getResource("../resources/Security_Guard.png").toExternalForm()
                + "'); \n-fx-background-position: center center;"
                + "\n-fx-background-repeat: stretch;"
                + "\n-fx-background-size: stretch;\n-fx-background-color: transparent;"));

    }

    @Test
    public void itemT() throws InterruptedException {
        room = new ChallengeRoom(1, startScreen);
        room.setupChallenge();

        assert (room.getChallengeItem().getImage().equals("../resources/T.png"));

    }

}
