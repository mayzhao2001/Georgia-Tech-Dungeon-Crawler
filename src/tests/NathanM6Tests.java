package tests;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.ConfigScreen;
import main.GameScreen1;
import main.GameState;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class NathanM6Tests extends ApplicationTest {

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

    @Test
    public void testInventoryOpens() throws InterruptedException {

        //press the 'i' key
        press(KeyCode.I);
        this.sleep(100);
        this.release(KeyCode.I);

        Window inventoryRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        Label invLabel = (Label) inventoryRoom.getScene().lookup("#inventory");

        assert (invLabel != null); //the inventory has a label named #inventory,
        //but the second one does
    }

    @Test
    public void testInventoryCloses() throws InterruptedException {
        //press the 'i' key twice
        press(KeyCode.I);
        this.sleep(100);
        this.release(KeyCode.I);

        press(KeyCode.I);
        this.sleep(100);
        this.release(KeyCode.I);

        Window currentRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        Label invLabel = (Label) currentRoom.getScene().lookup("#inventory");

        assert (invLabel == null); //current screen is not inventory - it closed
    }
}
