package tests;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.ConfigScreen;
import main.LastRoom;
import main.WelcScreen;
import main.WinScreen;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class EmmaM3Tests extends ApplicationTest {
    private static final int TIMEOUT = 500;
    private LastRoom lastRoom;
    private WinScreen winScreen;
    private WelcScreen welcScreen;

    @Override
    public void start(Stage primaryStage) throws Exception {
        lastRoom = new LastRoom(
                ConfigScreen.Difficulty.IN_STATE, ConfigScreen.Weapon.PENCIL, 10047834);
        primaryStage.setScene(lastRoom.getScene());
        primaryStage.show();

        //winScreen = new WinScreen();
        //primaryStage.setScene(winScreen.getScene());
        //primaryStage.show();
    }

    @Before
    public void setUp() {
        try {
            //initialize JavaFX env without launching a window
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
            //the javaFX environment was already set up
        }

        lastRoom = new LastRoom(
                ConfigScreen.Difficulty.IN_STATE, ConfigScreen.Weapon.PENCIL, 10047834);
        WinScreen winScreen = new WinScreen();

    }

    //test loading
    @Test(timeout = TIMEOUT)
    public void testFxmlLoading() {
        assert (lastRoom.getRoot() != null);
        assert (lastRoom.getScene().getRoot() != null);
    }

    //test to see if correct money shows up in label, even if it is arbitrary
    @Test(timeout = TIMEOUT)
    public void testMoneyLabel() {
        assert (lastRoom.getMoney() == 10047834);
        verifyThat("#money", isVisible());
        verifyThat("#money",
            (Label m) -> m.getText().contains("Room: Final Room" + "Money: $" + 10047834));

    }

    //test to see if exit room leads to win screen
    @Test
    public void exitToWin() {
        verifyThat("#Door", isVisible());
        clickOn("#Door");
        verifyThat("#buttonStartOver", (Button s) -> s.isVisible());

    }


}