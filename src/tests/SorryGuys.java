package tests;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.*;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;


public class SorryGuys extends ApplicationTest {
    private static final int TIMEOUT = 500;
    private DieScreen dieScreen;
    private WelcScreen welcScreen;
    private GameScreen1 startScreen;

    @Override
    public void start(Stage primaryStage) throws Exception {
        dieScreen = new DieScreen();
        primaryStage.setScene(dieScreen.getScene());
        primaryStage.show();

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

        dieScreen = new DieScreen();
        WinScreen winScreen = new WinScreen();

    }

    //test to see if player dying screen leads to welcome screen
    @Test
    public void dyingToWelc() {
        verifyThat("#buttonStartOver", isVisible());
        clickOn("#buttonStartOver");
        verifyThat("#buttonPlay", (Button s) -> s.isVisible());

    }


}